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

import com.jinkeloid.mispd.Assets;
import com.jinkeloid.mispd.Dungeon;
import com.jinkeloid.mispd.actors.Actor;
import com.jinkeloid.mispd.actors.Char;
import com.jinkeloid.mispd.actors.buffs.Buff;
import com.jinkeloid.mispd.actors.buffs.FlavourBuff;
import com.jinkeloid.mispd.actors.mobs.Mob;
import com.jinkeloid.mispd.effects.CellEmitter;
import com.jinkeloid.mispd.effects.Speck;
import com.jinkeloid.mispd.items.Heap;
import com.jinkeloid.mispd.messages.Messages;
import com.jinkeloid.mispd.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

public class StoneOfAggression extends Runestone {
	
	{
		image = ItemSpriteSheet.STONE_AGGRESSION;
	}
	
	@Override
	protected void activate(int cell) {
		
		Char ch = Actor.findChar( cell );
		
		if (ch != null) {
			if (ch.alignment == Char.Alignment.ENEMY) {
				Buff.prolong(ch, Aggression.class, Aggression.DURATION / 5f);
			} else {
				Buff.prolong(ch, Aggression.class, Aggression.DURATION);
			}
			CellEmitter.center(cell).start( Speck.factory( Speck.SCREAM ), 0.3f, 3 );
			Sample.INSTANCE.play( Assets.Sounds.READ );
		} else {
			//Item.onThrow
			Heap heap = Dungeon.level.drop( this, cell );
			if (!heap.isEmpty()) {
				heap.sprite.drop( cell );
			}
		}
		
	}
	
	public static class Aggression extends FlavourBuff {
		
		public static final float DURATION = 20f;
		
		{
			type = buffType.NEGATIVE;
			announced = true;
		}
		
		@Override
		public void storeInBundle( Bundle bundle ) {
			super.storeInBundle(bundle);
		}
		
		@Override
		public void restoreFromBundle( Bundle bundle ) {
			super.restoreFromBundle( bundle );
		}
		
		@Override
		public void detach() {
			//if our target is an enemy, reset the aggro of any enemies targeting it
			if (target.isAlive()) {
				if (target.alignment == Char.Alignment.ENEMY) {
					for (Mob m : Dungeon.level.mobs) {
						if (m.alignment == Char.Alignment.ENEMY && m.isTargeting(target)) {
							m.aggro(null);
						}
					}
				}
			}
			super.detach();
			
		}
		
		@Override
		public String toString() {
			return Messages.get(this, "name");
		}
		
	}
	
}
