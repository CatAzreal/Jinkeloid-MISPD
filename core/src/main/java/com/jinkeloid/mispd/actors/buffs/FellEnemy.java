package com.jinkeloid.mispd.actors.buffs;

import com.jinkeloid.mispd.messages.Messages;
import com.jinkeloid.mispd.ui.BuffIndicator;

public class FellEnemy extends FlavourBuff{

    public static float DURATION = 0f;
    public static float MAX = 200f;

    {
        type = buffType.POSITIVE;
    }

    @Override
    public boolean act() {
        if (DURATION > MAX) DURATION = MAX;
        return super.act();
    }

    @Override
    public int icon() {
        return BuffIndicator.FELLENEMY;
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", dispTurns());
    }

}
