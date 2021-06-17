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

package com.jinkeloid.mispd.windows;

import com.jinkeloid.mispd.Dungeon;
import com.jinkeloid.mispd.actors.hero.Perk;
import com.jinkeloid.mispd.actors.mobs.Mob;
import com.jinkeloid.mispd.messages.Messages;
import com.jinkeloid.mispd.scenes.PixelScene;
import com.jinkeloid.mispd.sprites.CharSprite;
import com.jinkeloid.mispd.ui.BuffIndicator;
import com.jinkeloid.mispd.ui.HealthBar;
import com.jinkeloid.mispd.ui.RenderedTextBlock;
import com.watabou.noosa.ui.Component;

public class WndInfoMob extends WndTitledMessage {

    public WndInfoMob(Mob mob) {

        super(new MobTitle(mob), mob.info());

    }

    private static class MobTitle extends Component {

        private static final int GAP = 2;

        private CharSprite image;
        private RenderedTextBlock name;
        private RenderedTextBlock healthState;
        private float mobHealth;
        private HealthBar health;
        private BuffIndicator buffs;

        public MobTitle(Mob mob) {

            name = PixelScene.renderTextBlock(Messages.titleCase(mob.name()), 9);
            name.hardlight(TITLE_COLOR);
            add(name);

            image = mob.sprite();
            add(image);

            health = new HealthBar();
            health.level(mob);
            add(health);

            healthState = PixelScene.renderTextBlock(Messages.titleCase(""), 6);
            healthState.hardlight(TITLE_COLOR);
            add(healthState);

            mobHealth = (float) (5 * mob.HP / mob.HT);

            buffs = new BuffIndicator(mob);
            add(buffs);
        }

        @Override
        protected void layout() {

            image.x = 0;
            image.y = Math.max(0, name.height() + health.height() - image.height());

            name.setPos(x + image.width + GAP,
                    image.height() > name.height() ? y + (image.height() - name.height()) / 2 : y);

            float w = width - image.width() - GAP;

            Perk.onHealthBarTrigger();
            health.setRect(image.width() + GAP, name.bottom() + GAP, w, health.height());
            if (!Dungeon.hero.hasPerk(Perk.LACK_OF_SENSE)) {
                health.visible = false;
                switch ((int) mobHealth) {
                    case 5:
                    case 4:
                        healthState.text("Healthy");
                        healthState.hardlight(0x0db53a);
                        break;
                    case 3:
                        healthState.text("Lightly_Damaged");
                        healthState.hardlight(0xd7f229);
                        break;
                    case 2:
                        healthState.text("Damaged");
                        healthState.hardlight(0xf4f734);
                        break;
                    case 1:
                        healthState.text("Wounded");
                        healthState.hardlight(0xe39219);
                        break;
                    case 0:
                        healthState.text("Severely_Wounded");
                        healthState.hardlight(0xba0606);
                        break;
                    default:
                        healthState.text("Healthy??");
                        healthState.hardlight(0x001296);
                        break;
                }
                healthState.setPos(name.right() + GAP * 3, name.bottom() - healthState.height());
            }

            buffs.setPos(
                    name.right() + GAP - 1,
                    name.bottom() - BuffIndicator.SIZE - 2
            );

            height = health.bottom();
            if (!Dungeon.hero.hasPerk(Perk.LACK_OF_SENSE)) {
                height = name.bottom() + GAP * 2;
            }
        }
    }
}
