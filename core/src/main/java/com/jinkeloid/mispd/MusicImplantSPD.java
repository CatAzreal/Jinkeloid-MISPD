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

package com.jinkeloid.mispd;

import com.jinkeloid.mispd.scenes.GameScene;
import com.jinkeloid.mispd.scenes.PixelScene;
import com.jinkeloid.mispd.scenes.TitleScene;
import com.jinkeloid.mispd.scenes.WelcomeScene;
import com.watabou.noosa.Game;
import com.watabou.noosa.Tilemap;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PlatformSupport;

public class MusicImplantSPD extends Game {

	//variable constants for specific older versions of shattered, used for data conversion
	//versions older than v0.7.5e are no longer supported, and data from them is ignored
	public static final int abandoned = 10000;
	public static final int peacock0_2_0   = 20000;
	
	public MusicImplantSPD(PlatformSupport platform ) {
		super( sceneClass == null ? TitleScene.class : sceneClass, platform );

		//v0.8.0
		com.watabou.utils.Bundle.addAlias(
				com.jinkeloid.mispd.actors.mobs.ArmoredBrute.class,
				"com.jinkeloid.mispd.actors.mobs.Shielded");
		com.watabou.utils.Bundle.addAlias(
				com.jinkeloid.mispd.actors.mobs.DM100.class,
				"com.jinkeloid.mispd.actors.mobs.Shaman");
		com.watabou.utils.Bundle.addAlias(
				com.jinkeloid.mispd.actors.mobs.Elemental.FireElemental.class,
				"com.jinkeloid.mispd.actors.mobs.Elemental");
		com.watabou.utils.Bundle.addAlias(
				com.jinkeloid.mispd.actors.mobs.Elemental.NewbornFireElemental.class,
				"com.jinkeloid.mispd.actors.mobs.NewbornElemental");
		com.watabou.utils.Bundle.addAlias(
				com.jinkeloid.mispd.actors.mobs.OldDM300.class,
				"com.jinkeloid.mispd.actors.mobs.DM300");
		com.watabou.utils.Bundle.addAlias(
				com.jinkeloid.mispd.levels.OldCavesBossLevel.class,
				"com.jinkeloid.mispd.levels.CavesBossLevel" );
		com.watabou.utils.Bundle.addAlias(
				com.jinkeloid.mispd.levels.OldCityBossLevel.class,
				"com.jinkeloid.mispd.levels.CityBossLevel" );
		com.watabou.utils.Bundle.addAlias(
				com.jinkeloid.mispd.levels.OldHallsBossLevel.class,
				"com.jinkeloid.mispd.levels.HallsBossLevel" );
		
	}
	
	@Override
	public void create() {
		super.create();

		updateSystemUI();
		MISPDAction.loadBindings();
		
		Music.INSTANCE.enable( MISPDSettings.music() );
		Music.INSTANCE.volume( MISPDSettings.musicVol()* MISPDSettings.musicVol()/100f );
		Sample.INSTANCE.enable( MISPDSettings.soundFx() );
		Sample.INSTANCE.volume( MISPDSettings.SFXVol()* MISPDSettings.SFXVol()/100f );

		Sample.INSTANCE.load( Assets.Sounds.all );
		
	}

	public static void switchNoFade(Class<? extends PixelScene> c){
		switchNoFade(c, null);
	}

	public static void switchNoFade(Class<? extends PixelScene> c, SceneChangeCallback callback) {
		PixelScene.noFade = true;
		switchScene( c, callback );
	}
	
	public static void seamlessResetScene(SceneChangeCallback callback) {
		if (scene() instanceof PixelScene){
			((PixelScene) scene()).saveWindows();
			switchNoFade((Class<? extends PixelScene>) sceneClass, callback );
		} else {
			resetScene();
		}
	}
	
	public static void seamlessResetScene(){
		seamlessResetScene(null);
	}
	
	@Override
	protected void switchScene() {
		super.switchScene();
		if (scene instanceof PixelScene){
			((PixelScene) scene).restoreWindows();
		}
	}
	
	@Override
	public void resize( int width, int height ) {
		if (width == 0 || height == 0){
			return;
		}

		if (scene instanceof PixelScene &&
				(height != Game.height || width != Game.width)) {
			PixelScene.noFade = true;
			((PixelScene) scene).saveWindows();
		}

		super.resize( width, height );

		updateDisplaySize();

	}
	
	@Override
	public void destroy(){
		super.destroy();
		GameScene.endActorThread();
	}
	
	public void updateDisplaySize(){
		platform.updateDisplaySize();
	}

	public static void updateSystemUI() {
		platform.updateSystemUI();
	}
}