package com.jinkeloid.mispd.actors.buffs;

import com.jinkeloid.mispd.Dungeon;
import com.jinkeloid.mispd.actors.hero.Hero;
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
        if (Dungeon.level.locked){
            spend(STEP);
            return true;
        }
        if (target.isAlive() && target instanceof Hero) {
            Hero hero = (Hero)target;
            if (isStarving()) {
                level -= STEP/2;
                //every value under starvation line would result in additional 0.1% damage to max health
                partialDamage += (1 - level/10) * target.HT/1000f;
                if (partialDamage > 1){
                    target.damage( (int)partialDamage, this);
                    partialDamage -= (int)partialDamage;
                }
            } else {
                float dynamicStep = STEP;
                if (isFull()){
                    dynamicStep *= 5f;
                }else if (isSatiated()){
                    dynamicStep *= 3f;
                }else if (isSatisfied()){
                    dynamicStep *= 1.5f;
                }else if (isStable()){
                }else if (isPeckish()){
                    dynamicStep *= 0.8f;
                }else{
                    dynamicStep *= 0.5f;
                }
                float newLevel = level - dynamicStep;
                if (newLevel <= HUNGRY && level > HUNGRY) {
                    GLog.n( Messages.get(this, "onhungry") );
                } else if (newLevel <= STARVING) {
                    GLog.w( Messages.get(this, "onstarving") );
                    hero.resting = false;
                    hero.damage( 1, this );
                    hero.interrupt();
                }
                level = newLevel;
            }
            spend( target.buff( Shadows.class ) == null ? STEP : STEP * 1.5f );
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
        }else if (isStable()||isPeckish()){
            return 0;
        }else if (isHungry()){
            return -1;
        }else {return -2;}
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
        }else if (isSatiated()){
            result = Messages.get(this, "desc_intro_satiated");
        }else if (isSatisfied()){
            result = Messages.get(this, "desc_intro_satisfied");
        }else if (isStable()){
            result = Messages.get(this, "desc_intro_stable");
        }else if (isPeckish()){
            result = Messages.get(this, "desc_intro_peckish");
        }else if (isHungry()){
            result = Messages.get(this, "desc_intro_hungry");
        } else { result = Messages.get(this, "desc_intro_starving");}
        result += Messages.get(this, "desc");

        return result;
    }

    public void satisfy( float energy ) {
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
