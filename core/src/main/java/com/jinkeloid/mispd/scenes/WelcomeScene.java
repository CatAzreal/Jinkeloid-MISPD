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

package com.jinkeloid.mispd.scenes;

import com.jinkeloid.mispd.Badges;
import com.jinkeloid.mispd.Challenges;
import com.jinkeloid.mispd.Chrome;
import com.jinkeloid.mispd.GamesInProgress;
import com.jinkeloid.mispd.Rankings;
import com.jinkeloid.mispd.MISPDSettings;
import com.jinkeloid.mispd.MusicImplantSPD;
import com.jinkeloid.mispd.effects.BannerSprites;
import com.jinkeloid.mispd.messages.Languages;
import com.jinkeloid.mispd.messages.Messages;
import com.jinkeloid.mispd.ui.Icons;
import com.jinkeloid.mispd.ui.RenderedTextBlock;
import com.jinkeloid.mispd.ui.StyledButton;
import com.watabou.glwrap.Blending;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.utils.FileUtils;

import java.util.Locale;

public class WelcomeScene extends PixelScene {

	private static final int LATEST_UPDATE = MusicImplantSPD.v0_9_2;

	@Override
	public void create() {
		super.create();

		final int previousVersion = MISPDSettings.version();

		if (MusicImplantSPD.versionCode == previousVersion && !MISPDSettings.intro()) {
			MusicImplantSPD.switchNoFade(TitleScene.class);
			return;
		}

		uiCamera.visible = false;

		int w = Camera.main.width;
		int h = Camera.main.height;

		Image title = BannerSprites.get( BannerSprites.Type.PIXEL_DUNGEON );
		title.brightness(0.6f);
		add( title );

		float topRegion = Math.max(title.height - 6, h*0.45f);

		title.x = (w - title.width()) / 2f;
		title.y = 2 + (topRegion - title.height()) / 2f;

		align(title);

		Image signs = new Image( BannerSprites.get( BannerSprites.Type.PIXEL_DUNGEON_SIGNS ) ) {
			private float time = 0;
			@Override
			public void update() {
				super.update();
				am = Math.max(0f, (float)Math.sin( time += Game.elapsed ));
				if (time >= 1.5f*Math.PI) time = 0;
			}
			@Override
			public void draw() {
				Blending.setLightMode();
				super.draw();
				Blending.setNormalMode();
			}
		};
		signs.x = title.x + (title.width() - signs.width())/2f;
		signs.y = title.y;
		add( signs );
		
		StyledButton okay = new StyledButton(Chrome.Type.GREY_BUTTON_TR, Messages.get(this, "continue")){
			@Override
			protected void onClick() {
				super.onClick();
				if (previousVersion == 0 || MISPDSettings.intro()){
					MISPDSettings.version(MusicImplantSPD.versionCode);
					GamesInProgress.selectedClass = null;
					GamesInProgress.curSlot = 1;
					MusicImplantSPD.switchScene(CharacterBuilderScene.class);
				} else {
					updateVersion(previousVersion);
					MusicImplantSPD.switchScene(TitleScene.class);
				}
			}
		};

		float buttonY = Math.min(topRegion + (PixelScene.landscape() ? 60 : 120), h - 24);

		if (previousVersion != 0 && !MISPDSettings.intro()){
			StyledButton changes = new StyledButton(Chrome.Type.GREY_BUTTON_TR, Messages.get(TitleScene.class, "changes")){
				@Override
				protected void onClick() {
					super.onClick();
					updateVersion(previousVersion);
					MusicImplantSPD.switchScene(ChangesScene.class);
				}
			};
			okay.setRect(title.x, buttonY, (title.width()/2)-2, 20);
			add(okay);

			changes.setRect(okay.right()+2, buttonY, (title.width()/2)-2, 20);
			changes.icon(Icons.get(Icons.NEWS));
			add(changes);
		} else {
			okay.text(Messages.get(TitleScene.class, "enter"));
			okay.setRect(title.x, buttonY, title.width(), 20);
			okay.icon(Icons.get(Icons.ENTER));
			add(okay);
		}

		RenderedTextBlock text = PixelScene.renderTextBlock(6);
		String message;
		if (previousVersion == 0 || MISPDSettings.intro()) {
			message = Messages.get(this, "welcome_msg");
		} else if (previousVersion <= MusicImplantSPD.versionCode) {
			if (previousVersion < LATEST_UPDATE){
				message = Messages.get(this, "update_intro");
				message += "\n\n" + Messages.get(this, "update_msg");
			} else {
				//TODO: change the messages here in accordance with the type of patch.
				message = Messages.get(this, "patch_intro");
				message += "\n";
				//message += "\n" + Messages.get(this, "patch_balance");
				message += "\n" + Messages.get(this, "patch_bugfixes");
				message += "\n" + Messages.get(this, "patch_translations");

			}
		} else {
			message = Messages.get(this, "what_msg");
		}
		text.text(message, w-20);
		float textSpace = okay.top() - topRegion - 4;
		text.setPos((w - text.width()) / 2f, (topRegion + 2) + (textSpace - text.height())/2);
		add(text);

	}

	private void updateVersion(int previousVersion){

		//update rankings, to update any data which may be outdated
		if (previousVersion < LATEST_UPDATE){
			int highestChalInRankings = 0;
			try {
				Rankings.INSTANCE.load();
				for (Rankings.Record rec : Rankings.INSTANCE.records.toArray(new Rankings.Record[0])){
					try {
						Rankings.INSTANCE.loadGameData(rec);
						if (rec.win) highestChalInRankings = Math.max(highestChalInRankings, Challenges.activeChallenges());
						Rankings.INSTANCE.saveGameData(rec);
					} catch (Exception e) {
						//if we encounter a fatal per-record error, then clear that record
						Rankings.INSTANCE.records.remove(rec);
						MusicImplantSPD.reportException(e);
					}
				}
				Rankings.INSTANCE.save();
			} catch (Exception e) {
				//if we encounter a fatal error, then just clear the rankings
				FileUtils.deleteFile( Rankings.RANKINGS_FILE );
				MusicImplantSPD.reportException(e);
			}

			//fixes a bug from v0.9.0- where champion badges would rarely not save
			if (highestChalInRankings > 0){
				Badges.loadGlobal();
				if (highestChalInRankings >= 1) Badges.addGlobal(Badges.Badge.CHAMPION_1);
				if (highestChalInRankings >= 3) Badges.addGlobal(Badges.Badge.CHAMPION_2);
				if (highestChalInRankings >= 6) Badges.addGlobal(Badges.Badge.CHAMPION_3);
				Badges.saveGlobal();
			}
		}

		//resetting language preference back to native for finnish speakers if they were on english
		//This is because Finnish was unmaintained for quite a while
//		if ( previousVersion <= 500
//				&& Languages.matchLocale(Locale.getDefault()) == Languages.FINNISH
//				&& Messages.lang() == Languages.ENGLISH) {
//			MISPDSettings.language(Languages.FINNISH);
//			Messages.setup(Languages.FINNISH);
//		}
		
		MISPDSettings.version(MusicImplantSPD.versionCode);
	}
	
}
