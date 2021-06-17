/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.jinkeloid.mispd.ui;

import com.jinkeloid.mispd.Dungeon;
import com.jinkeloid.mispd.actors.Char;
import com.jinkeloid.mispd.actors.hero.Perk;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.ui.Component;

public class HealthBar extends Component {

    //Health orb logic also lives here, which would change depending on perk
    private static final int COLOR_BG = 0xFFCC0000;
    private static final int COLOR_HP = 0xFF00EE00;
    private static final int COLOR_SHLD = 0xFFBBEEBB;

    private static final int COLOR_H = 0xFF0db53a;
    private static final int COLOR_LD = 0xFFd7f229;
    private static final int COLOR_D = 0xFFf4f734;
    private static final int COLOR_W = 0xFFe39219;
    private static final int COLOR_SW = 0xFFba0606;

    private static final int HEIGHT = 2;

    private ColorBlock Bg;
    private ColorBlock Shld;
    private ColorBlock Hp;
    private ColorBlock Orb;

    private float health;
    private float shield;

    @Override
    protected void createChildren() {
            Bg = new ColorBlock(1, 1, COLOR_BG);
            add(Bg);

            Shld = new ColorBlock(1, 1, COLOR_SHLD);
            add(Shld);

            Hp = new ColorBlock(1, 1, COLOR_HP);
            add(Hp);

            Orb = new ColorBlock(1, 1, COLOR_H);
            add(Orb);

        height = HEIGHT;
    }

    @Override
    protected void layout() {
        Perk.onHealthBarTrigger();
        if (Dungeon.hero.hasPerk(Perk.LACK_OF_SENSE)) {
            Orb.visible = false;
            Bg.x = Shld.x = Hp.x = x;
            Bg.y = Shld.y = Hp.y = y;

            Bg.size(width, height);

            //logic here rounds up to the nearest pixel
            float pixelWidth = width;
            if (camera() != null) pixelWidth *= camera().zoom;
            Shld.size(width * (float) Math.ceil(shield * pixelWidth) / pixelWidth, height);
            Hp.size(width * (float) Math.ceil(health * pixelWidth) / pixelWidth, height);
        } else {
            Bg.visible = Shld.visible = Hp.visible = false;
            Orb.x = x;
            Orb.y = y;
            Orb.size(width, height);
            switch ((int) (health * 5)) {
                case 5:
                case 4:
                    Orb.color(COLOR_H);
                    break;
                case 3:
                    Orb.color(COLOR_LD);
                    break;
                case 2:
                    Orb.color(COLOR_D);
                    break;
                case 1:
                    Orb.color(COLOR_W);
                    break;
                case 0:
                    Orb.color(COLOR_SW);
                    break;
                default:
                    Orb.color(0x00129600);
                    break;
            }
        }
    }

    public void level(float value) {
        level(value, 0f);
    }

    public void level(float health, float shield) {
        this.health = health;
        this.shield = shield;
        layout();
    }

    public void level(Char c) {
        float health = c.HP;
        float shield = c.shielding();
        float max = Math.max(health + shield, c.HT);

        level(health / max, (health + shield) / max);
    }
}
