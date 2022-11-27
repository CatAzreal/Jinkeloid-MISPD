package com.jinkeloid.mispd.items.lightsource;

import com.jinkeloid.mispd.actors.buffs.Buff;
import com.jinkeloid.mispd.actors.buffs.Emptyoffhand;
import com.jinkeloid.mispd.actors.hero.Hero;
import com.jinkeloid.mispd.actors.hero.Perk;
import com.jinkeloid.mispd.items.EquipableItem;
import com.jinkeloid.mispd.items.KindOfWeapon;
import com.jinkeloid.mispd.levels.Level;
import com.jinkeloid.mispd.mechanics.Ballistica;
import com.jinkeloid.mispd.scenes.CellSelector;
import com.jinkeloid.mispd.scenes.GameScene;
import com.jinkeloid.mispd.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class Lightsource extends EquipableItem {

    private static final String ACTIVE = "active";
    private static final String CHARGE = "charge";
    public static final String AC_BURN = "BURN";

    public static final String AC_LIGHT = "LIGHT";
    public static final String AC_SNUFF = "SNUFF";

    public boolean active;
    public int charge;
    public int maxCharge;

    public float TIME_TO_LIT = 5f;
    public float TIME_TO_SNUFF = 5f;
    public float TIME_TO_EQUIP = 1f;

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( ACTIVE, active );
        bundle.put( CHARGE, charge );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        active = bundle.getBoolean( ACTIVE );
        charge = bundle.getInt( CHARGE );

        updateSprite();
    }

    public int getCharge() {
        return charge;
    }

    public boolean isActivated() {
        return active;
    }

    //Most light source would be off-hand equipments, so they use offhand logic by default and would be overridden in other cases.
    @Override
    public boolean doEquip(Hero hero) {
        detachAll( hero.belongings.backpack );

        if (hero.belongings.offhand == null || hero.belongings.offhand.doUnequip( hero, true )) {

            hero.belongings.offhand = this;
            Buff.detach(hero, Emptyoffhand.class);
            activate( hero );
            Perk.onItemEquipped(hero, this);

            cursedKnown = true;

            //If hero have slowpoke, then switching and equipping weapon would cost them 10x turns
            Perk.onWeaponEquipTrigger();
            if (hero.hasPerk(Perk.SLOWPOKE))hero.spendAndNext( TIME_TO_EQUIP * 10 );
                //If hero have weapon switch perk then let them switch weapon instantly
            else if (!KindOfWeapon.instantSwitch)hero.spendAndNext( TIME_TO_EQUIP );
            else {
                hero.spendAndNext(0);
                KindOfWeapon.instantSwitch = false;
                GLog.i("instantSwitch performed");
            }
            return true;
        } else {
            collect( hero.belongings.backpack, false);
            return false;
        }
    }

    @Override
    public boolean doUnequip( Hero hero, boolean collect, boolean single ) {
        if (super.doUnequip( hero, collect, single )) {
            hero.belongings.offhand = null;
            Buff.affect(hero, Emptyoffhand.class);
            return true;
        } else {
            return false;
        }
    }

    public void updateSprite(){}

    public void spendCharge() {
        charge--;
        updateQuickslot();
    }

    public void addCharge(int addCharge) {
        charge += addCharge;
        updateQuickslot();
    }

    //used for overriding only
    public void activate( Hero hero, boolean voluntary ) {}
    public void deactivate( Hero hero, boolean voluntary ) {}

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    //todo: allow lightsources to burn tiles nearby
//    protected static CellSelector.Listener burner = new CellSelector.Listener() {
//        @Override
//        public void onSelect( Integer target ) {
//
//            if (target != null) {
//
//                Ballistica.cast( curUser.pos, target, false, true );
//
//                int cell = Ballistica.trace[ 0 ];
//
//                if( Ballistica.distance > 0 ){
//                    cell = Ballistica.trace[ 1 ];
//                }
//
//                if( Level.flammable[ cell ] || !Level.solid[ cell ] && !Level.chasm[ cell ] ){
//                    GameScene.add( Blob.seed( cell, 5, Fire.class ) );
//                }
//
//                ((OilLantern)curItem).flasks--;
//                Invisibility.dispel();
//
//                if( curUser.pos == cell ) {
//                    GLog.i( TXT_BURN_SELF );
//                } else if( Level.flammable[ cell ] || !Level.solid[ cell ] && !Level.chasm[ cell ] ){
//                    GLog.i( TXT_BURN_TILE );
//                } else {
//                    GLog.i( TXT_BURN_FAIL );
//                }
//
//                Sample.INSTANCE.play(Assets.SND_BURNING, 0.6f, 0.6f, 1.5f);
//                CellEmitter.get( cell ).burst( FlameParticle.FACTORY, 5 );
//
//                curUser.sprite.operate(cell);
//                curUser.busy();
//                curUser.spend( Actor.TICK );
//
//            }
//        }
//        @Override
//        public String prompt() {
//            return "选择一处要点火的地格";
//        }
//    };

}
