package com.jinkeloid.mispd.items.lightsource;

import com.jinkeloid.mispd.Assets;
import com.jinkeloid.mispd.Dungeon;
import com.jinkeloid.mispd.actors.buffs.Buff;
import com.jinkeloid.mispd.actors.buffs.Emptyoffhand;
import com.jinkeloid.mispd.actors.buffs.Light;
import com.jinkeloid.mispd.actors.buffs.LightOld;
import com.jinkeloid.mispd.actors.hero.Hero;
import com.jinkeloid.mispd.items.Item;
import com.jinkeloid.mispd.messages.Messages;
import com.jinkeloid.mispd.sprites.ItemSpriteSheet;
import com.jinkeloid.mispd.utils.GLog;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class FireTorch extends Lightsource{
    {
        image = ItemSpriteSheet.TORCH;
        identify();
        bones = false;

        maxCharge = charge = 500;
        active = false;
        updateSprite();
    }

    @Override
    public void updateSprite() {
        super.updateSprite();
        image = isActivated() ? ItemSpriteSheet.TORCHLIT : ItemSpriteSheet.TORCH;
    }

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );

        actions.add( isActivated() ? AC_SNUFF : this.isEquipped(hero) ? AC_LIGHT : AC_THROW);
        if (isActivated()) actions.remove( AC_UNEQUIP );
//        actions.add( AC_BURN );

        if (this.isEquipped(hero))
            actions.remove( AC_THROW );
        actions.remove( AC_THROW );
        actions.remove( AC_DROP );

        return actions;
    }

    @Override
    public void execute(Hero hero, String action ) {

        if (action.equals( AC_LIGHT ) && this.isEquipped(hero)) {
            if( charge > 0 ){
                if( hero.buff( Light.class ) == null ){
                    activate( hero, true );
                } else {
                    GLog.i( "another lightsource is lit" );
                }
            }
        } else if (action.equals( AC_SNUFF ) ) {
            if( isActivated() ){
                deactivate( hero, true );
            }
//        } else if (action.equals( AC_BURN ) ) {
//            if ( flasks > 0 ) {
//                curUser = hero;
//                curItem = this;
//                GameScene.selectCell( burner );
//            } else {
//                GLog.w( TXT_CANT_BURN );
//            }
        } else {
            super.execute( hero, action );
        }
    }

    @Override
    public boolean isEquipped( Hero hero ) {
        return hero.belongings.offhand == this;
    }

    public void activate( Hero hero, boolean voluntary ) {

        active = true;
        updateSprite();

        hero.search( false );

        if( voluntary ){
            hero.spend( TIME_TO_LIT );
            charge += TIME_TO_LIT;
            hero.busy();

            GLog.i(Messages.get(this, "lit"));
            hero.sprite.operate( hero.pos );
        }
        Buff.affect( hero, Light.TorchLight.class );
        Sample.INSTANCE.play( Assets.Sounds.CLICK );
        updateQuickslot();

        Dungeon.observe();
    }

    public void deactivate( Hero hero, boolean voluntary ) {
        if( voluntary ){
            hero.spend( TIME_TO_SNUFF );
            hero.busy();
            hero.sprite.operate( hero.pos );
            GLog.i(Messages.get(this, "snuff"));
        } else {
            GLog.i(Messages.get(this, "snuffed"));
        }
        active = false;
        updateSprite();
        hero.buff( Light.TorchLight.class ).ManualDetach();
        Sample.INSTANCE.play( Assets.Sounds.CLICK );
        updateQuickslot();

        Dungeon.observe();
    }

    @Override
    public void spendCharge() {
        super.spendCharge();
        if (charge <= 0){
            charge = 0;
            this.deactivate(Dungeon.hero, false);
            Dungeon.hero.belongings.offhand = null;
            Buff.affect(Dungeon.hero, Emptyoffhand.class);
            GLog.i("Torch depleted");
        }
    }

    @Override
    public String desc() {
        return super.desc() +
                (active ? Messages.get(this, "state_lit") :
                        Messages.get(this, "state_unlit"))
                + Messages.get(this, "descturn", charge);
    }

    @Override
    public int value() {
        return 25;
    }
}
