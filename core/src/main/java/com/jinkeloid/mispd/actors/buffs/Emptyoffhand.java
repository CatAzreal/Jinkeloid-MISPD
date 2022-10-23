package com.jinkeloid.mispd.actors.buffs;

import com.jinkeloid.mispd.messages.Messages;
import com.jinkeloid.mispd.ui.BuffIndicator;

public class Emptyoffhand extends Buff {

    {
        type = buffType.POSITIVE;
        announced = true;
    }

    public static float evasionBonus = 0.05f;
    public static float dmgBonus = 0.10f;

    @Override
    public int icon() {
        return BuffIndicator.OFFEMPTY;
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc");
    }

}
