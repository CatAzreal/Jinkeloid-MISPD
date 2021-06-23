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

import com.jinkeloid.mispd.actors.Char;
import com.jinkeloid.mispd.items.food.MysteryMeat;
import com.jinkeloid.mispd.sprites.CrabSprite;
import com.watabou.utils.Random;

public class Crab extends Mob {

	{
		spriteClass = CrabSprite.class;
		
		HP = HT = 15;
		attackSkill = 12;
		defenseSkill = 5;
		baseSpeed = 2f;
		minDamage = 1;
		maxDamage = 7;
		minDR = 0;
		maxDR = 4;

		EXP = 4;
		maxLvl = 9;
		
		loot = new MysteryMeat();
		lootChance = 0.167f;
	}
	
//	@Override
//	public int damageRoll() {
//		return Random.NormalIntRange( minDamage, maxDamage );
//	}
//
//	@Override
//	public int attackSkill( Char target ) {
//		return attackSkill;
//	}
//
//	@Override
//	public int drRoll() {
//		return Random.NormalIntRange(minDR, maxDR);
//	}
}
