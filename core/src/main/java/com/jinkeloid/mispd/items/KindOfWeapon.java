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

package com.jinkeloid.mispd.items;

import com.jinkeloid.mispd.Assets;
import com.jinkeloid.mispd.Dungeon;
import com.jinkeloid.mispd.actors.Actor;
import com.jinkeloid.mispd.actors.Char;
import com.jinkeloid.mispd.actors.hero.Hero;
import com.jinkeloid.mispd.actors.hero.Perk;
import com.jinkeloid.mispd.items.weapon.missiles.MissileWeapon;
import com.jinkeloid.mispd.messages.Messages;
import com.jinkeloid.mispd.utils.BArray;
import com.jinkeloid.mispd.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

abstract public class KindOfWeapon extends EquipableItem {

	public static final String AC_EQUIPINST		= "EQUIPINST";

	protected static final float TIME_TO_EQUIP = 1f;

	protected String hitSound = Assets.Sounds.HIT;
	protected float hitSoundPitch = 1f;
	public static boolean instantSwitch = false;

	@Override
	public ArrayList<String> actions(Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.remove( isEquipped( hero ) ? AC_UNEQUIP : AC_EQUIP );
		actions.add( isEquipped( hero ) ? AC_UNEQUIP : (instantSwitch && (!MissileWeapon.class.isAssignableFrom(this.getClass()))) ? AC_EQUIPINST : AC_EQUIP );
		return actions;
	}

	@Override
	public boolean isEquipped( Hero hero ) {
		return hero.belongings.mainhand == this || hero.belongings.stashedWeapon == this;
	}
	
	@Override
	public boolean doEquip( Hero hero ) {

		detachAll( hero.belongings.backpack );

		boolean equip = instantSwitch ? hero.belongings.mainhand.doUnequip( hero, true, true, true) : hero.belongings.mainhand.doUnequip( hero, true);
		
		if (hero.belongings.mainhand == null || equip) {
			
			hero.belongings.mainhand = this;
			activate( hero );
			Perk.onItemEquipped(hero, this);
			updateQuickslot();

			cursedKnown = true;
			if (cursed) {
				equipCursed( hero );
				GLog.n( Messages.get(KindOfWeapon.class, "equip_cursed") );
			}

			//If hero have slowpoke, then switching and equipping weapon would cost them 10x turns
			Perk.onWeaponEquipTrigger();
			if (hero.hasPerk(Perk.SLOWPOKE))hero.spendAndNext( TIME_TO_EQUIP * 10 );
			//If hero have weapon switch perk then let them switch weapon instantly
			else if (!instantSwitch)hero.spendAndNext( TIME_TO_EQUIP );
			else {
			instantSwitch = false;
			GLog.i(Messages.get(KindOfWeapon.class, "instant_switch"));
			}
			//If hero have slowpoke, grant random debuff when the weapon is equipped
//			if (hero.hasPerk(Perk.SLOWPOKE)){
//				switch (Random.Int(6)){
//					case 0: default:    Buff.affect(hero, Cripple.class, 5f);      break;
//					case 1:             Buff.affect(hero, ChampionEnemy.Projecting.class);   break;
//					case 2:             Buff.affect(hero, ChampionEnemy.AntiMagic.class);    break;
//					case 3:             Buff.affect(hero, ChampionEnemy.Giant.class);        break;
//					case 4:             Buff.affect(hero, ChampionEnemy.Blessed.class);      break;
//					case 5:             Buff.affect(hero, ChampionEnemy.Growing.class);      break;
//				}
//			}
			return true;
		} else {
			
			collect( hero.belongings.backpack, false);
			return false;
		}
	}

	@Override
	public boolean doUnequip( Hero hero, boolean collect, boolean single ) {
		if (super.doUnequip( hero, collect, single )) {

			hero.belongings.mainhand = null;
			return true;

		} else {

			return false;

		}
	}

	public int min(){
		return min(buffedLvl());
	}

	public int max(){
		return max(buffedLvl());
	}

	abstract public int min(int lvl);
	abstract public int max(int lvl);

	public int damageRoll( Char owner ) {
		return Random.NormalIntRange( min(), max() );
	}
	
	public float accuracyFactor( Char owner ) {
		return 1f;
	}
	
	public float speedFactor( Char owner ) {
		return 1f;
	}

	public int reachFactor( Char owner ){
		return 1;
	}
	
	public boolean canReach( Char owner, int target){
		if (Dungeon.level.distance( owner.pos, target ) > reachFactor(owner)){
			return false;
		} else {
			boolean[] passable = BArray.not(Dungeon.level.solid, null);
			for (Char ch : Actor.chars()) {
				if (ch != owner) passable[ch.pos] = false;
			}
			
			PathFinder.buildDistanceMap(target, passable, reachFactor(owner));
			
			return PathFinder.distance[owner.pos] <= reachFactor(owner);
		}
	}

	public int defenseFactor( Char owner ) {
		return 0;
	}
	
	public int proc( Char attacker, Char defender, int damage ) {
		return damage;
	}

	public void hitSound( float pitch ){
		Sample.INSTANCE.play(hitSound, 1, pitch * hitSoundPitch);
	}
	
}
