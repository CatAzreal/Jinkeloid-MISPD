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

package com.jinkeloid.mispd.actors.hero;

import com.jinkeloid.mispd.Assets;
import com.jinkeloid.mispd.Dungeon;
import com.jinkeloid.mispd.actors.Actor;
import com.jinkeloid.mispd.actors.Char;
import com.jinkeloid.mispd.actors.buffs.ArtifactRecharge;
import com.jinkeloid.mispd.actors.buffs.Buff;
import com.jinkeloid.mispd.actors.buffs.CounterBuff;
import com.jinkeloid.mispd.actors.buffs.Emptyoffhand;
import com.jinkeloid.mispd.actors.buffs.EnhancedRings;
import com.jinkeloid.mispd.actors.buffs.FlavourBuff;
import com.jinkeloid.mispd.actors.buffs.Haste;
import com.jinkeloid.mispd.actors.buffs.Recharging;
import com.jinkeloid.mispd.actors.buffs.Roots;
import com.jinkeloid.mispd.actors.buffs.Satiation;
import com.jinkeloid.mispd.actors.buffs.WandEmpower;
import com.jinkeloid.mispd.actors.mobs.Mob;
import com.jinkeloid.mispd.effects.CellEmitter;
import com.jinkeloid.mispd.effects.Speck;
import com.jinkeloid.mispd.effects.SpellSprite;
import com.jinkeloid.mispd.effects.particles.LeafParticle;
import com.jinkeloid.mispd.items.BrokenSeal;
import com.jinkeloid.mispd.items.Item;
import com.jinkeloid.mispd.items.armor.Armor;
import com.jinkeloid.mispd.items.artifacts.CloakOfShadows;
import com.jinkeloid.mispd.items.artifacts.HornOfPlenty;
import com.jinkeloid.mispd.items.rings.Ring;
import com.jinkeloid.mispd.items.scrolls.ScrollOfRecharging;
import com.jinkeloid.mispd.items.wands.Wand;
import com.jinkeloid.mispd.items.weapon.Weapon;
import com.jinkeloid.mispd.items.weapon.melee.MagesStaff;
import com.jinkeloid.mispd.items.weapon.melee.MeleeWeapon;
import com.jinkeloid.mispd.items.weapon.missiles.MissileWeapon;
import com.jinkeloid.mispd.levels.Level;
import com.jinkeloid.mispd.levels.Terrain;
import com.jinkeloid.mispd.messages.Messages;
import com.jinkeloid.mispd.scenes.GameScene;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public enum Perk {

	//Positive Perks
	CATS_EYES(0, 1, perkType.POSITIVE, PerkIndex.CAT_EYE, new int[]{PerkIndex.SHORT_SIGHTED, PerkIndex.NYCTALOPIA}),
	//viewrange + 1(if game works like before it won't exceed 8)
	INCONSPICUOUS(0, 2, perkType.POSITIVE, PerkIndex.INCONSPICUOUS, new int[]{PerkIndex.CONSPICUOUS}),
	//basically ring of stealth
	QUICK_DRAW(0, 3, perkType.POSITIVE, PerkIndex.QUICK_DRAW, new int[]{PerkIndex.SLOWPOKE}),
	//every turn's first weapon switch is instant
	ON_DIET(0, 3, perkType.POSITIVE, PerkIndex.ON_DIET, new int[]{PerkIndex.BIG_STOMACH}, new int[]{PerkIndex.ABSTINENCE}),
	//slower hunger build up time
	ABSTINENCE(0, 4, perkType.POSITIVE, PerkIndex.ABSTINENCE, new int[]{PerkIndex.BIG_STOMACH}, new int[]{PerkIndex.ON_DIET}),
	//replaces hunger debuff with weaker version
	DEXTEROUS(0, 3, perkType.POSITIVE, PerkIndex.DEXTEROUS, new int[]{PerkIndex.CLUMSY}),
	//every turn's first throwable use is instant
	ORGANIZED(0, 4, perkType.POSITIVE, PerkIndex.ORGANIZED, new int[]{PerkIndex.DISORGANIZED}),
	//+backpacksize
	LUCKY(0, 4, perkType.POSITIVE, PerkIndex.LUCKY, new int[]{PerkIndex.UNLUCKY}),
	//ring of wealth + 2
	QUICK_LEARNER(0, 5, perkType.POSITIVE, PerkIndex.QUICK_LEARNER, null),
	//faster exp gain, increase the effective exp gain level of monsters by 1
	STOUT(0, 6, perkType.POSITIVE, PerkIndex.STOUT, new int[]{PerkIndex.VERY_STRONG}),
	//Every 10 level will grant you 1 strength
//	IRON_LUNG(0, 5, perkType.POSITIVE, PerkIndex.IRON_LUNG, null),
//	//stamina+(not available yet)
//	ARMOR_PROFICIENCY(0, 6, perkType.POSITIVE, PerkIndex.ARMOR_PROFICIENCY, new int[]{PerkIndex.ROOKIE}),
//	//changing armor would cost 1/5 of usual time(armor changing turn cost would be significantly increased), grant bonus depending on armor type
	BIOLOGIST(0, 4, perkType.POSITIVE, PerkIndex.BIOLOGIST, new int[]{PerkIndex.ILLITERATE}),
	//show detailed info about mob, their health, dmgrange, dr and weakness(if have any)
	ADRENALINE(0, 7, perkType.POSITIVE, PerkIndex.ADRENALINE, null),
//	//the horror of enemy can stimulate you, strengthening reflex, resulting less penalty on accuracy and bonus to attackspeed and movement speed
	BRAVE(0, 5, perkType.POSITIVE, PerkIndex.BRAVE, new int[]{PerkIndex.PACIFIST}, new int[]{PerkIndex.CONFIDENT}),
//	//slower horror increase on low health, more damage output on low health
	CONFIDENT(0, 5, perkType.POSITIVE, PerkIndex.CONFIDENT, new int[]{PerkIndex.PACIFIST}, new int[]{PerkIndex.BRAVE}),
//	//slower horror increase on near full health, more accuracy near full health
	STURDY(0, 6, perkType.POSITIVE, PerkIndex.STURDY, new int[]{PerkIndex.FRAIL}),
	//+3 starting health, +2 max health per level
	VERY_STRONG(0, 10, perkType.POSITIVE, PerkIndex.VERY_STRONG, new int[]{PerkIndex.STOUT}),
	//+1 starting strength, +1 strength per 7 level
	GOBBLER(0, 2, perkType.POSITIVE, PerkIndex.GOBBLER, new int[]{PerkIndex.NIBBLING}),
	//eat food at an extremely fast pace
	MIND_COMPASS(0, 2, perkType.POSITIVE, PerkIndex.MIND_COMPASS, new int[]{PerkIndex.AMNESIA}),
	//eat food at an extremely fast pace

	//Negative Perks
	SHORT_SIGHTED(0, 1, perkType.NEGATIVE, PerkIndex.SHORT_SIGHTED, new int[]{PerkIndex.CAT_EYE, PerkIndex.NYCTALOPIA}, new int[]{PerkIndex.NYCTALOPIA}),
	//viewrange - 1(to minimum of 2)
	CONSPICUOUS(0, 2, perkType.NEGATIVE, PerkIndex.CONSPICUOUS, new int[]{PerkIndex.INCONSPICUOUS}),
	//basically cursed ring of stealth
	SLOWPOKE(0, 1, perkType.NEGATIVE, PerkIndex.SLOWPOKE, new int[]{PerkIndex.QUICK_DRAW}),
	//switching weapon during battle would be a really bad idea
	BIG_STOMACH(0, 3, perkType.NEGATIVE, PerkIndex.BIG_STOMACH, new int[]{PerkIndex.ON_DIET, PerkIndex.ABSTINENCE}),
	//faster hunger build up
	CLUMSY(0, 3, perkType.NEGATIVE, PerkIndex.CLUMSY, new int[]{PerkIndex.DEXTEROUS}),
	//using consumable could fail and skip a whole turn(30% chance)
	DISORGANIZED(0, 4, perkType.NEGATIVE, PerkIndex.DISORGANIZED, new int[]{PerkIndex.ORGANIZED}),
	//-backpacksize
	UNLUCKY(0, 4, perkType.NEGATIVE, PerkIndex.UNLUCKY, new int[]{PerkIndex.LUCKY}),
	//cursed ring of wealth
//	ROOKIE(0, 1, perkType.NEGATIVE, PerkIndex.ROOKIE, new int[]{PerkIndex.ARMOR_PROFICIENCY}),
//	you better choose somewhere safe to take off your armor, and each of them will provide you unique penalties
	ILLITERATE(0, 6, perkType.NEGATIVE, PerkIndex.ILLITERATE, new int[]{PerkIndex.BIOLOGIST}),
	//no journals, reading scrolls(except SoUs) may have unexpected outcome
	PACIFIST(0, 5, perkType.NEGATIVE, PerkIndex.PACIFIST, new int[]{PerkIndex.BRAVE, PerkIndex.CONFIDENT}),
//	You can never get used to the horror of combat, killing enemies does not halt your natrual horror growth
	NICTOPHOBIA(0, 7, perkType.NEGATIVE, PerkIndex.NICTOPHOBIA, new int[]{PerkIndex.BRAVE, PerkIndex.CONFIDENT}),
	//	You can never get used to the horror of combat, killing enemies does not halt your natrual horror growth
	NYCTALOPIA(0, 8, perkType.NEGATIVE, PerkIndex.NYCTALOPIA, new int[]{PerkIndex.CAT_EYE, PerkIndex.SHORT_SIGHTED}, new int[]{PerkIndex.SHORT_SIGHTED}),
	//You are (almost) blind, only the one tile around you are visible, good thing is you can still (barely) read scrolls and identify things
	AMNESIA(0, 6, perkType.NEGATIVE, PerkIndex.AMNESIA, new int[]{PerkIndex.MIND_COMPASS}),
	//when finding a way to somewhere, you always managed to get to the wrong way, not because you are stupid,
	//you are just not good at determining direction and remembering places, using scroll of magic mapping would have different outcomes
	LACK_OF_SENSE(0, 2, perkType.NEGATIVE, PerkIndex.LACK_OF_SENSE, null),
	//you sense is so low that you can't grasp the exact condition yourself is currently in
	//health and stamina will only display it's vague value
	UNRESPONSIVE(0, 4, perkType.NEGATIVE, PerkIndex.UNRESPONSIVE, null),
	//cannot surprise attack
	FRAIL(0, 6, perkType.NEGATIVE, PerkIndex.FRAIL, new int[]{PerkIndex.STURDY}),
	//-2 starting health, -2 max health per level
	NIBBLING(0, 2, perkType.NEGATIVE, PerkIndex.NIBBLING, new int[]{PerkIndex.GOBBLER}),
	//eat food slowly

	//Warrior T1
	HEARTY_MEAL(0), ARMSMASTERS_INTUITION(1), TEST_SUBJECT(2), IRON_WILL(3),
	//Warrior T2
	IRON_STOMACH(4), RESTORED_WILLPOWER(5), RUNIC_TRANSFERENCE(6), LETHAL_MOMENTUM(7), IMPROVISED_PROJECTILES(8),
	//Warrior T3
	HOLD_FAST(9, 3), STRONGMAN(10, 3),
	//Berserker T3
	ENDLESS_RAGE(11, 3), BERSERKING_STAMINA(12, 3), ENRAGED_CATALYST(13, 3),
	//Gladiator T3
	CLEAVE(14, 3), LETHAL_DEFENSE(15, 3), ENHANCED_COMBO(16, 3),

	//Mage T1
	EMPOWERING_MEAL(32), SCHOLARS_INTUITION(33), TESTED_HYPOTHESIS(34), BACKUP_BARRIER(35),
	//Mage T2
	ENERGIZING_MEAL(36), ENERGIZING_UPGRADE(37), WAND_PRESERVATION(38), ARCANE_VISION(39), SHIELD_BATTERY(40),
	//Mage T3
	EMPOWERING_SCROLLS(41, 3), ALLY_WARP(42, 3),
	//Battlemage T3
	EMPOWERED_STRIKE(43, 3), MYSTICAL_CHARGE(44, 3), EXCESS_CHARGE(45, 3),
	//Warlock T3
	SOUL_EATER(46, 3), SOUL_SIPHON(47, 3), NECROMANCERS_MINIONS(48, 3),

	//Rogue T1
	CACHED_RATIONS(64), THIEFS_INTUITION(65), SUCKER_PUNCH(66), PROTECTIVE_SHADOWS(67),
	//Rogue T2
	MYSTICAL_MEAL(68), MYSTICAL_UPGRADE(69), WIDE_SEARCH(70), SILENT_STEPS(71), ROGUES_FORESIGHT(72),
	//Rogue T3
	ENHANCED_RINGS(73, 3), LIGHT_CLOAK(74, 3),
	//Assassin T3
	ENHANCED_LETHALITY(75, 3), ASSASSINS_REACH(76, 3), BOUNTY_HUNTER(77, 3),
	//Freerunner T3
	EVASIVE_ARMOR(78, 3), PROJECTILE_MOMENTUM(79, 3), SPEEDY_STEALTH(80, 3),

	//Huntress T1
	NATURES_BOUNTY(96), SURVIVALISTS_INTUITION(97), FOLLOWUP_STRIKE(98), NATURES_AID(99),
	//Huntress T2
	INVIGORATING_MEAL(100), RESTORED_NATURE(101), REJUVENATING_STEPS(102), HEIGHTENED_SENSES(103), DURABLE_PROJECTILES(104),
	//Huntress T3
	POINT_BLANK(105, 3), SEER_SHOT(106, 3),
	//Sniper T3
	FARSIGHT(107, 3), SHARED_ENCHANTMENT(108, 3), SHARED_UPGRADES(109, 3),
	//Warden T3
	DURABLE_TIPS(110, 3), BARKSKIN(111, 3), SHIELDING_DEW(112, 3);

	public static class ImprovisedProjectileCooldown extends FlavourBuff{};
	public static class LethalMomentumTracker extends FlavourBuff{};
	public static class WandPreservationCounter extends CounterBuff{};
	public static class EmpoweredStrikeTracker extends FlavourBuff{};
	public static class BountyHunterTracker extends FlavourBuff{};
	public static class RejuvenatingStepsCooldown extends FlavourBuff{};
	public static class SeerShotCooldown extends FlavourBuff{};

	int icon;
	int pointCosts;
	perkType type;
	//positive perks have ids from 100-199; while negative perks have ids from 200-299, which are more than enough
	int id;
	//Store the conflicting perk, which will be disabled when current perk is selected
	int[] conflictPerks;
	//Store the pairing Perk, without both this perk and its pairing perks unselected the conflicting perk will remain locked
	int[] pairingPerks;

	//This method is needed to let the menu know which perk is their counterpart,
	//while preset int array is a better idea it can be difficult to maintain

	// tiers 1/2/3/4 start at levels 2/7/13/21
	public static int[] tierLevelThresholds = new int[]{0, 2, 7, 13, 21, 31};
	// Character points needed to advance to the next difficulty level
	public static int[] difficultyThresholds = new int[]{30, 15, 8, 0, -8, -15, -30};

	Perk(int icon){
		this.icon = icon;
		this.pointCosts = 1;
		this.type = perkType.NOT_APPLICABLE;
		this.id = 0;
		this.conflictPerks = null;
		this.pairingPerks = null;
	}

	Perk(int icon, int pointCosts){
		this.icon = icon;
		this.pointCosts = pointCosts;
		this.type = perkType.NOT_APPLICABLE;
		this.id = 0;
		this.conflictPerks = null;
		this.pairingPerks = null;
	}

	Perk(int icon, int pointCosts, perkType type, int id, int[] conflictPerks){
		this.icon = icon;
		this.pointCosts = pointCosts;
		this.type = type;
		this.id = id;
		this.conflictPerks = conflictPerks;
		this.pairingPerks = null;
	}

	Perk(int icon, int pointCosts, perkType type, int id, int[] conflictPerks, int[] pairingPerks){
		this.icon = icon;
		this.pointCosts = pointCosts;
		this.type = type;
		this.id = id;
		this.conflictPerks = conflictPerks;
		this.pairingPerks = pairingPerks;
	}


	public static List<Perk> getPerksByType(perkType type){
		//get all perks
		List<Perk> perkList = new ArrayList<>(EnumSet.allOf(Perk.class));
		List<Perk> removeList = new ArrayList<>();
		Perk temp;
		//sort the perks
		for(int i=0; i < perkList.size(); i++){
			for(int j=1; j < (perkList.size()-i); j++){
				if(perkList.get(j - 1).id > perkList.get(j).id){
					temp = perkList.get(j - 1);
					perkList.set(j - 1, perkList.get(j));
					perkList.set(j, temp);
				}
			}
		}
		//pick
		for (Perk perk : perkList) {
			if (perk.type != type){
				removeList.add(perk);
			}
		}
		for (Perk perk : removeList){
			perkList.remove(perk);
		}
		return perkList;
	}

	public static String getPerkNameByID(int id){
		List<Perk> perkList = new ArrayList<>(EnumSet.allOf(Perk.class));
		for (Perk perk : perkList){
			if (perk.id == id && perk.type == perkType.POSITIVE){
				return "{"+perk.title()+"{";
			} else if (perk.id == id && perk.type == perkType.NEGATIVE){
				return "}"+perk.title()+"}";
			}
		}
		return null;
	}

	public static Perk getPerkByID(int id){
		List<Perk> perkList = new ArrayList<>(EnumSet.allOf(Perk.class));
		for (Perk perk : perkList){
			if (perk.id == id){
				return perk;
			}
		}
		return null;
	}

	public enum perkType {
		POSITIVE,
		NEGATIVE,
		NOT_APPLICABLE
	}

	public int icon(){
		return icon;
	}

	public int pointCosts(){
		return pointCosts;
	}

	public int id(){
		return id;
	}

	public int[] conflictPerks() { return conflictPerks; }

	public int[] pairingPerks() { return pairingPerks; }
	//currently only positive perk check is sufficient, might need more types later
	public boolean isPositive() { return type == perkType.POSITIVE; }

	public String title(){
		return Messages.get(this, name() + ".title");
	}

	public String desc(){
		return Messages.get(this, name() + ".desc");
	}

	//Triggers are for event tagging only

	//Determining if a perk has effect on hero's status
	public static void onHeroStatusTrigger(){}
	//Determining if a perk has effect on Health bar related action
	public static void onHealthBarTrigger(){}
	//View range
	public static void onViewRangeTrigger(){}
	//Satiation
	public static void onSatiationTrigger(){}
	//Inventory
	public static void onInventoryTrigger(){}
	//Fog of War
	public static void onFOWTrigger(){}
	//Stealth
	public static void onStealthTrigger(){}
	//mobInfo
	public static void onMobInfoTrigger(){}
	//throwing things
	public static void onItemThrowTrigger(){}
	//Equipping Weapons
	public static void onWeaponEquipTrigger(){}

	public static void onTalentUpgraded( Hero hero, Perk perk){
		if (perk == NATURES_BOUNTY){
			if ( hero.pointsInTalent(NATURES_BOUNTY) == 1) Buff.count(hero, NatureBerriesAvailable.class, 4);
			else                                           Buff.count(hero, NatureBerriesAvailable.class, 2);
		}

		if (perk == ARMSMASTERS_INTUITION && hero.pointsInTalent(ARMSMASTERS_INTUITION) == 2){
			if (hero.belongings.mainhand != null) hero.belongings.mainhand.identify();
			if (hero.belongings.armor != null)  hero.belongings.armor.identify();
		}
		if (perk == THIEFS_INTUITION && hero.pointsInTalent(THIEFS_INTUITION) == 2){
			if (hero.belongings.ring instanceof Ring) hero.belongings.ring.identify();
			if (hero.belongings.misc instanceof Ring) hero.belongings.misc.identify();
			for (Item item : Dungeon.hero.belongings){
				if (item instanceof Ring){
					((Ring) item).setKnown();
				}
			}
		}
		if (perk == THIEFS_INTUITION && hero.pointsInTalent(THIEFS_INTUITION) == 1){
			if (hero.belongings.ring instanceof Ring) hero.belongings.ring.setKnown();
			if (hero.belongings.misc instanceof Ring) ((Ring) hero.belongings.misc).setKnown();
		}

		if (perk == FARSIGHT){
			Dungeon.observe();
		}
	}

	public static class CachedRationsDropped extends CounterBuff{};
	public static class NatureBerriesAvailable extends CounterBuff{};

	public static void onFoodEaten( Hero hero, float foodVal, Item foodSource ){
		if (hero.hasPerk(HEARTY_MEAL)){
			//3/5 HP healed, when hero is below 25% health
			if (hero.HP <= hero.HT/4) {
				hero.HP = Math.min(hero.HP + 1 + 2 * hero.pointsInTalent(HEARTY_MEAL), hero.HT);
				hero.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1+hero.pointsInTalent(HEARTY_MEAL));
			//2/3 HP healed, when hero is below 50% health
			} else if (hero.HP <= hero.HT/2){
				hero.HP = Math.min(hero.HP + 1 + hero.pointsInTalent(HEARTY_MEAL), hero.HT);
				hero.sprite.emitter().burst(Speck.factory(Speck.HEALING), hero.pointsInTalent(HEARTY_MEAL));
			}
		}
		if (hero.hasPerk(IRON_STOMACH)){
			if (hero.cooldown() > 0) {
				Buff.affect(hero, WarriorFoodImmunity.class, hero.cooldown());
			}
		}
		if (hero.hasPerk(EMPOWERING_MEAL)){
			//2/3 bonus wand damage for next 3 zaps
			Buff.affect( hero, WandEmpower.class).set(1 + hero.pointsInTalent(EMPOWERING_MEAL), 3);
			ScrollOfRecharging.charge( hero );
		}
		if (hero.hasPerk(ENERGIZING_MEAL)){
			//5/8 turns of recharging
			Buff.prolong( hero, Recharging.class, 2 + 3*(hero.pointsInTalent(ENERGIZING_MEAL)) );
			ScrollOfRecharging.charge( hero );
		}
		if (hero.hasPerk(MYSTICAL_MEAL)){
			//3/5 turns of recharging
			Buff.affect( hero, ArtifactRecharge.class).set(1 + 2*(hero.pointsInTalent(MYSTICAL_MEAL))).ignoreHornOfPlenty = foodSource instanceof HornOfPlenty;
			ScrollOfRecharging.charge( hero );
		}
		if (hero.hasPerk(INVIGORATING_MEAL)){
			//effectively 1/2 turns of haste
			Buff.prolong( hero, Haste.class, 0.67f+hero.pointsInTalent(INVIGORATING_MEAL));
		}
	}

	public static class WarriorFoodImmunity extends FlavourBuff{
		{ actPriority = HERO_PRIO+1; }
	}

	public static float itemIDSpeedFactor( Hero hero, Item item ){
		// 1.75x/2.5x speed with huntress talent
		float factor = 1f + hero.pointsInTalent(SURVIVALISTS_INTUITION)*0.75f;

		// 2x/instant for Warrior (see onItemEquipped)
		if (item instanceof MeleeWeapon || item instanceof Armor){
			factor *= 1f + hero.pointsInTalent(ARMSMASTERS_INTUITION);
		}
		// 3x/instant for mage (see Wand.wandUsed())
		if (item instanceof Wand){
			factor *= 1f + 2*hero.pointsInTalent(SCHOLARS_INTUITION);
		}
		// 2x/instant for rogue (see onItemEqupped), also id's type on equip/on pickup
		if (item instanceof Ring){
			factor *= 1f + hero.pointsInTalent(THIEFS_INTUITION);
		}
		return factor;
	}

	public static void onHealingPotionUsed( Hero hero ){
		if (hero.hasPerk(RESTORED_WILLPOWER)){
			BrokenSeal.WarriorShield shield = hero.buff(BrokenSeal.WarriorShield.class);
			if (shield != null){
				int shieldToGive = Math.round(shield.maxShield() * 0.33f*(1+hero.pointsInTalent(RESTORED_WILLPOWER)));
				shield.supercharge(shieldToGive);
			}
		}
		if (hero.hasPerk(RESTORED_NATURE)){
			ArrayList<Integer> grassCells = new ArrayList<>();
			for (int i : PathFinder.NEIGHBOURS8){
				grassCells.add(hero.pos+i);
			}
			Random.shuffle(grassCells);
			for (int cell : grassCells){
				Char ch = Actor.findChar(cell);
				if (ch != null){
					Buff.affect(ch, Roots.class, 1f + hero.pointsInTalent(RESTORED_NATURE));
				}
				if (Dungeon.level.map[cell] == Terrain.EMPTY ||
						Dungeon.level.map[cell] == Terrain.EMBERS ||
						Dungeon.level.map[cell] == Terrain.EMPTY_DECO){
					Level.set(cell, Terrain.GRASS);
					GameScene.updateMap(cell);
				}
				CellEmitter.get(cell).burst(LeafParticle.LEVEL_SPECIFIC, 4);
			}
			if (hero.pointsInTalent(RESTORED_NATURE) == 1){
				grassCells.remove(0);
				grassCells.remove(0);
				grassCells.remove(0);
			}
			for (int cell : grassCells){
				int t = Dungeon.level.map[cell];
				if ((t == Terrain.EMPTY || t == Terrain.EMPTY_DECO || t == Terrain.EMBERS
						|| t == Terrain.GRASS || t == Terrain.FURROWED_GRASS)
						&& Dungeon.level.plants.get(cell) == null){
					Level.set(cell, Terrain.HIGH_GRASS);
					GameScene.updateMap(cell);
				}
			}
			Dungeon.observe();
		}
	}

	public static void onUpgradeScrollUsed( Hero hero ){
		if (hero.hasPerk(ENERGIZING_UPGRADE)){
			MagesStaff staff = hero.belongings.getItem(MagesStaff.class);
			if (staff != null){
				staff.gainCharge(1 + hero.pointsInTalent(ENERGIZING_UPGRADE), true);
				ScrollOfRecharging.charge( Dungeon.hero );
				SpellSprite.show( hero, SpellSprite.CHARGE );
			}
		}
		if (hero.hasPerk(MYSTICAL_UPGRADE)){
			CloakOfShadows cloak = hero.belongings.getItem(CloakOfShadows.class);
			if (cloak != null){
				cloak.overCharge(1 + hero.pointsInTalent(MYSTICAL_UPGRADE));
				ScrollOfRecharging.charge( Dungeon.hero );
				SpellSprite.show( hero, SpellSprite.CHARGE );
			}
		}
	}

	public static void onArtifactUsed( Hero hero ){
		if (hero.hasPerk(ENHANCED_RINGS)){
			Buff.prolong(hero, EnhancedRings.class, 3f*hero.pointsInTalent(ENHANCED_RINGS));
		}
	}

	public static void onItemEquipped( Hero hero, Item item ){
		if (hero.pointsInTalent(ARMSMASTERS_INTUITION) == 2 && (item instanceof Weapon || item instanceof Armor)){
			item.identify();
		}
		if (hero.hasPerk(THIEFS_INTUITION) && item instanceof Ring){
			if (hero.pointsInTalent(THIEFS_INTUITION) == 2){
				item.identify();
			} else {
				((Ring) item).setKnown();
			}
		}
	}

	public static void onItemCollected( Hero hero, Item item ){
		if (hero.pointsInTalent(THIEFS_INTUITION) == 2){
			if (item instanceof Ring) ((Ring) item).setKnown();
		}
	}

	//note that IDing can happen in alchemy scene, so be careful with VFX here
	public static void onItemIdentified( Hero hero, Item item ){
		if (hero.hasPerk(TEST_SUBJECT)){
			//heal for 2/3 HP
			hero.HP = Math.min(hero.HP + 1 + hero.pointsInTalent(TEST_SUBJECT), hero.HT);
			Emitter e = hero.sprite.emitter();
			if (e != null) e.burst(Speck.factory(Speck.HEALING), hero.pointsInTalent(TEST_SUBJECT));
		}
		if (hero.hasPerk(TESTED_HYPOTHESIS)){
			//2/3 turns of wand recharging
			Buff.affect(hero, Recharging.class, 1f + hero.pointsInTalent(TESTED_HYPOTHESIS));
			ScrollOfRecharging.charge(hero);
		}
	}

	public static int onAttackProc( Hero hero, Char enemy, int dmg ){
		if (hero.hasPerk(Perk.SUCKER_PUNCH)
				&& enemy instanceof Mob && ((Mob) enemy).surprisedBy(hero)
				&& enemy.buff(SuckerPunchTracker.class) == null){
			dmg += Random.IntRange(hero.pointsInTalent(Perk.SUCKER_PUNCH) , 2);
			Buff.affect(enemy, SuckerPunchTracker.class);
		}

		if (hero.hasPerk(Perk.FOLLOWUP_STRIKE)) {
			if (hero.belongings.mainhand instanceof MissileWeapon) {
				Buff.affect(enemy, FollowupStrikeTracker.class);
			} else if (enemy.buff(FollowupStrikeTracker.class) != null){
				dmg += 1 + hero.pointsInTalent(FOLLOWUP_STRIKE);
				if (!(enemy instanceof Mob) || !((Mob) enemy).surprisedBy(hero)){
					Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG, 0.75f, 1.2f);
				}
				enemy.buff(FollowupStrikeTracker.class).detach();
			}
		}

		float totalDMGModifier = Satiation.satiationDMGBonus() +
				(hero.buff(Emptyoffhand.class) != null ? Emptyoffhand.dmgBonus : 0);
		if (hero.hasPerk(BRAVE) && (float)hero.HP/hero.HT < 0.25f)
			totalDMGModifier += 0.15f;

		return (int)Math.ceil(dmg*totalDMGModifier);
	}

	public static class SuckerPunchTracker extends Buff{};
	public static class FollowupStrikeTracker extends Buff{};

	public static final int MAX_TALENT_TIERS = 3;

	public static void initClassTalents( Hero hero ){
		initClassTalents( hero.heroClass, hero.perks);
	}

	public static void initClassTalents( HeroClass cls, ArrayList<Perk> talents ){
//		while (talents.size() < MAX_TALENT_TIERS){
//			talents.add(new LinkedHashMap<>());
//		}

		ArrayList<Perk> tierPerks = new ArrayList<>();

		//tier 1
		switch (cls){
			case WARRIOR: default:
				Collections.addAll(tierPerks, HEARTY_MEAL, ARMSMASTERS_INTUITION, TEST_SUBJECT, IRON_WILL);
				break;
			case MAGE:
				Collections.addAll(tierPerks, EMPOWERING_MEAL, SCHOLARS_INTUITION, TESTED_HYPOTHESIS, BACKUP_BARRIER);
				break;
			case ROGUE:
				Collections.addAll(tierPerks, CACHED_RATIONS, THIEFS_INTUITION, SUCKER_PUNCH, PROTECTIVE_SHADOWS);
				break;
			case HUNTRESS:
				Collections.addAll(tierPerks, NATURES_BOUNTY, SURVIVALISTS_INTUITION, FOLLOWUP_STRIKE, NATURES_AID);
				break;
		}
//		for (Perk perk : tierPerks){
//			talents.get(0).put(perk, 0);
//		}
//		tierPerks.clear();

		//tier 2
		switch (cls){
			case WARRIOR: default:
				Collections.addAll(tierPerks, IRON_STOMACH, RESTORED_WILLPOWER, RUNIC_TRANSFERENCE, LETHAL_MOMENTUM, IMPROVISED_PROJECTILES);
				break;
			case MAGE:
				Collections.addAll(tierPerks, ENERGIZING_MEAL, ENERGIZING_UPGRADE, WAND_PRESERVATION, ARCANE_VISION, SHIELD_BATTERY);
				break;
			case ROGUE:
				Collections.addAll(tierPerks, MYSTICAL_MEAL, MYSTICAL_UPGRADE, WIDE_SEARCH, SILENT_STEPS, ROGUES_FORESIGHT);
				break;
			case HUNTRESS:
				Collections.addAll(tierPerks, INVIGORATING_MEAL, RESTORED_NATURE, REJUVENATING_STEPS, HEIGHTENED_SENSES, DURABLE_PROJECTILES);
				break;
		}
//		for (Perk perk : tierPerks){
//			talents.get(1).put(perk, 0);
//		}
//		tierPerks.clear();

		//tier 3
		switch (cls){
			case WARRIOR: default:
				Collections.addAll(tierPerks, HOLD_FAST, STRONGMAN);
				break;
			case MAGE:
				Collections.addAll(tierPerks, EMPOWERING_SCROLLS, ALLY_WARP);
				break;
			case ROGUE:
				Collections.addAll(tierPerks, ENHANCED_RINGS, LIGHT_CLOAK);
				break;
			case HUNTRESS:
				Collections.addAll(tierPerks, POINT_BLANK, SEER_SHOT);
				break;
		}
//		for (Perk perk : tierPerks){
//			talents.get(2).put(perk, 0);
//		}
//		tierPerks.clear();

		//tier4
		//TBD
	}

	public static void initSubclassTalents( Hero hero ){
		initSubclassTalents( hero.subClass, hero.perks);
	}

	public static void initSubclassTalents( HeroSubClass cls, ArrayList<Perk> talents ){
		if (cls == HeroSubClass.NONE) return;

//		while (talents.size() < MAX_TALENT_TIERS){
//			talents.add(new LinkedHashMap<>());
//		}

		ArrayList<Perk> tierPerks = new ArrayList<>();

		//tier 3
		switch (cls){
			case BERSERKER: default:
				Collections.addAll(tierPerks, ENDLESS_RAGE, BERSERKING_STAMINA, ENRAGED_CATALYST);
				break;
			case GLADIATOR:
				Collections.addAll(tierPerks, CLEAVE, LETHAL_DEFENSE, ENHANCED_COMBO);
				break;
			case BATTLEMAGE:
				Collections.addAll(tierPerks, EMPOWERED_STRIKE, MYSTICAL_CHARGE, EXCESS_CHARGE);
				break;
			case WARLOCK:
				Collections.addAll(tierPerks, SOUL_EATER, SOUL_SIPHON, NECROMANCERS_MINIONS);
				break;
			case ASSASSIN:
				Collections.addAll(tierPerks, ENHANCED_LETHALITY, ASSASSINS_REACH, BOUNTY_HUNTER);
				break;
			case FREERUNNER:
				Collections.addAll(tierPerks, EVASIVE_ARMOR, PROJECTILE_MOMENTUM, SPEEDY_STEALTH);
				break;
			case SNIPER:
				Collections.addAll(tierPerks, FARSIGHT, SHARED_ENCHANTMENT, SHARED_UPGRADES);
				break;
			case WARDEN:
				Collections.addAll(tierPerks, DURABLE_TIPS, BARKSKIN, SHIELDING_DEW);
				break;
		}
//		for (Perk perk : tierPerks){
//			talents.get(2).put(perk, 0);
//		}
//		tierPerks.clear();

		//tier4
		//TBD
	}

	private static final String PERK_INDEX = "perk_";
	private static final String PERK_COUNT = "perkCount";
	private static final String CHAR_POINTS = "charPoints";

	public static void storePerksInBundle(Bundle bundle, Hero hero ){
			int i = 0;
			int k = 0;
			for (Perk perk : hero.perks){
				k = k + (perk.type == perkType.POSITIVE ? perk.pointCosts : -perk.pointCosts);
				bundle.put(PERK_INDEX + i, perk.name());
				i++;
			}
			//this should store all the perks this hero processed
			bundle.put(PERK_COUNT, i);
			bundle.put(CHAR_POINTS, k);
	}

	public static void restorePerksFromBundle(Bundle bundle, Hero hero ){
		//the most primitive way I can think of to retrieve such values
		for (int i = 0; i < bundle.getInt(PERK_COUNT); i++){
			hero.perks.add( Perk.valueOf(bundle.getString(PERK_INDEX +i )) );
		}
		hero.charPoint = bundle.getInt(CHAR_POINTS);
	}

}
