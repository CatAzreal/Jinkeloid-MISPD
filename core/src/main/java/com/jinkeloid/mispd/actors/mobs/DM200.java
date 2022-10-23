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
import com.jinkeloid.mispd.actors.blobs.Blob;
import com.jinkeloid.mispd.actors.blobs.ToxicGas;
import com.jinkeloid.mispd.items.Generator;
import com.jinkeloid.mispd.items.Item;
import com.jinkeloid.mispd.mechanics.Ballistica;
import com.jinkeloid.mispd.scenes.GameScene;
import com.jinkeloid.mispd.sprites.DM200Sprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class DM200 extends Mob {

	{
		spriteClass = DM200Sprite.class;

		HP = HT = 80;
		attackSkill = 20;
		defenseSkill = 12;
		minDamage = 10;
		maxDamage = 25;
		minDR = 0;
		maxDR = 8;

		EXP = 90;
		maxLvl = 17;

		loot = Random.oneOf(Generator.Category.WEAPON, Generator.Category.ARMOR);
		lootChance = 0.125f; //initially, see rollToDropLoot

		properties.add(Property.INORGANIC);
		properties.add(Property.LARGE);

		HUNTING = new Hunting();
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

	@Override
	public void rollToDropLoot() {
		//each drop makes future drops 1/2 as likely
		// so loot chance looks like: 1/8, 1/16, 1/32, 1/64, etc.
		lootChance *= Math.pow(1/2f, Dungeon.LimitedDrops.DM200_EQUIP.count);
		super.rollToDropLoot();
	}

	protected Item createLoot() {
		Dungeon.LimitedDrops.DM200_EQUIP.count++;
		//uses probability tables for dwarf city
		if (loot == Generator.Category.WEAPON){
			return Generator.randomWeapon(4);
		} else {
			return Generator.randomArmor(4);
		}
	}

	private int ventCooldown = 0;

	private static final String VENT_COOLDOWN = "vent_cooldown";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(VENT_COOLDOWN, ventCooldown);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		ventCooldown = bundle.getInt( VENT_COOLDOWN );
	}

	@Override
	protected boolean act() {
		ventCooldown--;
		return super.act();
	}

	public void onZapComplete(){
		zap();
		next();
	}

	private void zap( ){
		spend( TICK );
		ventCooldown = 30;

		Ballistica trajectory = new Ballistica(pos, enemy.pos, Ballistica.STOP_TARGET);

		for (int i : trajectory.subPath(0, trajectory.dist)){
			GameScene.add(Blob.seed(i, 20, ToxicGas.class));
		}
		GameScene.add(Blob.seed(trajectory.collisionPos, 100, ToxicGas.class));

	}

	private class Hunting extends Mob.Hunting{

		@Override
		public boolean act(boolean enemyInFOV, boolean justAlerted) {
			if (!enemyInFOV || canAttack(enemy)) {
				return super.act(enemyInFOV, justAlerted);
			} else {
				enemySeen = true;
				target = enemy.pos;

				int oldPos = pos;

				if (ventCooldown <= 0 && distance(enemy) >= 1 && Random.Int(100/distance(enemy)) == 0){
					if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
						sprite.zap( enemy.pos );
						return false;
					} else {
						zap();
						return true;
					}

				} else if (getCloser( target )) {
					spend( 1 / speed() );
					return moveSprite( oldPos,  pos );

				} else if (ventCooldown <= 0) {
					if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
						sprite.zap( enemy.pos );
						return false;
					} else {
						zap();
						return true;
					}

				} else {
					spend( TICK );
					return true;
				}

			}
		}
	}

}
