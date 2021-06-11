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

package com.jinkeloid.mispd.items.armor.glyphs;

import com.jinkeloid.mispd.actors.Char;
import com.jinkeloid.mispd.actors.buffs.Charm;
import com.jinkeloid.mispd.actors.buffs.Degrade;
import com.jinkeloid.mispd.actors.buffs.Hex;
import com.jinkeloid.mispd.actors.buffs.MagicalSleep;
import com.jinkeloid.mispd.actors.buffs.Vulnerable;
import com.jinkeloid.mispd.actors.buffs.Weakness;
import com.jinkeloid.mispd.actors.mobs.DM100;
import com.jinkeloid.mispd.actors.mobs.Eye;
import com.jinkeloid.mispd.actors.mobs.Shaman;
import com.jinkeloid.mispd.actors.mobs.Warlock;
import com.jinkeloid.mispd.actors.mobs.Yog;
import com.jinkeloid.mispd.actors.mobs.YogFist;
import com.jinkeloid.mispd.items.armor.Armor;
import com.jinkeloid.mispd.items.wands.WandOfBlastWave;
import com.jinkeloid.mispd.items.wands.WandOfDisintegration;
import com.jinkeloid.mispd.items.wands.WandOfFireblast;
import com.jinkeloid.mispd.items.wands.WandOfFrost;
import com.jinkeloid.mispd.items.wands.WandOfLightning;
import com.jinkeloid.mispd.items.wands.WandOfLivingEarth;
import com.jinkeloid.mispd.items.wands.WandOfMagicMissile;
import com.jinkeloid.mispd.items.wands.WandOfPrismaticLight;
import com.jinkeloid.mispd.items.wands.WandOfTransfusion;
import com.jinkeloid.mispd.items.wands.WandOfWarding;
import com.jinkeloid.mispd.levels.traps.DisintegrationTrap;
import com.jinkeloid.mispd.levels.traps.GrimTrap;
import com.jinkeloid.mispd.sprites.ItemSprite;
import com.watabou.utils.Random;

import java.util.HashSet;

public class AntiMagic extends Armor.Glyph {

	private static ItemSprite.Glowing TEAL = new ItemSprite.Glowing( 0x88EEFF );
	
	public static final HashSet<Class> RESISTS = new HashSet<>();
	static {
		RESISTS.add( MagicalSleep.class );
		RESISTS.add( Charm.class );
		RESISTS.add( Weakness.class );
		RESISTS.add( Vulnerable.class );
		RESISTS.add( Hex.class );
		RESISTS.add( Degrade.class );
		
		RESISTS.add( DisintegrationTrap.class );
		RESISTS.add( GrimTrap.class );

		RESISTS.add( WandOfBlastWave.class );
		RESISTS.add( WandOfDisintegration.class );
		RESISTS.add( WandOfFireblast.class );
		RESISTS.add( WandOfFrost.class );
		RESISTS.add( WandOfLightning.class );
		RESISTS.add( WandOfLivingEarth.class );
		RESISTS.add( WandOfMagicMissile.class );
		RESISTS.add( WandOfPrismaticLight.class );
		RESISTS.add( WandOfTransfusion.class );
		RESISTS.add( WandOfWarding.Ward.class );
		
		RESISTS.add( DM100.LightningBolt.class );
		RESISTS.add( Shaman.EarthenBolt.class );
		RESISTS.add( Warlock.DarkBolt.class );
		RESISTS.add( Eye.DeathGaze.class );
		RESISTS.add( Yog.BurningFist.DarkBolt.class );
		RESISTS.add( YogFist.BrightFist.LightBeam.class );
		RESISTS.add( YogFist.DarkFist.DarkBolt.class );
	}
	
	@Override
	public int proc(Armor armor, Char attacker, Char defender, int damage) {
		//no proc effect, see Hero.damage
		return damage;
	}
	
	public static int drRoll( int level ){
		return Random.NormalIntRange(level, 3 + Math.round(level*1.5f));
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return TEAL;
	}

}