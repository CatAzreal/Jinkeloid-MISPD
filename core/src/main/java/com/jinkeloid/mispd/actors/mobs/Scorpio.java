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

package com.jinkeloid.mispd.actors.mobs;

import com.jinkeloid.mispd.Dungeon;
import com.jinkeloid.mispd.actors.Char;
import com.jinkeloid.mispd.actors.buffs.Buff;
import com.jinkeloid.mispd.actors.buffs.Cripple;
import com.jinkeloid.mispd.actors.buffs.LightOld;
import com.jinkeloid.mispd.items.Generator;
import com.jinkeloid.mispd.items.Item;
import com.jinkeloid.mispd.items.potions.Potion;
import com.jinkeloid.mispd.items.potions.PotionOfHealing;
import com.jinkeloid.mispd.items.potions.PotionOfStrength;
import com.jinkeloid.mispd.mechanics.Ballistica;
import com.jinkeloid.mispd.sprites.ScorpioSprite;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

public class Scorpio extends Mob {
	
	{
		spriteClass = ScorpioSprite.class;
		
		HP = HT = 110;
		attackSkill = 36;
		defenseSkill = 24;
		minDamage = 30;
		maxDamage = 40;
		minDR = 0;
		maxDR = 16;
		viewDistance = LightOld.DISTANCE;
		
		EXP = 140;
		maxLvl = 27;
		
		loot = Generator.Category.POTION;
		lootChance = 0.5f;

		properties.add(Property.DEMONIC);
	}
	
//	@Override
//	public int damageRoll() {
//		return Random.NormalIntRange( 30, 40 );
//	}
//
//	@Override
//	public int attackSkill( Char target ) {
//		return 36;
//	}
//
//	@Override
//	public int drRoll() {
//		return Random.NormalIntRange(0, 16);
//	}
	
	@Override
	protected boolean canAttack( Char enemy ) {
		Ballistica attack = new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE);
		return !Dungeon.level.adjacent( pos, enemy.pos ) && attack.collisionPos == enemy.pos;
	}
	
	@Override
	public int attackProc( Char enemy, int damage ) {
		damage = super.attackProc( enemy, damage );
		if (Random.Int( 2 ) == 0) {
			Buff.prolong( enemy, Cripple.class, Cripple.DURATION );
		}
		
		return damage;
	}
	
	@Override
	protected boolean getCloser( int target ) {
		if (state == HUNTING) {
			return enemySeen && getFurther( target );
		} else {
			return super.getCloser( target );
		}
	}
	
	@Override
	protected Item createLoot() {
		Class<?extends Potion> loot;
		do{
			loot = (Class<? extends Potion>) Random.oneOf(Generator.Category.POTION.classes);
		} while (loot == PotionOfHealing.class || loot == PotionOfStrength.class);

		return Reflection.newInstance(loot);
	}
	
}
