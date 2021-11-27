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

package com.jinkeloid.mispd.items.keys;

import com.jinkeloid.mispd.Dungeon;
import com.jinkeloid.mispd.MISPDSettings;
import com.jinkeloid.mispd.MusicImplantSPD;
import com.jinkeloid.mispd.actors.hero.Hero;
import com.jinkeloid.mispd.sprites.ItemSpriteSheet;
import com.jinkeloid.mispd.windows.WndSupportPrompt;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;

import java.io.IOException;

public class SkeletonKey extends Key {
	
	{
		image = ItemSpriteSheet.SKELETON_KEY;
	}
	
	public SkeletonKey() {
		this( 0 );
	}
	
	public SkeletonKey( int depth ) {
		super();
		this.depth = depth;
	}

	@Override
	public boolean doPickUp(Hero hero) {
//		if(!MISPDSettings.supportNagged()){
//			try {
//				Dungeon.saveAll();
//				Game.runOnRenderThread(new Callback() {
//					@Override
//					public void call() {
//						MusicImplantSPD.scene().add(new WndSupportPrompt());
//					}
//				});
//			} catch (IOException e) {
//				MusicImplantSPD.reportException(e);
//			}
//
//		}
		
		return super.doPickUp(hero);
	}

}
