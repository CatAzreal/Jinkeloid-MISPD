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

package com.jinkeloid.mispd.items.stones;

import com.jinkeloid.mispd.effects.Enchanting;
import com.jinkeloid.mispd.effects.Speck;
import com.jinkeloid.mispd.items.Item;
import com.jinkeloid.mispd.items.armor.Armor;
import com.jinkeloid.mispd.items.weapon.Weapon;
import com.jinkeloid.mispd.messages.Messages;
import com.jinkeloid.mispd.sprites.ItemSpriteSheet;
import com.jinkeloid.mispd.utils.GLog;
import com.jinkeloid.mispd.windows.WndBag;

public class StoneOfEnchantment extends InventoryStone {
	
	{
		mode = WndBag.Mode.ENCHANTABLE;
		image = ItemSpriteSheet.STONE_ENCHANT;

		unique = true;
	}
	
	@Override
	protected void onItemSelected(Item item) {
		
		if (item instanceof Weapon) {
			
			((Weapon)item).enchant();
			
		} else {
			
			((Armor)item).inscribe();
			
		}
		
		curUser.sprite.emitter().start( Speck.factory( Speck.LIGHT ), 0.1f, 5 );
		Enchanting.show( curUser, item );
		
		if (item instanceof Weapon) {
			GLog.p(Messages.get(this, "weapon"));
		} else {
			GLog.p(Messages.get(this, "armor"));
		}
		
		useAnimation();
		
	}
	
	@Override
	public int value() {
		return 30 * quantity;
	}
}
