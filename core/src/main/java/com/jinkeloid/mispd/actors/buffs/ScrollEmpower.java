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

package com.jinkeloid.mispd.actors.buffs;

import com.jinkeloid.mispd.Dungeon;
import com.jinkeloid.mispd.actors.hero.Perk;
import com.jinkeloid.mispd.items.Item;
import com.jinkeloid.mispd.messages.Messages;
import com.jinkeloid.mispd.ui.BuffIndicator;
import com.watabou.noosa.Image;

public class ScrollEmpower extends FlavourBuff {

	{
		type = buffType.POSITIVE;
	}

	@Override
	public void detach() {
		super.detach();
		Item.updateQuickslot();
	}

	@Override
	public int icon() {
		return BuffIndicator.UPGRADE;
	}

	@Override
	public void tintIcon(Image icon) {
		icon.hardlight(1, 1, 0);
	}

	@Override
	public float iconFadePercent() {
		return Math.max(0, (20-visualcooldown()) / 20f);
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", (int)visualcooldown(), Dungeon.hero.pointsInTalent(Perk.EMPOWERING_SCROLLS));
	}

}
