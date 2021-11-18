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

import com.jinkeloid.mispd.Assets;
import com.jinkeloid.mispd.Chrome;
import com.jinkeloid.mispd.GamesInProgress;
import com.jinkeloid.mispd.MISPDSettings;
import com.jinkeloid.mispd.MusicImplantSPD;
import com.jinkeloid.mispd.effects.Fireball;
import com.jinkeloid.mispd.messages.Messages;
import com.jinkeloid.mispd.services.news.News;
import com.jinkeloid.mispd.services.updates.AvailableUpdateData;
import com.jinkeloid.mispd.services.updates.Updates;
import com.jinkeloid.mispd.ui.AnimatedButton;
import com.jinkeloid.mispd.ui.AnimatedSprite;
import com.jinkeloid.mispd.ui.AnimatedTitle;
import com.jinkeloid.mispd.ui.IconButton;
import com.jinkeloid.mispd.ui.Icons;
import com.jinkeloid.mispd.ui.StyledButton;
import com.jinkeloid.mispd.ui.TitleBg;
import com.jinkeloid.mispd.ui.Window;
import com.jinkeloid.mispd.utils.GLog;
import com.jinkeloid.mispd.windows.WndMessage;
import com.jinkeloid.mispd.windows.WndOptions;
import com.jinkeloid.mispd.windows.WndSettings;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Callback;
import com.watabou.utils.ColorMath;
import com.watabou.utils.DeviceCompat;

import java.util.Date;

import sun.security.mscapi.PRNG;

public class TitleScene extends PixelScene {
	AnimatedButton btnStart;
	AnimatedButton btnSigil;
	AnimatedButton btnGameLog;
	AnimatedButton btnConfig;
	AnimatedButton btnCredit;

	@Override
	public void create() {
		
		super.create();

		Music.INSTANCE.play( Assets.Music.THEME, true );

		uiCamera.visible = false;
		
		int w = Camera.main.width;
		int h = Camera.main.height;

		AnimatedTitle aniTitle = new AnimatedTitle();
		float titleScale = (float) w/aniTitle.width/1.5f;
		Component title = new Component(){
			@Override
			protected void createChildren() {
				add(aniTitle);
			}

			@Override
			protected void layout() {
				aniTitle.scale.set(titleScale);
				aniTitle.x = (w - aniTitle.width * titleScale)/2;
				aniTitle.y = 12;
			}
		};
		aniTitle.play(aniTitle.idle);
		float topRegion = Math.max(aniTitle.y + aniTitle.height * titleScale + 6, h*0.33f);

		TitleBg titleBg = new TitleBg(new Callback() {
			@Override
			public void call() {
				aniTitle.alpha(1);
				aniTitle.play(aniTitle.init);
			}
		});


		AnimatedSprite spriteStart = new AnimatedSprite(Assets.Sprites.A_BTN_START, new int[] {176, 64, 16, 16}, new int[] {});
		AnimatedSprite spriteSigil = new AnimatedSprite(Assets.Sprites.A_BTN_SIGIL, new int[] {176, 64, 33, 19}, new int[] {});
		AnimatedSprite spriteGameLog = new AnimatedSprite(Assets.Sprites.A_BTN_GAMELOG, new int[] {176, 64, 33, 19}, new int[] {});
		AnimatedSprite spriteConfig = new AnimatedSprite(Assets.Sprites.A_BTN_CONFIG, new int[] {176, 64, 33, 19}, new int[] {});
		AnimatedSprite spriteCredit = new AnimatedSprite(Assets.Sprites.A_BTN_CREDIT, new int[] {176, 64, 33, 19}, new int[] {});

		btnStart = new AnimatedButton(spriteStart){
			@Override
			protected void onClick() {
				super.onClick();
				if (GamesInProgress.checkAll().size() == 0){
					GamesInProgress.selectedClass = null;
					GamesInProgress.curSlot = 1;
					spriteStart.callback = new Callback() {
						@Override
						public void call() {
							if (!uiTrigger) MusicImplantSPD.switchNoFade(CharacterBuilderScene.class);
						}
					};
				} else {
					spriteStart.callback = new Callback() {
						@Override
						public void call() {
							if (btnStart.sprite.alpha() == 1) MusicImplantSPD.switchNoFade( StartScene.class );
						}
					};
				}
			}

			@Override
			protected boolean onLongClick() {
				//making it easier to start runs quickly while debugging
				if (DeviceCompat.isDebug()) {
					GamesInProgress.selectedClass = null;
					GamesInProgress.curSlot = 1;
					spriteStart.callback = new Callback() {
						@Override
						public void call() {
							MusicImplantSPD.switchNoFade(CharacterBuilderScene.class);
						}
					};
					return true;
				}
				return super.onLongClick();
			}
		};
		spriteStart.play(spriteStart.unpressed);

		btnSigil = new AnimatedButton(spriteSigil, 1.5f){
			@Override
			protected void onPointerDown() { }
			@Override
			protected void onPointerUp() {}
			@Override
			protected void onClick() {
				if (btnSigil.sprite.alpha() == 1) MusicImplantSPD.scene().add(new WndMessage("Sigil is not available yet"));
//				super.onClick();
//				spriteSigil.callback = new Callback() {
//					@Override
//					public void call() {
//						MusicImplantSPD.switchNoFade( BadgesScene.class );
//					}
//				};
			}

			@Override
			protected boolean onLongClick() {
				return super.onLongClick();
			}
		};
		spriteSigil.play(spriteSigil.disabled);

		btnGameLog = new AnimatedButton(spriteGameLog, 1.5f){
			@Override
			protected void onClick() {
				super.onClick();
				spriteGameLog.callback = new Callback() {
					@Override
					public void call() {
						if (btnGameLog.sprite.alpha() == 1) MusicImplantSPD.switchNoFade(RankingsScene.class);
					}
				};
			}

			@Override
			protected boolean onLongClick() {
				return super.onLongClick();
			}
		};
		spriteGameLog.play(spriteGameLog.unpressed);

		btnConfig = new AnimatedButton(spriteConfig, 1.5f){
			@Override
			protected void onClick() {
				super.onClick();
				spriteConfig.callback = new Callback() {
					@Override
					public void call() {
						if (btnConfig.sprite.alpha() == 1) MusicImplantSPD.scene().add(new WndSettings());
					}
				};
			}

			@Override
			protected boolean onLongClick() {
				return super.onLongClick();
			}
		};
		spriteConfig.play(spriteConfig.unpressed);

		btnCredit = new AnimatedButton(spriteCredit, 1.5f){
			@Override
			protected void onClick() {
				super.onClick();
				spriteCredit.callback = new Callback() {
					@Override
					public void call() {
						if (btnCredit.sprite.alpha() == 1) MusicImplantSPD.switchScene( CreditScene.class );
					}
				};
			}

			@Override
			protected boolean onLongClick() {
				return super.onLongClick();
			}
		};
		spriteCredit.play(spriteCredit.unpressed);


		PixelScene.align(titleBg);
		add(titleBg);
		add(title);
		align(title);
		add(btnStart);
		add(btnSigil);
		add(btnGameLog);
		add(btnConfig);
		add(btnCredit);
//		placeTorch(title.x + 22, title.y + 46);
//		placeTorch(title.x + title.width - 22, title.y + 46);

//		Image signs = new Image( BannerSprites.get( BannerSprites.Type.PIXEL_DUNGEON_SIGNS ) ) {
//			private float time = 0;
//			@Override
//			public void update() {
//				super.update();
//				am = Math.max(0f, (float)Math.sin( time += Game.elapsed ));
//				if (time >= 1.5f*Math.PI) time = 0;
//			}
//			@Override
//			public void draw() {
//				Blending.setLightMode();
//				super.draw();
//				Blending.setNormalMode();
//			}
//		};
//		signs.x = title.x + (title.width() - signs.width())/2f;
//		signs.y = title.y;
//		add( signs );

		if (Game.startTrigger){
			aniTitle.alpha(0);
			btnStart.sprite.alpha(0);
			btnSigil.sprite.alpha(0);
			btnGameLog.sprite.alpha(0);
			btnConfig.sprite.alpha(0);
			btnCredit.sprite.alpha(0);
		}


		aniTitle.callback = new Callback() {
			@Override
			public void call() {
				uiTrigger = true;
			}
		};

//		StyledButton btnSupport = new SupportButton(GREY_TR, Messages.get(this, "support"));
//		add(btnSupport);

//		StyledButton btnNews = new NewsButton(GREY_TR, Messages.get(this, "news"));
//		btnNews.icon(Icons.get(Icons.NEWS));
//		add(btnNews);
//
//		StyledButton btnChanges = new ChangesButton(GREY_TR, Messages.get(this, "changes"));
//		btnChanges.icon(Icons.get(Icons.CHANGES));
//		add(btnChanges);

		final int BTN_HEIGHT = 20;
		int GAP = (int)(h - topRegion - (landscape() ? 3 : 4)*BTN_HEIGHT)/3;
		GAP /= landscape() ? 3 : 5;
		GAP = Math.max(GAP, 2);

		IconButton btnTencentQQ = new IconButton(new Image(Assets.Interfaces.TENCENT_QQ), false){
			@Override
			protected void onClick() { DeviceCompat.openURI(MISPDSettings.URI_QQ); }

			@Override
			protected void layout() {
				icon.scale.set((float)18/900);
				super.layout();
			}
		};
		add(btnTencentQQ);

		IconButton btnDiscord = new IconButton(new Image(Assets.Interfaces.DISCORD), false){
			@Override
			protected void onClick() {
				DeviceCompat.openURI(MISPDSettings.URI_DISCORD);
			}

			@Override
			protected void layout() {
				icon.scale.set((float)18/900);
				super.layout();
			}
		};
		add(btnDiscord);

		if (landscape()) {
//			btnPlay.setRect(title.left()-50, topRegion+GAP, ((title.width()+100)/2)-1, BTN_HEIGHT);
//			align(btnPlay);
//			btnRankings.setRect(btnPlay.left(), btnPlay.bottom()+ GAP, (btnPlay.width()*.67f)-1, BTN_HEIGHT);
//			btnBadges.setRect(btnRankings.left(), btnRankings.bottom()+GAP, btnRankings.width(), BTN_HEIGHT);
//			btnNews.setRect(btnRankings.right()+2, btnRankings.top(), btnRankings.width(), BTN_HEIGHT);
//			btnChanges.setRect(btnNews.left(), btnNews.bottom() + GAP, btnRankings.width(), BTN_HEIGHT);
//			btnSettings.setRect(btnRankings.right()+2, btnRankings.top(), btnRankings.width(), BTN_HEIGHT);
//			btnAbout.setRect(btnSettings.left(), btnSettings.bottom() + GAP, btnRankings.width(), BTN_HEIGHT);
		} else {
			btnStart.setRect((w - spriteStart.spriteWidth(btnStart.scale))/2, topRegion+GAP, spriteStart.spriteWidth(btnStart.scale), spriteStart.spriteHeight(btnStart.scale));
			btnSigil.setRect(w / 2f - spriteSigil.spriteWidth(btnSigil.scale), btnStart.bottom()+ GAP, spriteSigil.spriteWidth(btnSigil.scale), spriteSigil.spriteHeight(btnSigil.scale));
			btnGameLog.setRect(w / 2f, btnSigil.top(), spriteGameLog.spriteWidth(btnGameLog.scale), spriteGameLog.spriteHeight(btnGameLog.scale));
			btnConfig.setRect(w / 2f - spriteConfig.spriteWidth(btnConfig.scale), btnSigil.bottom()+ GAP, spriteConfig.spriteWidth(btnConfig.scale), spriteConfig.spriteHeight(btnConfig.scale));
			btnCredit.setRect(w / 2f, btnConfig.top(), spriteCredit.spriteWidth(btnCredit.scale), spriteCredit.spriteHeight(btnCredit.scale));
			btnDiscord.setRect(2, camera().height - 16, 16, 16 );
			btnTencentQQ.setRect(btnDiscord.right(), camera().height - 16, 16, 16 );
//			btnRankings.setRect(btnStart.left(), btnConfig.bottom()+ GAP, (btnStart.width()/2)-1, BTN_HEIGHT);
//			btnBadges.setRect(btnRankings.right()+2, btnRankings.top(), btnRankings.width(), BTN_HEIGHT);
//			btnNews.setRect(btnRankings.left(), btnRankings.bottom()+ GAP, btnRankings.width(), BTN_HEIGHT);
//			btnChanges.setRect(btnNews.right()+2, btnNews.top(), btnNews.width(), BTN_HEIGHT);
//			btnSettings.setRect(btnRankings.left(), btnRankings.bottom()+GAP, btnRankings.width(), BTN_HEIGHT);
//			btnAbout.setRect(btnRankings.right()+2, btnRankings.top(), btnRankings.width(), BTN_HEIGHT);
		}

		BitmapText version = new BitmapText( "v" + Game.version, pixelFont);
		version.measure();
		version.hardlight( 0x888888 );
		version.x = w - version.width() - 4;
		version.y = h - version.height() - 2;
		add( version );

		fadeIn();
	}
	
	private void placeTorch( float x, float y ) {
		Fireball fb = new Fireball();
		fb.setPos( x, y );
		add( fb );
	}

	private static class NewsButton extends StyledButton {

		public NewsButton(Chrome.Type type, String label ){
			super(type, label);
			if (MISPDSettings.news()) News.checkForNews();
		}

		int unreadCount = -1;

		@Override
		public void update() {
			super.update();

			if (unreadCount == -1 && News.articlesAvailable()){
				long lastRead = MISPDSettings.newsLastRead();
				if (lastRead == 0){
					if (News.articles().get(0) != null) {
						MISPDSettings.newsLastRead(News.articles().get(0).date.getTime());
					}
				} else {
					unreadCount = News.unreadArticles(new Date(MISPDSettings.newsLastRead()));
					if (unreadCount > 0) {
						unreadCount = Math.min(unreadCount, 9);
						text(text() + "(" + unreadCount + ")");
					}
				}
			}

			if (unreadCount > 0){
				textColor(ColorMath.interpolate( 0xFFFFFF, Window.SHPX_COLOR, 0.5f + (float)Math.sin(Game.timeTotal*5)/2f));
			}
		}

		@Override
		protected void onClick() {
			super.onClick();
			MusicImplantSPD.switchNoFade( NewsScene.class );
		}
	}

	private static class ChangesButton extends StyledButton {

		public ChangesButton( Chrome.Type type, String label ){
			super(type, label);
			if (MISPDSettings.updates()) Updates.checkForUpdate();
		}

		boolean updateShown = false;

		@Override
		public void update() {
			super.update();

			if (!updateShown && (Updates.updateAvailable() || Updates.isInstallable())){
				updateShown = true;
				if (Updates.isInstallable())    text(Messages.get(TitleScene.class, "install"));
				else                            text(Messages.get(TitleScene.class, "update"));
			}

			if (updateShown){
				textColor(ColorMath.interpolate( 0xFFFFFF, Window.SHPX_COLOR, 0.5f + (float)Math.sin(Game.timeTotal*5)/2f));
			}
		}

		@Override
		protected void onClick() {
			if (Updates.isInstallable()){
				Updates.launchInstall();

			} else if (Updates.updateAvailable()){
				AvailableUpdateData update = Updates.updateData();

				MusicImplantSPD.scene().addToFront(new WndOptions(
						update.versionName == null ? Messages.get(this,"title") : Messages.get(this,"versioned_title", update.versionName),
						update.desc == null ? Messages.get(this,"desc") : update.desc,
						Messages.get(this,"update"),
						Messages.get(this,"changes")
				) {
					@Override
					protected void onSelect(int index) {
						if (index == 0) {
							Updates.launchUpdate(Updates.updateData());
						} else if (index == 1){
							ChangesScene.changesSelected = 0;
							MusicImplantSPD.switchNoFade( ChangesScene.class );
						}
					}
				});

			} else {
				ChangesScene.changesSelected = 0;
				MusicImplantSPD.switchNoFade( ChangesScene.class );
			}
		}

	}

//	private static class SettingsButton extends StyledButton {
//
//		public SettingsButton( Chrome.Type type, String label ){
//			super(type, label);
//			if (Messages.lang().status() == Languages.Status.INCOMPLETE){
//				icon(Icons.get(Icons.LANGS));
//				icon.hardlight(1.5f, 0, 0);
//			} else {
//				icon(Icons.get(Icons.PREFS));
//			}
//		}
//
//		@Override
//		public void update() {
//			super.update();
//
//			if (Messages.lang().status() == Languages.Status.INCOMPLETE){
//				textColor(ColorMath.interpolate( 0xFFFFFF, CharSprite.NEGATIVE, 0.5f + (float)Math.sin(Game.timeTotal*5)/2f));
//			}
//		}
//
//		@Override
//		protected void onClick() {
//			if (Messages.lang().status() == Languages.Status.INCOMPLETE){
//				WndSettings.last_index = 4;
//			}
//			MusicImplantSPD.scene().add(new WndSettings());
//		}
//	}

	private static class SupportButton extends StyledButton{

		public SupportButton( Chrome.Type type, String label ){
			super(type, label);
			icon(Icons.get(Icons.GOLD));
			textColor(Window.TITLE_COLOR);
		}

		@Override
		protected void onClick() {
			MusicImplantSPD.switchNoFade(SupporterScene.class);
		}
	}

	private boolean uiTrigger;
	private float timer;
	@Override
	public void update() {
		super.update();
		if (uiTrigger){
			timer += Game.elapsed;
			if (btnStart.sprite.alpha() == 0 && timer >= 0.25f){
				btnStart.sprite.alpha(1);
				timer = 0;
				return;
			}
			if ((btnSigil.sprite.alpha() == 0 || btnGameLog.sprite.alpha() == 0) && timer >= 0.25f){
				btnSigil.sprite.alpha(1);
				btnGameLog.sprite.alpha(1);
				timer = 0;
				return;
			}
			if ((btnConfig.sprite.alpha() == 0 || btnCredit.sprite.alpha() == 0) && timer >= 0.25f){
				btnConfig.sprite.alpha(1);
				btnCredit.sprite.alpha(1);
				uiTrigger = false;
				timer = 0;
			}
		}
	}
}
