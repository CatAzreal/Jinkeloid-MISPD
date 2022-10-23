package com.jinkeloid.mispd.actors.buffs;

import static com.jinkeloid.mispd.Dungeon.hero;

import com.jinkeloid.mispd.Dungeon;
import com.jinkeloid.mispd.actors.hero.Perk;
import com.jinkeloid.mispd.items.lightsource.Candle;
import com.jinkeloid.mispd.items.lightsource.FireTorch;
import com.jinkeloid.mispd.messages.Messages;
import com.jinkeloid.mispd.sprites.CharSprite;
import com.jinkeloid.mispd.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class Light extends Buff{

    {
        type = buffType.POSITIVE;
    }
    protected float left;
    private static final String BRIGHTNESS	= "brightness";
    private static final String LEFT	= "left";

    public float DURATION;
    public float DELAY = 1f;
    public static int visionBonus = 0;
    public enum brightness{
        DIM,
        MODRERATE,
        BRIGHT,
        NONE;
    }
    public static brightness lightIntensity = brightness.NONE;

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( BRIGHTNESS, lightIntensity.toString() );
        bundle.put( LEFT, left );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        lightIntensity = Enum.valueOf(brightness.class, bundle.getString( BRIGHTNESS ));
        left = bundle.getFloat( LEFT );
    }

//    @Override
//    public boolean attachTo( Char target ) {
//        if (super.attachTo( target )) {
//            if (Dungeon.level != null) {
//                target.viewDistance = Math.max( Dungeon.level.viewDistance, DISTANCE );
//                Dungeon.observe();
//            }
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    @Override
//    public void detach() {
//        target.viewDistance = Dungeon.level.viewDistance;
//        Dungeon.observe();
//        super.detach();
//    }
//
//    public void weaken( int amount ){
//        spend(-amount);
//    }
//
//    @Override
//    public int icon() {
//        return BuffIndicator.LIGHT;
//    }
//
//    @Override
//    public float iconFadePercent() {
//        return Math.max(0, (DURATION - visualcooldown()) / DURATION);
//    }

    @Override
    public void fx(boolean on) {
        if (on) target.sprite.add(CharSprite.State.ILLUMINATED);
        else target.sprite.remove(CharSprite.State.ILLUMINATED);
    }

    @Override
    public boolean act(){
        hero.viewDistance = Math.max(8, hero.viewDistance + visionBonus);
        return true;
    }

    @Override
    public void detach(){
        super.detach();
        resetLight();
    }

    private static void resetLight(){
        Dungeon.updateVisionRange();
        lightIntensity = Light.brightness.NONE;
        visionBonus = 0;
    }

    public void ManualDetach(){
        detach();
    }

    public static class CandleLight extends Light{
        {
            lightIntensity = brightness.DIM;
            visionBonus = 1;
        }

        Candle candle = hero.belongings.getItem( Candle.class );

        @Override
        public boolean act() {
            super.act();
            if( candle != null && candle.isActivated() && candle.getCharge() > 0 ){
                candle.spendCharge();
                spend( DELAY );
            } else {
                candle.deactivate( hero, false );
            }
            return true;
        }

        @Override
        public int icon() {
            return BuffIndicator.DIMLIGHT;
        }

        @Override
        public String toString() {
            return Messages.get(Light.class, "dimlight.name");
        }

        @Override
        public String desc() {
            return Messages.get(this, "dimlight.desc", (int)(candle.charge * DELAY));
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, candle.charge / candle.maxCharge);
        }
    }

    public static class TorchLight extends Light{
        {
            lightIntensity = brightness.MODRERATE;
            visionBonus = 2;
        }

        FireTorch torch = hero.belongings.getItem( FireTorch.class );

        @Override
        public boolean act() {
            super.act();
            if( torch != null && torch.isActivated() && torch.getCharge() > 0 ){
                torch.spendCharge();
                spend( DELAY );
            } else {
                torch.deactivate( hero, false );
            }
            return true;
        }

        @Override
        public int icon() {
            return BuffIndicator.MODLIGHT;
        }

        @Override
        public String toString() {
            return Messages.get(this, "modlight.name");
        }

        @Override
        public String desc() {
            return Messages.get(this, "modlight.desc", (int)(torch.charge * DELAY));
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, torch.charge / torch.maxCharge);
        }
    }

    public static class PortableLanternLight extends Light{
        {
            lightIntensity = brightness.MODRERATE;
            visionBonus = 2;
        }

        @Override
        public int icon() {
            return BuffIndicator.MODLIGHT;
        }

        @Override
        public String toString() {
            return Messages.get(this, "name");
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }
    }

    public static class LanternLight extends Light{
        {
            lightIntensity = brightness.BRIGHT;
        }

        @Override
        public int icon() {
            return BuffIndicator.BRIGHTLIGHT;
        }

        @Override
        public String toString() {
            return Messages.get(this, "name");
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }
    }

    public static class HelmetLight extends Light{
        {
            lightIntensity = brightness.MODRERATE;
        }

        @Override
        public int icon() {
            return BuffIndicator.MODLIGHT;
        }

        @Override
        public String toString() {
            return Messages.get(this, "name");
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }
    }
}
