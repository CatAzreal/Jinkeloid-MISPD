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
import com.jinkeloid.mispd.items.Gold;
import com.jinkeloid.mispd.sprites.GnollSprite;
import com.watabou.utils.Random;

public class Gnoll extends Mob {
	
	{
		spriteClass = GnollSprite.class;
		
		HP = HT = 12;
		attackSkill = 10;
		defenseSkill = 4;
		minDamage = 1;
		maxDamage = 6;
		minDR = 0;
		maxDR = 2;
		
		EXP = 20;
		maxLvl = 8;
		
		loot = Gold.class;
		lootChance = 0.5f;
	}
	
//	@Override
//	public int damageRoll() {
//		return Random.NormalIntRange( 1, 6 );
//	}
//
//	@Override
//	public int attackSkill( Char target ) {
//		return 10;
//	}
//
//	@Override
//	public int drRoll() {
//		return Random.NormalIntRange(0, 2);
//	}
}
