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

package com.jinkeloid.mispd.items.food;

import com.jinkeloid.mispd.Assets;
import com.jinkeloid.mispd.Badges;
import com.jinkeloid.mispd.Challenges;
import com.jinkeloid.mispd.Dungeon;
import com.jinkeloid.mispd.Statistics;
import com.jinkeloid.mispd.actors.buffs.Buff;
import com.jinkeloid.mispd.actors.buffs.Hunger;
import com.jinkeloid.mispd.actors.buffs.Satiation;
import com.jinkeloid.mispd.actors.hero.Hero;
import com.jinkeloid.mispd.actors.hero.Perk;
import com.jinkeloid.mispd.effects.SpellSprite;
import com.jinkeloid.mispd.items.Item;
import com.jinkeloid.mispd.messages.Messages;
import com.jinkeloid.mispd.sprites.ItemSpriteSheet;
import com.jinkeloid.mispd.utils.GLog;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class Food extends Item {

	public static final float TIME_TO_EAT	= 3f;
	
	public static final String AC_EAT	= "EAT";
	
	public float energy = Satiation.PECKISH;
	
	{
		stackable = true;
		image = ItemSpriteSheet.RATION;

		bones = true;
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_EAT );
		return actions;
	}
	
	@Override
	public void execute( Hero hero, String action ) {

		super.execute( hero, action );

		if (action.equals( AC_EAT )) {
			
			detach( hero.belongings.backpack );
			
			satisfy(hero);
			GLog.i( Messages.get(this, "eat_msg") );
			
			hero.sprite.operate( hero.pos );
			hero.busy();
			SpellSprite.show( hero, SpellSprite.FOOD );
			Sample.INSTANCE.play( Assets.Sounds.EAT );
			
			hero.spend( eatingTime() );

			Perk.onFoodEaten(hero, energy, this);
			
			Statistics.foodEaten++;
			Badges.validateFoodEaten();
			
		}
	}

	protected float eatingTime(){
		if (Dungeon.hero.hasPerk(Perk.IRON_STOMACH)
			|| Dungeon.hero.hasPerk(Perk.ENERGIZING_MEAL)
			|| Dungeon.hero.hasPerk(Perk.MYSTICAL_MEAL)
			|| Dungeon.hero.hasPerk(Perk.INVIGORATING_MEAL)){
			return TIME_TO_EAT - 2;
		} else {
			return TIME_TO_EAT;
		}
	}
	
	protected void satisfy( Hero hero ){
		if (Dungeon.isChallenged(Challenges.NO_FOOD)){
			Buff.affect(hero, Satiation.class).satisfy(energy/3f);
		} else {
			Buff.affect(hero, Satiation.class).satisfy(energy);
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
		return 10 * quantity;
	}
}
