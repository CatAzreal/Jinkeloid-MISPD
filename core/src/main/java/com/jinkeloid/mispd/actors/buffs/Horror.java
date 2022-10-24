package com.jinkeloid.mispd.actors.buffs;

import com.jinkeloid.mispd.Dungeon;
import com.jinkeloid.mispd.actors.hero.Hero;
import com.jinkeloid.mispd.actors.hero.Perk;
import com.jinkeloid.mispd.ui.HorrorGauge;
import com.watabou.utils.Bundle;

public class Horror extends Buff {

    private static final float STEP	= 1f;

    private static float level;

    private static final String LEVEL			= "level";

    public static final float FRIGHTENED	= 25f;
    public static final float HORRIFIED	    = 50f;
    public static final float TREMBLING	    = 75f;
    public static final float MAX	        = 100f;
    private static final float UNEASY_growth        = 0.1f;
    private static final float FRIGHTENED_growth    = 0.07f;
    private static final float HORRIFIED_growth     = 0.05f;
    private static final float TREMBLING_growth     = 0.03f;

    public static void resetHorror() { level = 0f; }
    public float horror() {
        return level;
    }

    public static float GetHorror(){
        return level;
    }

    public static void SetHorror(float horror){
        level = horror;
    }

    public static void ModHorror(float horror){
        level += horror;
        if (level < 0) level = Math.min(0.05f,level);
        if (level > 100) level = Math.max(99.5f, level);
    }

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( LEVEL, level );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        level = bundle.getFloat( LEVEL );
    }

    @Override
    public boolean act() {
        Hero hero;
        if (target.isAlive() && target instanceof Hero) {
            hero = (Hero) target;
        } else {
            return false;
        }
        float dynamicStep = STEP;
        if (isUneasy()){
            switch (Light.lightIntensity){
                case DIM:
                    dynamicStep *= 0.5f * UNEASY_growth;
                    break;
                case MODRERATE:
                    dynamicStep *= 0;
                    break;
                case BRIGHT:
                    dynamicStep *= -0.07f;
                    break;
                default:
                    dynamicStep *= UNEASY_growth;
                    break;
            }
        }else if (isFrightened()){
            switch (Light.lightIntensity) {
                case DIM:
                    dynamicStep *= 0;
                case MODRERATE:
                    dynamicStep *= -0.02f;
                    break;
                case BRIGHT:
                    dynamicStep *= -0.08f;
                    break;
                default:
                    dynamicStep *= FRIGHTENED_growth;
                    break;
            }
        }else if (isHorrified()){
                switch (Light.lightIntensity) {
                    case DIM:
                        dynamicStep *= 0;
                        break;
                    case MODRERATE:
                        dynamicStep *= -0.05f;
                        break;
                    case BRIGHT:
                        dynamicStep *= -0.1f;
                        break;
                    default:
                        dynamicStep *= HORRIFIED_growth;
                        break;
                }
        }else if (isTrembling()){
                    switch (Light.lightIntensity) {
                        case DIM:
                            dynamicStep *= -0.05f;
                            break;
                        case MODRERATE:
                            dynamicStep *= -0.07f;
                            break;
                        case BRIGHT:
                            dynamicStep *= -0.15f;
                            break;
                        default:
                            dynamicStep *= TREMBLING_growth;
                            break;
                    }
        }
        //if a foe is slain by hero recently, horror growth would be halted
        if (dynamicStep > 0 && hero.buff(FellEnemy.class) != null) dynamicStep = Math.min(dynamicStep, 0);
        //if the region is secured and hero horror level is near or above horrified threshold, halt horror growth
        if (Dungeon.depth <= Dungeon.progress.val * 5 && Horror.GetHorror() > HORRIFIED - 0.5f) dynamicStep = Math.min(dynamicStep, 0);
        //Perk calculation
        if (dynamicStep > 0){
            if (hero.hasPerk(Perk.NICTOPHOBIA))
                dynamicStep *= 2;
            else if (hero.hasPerk(Perk.CONFIDENT) && (float)hero.HP/hero.HT > 0.90f)
                dynamicStep *= 0.5;
            else if (hero.hasPerk(Perk.BRAVE) && (float)hero.HP/hero.HT < 0.25f)
                dynamicStep *= 0;
        }

        float newLevel = level + dynamicStep;
        int diff = (int)(newLevel / 25) - (int)(level / 25);
        //making sure horror value doesn't exceed the default range
        level = Math.max(Math.min(newLevel, MAX), 0);
        spend(STEP);
        HorrorGauge.updateGauge(diff);
        if (diff == 0)return true;
        if (diff > 0 && Horror.isTrembling()){
            hero.vulnerabilities.add(Hex.class);
            hero.vulnerabilities.add(Charm.class);
            hero.vulnerabilities.add(Vulnerable.class);
            hero.vulnerabilities.add(Vertigo.class);
        }
        if (diff < 0 && !Horror.isTrembling()){
            hero.vulnerabilities.remove(Hex.class);
            hero.vulnerabilities.remove(Charm.class);
            hero.vulnerabilities.remove(Vulnerable.class);
            hero.vulnerabilities.remove(Vertigo.class);
        }
        return true;
    }

    public static boolean isUneasy()  {
        return level <= FRIGHTENED;
    }
    public static boolean isFrightened()    {
        return FRIGHTENED < level && level <= HORRIFIED;
    }
    public static boolean isHorrified()   {
        return HORRIFIED < level && level <= TREMBLING;
    }
    public static boolean isTrembling()    {
        return TREMBLING < level && level <= MAX;
    }

    public static float accPenalty() {return !isUneasy() ? Math.min(level * 0.4f, 30) : 0;}
    public static float movBonus() {
        if (!Dungeon.hero.hasPerk(Perk.ADRENALINE)) return 0;
        return !isUneasy() ? Math.min(level * 0.15f, 15) : 0;
    }
    public static float evaBonus() {
        if (!Dungeon.hero.hasPerk(Perk.ADRENALINE)) return 0;
        return (!isFrightened() && !isUneasy()) ? Math.min(level * 0.25f, 25) : 0;
    }
    public static float atkspeedBonus() {
        if (!Dungeon.hero.hasPerk(Perk.ADRENALINE)) return 0;
        return isTrembling() ? Math.min((level - 70) * 0.5f, 15) : 0;
    }
}
