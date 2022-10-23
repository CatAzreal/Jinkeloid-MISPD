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

import com.jinkeloid.mispd.Badges;
import com.jinkeloid.mispd.actors.Char;
import com.jinkeloid.mispd.items.Generator;
import com.jinkeloid.mispd.messages.Messages;
import com.jinkeloid.mispd.sprites.SnakeSprite;
import com.jinkeloid.mispd.utils.GLog;
import com.watabou.utils.Random;

public class Snake extends Mob {
	
	{
		spriteClass = SnakeSprite.class;
		
		HP = HT = 4;
		attackSkill = 10;
		defenseSkill = 18;
		minDamage = 1;
		maxDamage = 4;
		
		EXP = 20;
		maxLvl = 7;
		
		loot = Generator.Category.SEED;
		lootChance = 0.25f;
	}
	
//	@Override
//	public int damageRoll() {
//		return Random.NormalIntRange( 1, 4 );
//	}
//
//	@Override
//	public int attackSkill( Char target ) {
//		return 10;
//	}

	private static int dodges = 0;

	@Override
	public String defenseVerb() {
		dodges++;
		if (dodges >= 3 && !Badges.isUnlocked(Badges.Badge.BOSS_SLAIN_1)){
			GLog.h(Messages.get(this, "hint"));
			dodges = 0;
		}
		return super.defenseVerb();
	}
}
