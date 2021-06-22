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
import com.jinkeloid.mispd.Dungeon;
import com.jinkeloid.mispd.GamesInProgress;
import com.jinkeloid.mispd.Rankings;
import com.jinkeloid.mispd.MISPDSettings;
import com.jinkeloid.mispd.MusicImplantSPD;
import com.jinkeloid.mispd.actors.hero.HeroClass;
import com.jinkeloid.mispd.actors.hero.Perk;
import com.jinkeloid.mispd.items.rings.RingOfForce;
import com.jinkeloid.mispd.items.weapon.melee.Glaive;
import com.jinkeloid.mispd.journal.Journal;
import com.jinkeloid.mispd.messages.Messages;
import com.jinkeloid.mispd.ui.ActionIndicator;
import com.jinkeloid.mispd.ui.CheckBox;
import com.jinkeloid.mispd.ui.ExitButton;
import com.jinkeloid.mispd.ui.IconButton;
import com.jinkeloid.mispd.ui.Icons;
import com.jinkeloid.mispd.ui.LinkedCheckBox;
import com.jinkeloid.mispd.ui.RenderedTextBlock;
import com.jinkeloid.mispd.ui.ScrollPane;
import com.jinkeloid.mispd.ui.StyledButton;
import com.jinkeloid.mispd.ui.Window;
import com.jinkeloid.mispd.utils.GLog;
import com.jinkeloid.mispd.windows.WndInfoPerk;
import com.jinkeloid.mispd.windows.WndMessage;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.PointerArea;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.GameMath;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CharacterBuilderScene extends PixelScene {
	//formally Hero Select Scene

	//could still be useful as static UI
	private Image background;
	private RenderedTextBlock prompt;
	private RenderedTextBlock posLabel;
	private RenderedTextBlock negLabel;
	private RenderedTextBlock pointPrompt;

	//fading UI elements
	private ArrayList<StyledButton> heroBtns = new ArrayList<>();
	private StyledButton startBtn;
	private IconButton btnExit;

	//Total character points spent
	private int charPoint;
	//Temporary list to store perks selected in this scene
	public static ArrayList<Perk> tempPerks;
	//Link map for dynamic perk buttons
	public Map<Integer, LinkedCheckBox> buttonLinkRef = new HashMap<Integer, LinkedCheckBox>();

	@Override
	public void create() {
		super.create();

		Badges.loadGlobal();
		Journal.loadGlobal();
		//each time we enter the scene, the perk list need to be cleared
		if(tempPerks != null) {
			tempPerks.clear();
		} else {
			tempPerks = new ArrayList<Perk>();
		}
//		background = new Image(HeroClass.WARRIOR.splashArt()){
//			@Override
//			public void update() {
//				if (rm > 1f){
//					rm -= Game.elapsed;
//					gm = bm = rm;
//				} else {
//					rm = gm = bm = 1;
//				}
//			}
//		};
//		background.scale.set(Camera.main.height/background.height);
//
//		background.x = (Camera.main.width - background.width())/2f;
//		background.y = (Camera.main.height - background.height())/2f;
//		background.visible = false;
//		PixelScene.align(background);
//		add(background);

//		if (background.x > 0){
//			Image fadeLeft = new Image(TextureCache.createGradient(0xFF000000, 0x00000000));
//			fadeLeft.x = background.x-2;
//			fadeLeft.scale.set(4, background.height());
//			add(fadeLeft);
//
//			Image fadeRight = new Image(fadeLeft);
//			fadeRight.x = background.x + background.width() + 2;
//			fadeRight.y = background.y + background.height();
//			fadeRight.angle = 180;
//			add(fadeRight);
//		}

		//PositiveList
		int w = Camera.main.width;
		int h = Camera.main.height;

		ScrollPane posList = new ScrollPane( new Component() );
		add( posList );

		Component posContent = posList.content();
		posContent.clear();

		List<Perk> posPerkList = Perk.getPerksByType(Perk.perkType.POSITIVE);

		float pos = 0;

		for (Perk perk: posPerkList) {
			LinkedCheckBox cb = new LinkedCheckBox( "_+" + perk.pointCosts() + "_ " +
					Messages.titleCase(perk.title()), 7, perk.id(), perk.oppositePerks()){
				@Override
				protected void layout() {
					super.layout();
					//The hotarea shouldn't overlap with the info button
					hotArea.width = width - 14;
					float margin = (height - text.height()) / 2;
					text.setPos( x + margin/2, y + margin);
					PixelScene.align(text);
				}

				@Override
				protected void onClick() {
					super.onClick();
					if (this.disabled()){return;}
					//if the button is checked after being pressed, add the character points to scene, else cancel it
					if (this.checked()){
						if (perk.oppositePerks() != null){
							for (int perkid : perk.oppositePerks()) {
								GLog.i("button is deactivated" + perkid);
								buttonLinkRef.get(perkid).disabled(true);
							}
						}
						charPoint += perk.pointCosts();
						tempPerks.add(perk);
					} else {
						if (perk.oppositePerks() != null){
							for (int perkid : perk.oppositePerks()) {
								GLog.i("button is activated" + perkid);
								buttonLinkRef.get(perkid).disabled(false);
							}
						}
						charPoint -= perk.pointCosts();
						tempPerks.remove(perk);
					}
					GLog.i("charpoint is" + charPoint);
				}
			};
			IconButton info = new IconButton(Icons.get(Icons.INFO), true){
				@Override
				protected void onClick() {
					super.onClick();
					MusicImplantSPD.scene().add(
							new WndInfoPerk(perk)
					);
				}
			};
			buttonLinkRef.put(perk.id(), cb);
			cb.setRect(0, pos, w/2-10, 16);
			posContent.add(cb);
			info.setRect(cb.right()-14, pos+3, 12, 12);
			posContent.add(info);
			pos = cb.bottom();
			pos += 2;
		}

		posContent.setSize( w/2-10, pos);

		//in case future me forgot, ScrollPane.setRect means the size it displays on screen
		posList.setRect( 5, 40, w/2-5, h-100 );
		posList.scrollTo(0, 0);


		//Negative Perk list starts here--------------------------------------------------
		ScrollPane negList = new ScrollPane( new Component() );
		add( negList );

		Component negContent = negList.content();
		negContent.clear();

		List<Perk> negPerkList = Perk.getPerksByType(Perk.perkType.NEGATIVE);

		pos = 0;

		for (Perk perk: negPerkList) {
			LinkedCheckBox cb = new LinkedCheckBox( "_-" + perk.pointCosts() + "_ " +
					Messages.titleCase(perk.title()), 7, perk.id(), perk.oppositePerks()){
				@Override
				protected void layout() {
					super.layout();
					hotArea.width = width - 14;
					float margin = (height - text.height()) / 2;
					text.setPos( x + margin/2, y + margin);
					PixelScene.align(text);
				}

				@Override
				protected void onClick() {
					super.onClick();
					if (this.disabled()){
						MusicImplantSPD.scene().add(
								new WndMessage("This perk cannot be picked because it conflict with another chosen perk! Unselect that perk first.")
						);
						return;}
					//if the button is checked after being pressed, add the character points to scene, else cancel it
					if (this.checked()){
						if (perk.oppositePerks() != null){
							for (int perkid : perk.oppositePerks()) {
								GLog.i("button is deactivated" + perkid);
								buttonLinkRef.get(perkid).disabled(true);
							}
						}
						charPoint -= perk.pointCosts();
						tempPerks.add(perk);
					} else {
						if (perk.oppositePerks() != null){
							for (int perkid : perk.oppositePerks()) {
								GLog.i("button is activated" + perkid);
								buttonLinkRef.get(perkid).disabled(false);
							}
						}
						charPoint += perk.pointCosts();
						tempPerks.remove(perk);
					}
					GLog.i("charpoint is" + charPoint);
				}
			};
			IconButton info = new IconButton(Icons.get(Icons.INFO), true){
				@Override
				protected void onClick() {
					super.onClick();
					MusicImplantSPD.scene().add(
							new WndInfoPerk(perk)
					);
				}
			};
			buttonLinkRef.put(perk.id(), cb);
			cb.setRect(0, pos, w/2-10, 16);
			negContent.add(cb);
			info.setRect(cb.right()-14, pos+3, 12, 12);
			negContent.add(info);
			pos = cb.bottom();
			pos += 2;
		}

		negContent.setSize( w/2-10, pos);

		negList.setRect( w/2+5, 40, w-5, h-100 );
		negList.scrollTo(0, 0);

		prompt = PixelScene.renderTextBlock(Messages.get(this, "title"), 12);
		prompt.hardlight(Window.TITLE_COLOR);
		prompt.setPos( (w - prompt.width())/2f, prompt.height());
		PixelScene.align(prompt);
		add(prompt);

		posLabel = PixelScene.renderTextBlock(Messages.get(this, "poslabel"), 10);
		posLabel.hardlight(0x33BB33);
		posLabel.setPos( (w/2 - posLabel.width())/2f, prompt.bottom() + 7);
		PixelScene.align(posLabel);
		add(posLabel);

		negLabel = PixelScene.renderTextBlock(Messages.get(this, "neglabel"), 10);
		negLabel.hardlight(0xcb2901);
		negLabel.setPos( (3*w/2 - negLabel.width())/2f, prompt.bottom() + 7);
		PixelScene.align(negLabel);
		add(negLabel);

		//point indicator
		pointPrompt = PixelScene.renderTextBlock(Messages.get(this, "pointprompt", charPoint), 10);
		pointPrompt.setPos( (w - pointPrompt.width())/2f, h - pointPrompt.height() - 30);
		PixelScene.align(pointPrompt);
		add(pointPrompt);

		startBtn = new StyledButton(Chrome.Type.GREY_BUTTON_TR, Messages.get(this, "startgame")){
			@Override
			protected void onClick() {
				super.onClick();

				GamesInProgress.selectedClass = HeroClass.WARRIOR;

				Dungeon.hero = null;
				ActionIndicator.action = null;
				InterlevelScene.mode = InterlevelScene.Mode.DESCEND;

				if (MISPDSettings.intro()) {
					MISPDSettings.intro( false );
					Game.switchScene( IntroScene.class );
				} else {
					Game.switchScene( InterlevelScene.class );
				}
			}
		};
		startBtn.icon(Icons.get(Icons.ENTER));
		startBtn.setSize(80, 21);
		startBtn.setPos((w - startBtn.width())/2f, (h + 2 - startBtn.height()));
		add(startBtn);

//		startBtn.visible = false;

//		infoButton = new IconButton(Icons.get(Icons.INFO)){
//			@Override
//			protected void onClick() {
//				super.onClick();
//				MusicImplantSPD.scene().addToFront(new WndHeroInfo(GamesInProgress.selectedClass));
//			}
//		};
//		infoButton.visible = false;
//		infoButton.setSize(21, 21);
//		add(infoButton);
//
//		HeroClass[] classes = HeroClass.values();
//
//		int btnWidth = HeroBtn.MIN_WIDTH;
//		int curX = (Camera.main.width - btnWidth * classes.length)/2;
//		if (curX > 0){
//			btnWidth += Math.min(curX/(classes.length/2), 15);
//			curX = (Camera.main.width - btnWidth * classes.length)/2;
//		}
//
//		int heroBtnleft = curX;
//		for (HeroClass cl : classes){
//			HeroBtn button = new HeroBtn(cl);
//			button.setRect(curX, Camera.main.height-HeroBtn.HEIGHT+3, btnWidth, HeroBtn.HEIGHT);
//			curX += btnWidth;
//			add(button);
//			heroBtns.add(button);
//		}
//
//		challengeButton = new IconButton(
//				Icons.get( MISPDSettings.challenges() > 0 ? Icons.CHALLENGE_ON :Icons.CHALLENGE_OFF)){
//			@Override
//			protected void onClick() {
//				MusicImplantSPD.scene().addToFront(new WndChallenges(MISPDSettings.challenges(), true) {
//					public void onBackPressed() {
//						super.onBackPressed();
//						icon(Icons.get(MISPDSettings.challenges() > 0 ? Icons.CHALLENGE_ON : Icons.CHALLENGE_OFF));
//					}
//				} );
//			}
//
//			@Override
//			public void update() {
//				if( !visible && GamesInProgress.selectedClass != null){
//					visible = true;
//				}
//				super.update();
//			}
//		};
//		challengeButton.setRect(heroBtnleft + 16, Camera.main.height-HeroBtn.HEIGHT-16, 21, 21);
//		challengeButton.visible = false;
//
//		if (DeviceCompat.isDebug() || Badges.isUnlocked(Badges.Badge.VICTORY)){
//			add(challengeButton);
//		} else {
//			Dungeon.challenges = 0;
//			MISPDSettings.challenges(0);
//		}

		btnExit = new ExitButton();
		btnExit.setPos(w - btnExit.width(), 0);
		add( btnExit );
		btnExit.visible = !MISPDSettings.intro() || Rankings.INSTANCE.totalNumber > 0;
//
//		PointerArea fadeResetter = new PointerArea(0, 0, Camera.main.width, Camera.main.height){
//			@Override
//			public boolean onSignal(PointerEvent event) {
//				resetFade();
//				return false;
//			}
//		};
//		add(fadeResetter);
//		resetFade();
//
//		if (GamesInProgress.selectedClass != null){
//			setSelectedHero(GamesInProgress.selectedClass);
//		}

		fadeIn();

	}

//	private void setSelectedHero(HeroClass cl){
//		GamesInProgress.selectedClass = cl;
//
//		background.texture( cl.splashArt() );
//		background.visible = true;
//		background.hardlight(1.5f,1.5f,1.5f);
//
//		prompt.visible = false;
//		startBtn.visible = true;
//		startBtn.text("Start");
//		startBtn.textColor(Window.TITLE_COLOR);
//		startBtn.setSize(startBtn.reqWidth() + 8, 21);
//		startBtn.setPos((Camera.main.width - startBtn.width())/2f, startBtn.top());
//		PixelScene.align(startBtn);
//
//		infoButton.visible = true;
//		infoButton.setPos(startBtn.right(), startBtn.top());
//
//		challengeButton.visible = true;
//		challengeButton.setPos(startBtn.left()-challengeButton.width(), startBtn.top());
//	}

	private float uiAlpha;

	@Override
	public void update() {
		super.update();
		btnExit.visible = !MISPDSettings.intro() || Rankings.INSTANCE.totalNumber > 0;
		//do not fade when a window is open
		for (Object v : members){
			if (v instanceof Window) resetFade();
		}
		if (GamesInProgress.selectedClass != null) {
			if (uiAlpha > 0f){
				uiAlpha -= Game.elapsed/4f;
			}
			float alpha = GameMath.gate(0f, uiAlpha, 1f);
			for (StyledButton b : heroBtns){
				b.alpha(alpha);
			}
			startBtn.alpha(alpha);
			btnExit.icon().alpha(alpha);
//			challengeButton.icon().alpha(alpha);
//			infoButton.icon().alpha(alpha);
		}
		pointPrompt.text(Messages.get(this, "pointprompt", charPoint));
	}

	private void resetFade(){
		//starts fading after 4 seconds, fades over 4 seconds.
		uiAlpha = 2f;
	}

	@Override
	protected void onBackPressed() {
		if (btnExit.visible){
			MusicImplantSPD.switchScene(TitleScene.class);
		} else {
			super.onBackPressed();
		}
	}

//	private class HeroBtn extends StyledButton {
//
//		private HeroClass cl;
//
//		private static final int MIN_WIDTH = 20;
//		private static final int HEIGHT = 24;
//
//		HeroBtn ( HeroClass cl ){
//			super(Chrome.Type.GREY_BUTTON_TR, "");
//
//			this.cl = cl;
//
//			icon(new Image(cl.spritesheet(), 0, 90, 12, 15));
//
//		}
//
//		@Override
//		public void update() {
//			super.update();
//			if (cl != GamesInProgress.selectedClass){
//				if (!cl.isUnlocked()){
//					icon.brightness(0.1f);
//				} else {
//					icon.brightness(0.6f);
//				}
//			} else {
//				icon.brightness(1f);
//			}
//		}
//
//		@Override
//		protected void onClick() {
//			super.onClick();
//
//			if( !cl.isUnlocked() ){
//				MusicImplantSPD.scene().addToFront( new WndMessage(cl.unlockMsg()));
//			} else if (GamesInProgress.selectedClass == cl) {
//				MusicImplantSPD.scene().add(new WndHeroInfo(cl));
//			} else {
//				setSelectedHero(cl);
//			}
//		}
//	}

//	private static class WndHeroInfo extends WndTabbed {
//
//		private RenderedTextBlock title;
//		private RenderedTextBlock info;
//
//		private TalentsPane talents;
//		private RedButton firstSub;
//		private RedButton secondSub;
//
//		private int WIDTH = 120;
//		private int HEIGHT = 120;
//		private int MARGIN = 2;
//		private int INFO_WIDTH = WIDTH - MARGIN*2;
//
//		private static boolean secondSubclass = false;
//
//		public WndHeroInfo( HeroClass cl ){
//
//			title = PixelScene.renderTextBlock(9);
//			title.hardlight(TITLE_COLOR);
//			add(title);
//
//			info = PixelScene.renderTextBlock(6);
//			add(info);
//
//			ArrayList<LinkedHashMap<Talent, Integer>> talentList = new ArrayList<>();
//			Talent.initClassTalents(cl, talentList);
//			Talent.initSubclassTalents(cl.subClasses()[secondSubclass ? 1 : 0], talentList);
//			talents = new TalentsPane(false, talentList);
//			add(talents);
//
//			firstSub = new RedButton(Messages.titleCase(cl.subClasses()[0].title()), 7){
//				@Override
//				protected void onClick() {
//					super.onClick();
//					if (secondSubclass){
//						secondSubclass = false;
//						hide();
//						WndHeroInfo newWindow = new WndHeroInfo(cl);
//						newWindow.talents.scrollTo(0, talents.content().camera.scroll.y);
//						newWindow.select(2);
//						MusicImplantSPD.scene().addToFront(newWindow);
//					}
//				}
//			};
//			if (!secondSubclass) firstSub.textColor(Window.TITLE_COLOR);
//			firstSub.setSize(40, firstSub.reqHeight()+2);
//			add(firstSub);
//
//			secondSub = new RedButton(Messages.titleCase(cl.subClasses()[1].title()), 7){
//				@Override
//				protected void onClick() {
//					super.onClick();
//					if (!secondSubclass){
//						secondSubclass = true;
//						hide();
//						WndHeroInfo newWindow = new WndHeroInfo(cl);
//						newWindow.talents.scrollTo(0, talents.content().camera.scroll.y);
//						newWindow.select(2);
//						MusicImplantSPD.scene().addToFront(newWindow);
//					}
//				}
//			};
//			if (secondSubclass) secondSub.textColor(Window.TITLE_COLOR);
//			secondSub.setSize(40, secondSub.reqHeight()+2);
//			add(secondSub);
//
//			Tab tab;
//			Image[] tabIcons;
//			switch (cl){
//				case WARRIOR: default:
//					tabIcons = new Image[]{
//							new ItemSprite(ItemSpriteSheet.SEAL, null),
//							new ItemSprite(ItemSpriteSheet.WORN_SHORTSWORD, null)
//					};
//					break;
//				case MAGE:
//					tabIcons = new Image[]{
//							new ItemSprite(ItemSpriteSheet.MAGES_STAFF, null),
//							new ItemSprite(ItemSpriteSheet.HOLDER, null)
//					};
//					break;
//				case ROGUE:
//					tabIcons = new Image[]{
//							new ItemSprite(ItemSpriteSheet.ARTIFACT_CLOAK, null),
//							new ItemSprite(ItemSpriteSheet.DAGGER, null)
//					};
//					break;
//				case HUNTRESS:
//					tabIcons = new Image[]{
//							new ItemSprite(ItemSpriteSheet.SPIRIT_BOW, null),
//							new ItemSprite(ItemSpriteSheet.GLOVES, null)
//					};
//					break;
//			}
//
//			tab = new IconTab( tabIcons[0] ){
//				@Override
//				protected void select(boolean value) {
//					super.select(value);
//					if (value){
//						title.text(Messages.titleCase(Messages.get(WndHeroInfo.class, "innate_title")));
//						info.text(Messages.get(cl, cl.name() + "_desc_innate"), INFO_WIDTH);
//					}
//				}
//			};
//			add(tab);
//
//			tab = new IconTab( tabIcons[1] ){
//				@Override
//				protected void select(boolean value) {
//					super.select(value);
//					if (value){
//						title.text(Messages.titleCase(Messages.get(WndHeroInfo.class, "loadout_title")));
//						info.text(Messages.get(cl, cl.name() + "_desc_loadout"), INFO_WIDTH);
//					}
//				}
//			};
//			add(tab);
//
//			tab = new IconTab( Icons.get(Icons.TALENT) ){
//				@Override
//				protected void select(boolean value) {
//					super.select(value);
//					if (value){
//						title.text(Messages.titleCase(Messages.get(WndHeroInfo.class, "talents_title")));
//						info.text(Messages.get(WndHeroInfo.class, "talents_desc"), INFO_WIDTH);
//					}
//					talents.visible = talents.active = value;
//					firstSub.visible = firstSub.active = value;
//					secondSub.visible = secondSub.active = value;
//				}
//			};
//			add(tab);
//
//			tab = new IconTab(new ItemSprite(ItemSpriteSheet.MASTERY, null)){
//				@Override
//				protected void select(boolean value) {
//					super.select(value);
//					if (value){
//						title.text(Messages.titleCase(Messages.get(WndHeroInfo.class, "subclasses_title")));
//						String msg = Messages.get(cl, cl.name() + "_desc_subclasses");
//						for (HeroSubClass sub : cl.subClasses()){
//							msg += "\n\n" + sub.desc();
//						}
//						info.text(msg, INFO_WIDTH);
//					}
//				}
//			};
//			add(tab);
//
//			resize(WIDTH, HEIGHT);
//			select(0);
//
//		}
//
//		@Override
//		public void select(Tab tab) {
//			super.select(tab);
//
//			title.setPos((WIDTH-title.width())/2, MARGIN);
//			info.setPos(MARGIN, title.bottom()+2*MARGIN);
//
//			firstSub.setPos((title.left() - firstSub.width())/2, 0);
//			secondSub.setPos(title.right() + (WIDTH - title.right() - secondSub.width())/2, 0);
//
//			talents.setRect(0, info.bottom()+MARGIN, WIDTH, HEIGHT - (info.bottom()+MARGIN));
//
//			resize(WIDTH, Math.max(HEIGHT, (int)info.bottom()));
//
//			layoutTabs();
//
//		}
//	}
}
