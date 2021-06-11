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
import com.jinkeloid.mispd.actors.buffs.Buff;
import com.jinkeloid.mispd.actors.buffs.Burning;
import com.jinkeloid.mispd.actors.buffs.Invisibility;
import com.jinkeloid.mispd.actors.buffs.Roots;
import com.jinkeloid.mispd.actors.mobs.Mob;
import com.jinkeloid.mispd.effects.particles.ElmoParticle;
import com.jinkeloid.mispd.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class MageArmor extends ClassArmor {
	
	{
		image = ItemSpriteSheet.ARMOR_MAGE;
	}
	
	@Override
	public void doSpecial() {

		charge -= 35;
		updateQuickslot();

		for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
			if (Dungeon.level.heroFOV[mob.pos]
				&& mob.alignment != Char.Alignment.ALLY) {
				Buff.affect( mob, Burning.class ).reignite( mob );
				Buff.prolong( mob, Roots.class, Roots.DURATION );
				mob.damage(Random.NormalIntRange(4, 16 + Dungeon.depth), new Burning());
			}
		}
		
		curUser.spend( Actor.TICK );
		curUser.sprite.operate( curUser.pos );
		Invisibility.dispel();
		curUser.busy();
		
		curUser.sprite.emitter().start( ElmoParticle.FACTORY, 0.025f, 20 );
		Sample.INSTANCE.play( Assets.Sounds.BURNING );
		Sample.INSTANCE.play( Assets.Sounds.BURNING );
		Sample.INSTANCE.play( Assets.Sounds.BURNING );
	}

}