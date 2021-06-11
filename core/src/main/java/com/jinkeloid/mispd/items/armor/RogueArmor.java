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

package com.jinkeloid.mispd.items.armor;

import com.jinkeloid.mispd.Assets;
import com.jinkeloid.mispd.Dungeon;
import com.jinkeloid.mispd.actors.Actor;
import com.jinkeloid.mispd.actors.Char;
import com.jinkeloid.mispd.actors.buffs.Blindness;
import com.jinkeloid.mispd.actors.buffs.Buff;
import com.jinkeloid.mispd.actors.buffs.Invisibility;
import com.jinkeloid.mispd.actors.mobs.Mob;
import com.jinkeloid.mispd.effects.CellEmitter;
import com.jinkeloid.mispd.effects.Speck;
import com.jinkeloid.mispd.items.scrolls.ScrollOfTeleportation;
import com.jinkeloid.mispd.messages.Messages;
import com.jinkeloid.mispd.scenes.CellSelector;
import com.jinkeloid.mispd.scenes.GameScene;
import com.jinkeloid.mispd.sprites.ItemSpriteSheet;
import com.jinkeloid.mispd.utils.BArray;
import com.jinkeloid.mispd.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

public class RogueArmor extends ClassArmor {
	
	{
		image = ItemSpriteSheet.ARMOR_ROGUE;
	}
	
	@Override
	public void doSpecial() {
		GameScene.selectCell( teleporter );
	}
	
	protected CellSelector.Listener teleporter = new  CellSelector.Listener() {
		
		@Override
		public void onSelect( Integer target ) {
			if (target != null) {
				
				PathFinder.buildDistanceMap(curUser.pos, BArray.not(Dungeon.level.solid,null), 8);
				
				if ( PathFinder.distance[target] == Integer.MAX_VALUE ||
					!Dungeon.level.heroFOV[target] ||
					Actor.findChar( target ) != null) {
					
					GLog.w( Messages.get(RogueArmor.class, "fov") );
					return;
				}

				charge -= 35;
				updateQuickslot();
				
				for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
					if (Dungeon.level.adjacent(mob.pos, curUser.pos) && mob.alignment != Char.Alignment.ALLY) {
						Buff.prolong( mob, Blindness.class, Blindness.DURATION/2f );
						if (mob.state == mob.HUNTING) mob.state = mob.WANDERING;
						mob.sprite.emitter().burst( Speck.factory( Speck.LIGHT ), 4 );
					}
				}
				Buff.affect(curUser, Invisibility.class, Invisibility.DURATION/2f);

				CellEmitter.get( curUser.pos ).burst( Speck.factory( Speck.WOOL ), 10 );
				ScrollOfTeleportation.appear( curUser, target );
				Sample.INSTANCE.play( Assets.Sounds.PUFF );
				Dungeon.level.occupyCell(curUser );
				Dungeon.observe();
				GameScene.updateFog();
				
				curUser.spendAndNext( Actor.TICK );
			}
		}
		
		@Override
		public String prompt() {
			return Messages.get(RogueArmor.class, "prompt");
		}
	};
}