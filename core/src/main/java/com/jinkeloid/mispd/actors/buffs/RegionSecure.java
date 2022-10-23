package com.jinkeloid.mispd.actors.buffs;

import com.jinkeloid.mispd.Dungeon;
import com.jinkeloid.mispd.messages.Messages;
import com.jinkeloid.mispd.ui.BuffIndicator;

public class RegionSecure extends Buff{

    {
        type = buffType.POSITIVE;
    }

    @Override
    public boolean act() {
        spend(1f);
        if (Dungeon.progress.val != Dungeon.region.val)
            detach();
        return true;
    }

    @Override
    public int icon() {
        switch (Dungeon.progress){
            case GOO:
                return BuffIndicator.SECURE_REGION1;
            case TENGU:
                return BuffIndicator.SECURE_REGION2;
            case DM300:
                return BuffIndicator.SECURE_REGION3;
            case KING:
                return BuffIndicator.SECURE_REGION4;
            case YOG:
                return BuffIndicator.SECURE_REGION5;
            default:
                return BuffIndicator.NONE;
        }
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        String key;
        switch (Dungeon.progress){
            case GOO:
                key = "desc_goo";
                break;
            case TENGU:
                key = "desc_tengu";
                break;
            case DM300:
                key = "desc_dm300";
                break;
            case KING:
                key = "desc_king";
                break;
            case YOG:
                key = "desc_yog";
                break;
            default:
                key = "desc_err";
                break;
        }
        return Messages.get(this, key);
    }

}
