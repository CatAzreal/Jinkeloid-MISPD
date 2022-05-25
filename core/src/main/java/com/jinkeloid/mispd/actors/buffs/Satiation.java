package com.jinkeloid.mispd.actors.buffs;

import com.jinkeloid.mispd.Dungeon;
import com.jinkeloid.mispd.actors.hero.Hero;
import com.jinkeloid.mispd.actors.hero.Perk;
import com.jinkeloid.mispd.items.artifacts.Artifact;
import com.jinkeloid.mispd.items.artifacts.HornOfPlenty;
import com.jinkeloid.mispd.messages.Messages;
import com.jinkeloid.mispd.ui.BuffIndicator;
import com.jinkeloid.mispd.utils.GLog;
import com.watabou.utils.Bundle;

public class Satiation extends Buff implements Hero.Doom {

    private static final float STEP	= 1f;

    public static final float SATIATED	= 600f;
    public static final float SATISFIED	= 500f;
    public static final float STABLE	= 400f;
    public static final float PECKISH	= 200f;
    public static final float HUNGRY	= 100f;
    public static final float STARVING	= 0f;
    private static final float FULL_decay   = 5f;
    private static final float SATIATED_decay   = 3f;
    private static final float SATISFIED_decay   = 1.5f;
    private static final float STABLE_decay   = 1f;
    private static final float PECKISH_decay   = 0.75f;
    private static final float HUNGRY_decay   = 0.4f;
    private static final float DIET_decay   = 0.7f;
    private static final float B_STOMACH_decay   = 1.5f;

    private static float level;
    private float partialDamage;

    private static final String LEVEL			= "level";
    private static final String PARTIALDAMAGE 	= "partialDamage";

    public static void resetSatiation() { level = 300f; }
    public int satiation() {
        return (int)Math.ceil(level);
    }

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( LEVEL, level );
        bundle.put( PARTIALDAMAGE, partialDamage );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        level = bundle.getFloat( LEVEL );
        partialDamage = bundle.getFloat(PARTIALDAMAGE);
    }

    @Override
    public boolean act() {
        boolean HTUpdate;
//        if (Dungeon.level.locked){
//            spend(STEP);
//            return true;
//        }
        if (target.isAlive() && target instanceof Hero) {
            Hero hero = (Hero)target;
            if (isStarving()) {
                level -= STEP/2;
                //every value under starvation line would result in additional 0.1% damage to max health per turn,
                //0.05% if abstinence
                //none if the floor is locked
                if (!Dungeon.level.locked) {
                    partialDamage += (1 - level / 10) * target.HT / (hero.hasPerk(Perk.ABSTINENCE) ? 2000f : 1000f);
                }
                if (partialDamage > 1){
                    target.damage( (int)partialDamage, this);
                    partialDamage -= (int)partialDamage;
                }
            } else {
                float dynamicStep = STEP;
                if (isFull()){
                    dynamicStep *= FULL_decay;
                }else if (isSatiated()){
                    dynamicStep *= SATIATED_decay;
                }else if (isSatisfied()){
                    dynamicStep *= SATISFIED_decay;
                }else if (isStable()){
                }else if (isPeckish()){
                    dynamicStep *= PECKISH_decay;
                }else{
                    dynamicStep *= HUNGRY_decay;
                }
                Perk.onSatiationTrigger();
                if (((Hero) target).hasPerk(Perk.ON_DIET)){
                    dynamicStep *= 0.7f;
                } else if (((Hero) target).hasPerk(Perk.BIG_STOMACH)){
                    dynamicStep *= 1.5f;
                }
                float newLevel = level - dynamicStep;
                if (newLevel <= HUNGRY && level > HUNGRY) {
                    GLog.n( Messages.get(this, "onhungry") );
                    HTUpdate = true;
                } else if (newLevel <= STARVING) {
                    GLog.w( Messages.get(this, "onstarving") );
                    HTUpdate = true;
                    hero.resting = false;
                    hero.interrupt();
                }
                //update player max health when satiation level increased
                if ((newLevel > HUNGRY && level <= HUNGRY) || (newLevel > STARVING && level <= STARVING)){
                    HTUpdate = true;
                }
                level = newLevel;
            }
            spend( target.buff( Shadows.class ) == null ? STEP : STEP * 1.5f );
            if (HTUpdate = true)
                ((Hero) target).updateHT(false);
        } else { diactivate(); }
        return true;
    }
    public static boolean isStarving()  {
        return level <= STARVING;
    }
    public static boolean isHungry()    {
        return STARVING < level && level <= HUNGRY;
    }
    public static boolean isPeckish()   {
        return HUNGRY < level && level <= PECKISH;
    }
    public static boolean isStable()    {
        return PECKISH < level && level <= STABLE;
    }
    public static boolean isSatisfied() {
        return STABLE < level && level <= SATISFIED;
    }
    public static boolean isSatiated()  {
        return SATISFIED < level && level <= SATIATED;
    }
    public static boolean isFull()      {
        return SATIATED < level;
    }

    public static int satiationSTRBonus() {
        if (isFull()){
            return 2;
        }else if (isSatisfied()||isSatiated()){
            return 1;
        }else if (isStarving()){
            return Dungeon.hero.hasPerk(Perk.ABSTINENCE) ? 0 : -1;
        }else {
            return 0;
        }
    }

    public static float satiationSPDBonus() {
        if (isStarving()){
            return Dungeon.hero.hasPerk(Perk.ABSTINENCE) ? 1f : 0.85f;
        }
        if (isFull()){
            return 0.9f;
        }
        return 1;
    }

    public static float satiationHPBonus() {
        if (isHungry()){
            return Dungeon.hero.hasPerk(Perk.ABSTINENCE) ? 1f : 0.9f;
        }
        if (isStarving()){
            return Dungeon.hero.hasPerk(Perk.ABSTINENCE) ? 1f : 0.8f;
        }
        return 1;
    }

    public static float satiationDMGBonus() {
        if (isStarving()){
            return Dungeon.hero.hasPerk(Perk.ABSTINENCE) ? 0.85f : 0.75f;
        }
        if (isHungry()){
            return Dungeon.hero.hasPerk(Perk.ABSTINENCE) ? 0.90f : 0.85f;
        }
        if (isPeckish()){
            return Dungeon.hero.hasPerk(Perk.ABSTINENCE) ? 0.95f : 0.9f;
        }
        return 1;
    }

    public static float satiationRegenBonus() {
        if (isFull()){
            return 0.1f;
        }else if (isSatiated()){
            return 0.2f;
        }else if (isSatisfied()){
            return 0.5f;
        }else if (isHungry()){
            return 2f;
        }
        return 1f;
    }

    @Override
    public int icon() {
        if (isFull()){
            return BuffIndicator.FULL;
        }else if (isSatiated()){
            return BuffIndicator.SATIATED;
        }else if (isSatisfied()){
            return BuffIndicator.SATISFIED;
        }else if (isStable()){
            return BuffIndicator.STABLE;
        }else if (isPeckish()){
            return BuffIndicator.PECKISH;
        }else if (isHungry()){
            return BuffIndicator.HUNGRY;
        }
        return BuffIndicator.STARVING;
    }

    @Override
    public String toString() {
        if (isFull()){
            return Messages.get(this, "full");
        }else if (isSatiated()){
            return Messages.get(this, "satiated");
        }else if (isSatisfied()){
            return Messages.get(this, "satisfied");
        }else if (isStable()){
            return Messages.get(this, "stable");
        }else if (isPeckish()){
            return Messages.get(this, "peckish");
        }else if (isHungry()){
            return Messages.get(this, "hungry");
        }
        return Messages.get(this, "starving");
    }

    @Override
    public String desc() {
        String result;
        if (isFull()){
            result = Messages.get(this, "desc_intro_full");
            result += Messages.get(this, "desc_turncount_full", decayTurnCount());
        }else if (isSatiated()){
            result = Messages.get(this, "desc_intro_satiated");
            result += Messages.get(this, "desc_turncount_satiated", decayTurnCount());
        }else if (isSatisfied()){
            result = Messages.get(this, "desc_intro_satisfied");
            result += Messages.get(this, "desc_turncount_satisfied", decayTurnCount());
        }else if (isStable()){
            result = Messages.get(this, "desc_intro_stable");
            result += Messages.get(this, "desc_turncount_stable", decayTurnCount());
        }else if (isPeckish()){
            if (!Dungeon.hero.hasPerk(Perk.ABSTINENCE)) result = Messages.get(this, "desc_intro_peckish");
            else result = Messages.get(this, "desc_intro_peckish_ab");
            result += Messages.get(this, "desc_turncount_peckish", decayTurnCount());
        }else if (isHungry()){
            if (!Dungeon.hero.hasPerk(Perk.ABSTINENCE)) result = Messages.get(this, "desc_intro_hungry");
            else result = Messages.get(this, "desc_intro_hungry_ab");
            result += Messages.get(this, "desc_turncount_hungry", decayTurnCount());
        } else {
            if (!Dungeon.hero.hasPerk(Perk.ABSTINENCE)) result = Messages.get(this, "desc_intro_starving");
            else result = Messages.get(this, "desc_intro_starving_ab");}
        result += Messages.get(this, "desc");

        return result;
    }

    int decayTurnCount(){
        float turnCount = 1;
        if (isFull()){
            turnCount = (level - SATIATED)/(STEP * FULL_decay);
        }else if (isSatiated()){
            turnCount = (level - SATISFIED)/(STEP * SATIATED_decay);
        }else if (isSatisfied()){
            turnCount = (level - STABLE)/(STEP * SATISFIED_decay);
        }else if (isStable()){
            turnCount = (level - PECKISH)/(STEP * STABLE_decay);
        }else if (isPeckish()){
            turnCount = (level - HUNGRY)/(STEP * PECKISH_decay);
        }else if (isHungry()){
            turnCount =  level/(STEP * HUNGRY_decay);
        } else { turnCount = 1000;}
        if (Dungeon.hero.hasPerk(Perk.ON_DIET))
            turnCount /= DIET_decay;
        if (Dungeon.hero.hasPerk(Perk.BIG_STOMACH))
            turnCount /= B_STOMACH_decay;
        return (int)Math.ceil(turnCount);
    }

    @Override
    public float iconFadePercent() {
        if (isSatiated()){
            return 1 - (level - SATISFIED)/(SATIATED - SATISFIED);
        }else if (isSatisfied()){
            return 1 - (level - STABLE)/(SATISFIED - STABLE);
        }else if (isStable()){
            return 1 - (level - PECKISH)/(STABLE - PECKISH);
        }else if (isPeckish()){
            return 1 - (level - HUNGRY)/(PECKISH - HUNGRY);
        }else if (isHungry()){
            return 1 - level/HUNGRY;
        } else { return 0;}
    }

    public void satisfy(float energy ) {
        Artifact.ArtifactBuff buff = target.buff( HornOfPlenty.hornRecharge.class );
        if (buff != null && buff.isCursed()){
            energy *= 0.67f;
            GLog.n( Messages.get(this, "cursedhorn") );
        }
        affectSatiation(energy);
    }

    public void affectSatiation(float energy ) {

        if (energy < 0 && target.buff(WellFed.class) != null){
            target.buff(WellFed.class).left += energy;
            BuffIndicator.refreshHero();
            return;
        }
        //if character is starving, eating anything would get them back to hunger again
        if (level < 0 && energy > 0){
            level = energy;
        } else {
            level += energy;
        }
        BuffIndicator.refreshHero();
    }

    @Override
    public void onDeath() {
        Dungeon.fail( getClass() );
        GLog.n( Messages.get(this, "ondeath") );
    }
}
