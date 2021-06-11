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

package com.jinkeloid.mispd.items.potions.exotic;

import com.jinkeloid.mispd.Assets;
import com.jinkeloid.mispd.Dungeon;
import com.jinkeloid.mispd.actors.blobs.Blob;
import com.jinkeloid.mispd.actors.blobs.SmokeScreen;
import com.jinkeloid.mispd.scenes.GameScene;
import com.jinkeloid.mispd.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class PotionOfShroudingFog extends ExoticPotion {
	
	{
		icon = ItemSpriteSheet.Icons.POTION_SHROUDFOG;
	}
	
	@Override
	public void shatter( int cell ) {
		
		if (Dungeon.level.heroFOV[cell]) {
			identify();
			
			splash( cell );
			Sample.INSTANCE.play( Assets.Sounds.SHATTER );
			Sample.INSTANCE.play( Assets.Sounds.GAS );
		}
		
		GameScene.add( Blob.seed( cell, 1000, SmokeScreen.class ) );
	}
	
}
