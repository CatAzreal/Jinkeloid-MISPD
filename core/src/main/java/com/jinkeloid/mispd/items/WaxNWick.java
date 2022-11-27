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
import com.jinkeloid.mispd.actors.buffs.Buff;
import com.jinkeloid.mispd.actors.buffs.LightOld;
import com.jinkeloid.mispd.actors.hero.Hero;
import com.jinkeloid.mispd.effects.particles.FlameParticle;
import com.jinkeloid.mispd.items.lightsource.Candle;
import com.jinkeloid.mispd.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;

import java.util.ArrayList;

public class WaxNWick extends Item {

	public static final String AC_RECHARGE	= "RECHARGE";
	
	public static final float TIME_TO_RECHARGE = 2;
	
	{
		image = ItemSpriteSheet.WAX_N_STRING;
		
		stackable = true;
		
		defaultAction = AC_RECHARGE;
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (hero.belongings.offhand instanceof Candle)
			actions.add( AC_RECHARGE );
		return actions;
	}
	
	@Override
	public void execute( Hero hero, String action ) {

		super.execute( hero, action );

		if (action.equals( AC_RECHARGE )) {

			//todo: allow player to choose which candleholder to recharge
			if (hero.belongings.offhand instanceof Candle){
				Candle candle = (Candle)hero.belongings.offhand;
				candle.addCharge(300);
			}
			hero.spend(TIME_TO_RECHARGE);
			hero.busy();

			hero.sprite.operate( hero.pos );

			detach( hero.belongings.backpack );
		}
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}
	
	@Override
	public int value() {
		return 6 * quantity;
	}

}
