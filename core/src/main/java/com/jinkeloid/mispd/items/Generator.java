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

import com.jinkeloid.mispd.Dungeon;
import com.jinkeloid.mispd.items.armor.Armor;
import com.jinkeloid.mispd.items.armor.ClothArmor;
import com.jinkeloid.mispd.items.armor.LeatherArmor;
import com.jinkeloid.mispd.items.armor.MailArmor;
import com.jinkeloid.mispd.items.armor.PlateArmor;
import com.jinkeloid.mispd.items.armor.ScaleArmor;
import com.jinkeloid.mispd.items.artifacts.AlchemistsToolkit;
import com.jinkeloid.mispd.items.artifacts.Artifact;
import com.jinkeloid.mispd.items.artifacts.CapeOfThorns;
import com.jinkeloid.mispd.items.artifacts.ChaliceOfBlood;
import com.jinkeloid.mispd.items.artifacts.CloakOfShadows;
import com.jinkeloid.mispd.items.artifacts.DriedRose;
import com.jinkeloid.mispd.items.artifacts.EtherealChains;
import com.jinkeloid.mispd.items.artifacts.HornOfPlenty;
import com.jinkeloid.mispd.items.artifacts.LloydsBeacon;
import com.jinkeloid.mispd.items.artifacts.MasterThievesArmband;
import com.jinkeloid.mispd.items.artifacts.SandalsOfNature;
import com.jinkeloid.mispd.items.artifacts.TalismanOfForesight;
import com.jinkeloid.mispd.items.artifacts.TimekeepersHourglass;
import com.jinkeloid.mispd.items.artifacts.UnstableSpellbook;
import com.jinkeloid.mispd.items.bags.Bag;
import com.jinkeloid.mispd.items.food.Food;
import com.jinkeloid.mispd.items.food.MysteryMeat;
import com.jinkeloid.mispd.items.food.Pasty;
import com.jinkeloid.mispd.items.potions.Potion;
import com.jinkeloid.mispd.items.potions.PotionOfExperience;
import com.jinkeloid.mispd.items.potions.PotionOfFrost;
import com.jinkeloid.mispd.items.potions.PotionOfHaste;
import com.jinkeloid.mispd.items.potions.PotionOfHealing;
import com.jinkeloid.mispd.items.potions.PotionOfInvisibility;
import com.jinkeloid.mispd.items.potions.PotionOfLevitation;
import com.jinkeloid.mispd.items.potions.PotionOfLiquidFlame;
import com.jinkeloid.mispd.items.potions.PotionOfMindVision;
import com.jinkeloid.mispd.items.potions.PotionOfParalyticGas;
import com.jinkeloid.mispd.items.potions.PotionOfPurity;
import com.jinkeloid.mispd.items.potions.PotionOfStrength;
import com.jinkeloid.mispd.items.potions.PotionOfToxicGas;
import com.jinkeloid.mispd.items.rings.Ring;
import com.jinkeloid.mispd.items.rings.RingOfAccuracy;
import com.jinkeloid.mispd.items.rings.RingOfElements;
import com.jinkeloid.mispd.items.rings.RingOfEnergy;
import com.jinkeloid.mispd.items.rings.RingOfEvasion;
import com.jinkeloid.mispd.items.rings.RingOfForce;
import com.jinkeloid.mispd.items.rings.RingOfFuror;
import com.jinkeloid.mispd.items.rings.RingOfHaste;
import com.jinkeloid.mispd.items.rings.RingOfMight;
import com.jinkeloid.mispd.items.rings.RingOfSharpshooting;
import com.jinkeloid.mispd.items.rings.RingOfTenacity;
import com.jinkeloid.mispd.items.rings.RingOfWealth;
import com.jinkeloid.mispd.items.scrolls.Scroll;
import com.jinkeloid.mispd.items.scrolls.ScrollOfIdentify;
import com.jinkeloid.mispd.items.scrolls.ScrollOfLullaby;
import com.jinkeloid.mispd.items.scrolls.ScrollOfMagicMapping;
import com.jinkeloid.mispd.items.scrolls.ScrollOfMirrorImage;
import com.jinkeloid.mispd.items.scrolls.ScrollOfRage;
import com.jinkeloid.mispd.items.scrolls.ScrollOfRecharging;
import com.jinkeloid.mispd.items.scrolls.ScrollOfRemoveCurse;
import com.jinkeloid.mispd.items.scrolls.ScrollOfRetribution;
import com.jinkeloid.mispd.items.scrolls.ScrollOfTeleportation;
import com.jinkeloid.mispd.items.scrolls.ScrollOfTerror;
import com.jinkeloid.mispd.items.scrolls.ScrollOfTransmutation;
import com.jinkeloid.mispd.items.scrolls.ScrollOfUpgrade;
import com.jinkeloid.mispd.items.stones.Runestone;
import com.jinkeloid.mispd.items.stones.StoneOfAffection;
import com.jinkeloid.mispd.items.stones.StoneOfAggression;
import com.jinkeloid.mispd.items.stones.StoneOfAugmentation;
import com.jinkeloid.mispd.items.stones.StoneOfBlast;
import com.jinkeloid.mispd.items.stones.StoneOfBlink;
import com.jinkeloid.mispd.items.stones.StoneOfClairvoyance;
import com.jinkeloid.mispd.items.stones.StoneOfDeepenedSleep;
import com.jinkeloid.mispd.items.stones.StoneOfDisarming;
import com.jinkeloid.mispd.items.stones.StoneOfEnchantment;
import com.jinkeloid.mispd.items.stones.StoneOfFlock;
import com.jinkeloid.mispd.items.stones.StoneOfIntuition;
import com.jinkeloid.mispd.items.stones.StoneOfShock;
import com.jinkeloid.mispd.items.wands.Wand;
import com.jinkeloid.mispd.items.wands.WandOfBlastWave;
import com.jinkeloid.mispd.items.wands.WandOfCorrosion;
import com.jinkeloid.mispd.items.wands.WandOfCorruption;
import com.jinkeloid.mispd.items.wands.WandOfDisintegration;
import com.jinkeloid.mispd.items.wands.WandOfFireblast;
import com.jinkeloid.mispd.items.wands.WandOfFrost;
import com.jinkeloid.mispd.items.wands.WandOfLightning;
import com.jinkeloid.mispd.items.wands.WandOfLivingEarth;
import com.jinkeloid.mispd.items.wands.WandOfMagicMissile;
import com.jinkeloid.mispd.items.wands.WandOfPrismaticLight;
import com.jinkeloid.mispd.items.wands.WandOfRegrowth;
import com.jinkeloid.mispd.items.wands.WandOfTransfusion;
import com.jinkeloid.mispd.items.wands.WandOfWarding;
import com.jinkeloid.mispd.items.weapon.melee.AssassinsBlade;
import com.jinkeloid.mispd.items.weapon.melee.BattleAxe;
import com.jinkeloid.mispd.items.weapon.melee.Crossbow;
import com.jinkeloid.mispd.items.weapon.melee.Dagger;
import com.jinkeloid.mispd.items.weapon.melee.Dirk;
import com.jinkeloid.mispd.items.weapon.melee.Flail;
import com.jinkeloid.mispd.items.weapon.melee.Gauntlet;
import com.jinkeloid.mispd.items.weapon.melee.Glaive;
import com.jinkeloid.mispd.items.weapon.melee.Gloves;
import com.jinkeloid.mispd.items.weapon.melee.Greataxe;
import com.jinkeloid.mispd.items.weapon.melee.Greatshield;
import com.jinkeloid.mispd.items.weapon.melee.Greatsword;
import com.jinkeloid.mispd.items.weapon.melee.HandAxe;
import com.jinkeloid.mispd.items.weapon.melee.Longsword;
import com.jinkeloid.mispd.items.weapon.melee.Mace;
import com.jinkeloid.mispd.items.weapon.melee.MagesStaff;
import com.jinkeloid.mispd.items.weapon.melee.MeleeWeapon;
import com.jinkeloid.mispd.items.weapon.melee.Quarterstaff;
import com.jinkeloid.mispd.items.weapon.melee.RoundShield;
import com.jinkeloid.mispd.items.weapon.melee.RunicBlade;
import com.jinkeloid.mispd.items.weapon.melee.Sai;
import com.jinkeloid.mispd.items.weapon.melee.Scimitar;
import com.jinkeloid.mispd.items.weapon.melee.Shortsword;
import com.jinkeloid.mispd.items.weapon.melee.Spear;
import com.jinkeloid.mispd.items.weapon.melee.Sword;
import com.jinkeloid.mispd.items.weapon.melee.WarHammer;
import com.jinkeloid.mispd.items.weapon.melee.Whip;
import com.jinkeloid.mispd.items.weapon.melee.WornShortsword;
import com.jinkeloid.mispd.items.weapon.missiles.Bolas;
import com.jinkeloid.mispd.items.weapon.missiles.FishingSpear;
import com.jinkeloid.mispd.items.weapon.missiles.ForceCube;
import com.jinkeloid.mispd.items.weapon.missiles.HeavyBoomerang;
import com.jinkeloid.mispd.items.weapon.missiles.Javelin;
import com.jinkeloid.mispd.items.weapon.missiles.Kunai;
import com.jinkeloid.mispd.items.weapon.missiles.MissileWeapon;
import com.jinkeloid.mispd.items.weapon.missiles.Shuriken;
import com.jinkeloid.mispd.items.weapon.missiles.ThrowingClub;
import com.jinkeloid.mispd.items.weapon.missiles.ThrowingHammer;
import com.jinkeloid.mispd.items.weapon.missiles.ThrowingKnife;
import com.jinkeloid.mispd.items.weapon.missiles.ThrowingSpear;
import com.jinkeloid.mispd.items.weapon.missiles.ThrowingStone;
import com.jinkeloid.mispd.items.weapon.missiles.Tomahawk;
import com.jinkeloid.mispd.items.weapon.missiles.Trident;
import com.jinkeloid.mispd.plants.Blindweed;
import com.jinkeloid.mispd.plants.Dreamfoil;
import com.jinkeloid.mispd.plants.Earthroot;
import com.jinkeloid.mispd.plants.Fadeleaf;
import com.jinkeloid.mispd.plants.Firebloom;
import com.jinkeloid.mispd.plants.Icecap;
import com.jinkeloid.mispd.plants.Plant;
import com.jinkeloid.mispd.plants.Rotberry;
import com.jinkeloid.mispd.plants.Sorrowmoss;
import com.jinkeloid.mispd.plants.Starflower;
import com.jinkeloid.mispd.plants.Stormvine;
import com.jinkeloid.mispd.plants.Sungrass;
import com.jinkeloid.mispd.plants.Swiftthistle;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class Generator {

	public enum Category {
		WEAPON	( 4,    MeleeWeapon.class),
		WEP_T1	( 0,    MeleeWeapon.class),
		WEP_T2	( 0,    MeleeWeapon.class),
		WEP_T3	( 0,    MeleeWeapon.class),
		WEP_T4	( 0,    MeleeWeapon.class),
		WEP_T5	( 0,    MeleeWeapon.class),
		
		ARMOR	( 3,    Armor.class ),
		
		MISSILE ( 3,    MissileWeapon.class ),
		MIS_T1  ( 0,    MissileWeapon.class ),
		MIS_T2  ( 0,    MissileWeapon.class ),
		MIS_T3  ( 0,    MissileWeapon.class ),
		MIS_T4  ( 0,    MissileWeapon.class ),
		MIS_T5  ( 0,    MissileWeapon.class ),
		
		WAND	( 2,    Wand.class ),
		RING	( 1,    Ring.class ),
		ARTIFACT( 1,    Artifact.class),
		
		FOOD	( 0,    Food.class ),
		
		POTION	( 16,   Potion.class ),
		SEED	( 2,    Plant.Seed.class ),
		
		SCROLL	( 16,   Scroll.class ),
		STONE   ( 2,    Runestone.class),
		
		GOLD	( 20,   Gold.class );
		
		public Class<?>[] classes;

		//some item types use a deck-based system, where the probs decrement as items are picked
		// until they are all 0, and then they reset. Those generator classes should define
		// defaultProbs. If defaultProbs is null then a deck system isn't used.
		//Artifacts in particular don't reset, no duplicates!
		public float[] probs;
		public float[] defaultProbs = null;
		
		public float prob;
		public Class<? extends Item> superClass;
		
		private Category( float prob, Class<? extends Item> superClass ) {
			this.prob = prob;
			this.superClass = superClass;
		}
		
		public static int order( Item item ) {
			for (int i=0; i < values().length; i++) {
				if (values()[i].superClass.isInstance( item )) {
					return i;
				}
			}
			
			return item instanceof Bag ? Integer.MAX_VALUE : Integer.MAX_VALUE - 1;
		}

		static {
			GOLD.classes = new Class<?>[]{
					Gold.class };
			GOLD.probs = new float[]{ 1 };
			
			POTION.classes = new Class<?>[]{
					PotionOfStrength.class, //2 drop every chapter, see Dungeon.posNeeded()
					PotionOfHealing.class,
					PotionOfMindVision.class,
					PotionOfFrost.class,
					PotionOfLiquidFlame.class,
					PotionOfToxicGas.class,
					PotionOfHaste.class,
					PotionOfInvisibility.class,
					PotionOfLevitation.class,
					PotionOfParalyticGas.class,
					PotionOfPurity.class,
					PotionOfExperience.class};
			POTION.defaultProbs = new float[]{ 0, 6, 4, 3, 3, 3, 2, 2, 2, 2, 2, 1 };
			POTION.probs = POTION.defaultProbs.clone();
			
			SEED.classes = new Class<?>[]{
					Rotberry.Seed.class, //quest item
					Sungrass.Seed.class,
					Fadeleaf.Seed.class,
					Icecap.Seed.class,
					Firebloom.Seed.class,
					Sorrowmoss.Seed.class,
					Swiftthistle.Seed.class,
					Blindweed.Seed.class,
					Stormvine.Seed.class,
					Earthroot.Seed.class,
					Dreamfoil.Seed.class,
					Starflower.Seed.class};
			SEED.defaultProbs = new float[]{ 0, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 2 };
			SEED.probs = SEED.defaultProbs.clone();
			
			SCROLL.classes = new Class<?>[]{
					ScrollOfUpgrade.class, //3 drop every chapter, see Dungeon.souNeeded()
					ScrollOfIdentify.class,
					ScrollOfRemoveCurse.class,
					ScrollOfMirrorImage.class,
					ScrollOfRecharging.class,
					ScrollOfTeleportation.class,
					ScrollOfLullaby.class,
					ScrollOfMagicMapping.class,
					ScrollOfRage.class,
					ScrollOfRetribution.class,
					ScrollOfTerror.class,
					ScrollOfTransmutation.class
			};
			SCROLL.defaultProbs = new float[]{ 0, 6, 4, 3, 3, 3, 2, 2, 2, 2, 2, 1 };
			SCROLL.probs = SCROLL.defaultProbs.clone();
			
			STONE.classes = new Class<?>[]{
					StoneOfEnchantment.class,   //1 is guaranteed to drop on floors 6-19
					StoneOfIntuition.class,     //1 additional stone is also dropped on floors 1-3
					StoneOfDisarming.class,
					StoneOfFlock.class,
					StoneOfShock.class,
					StoneOfBlink.class,
					StoneOfDeepenedSleep.class,
					StoneOfClairvoyance.class,
					StoneOfAggression.class,
					StoneOfBlast.class,
					StoneOfAffection.class,
					StoneOfAugmentation.class  //1 is sold in each shop
			};
			STONE.defaultProbs = new float[]{ 0, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 0 };
			STONE.probs = STONE.defaultProbs.clone();

			WAND.classes = new Class<?>[]{
					WandOfMagicMissile.class,
					WandOfLightning.class,
					WandOfDisintegration.class,
					WandOfFireblast.class,
					WandOfCorrosion.class,
					WandOfBlastWave.class,
					WandOfLivingEarth.class,
					WandOfFrost.class,
					WandOfPrismaticLight.class,
					WandOfWarding.class,
					WandOfTransfusion.class,
					WandOfCorruption.class,
					WandOfRegrowth.class };
			WAND.probs = new float[]{ 4, 4, 4, 4, 4, 3, 3, 3, 3, 3, 3, 3, 3 };
			
			//see generator.randomWeapon
			WEAPON.classes = new Class<?>[]{};
			WEAPON.probs = new float[]{};
			
			WEP_T1.classes = new Class<?>[]{
					WornShortsword.class,
					Gloves.class,
					Dagger.class,
					MagesStaff.class
			};
			WEP_T1.probs = new float[]{ 1, 1, 1, 0 };
			
			WEP_T2.classes = new Class<?>[]{
					Shortsword.class,
					HandAxe.class,
					Spear.class,
					Quarterstaff.class,
					Dirk.class
			};
			WEP_T2.probs = new float[]{ 6, 5, 5, 4, 4 };
			
			WEP_T3.classes = new Class<?>[]{
					Sword.class,
					Mace.class,
					Scimitar.class,
					RoundShield.class,
					Sai.class,
					Whip.class
			};
			WEP_T3.probs = new float[]{ 6, 5, 5, 4, 4, 4 };
			
			WEP_T4.classes = new Class<?>[]{
					Longsword.class,
					BattleAxe.class,
					Flail.class,
					RunicBlade.class,
					AssassinsBlade.class,
					Crossbow.class
			};
			WEP_T4.probs = new float[]{ 6, 5, 5, 4, 4, 4 };
			
			WEP_T5.classes = new Class<?>[]{
					Greatsword.class,
					WarHammer.class,
					Glaive.class,
					Greataxe.class,
					Greatshield.class,
					Gauntlet.class
			};
			WEP_T5.probs = new float[]{ 6, 5, 5, 4, 4, 4 };
			
			//see Generator.randomArmor
			ARMOR.classes = new Class<?>[]{
					ClothArmor.class,
					LeatherArmor.class,
					MailArmor.class,
					ScaleArmor.class,
					PlateArmor.class };
			ARMOR.probs = new float[]{ 0, 0, 0, 0, 0 };
			
			//see Generator.randomMissile
			MISSILE.classes = new Class<?>[]{};
			MISSILE.probs = new float[]{};
			
			MIS_T1.classes = new Class<?>[]{
					ThrowingStone.class,
					ThrowingKnife.class
			};
			MIS_T1.probs = new float[]{ 6, 5 };
			
			MIS_T2.classes = new Class<?>[]{
					FishingSpear.class,
					ThrowingClub.class,
					Shuriken.class
			};
			MIS_T2.probs = new float[]{ 6, 5, 4 };
			
			MIS_T3.classes = new Class<?>[]{
					ThrowingSpear.class,
					Kunai.class,
					Bolas.class
			};
			MIS_T3.probs = new float[]{ 6, 5, 4 };
			
			MIS_T4.classes = new Class<?>[]{
					Javelin.class,
					Tomahawk.class,
					HeavyBoomerang.class
			};
			MIS_T4.probs = new float[]{ 6, 5, 4 };
			
			MIS_T5.classes = new Class<?>[]{
					Trident.class,
					ThrowingHammer.class,
					ForceCube.class
			};
			MIS_T5.probs = new float[]{ 6, 5, 4 };
			
			FOOD.classes = new Class<?>[]{
					Food.class,
					Pasty.class,
					MysteryMeat.class };
			FOOD.probs = new float[]{ 4, 1, 0 };
			
			RING.classes = new Class<?>[]{
					RingOfAccuracy.class,
					RingOfEvasion.class,
					RingOfElements.class,
					RingOfForce.class,
					RingOfFuror.class,
					RingOfHaste.class,
					RingOfEnergy.class,
					RingOfMight.class,
					RingOfSharpshooting.class,
					RingOfTenacity.class,
					RingOfWealth.class};
			RING.probs = new float[]{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
			
			ARTIFACT.classes = new Class<?>[]{
					CapeOfThorns.class,
					ChaliceOfBlood.class,
					CloakOfShadows.class,
					HornOfPlenty.class,
					MasterThievesArmband.class,
					SandalsOfNature.class,
					TalismanOfForesight.class,
					TimekeepersHourglass.class,
					UnstableSpellbook.class,
					AlchemistsToolkit.class,
					DriedRose.class,
					LloydsBeacon.class,
					EtherealChains.class
			};
			ARTIFACT.defaultProbs = new float[]{ 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1};
			ARTIFACT.probs = ARTIFACT.defaultProbs.clone();
		}
	}

	private static final float[][] floorSetTierProbs = new float[][] {
			{0, 75, 20,  4,  1},
			{0, 25, 50, 20,  5},
			{0,  0, 40, 50, 10},
			{0,  0, 20, 40, 40},
			{0,  0,  0, 20, 80}
	};
	
	private static HashMap<Category,Float> categoryProbs = new LinkedHashMap<>();

	public static void fullReset() {
		generalReset();
		for (Category cat : Category.values()) {
			reset(cat);
		}
	}

	public static void generalReset(){
		for (Category cat : Category.values()) {
			categoryProbs.put( cat, cat.prob );
		}
	}

	public static void reset(Category cat){
		if (cat.defaultProbs != null) cat.probs = cat.defaultProbs.clone();
	}
	
	public static Item random() {
		Category cat = Random.chances( categoryProbs );
		if (cat == null){
			generalReset();
			cat = Random.chances( categoryProbs );
		}
		categoryProbs.put( cat, categoryProbs.get( cat ) - 1);
		return random( cat );
	}
	
	public static Item random( Category cat ) {
		switch (cat) {
			case ARMOR:
				return randomArmor();
			case WEAPON:
				return randomWeapon();
			case MISSILE:
				return randomMissile();
			case ARTIFACT:
				Item item = randomArtifact();
				//if we're out of artifacts, return a ring instead.
				return item != null ? item : random(Category.RING);
			default:
				int i = Random.chances(cat.probs);
				if (i == -1) {
					reset(cat);
					i = Random.chances(cat.probs);
				}
				if (cat.defaultProbs != null) cat.probs[i]--;
				return ((Item) Reflection.newInstance(cat.classes[i])).random();
		}
	}

	//overrides any deck systems and always uses default probs
	public static Item randomUsingDefaults( Category cat ){
		if (cat.defaultProbs == null) {
			return random(cat); //currently covers weapons/armor/missiles
		} else {
			return ((Item) Reflection.newInstance(cat.classes[Random.chances(cat.defaultProbs)])).random();
		}
	}
	
	public static Item random( Class<? extends Item> cl ) {
		return Reflection.newInstance(cl).random();
	}

	public static Armor randomArmor(){
		return randomArmor(Dungeon.depth / 5);
	}
	
	public static Armor randomArmor(int floorSet) {

		floorSet = (int)GameMath.gate(0, floorSet, floorSetTierProbs.length-1);
		
		Armor a = (Armor)Reflection.newInstance(Category.ARMOR.classes[Random.chances(floorSetTierProbs[floorSet])]);
		a.random();
		return a;
	}

	public static final Category[] wepTiers = new Category[]{
			Category.WEP_T1,
			Category.WEP_T2,
			Category.WEP_T3,
			Category.WEP_T4,
			Category.WEP_T5
	};

	public static MeleeWeapon randomWeapon(){
		return randomWeapon(Dungeon.depth / 5);
	}
	
	public static MeleeWeapon randomWeapon(int floorSet) {

		floorSet = (int)GameMath.gate(0, floorSet, floorSetTierProbs.length-1);
		
		Category c = wepTiers[Random.chances(floorSetTierProbs[floorSet])];
		MeleeWeapon w = (MeleeWeapon)Reflection.newInstance(c.classes[Random.chances(c.probs)]);
		w.random();
		return w;
	}
	
	public static final Category[] misTiers = new Category[]{
			Category.MIS_T1,
			Category.MIS_T2,
			Category.MIS_T3,
			Category.MIS_T4,
			Category.MIS_T5
	};
	
	public static MissileWeapon randomMissile(){
		return randomMissile(Dungeon.depth / 5);
	}
	
	public static MissileWeapon randomMissile(int floorSet) {
		
		floorSet = (int)GameMath.gate(0, floorSet, floorSetTierProbs.length-1);
		
		Category c = misTiers[Random.chances(floorSetTierProbs[floorSet])];
		MissileWeapon w = (MissileWeapon)Reflection.newInstance(c.classes[Random.chances(c.probs)]);
		w.random();
		return w;
	}

	//enforces uniqueness of artifacts throughout a run.
	public static Artifact randomArtifact() {

		Category cat = Category.ARTIFACT;
		int i = Random.chances( cat.probs );

		//if no artifacts are left, return null
		if (i == -1){
			return null;
		}

		cat.probs[i]--;
		return (Artifact) Reflection.newInstance((Class<? extends Artifact>) cat.classes[i]).random();

	}

	public static boolean removeArtifact(Class<?extends Artifact> artifact) {
		Category cat = Category.ARTIFACT;
		for (int i = 0; i < cat.classes.length; i++){
			if (cat.classes[i].equals(artifact) && cat.probs[i] > 0) {
				cat.probs[i] = 0;
				return true;
			}
		}
		return false;
	}

	private static final String GENERAL_PROBS = "general_probs";
	private static final String CATEGORY_PROBS = "_probs";
	
	public static void storeInBundle(Bundle bundle) {
		Float[] genProbs = categoryProbs.values().toArray(new Float[0]);
		float[] storeProbs = new float[genProbs.length];
		for (int i = 0; i < storeProbs.length; i++){
			storeProbs[i] = genProbs[i];
		}
		bundle.put( GENERAL_PROBS, storeProbs);

		for (Category cat : Category.values()){
			if (cat.defaultProbs == null) continue;
			boolean needsStore = false;
			for (int i = 0; i < cat.probs.length; i++){
				if (cat.probs[i] != cat.defaultProbs[i]){
					needsStore = true;
					break;
				}
			}

			if (needsStore){
				bundle.put(cat.name().toLowerCase() + CATEGORY_PROBS, cat.probs);
			}
		}
	}

	public static void restoreFromBundle(Bundle bundle) {
		fullReset();

		if (bundle.contains(GENERAL_PROBS)){
			float[] probs = bundle.getFloatArray(GENERAL_PROBS);
			for (int i = 0; i < probs.length; i++){
				categoryProbs.put(Category.values()[i], probs[i]);
			}
		}

		for (Category cat : Category.values()){
			if (bundle.contains(cat.name().toLowerCase() + CATEGORY_PROBS)){
				float[] probs = bundle.getFloatArray(cat.name().toLowerCase() + CATEGORY_PROBS);
				if (cat.defaultProbs != null && probs.length == cat.defaultProbs.length){
					cat.probs = probs;
				}
			}
		}

		//pre-0.8.1
		if (bundle.contains("spawned_artifacts")) {
			for (Class<? extends Artifact> artifact : bundle.getClassArray("spawned_artifacts")) {
				Category cat = Category.ARTIFACT;
				for (int i = 0; i < cat.classes.length; i++) {
					if (cat.classes[i].equals(artifact)) {
						cat.probs[i] = 0;
					}
				}
			}
		}
		
	}
}
