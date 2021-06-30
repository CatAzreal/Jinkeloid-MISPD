package com.jinkeloid.mispd.actors.buffs;

import com.jinkeloid.mispd.actors.hero.Hero;
import com.jinkeloid.mispd.actors.hero.Perk;
import com.jinkeloid.mispd.items.Item;
import com.jinkeloid.mispd.messages.Messages;
import com.jinkeloid.mispd.utils.GLog;

public class RegenPerTurn extends Buff {

    private int counter = 0;
    //this class will handle all hero stats that update every turn
    {
        actPriority = HERO_PRIO - 1;
    }

    @Override
    public boolean act() {
        if (((Hero)target).isAlive()) {
            float horrorRatio = ((Hero)target).Horror / ((Hero)target).GuiltMax;
            float healthRatio = (float)target.HP / target.HT;
            if (((Hero)target).Horror > 0) {
                //horror value decay starts from 0.02 to 0.2 per turn, depending on player's current horror level
                double horrorDecay = 0.02f * Math.pow(10, horrorRatio);
//                if (healthRatio < 0.5f){
//                    horrorDecay *= 1.5f;
//                }
//                if (healthRatio < 0.2f){
//                    horrorDecay *= 3f;
//                }
                ((Hero)target).Horror -= horrorDecay;
                if (((Hero)target).Horror < 0 ){
                    ((Hero)target).Horror = 0;
                }
            }
            Perk.onItemThrow();
            //Re-apply the free throw to hero every turn
            if (((Hero) target).hasPerk(Perk.DEXTEROUS) && !Item.instantAct){
                counter++;
                if (counter == 2){
                    GLog.i(Messages.get(this, "instantrestore"));
                    Item.instantAct = true;
                    counter = 0;
                }
            }
            spend( 1 );

        } else {

            diactivate();

        }
        return true;
    }
}
