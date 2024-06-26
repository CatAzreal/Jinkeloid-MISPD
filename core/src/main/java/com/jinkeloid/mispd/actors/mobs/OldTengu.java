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

import com.jinkeloid.mispd.Assets;
import com.jinkeloid.mispd.Badges;
import com.jinkeloid.mispd.Dungeon;
import com.jinkeloid.mispd.actors.Actor;
import com.jinkeloid.mispd.actors.Char;
import com.jinkeloid.mispd.actors.buffs.LockedFloor;
import com.jinkeloid.mispd.actors.hero.HeroSubClass;
import com.jinkeloid.mispd.effects.CellEmitter;
import com.jinkeloid.mispd.effects.Speck;
import com.jinkeloid.mispd.items.TomeOfMastery;
import com.jinkeloid.mispd.items.artifacts.DriedRose;
import com.jinkeloid.mispd.items.artifacts.LloydsBeacon;
import com.jinkeloid.mispd.items.scrolls.ScrollOfMagicMapping;
import com.jinkeloid.mispd.levels.Level;
import com.jinkeloid.mispd.levels.OldPrisonBossLevel;
import com.jinkeloid.mispd.levels.Terrain;
import com.jinkeloid.mispd.levels.traps.GrippingTrap;
import com.jinkeloid.mispd.mechanics.Ballistica;
import com.jinkeloid.mispd.messages.Messages;
import com.jinkeloid.mispd.scenes.GameScene;
import com.jinkeloid.mispd.sprites.TenguSprite;
import com.jinkeloid.mispd.ui.BossHealthBar;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

//Exists to support pre-0.7.5 saves
public class OldTengu extends Mob {
	
	{
		spriteClass = TenguSprite.class;
		
		HP = HT = 120;
		EXP = 200;
		attackSkill = 20;
		defenseSkill = 20;
		minDamage = 6;
		maxDamage = 20;
		minDR = 0;
		maxDR = 5;

		HUNTING = new Hunting();

		flying = true; //doesn't literally fly, but he is fleet-of-foot enough to avoid hazards

		properties.add(Property.BOSS);
	}
	
	@Override
	protected void onAdd() {
		//when he's removed and re-added to the fight, his time is always set to now.
		spend(-cooldown());
		super.onAdd();
	}

//	@Override
//	public int damageRoll() {
//		return Random.NormalIntRange( 6, 20 );
//	}
//
//	@Override
//	public int attackSkill( Char target ) {
//		return 20;
//	}
//
//	@Override
//	public int drRoll() {
//		return Random.NormalIntRange(0, 5);
//	}

	@Override
	public void damage(int dmg, Object src) {
		
		OldPrisonBossLevel.State state = ((OldPrisonBossLevel)Dungeon.level).state();
		
		int hpBracket;
		if (state == OldPrisonBossLevel.State.FIGHT_START){
			hpBracket = 12;
		} else {
			hpBracket = 20;
		}

		int beforeHitHP = HP;
		super.damage(dmg, src);
		dmg = beforeHitHP - HP;

		LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
		if (lock != null) {
			int multiple = state == OldPrisonBossLevel.State.FIGHT_START ? 1 : 4;
			lock.addTime(dmg*multiple);
		}

		//phase 2 of the fight is over
		if (HP == 0 && state == OldPrisonBossLevel.State.FIGHT_ARENA) {
			//let full attack action complete first
			Actor.add(new Actor() {
				
				{
					actPriority = VFX_PRIO;
				}
				
				@Override
				protected boolean act() {
					Actor.remove(this);
					((OldPrisonBossLevel)Dungeon.level).progress();
					return true;
				}
			});
			return;
		}
		
		//phase 1 of the fight is over
		if (state == OldPrisonBossLevel.State.FIGHT_START && HP <= HT/2){
			HP = (HT/2)-1;
			yell(Messages.get(this, "interesting"));
			((OldPrisonBossLevel)Dungeon.level).progress();
			BossHealthBar.bleed(true);

		//if tengu has lost a certain amount of hp, jump
		} else if (beforeHitHP / hpBracket != HP / hpBracket) {
			jump();
		}
	}

	@Override
	public boolean isAlive() {
		return Dungeon.level.mobs.contains(this); //Tengu has special death rules, see prisonbosslevel.progress()
	}

	@Override
	public void die( Object cause ) {
		
		if (Dungeon.hero.subClass == HeroSubClass.NONE) {
			Dungeon.level.drop( new TomeOfMastery(), pos ).sprite.drop();
		}
		
		GameScene.bossSlain();
		super.die( cause );
		
		Badges.validateBossSlain();

		LloydsBeacon beacon = Dungeon.hero.belongings.getItem(LloydsBeacon.class);
		if (beacon != null) {
			beacon.upgrade();
		}
		
		yell( Messages.get(this, "defeated") );
	}

	@Override
	protected boolean canAttack( Char enemy ) {
		return new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE).collisionPos == enemy.pos;
	}

	//tengu's attack is always visible
	@Override
	protected boolean doAttack(Char enemy) {
		sprite.attack( enemy.pos );
		spend( attackDelay() );
		return false;
	}

	private void jump() {
		
		Level level = Dungeon.level;
		
		//incase tengu hasn't had a chance to act yet
		if (fieldOfView == null || fieldOfView.length != Dungeon.level.length()){
			fieldOfView = new boolean[Dungeon.level.length()];
			Dungeon.level.updateFieldOfView( this, fieldOfView );
		}
		
		if (enemy == null) enemy = chooseEnemy();
		if (enemy == null) return;

		int newPos;
		//if we're in phase 1, want to warp around within the room
		if (((OldPrisonBossLevel)Dungeon.level).state() == OldPrisonBossLevel.State.FIGHT_START) {
			
			//place new traps
			int tries;
			for (int i=0; i < 4; i++) {
				int trapPos;
				tries = 15;
				do {
					trapPos = Random.Int( level.length() );
				} while (tries-- > 0 && level.map[trapPos] != Terrain.INACTIVE_TRAP
						&& level.map[trapPos] != Terrain.TRAP);
				
				if (level.map[trapPos] == Terrain.INACTIVE_TRAP) {
					level.setTrap( new GrippingTrap().reveal(), trapPos );
					Level.set( trapPos, Terrain.TRAP );
					ScrollOfMagicMapping.discover( trapPos );
				}
			}
			
			tries = 50;
			do {
				newPos = Random.IntRange(3, 7) + 32*Random.IntRange(26, 30);
			} while ( (level.adjacent(newPos, enemy.pos) || Actor.findChar(newPos) != null)
					&& --tries > 0);
			if (tries <= 0) return;

		//otherwise go wherever, as long as it's a little bit away
		} else {
			do {
				newPos = Random.Int(level.length());
			} while (
					level.solid[newPos] ||
					level.distance(newPos, enemy.pos) < 8 ||
					Actor.findChar(newPos) != null);
		}
		
		if (level.heroFOV[pos]) CellEmitter.get( pos ).burst( Speck.factory( Speck.WOOL ), 6 );

		sprite.move( pos, newPos );
		move( newPos );
		
		if (level.heroFOV[newPos]) CellEmitter.get( newPos ).burst( Speck.factory( Speck.WOOL ), 6 );
		Sample.INSTANCE.play( Assets.Sounds.PUFF );
		
		spend( 1 / speed() );
	}
	
	@Override
	public void notice() {
		super.notice();
		if (!BossHealthBar.isAssigned()) {
			BossHealthBar.assignBoss(this);
			if (HP <= HT/2) BossHealthBar.bleed(true);
			if (HP == HT) {
				yell(Messages.get(this, "notice_mine", Dungeon.hero.name()));
				for (Char ch : Actor.chars()){
					if (ch instanceof DriedRose.GhostHero){
						((DriedRose.GhostHero) ch).sayBoss();
					}
				}
			} else {
				yell(Messages.get(this, "notice_face", Dungeon.hero.name()));
			}
		}
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		BossHealthBar.assignBoss(this);
		if (HP <= HT/2) BossHealthBar.bleed(true);
	}

	//tengu is always hunting
	private class Hunting extends Mob.Hunting{

		@Override
		public boolean act(boolean enemyInFOV, boolean justAlerted) {
			enemySeen = enemyInFOV;
			if (enemyInFOV && !isCharmedBy( enemy ) && canAttack( enemy )) {

				return doAttack( enemy );

			} else {

				if (enemyInFOV) {
					target = enemy.pos;
				} else {
					chooseEnemy();
					if (enemy != null) {
						target = enemy.pos;
					}
				}

				spend( TICK );
				return true;

			}
		}
	}
}
