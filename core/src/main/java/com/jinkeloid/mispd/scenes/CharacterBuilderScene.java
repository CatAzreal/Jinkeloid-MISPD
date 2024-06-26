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

import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import com.jinkeloid.mispd.Assets;
import com.jinkeloid.mispd.Badges;
import com.jinkeloid.mispd.Chrome;
import com.jinkeloid.mispd.Dungeon;
import com.jinkeloid.mispd.GamesInProgress;
import com.jinkeloid.mispd.Rankings;
import com.jinkeloid.mispd.MISPDSettings;
import com.jinkeloid.mispd.MusicImplantSPD;
import com.jinkeloid.mispd.actors.hero.HeroClass;
import com.jinkeloid.mispd.actors.hero.Perk;
import com.jinkeloid.mispd.items.weapon.melee.Gloves;
import com.jinkeloid.mispd.journal.Journal;
import com.jinkeloid.mispd.messages.Messages;
import com.jinkeloid.mispd.ui.ActionIndicator;
import com.jinkeloid.mispd.ui.ExitButton;
import com.jinkeloid.mispd.ui.IconButton;
import com.jinkeloid.mispd.ui.Icons;
import com.jinkeloid.mispd.ui.LinkedCheckBox;
import com.jinkeloid.mispd.ui.RenderedTextBlock;
import com.jinkeloid.mispd.ui.ScrollPane;
import com.jinkeloid.mispd.ui.StyledButton;
import com.jinkeloid.mispd.ui.Tag;
import com.jinkeloid.mispd.ui.Window;
import com.jinkeloid.mispd.utils.GLog;
import com.jinkeloid.mispd.windows.WndGameInProgress;
import com.jinkeloid.mispd.windows.WndInfoPerk;
import com.jinkeloid.mispd.windows.WndMessage;
import com.jinkeloid.mispd.windows.WndSetup;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.GameMath;

import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class CharacterBuilderScene extends PixelScene {
	//formally Hero Select Scene

	//could still be useful as static UI
	private Image background;
	private RenderedTextBlock prompt;
	private RenderedTextBlock posLabel;
	private RenderedTextBlock negLabel;
	private RenderedTextBlock pointPrompt;
	int w = Camera.main.width;
	int h = Camera.main.height;
	float gemPosition = w/2f;
	float gemDistance = 26;
	public static final int effortless 	= 30;
	public static final int casual 		= 12;
	public static final int risky = -12;
	public static final int challenging = -30;
	public enum difficulty {
		EFFORTLESS,
		CASUAL,
		STANDARD,
		RISKY,
		CHALLENGING
	}

	//fading UI elements
	private ArrayList<StyledButton> heroBtns = new ArrayList<>();
	private StyledButton startBtn;
	private StyledButton shopBtn;
	private StyledButton menuBtn;

	private StyledButton setupBtn;
	private StyledButton saveBtn;
	private IconButton btnExit;
	private IconButton pointMeter;
	private IconButton gemPointer;
	private IconButton logo;

	//Total character points spent
	public static int charPoint;
	//Temporary list to store perks selected in this scene
	public static ArrayList<Perk> tempPerks;
	//Link map for dynamic perk buttons
	public Map<Integer, LinkedCheckBox> buttonLinkRef = new HashMap<Integer, LinkedCheckBox>();
	//need this list to access all checkboxes
	public ArrayList<LinkedCheckBox> cbList = new ArrayList<LinkedCheckBox>();

	public difficulty curDiff(int charPoint) {
		switch ((charPoint > effortless ) 	? 0 :
				(charPoint > casual) 		? 1 :
				(charPoint >= risky) 		? 2 :
				(charPoint >= challenging) 	? 3 : 4) {
			case 0:
				gemPosition = w/2f - gemDistance*2;
				return difficulty.EFFORTLESS;
			case 1:
				gemPosition = w/2f - gemDistance;
				return difficulty.CASUAL;
			case 2:
				gemPosition = w/2f;
				return difficulty.STANDARD;
			case 3:
				gemPosition = w/2f + gemDistance;
				return difficulty.RISKY;
			case 4:
				gemPosition = w/2f + gemDistance*2;
				return difficulty.CHALLENGING;
		}
		return difficulty.STANDARD;
	}

	@Override
	public void create() {
		super.create();
		charPoint = 0;
		Badges.loadGlobal();
		Journal.loadGlobal();
		//each time we enter the scene, the perk list need to be cleared
		if(tempPerks != null) {
			tempPerks.clear();
		} else {
			tempPerks = new ArrayList<Perk>();
		}

		float pos = 0;

		//positive list
		{
			ScrollPane posList = new ScrollPane( new Component() );
			add( posList );

			Component posContent = posList.content();
			posContent.clear();


			List<Perk> posPerkList = Perk.getPerksByType(Perk.perkType.POSITIVE);

			for (Perk perk : posPerkList) {
				LinkedCheckBox cb = new LinkedCheckBox("_+" + perk.pointCosts() + "_ " +
						Messages.titleCase(perk.title()), 7, perk.id(), perk.conflictPerks(), true) {
					@Override
					protected void layout() {
						super.layout();
						//The hotarea shouldn't overlap with the info button
						hotArea.width = width - 14;
						float margin = (height - text.height()) / 2;
						text.setPos(x + margin / 2, y + margin);
						PixelScene.align(text);
					}

					@Override
					protected void onClick() {
						super.onClick();
						perkOnSelect(this, perk);
					}
				};
				IconButton info = new IconButton(Icons.get(Icons.INFO), true) {
					@Override
					protected void onClick() {
						super.onClick();
						MusicImplantSPD.scene().add(
								new WndInfoPerk(perk)
						);
					}

					@Override
					protected void layout() {
						super.layout();

						if (icon != null) {
							icon.x = x + (width - icon.width() / 2f) / 2f;
							icon.y = y + (height - icon.height() / 2f) / 2f;
							icon.scale.set(0.67f);
							PixelScene.align(icon);
						}
					}
				};
				cbList.add(cb);
				buttonLinkRef.put(perk.id(), cb);
				cb.setRect(0, pos, w / 2 - 10, 16);
				posContent.add(cb);
				info.setRect(cb.right() - 16, pos + 2, 8, 8);
				posContent.add(info);
				pos = cb.bottom();
				pos += 2;
			}

			posContent.setSize(w / 2 - 10, pos);

			//in case future me forgot, ScrollPane.setRect means the size it displays on screen
			posList.setRect(5, 40, w / 2 - 5, h - 110);
			posList.scrollTo(0, 0);
		}


		//Negative Perk list starts here
		{
			ScrollPane negList = new ScrollPane( new Component() );
			add( negList );

			Component negContent = negList.content();
			negContent.clear();

			List<Perk> negPerkList = Perk.getPerksByType(Perk.perkType.NEGATIVE);

			pos = 0;

			for (Perk perk: negPerkList) {
				LinkedCheckBox cb = new LinkedCheckBox( "_-" + perk.pointCosts() + "_ " +
						Messages.titleCase(perk.title()), 7, perk.id(), perk.conflictPerks(), false){
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
						perkOnSelect(this, perk);
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
					@Override
					protected void layout() {
						super.layout();

						if (icon != null) {
							icon.x = x + (width - icon.width()/2f) / 2f;
							icon.y = y + (height - icon.height()/2f) / 2f;
							icon.scale.set(0.5f);
							PixelScene.align(icon);
						}
					}
				};
				cbList.add(cb);
				buttonLinkRef.put(perk.id(), cb);
				cb.setRect(0, pos, w/2-10, 16);
				negContent.add(cb);
				info.setRect(cb.right()-14, pos+4, 6, 6);
				negContent.add(info);
				pos = cb.bottom();
				pos += 2;
			}

			negContent.setSize( w/2-10, pos);

			negList.setRect( w/2+5, 40, w-5, h-110 );
			negList.scrollTo(0, 0);
		}

		logo = new IconButton(Icons.get(Icons.TRAIT));
		add(logo);

		prompt = PixelScene.renderTextBlock(Messages.get(this, "title"), 12);
		prompt.hardlight(Window.TITLE_COLOR);
		add(prompt);
		//logo and title
		logo.setPos( (w - 16 - prompt.width())/2f, 16);
		prompt.setPos( (w + 16 - prompt.width())/2f, prompt.height());

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

		shopBtn = new StyledButton(Chrome.Type.TAG_BIG, Messages.get(this, "shop"), 9){
			@Override
			protected void onClick() {
				super.onClick();
				Game.switchScene( ShopScene.class );
			}

			@Override
			protected void layout() {
				super.layout();
				icon.x = x + (width() - icon.width)/2f + 1;
				icon.y = y + 1f;
				PixelScene.align(icon);

				text.setPos(
						x + (width() - text.width())/2f + 1f,
						icon.y + icon.height - 1f
				);
				PixelScene.align(text);
			}
		};
		shopBtn.icon(Icons.get(Icons.SHOP));
		shopBtn.setSize(48, 40);
		shopBtn.setPos(w - shopBtn.width(), (h - shopBtn.height()));
		add(shopBtn);

		menuBtn = new StyledButton(Chrome.Type.TAG_BIG, Messages.get(this, "menu"), 9, true){
			@Override
			protected void onClick() {
				super.onClick();
				Game.switchScene( TitleScene.class );
			}

			@Override
			protected void layout() {
				super.layout();
				icon.x = x + (width() - icon.width)/2f - 2;
				icon.y = y + 1f;
				PixelScene.align(icon);

				text.setPos(
						x + (width() - text.width())/2f - 1f,
						icon.y + icon.height - 1f
				);
				PixelScene.align(text);
			}
		};
		menuBtn.icon(Icons.get(Icons.MENU));
		menuBtn.setSize(48, 40);
		menuBtn.setPos(0, (h - menuBtn.height()));
		add(menuBtn);

		//point indicator
		pointPrompt = PixelScene.renderTextBlock(String.valueOf(charPoint), 10);
		pointPrompt.setPos( ((float)w - pointPrompt.width())/2 , h - pointPrompt.height() - menuBtn.height() - 12f);
		PixelScene.align(pointPrompt);
		add(pointPrompt);

		//point meter
		pointMeter = new IconButton(Icons.get(Icons.METER));
		pointMeter.setPos( (float) w/2, h - pointMeter.height() - menuBtn.height() - 10f);
		PixelScene.align(pointMeter);
		add(pointMeter);

		//gem pointer
		gemPointer = new IconButton(Icons.get(Icons.GEM));
		gemPointer.setPos( 0, pointMeter.top() + 4f);
		PixelScene.align(gemPointer);
		add(gemPointer);

		setupBtn = new StyledButton(Chrome.Type.GREY_BUTTON, Messages.get(WndSetup.class, "load"), 8){
			@Override
			protected void onClick() {
				super.onClick();
				MusicImplantSPD.scene().add( new WndSetup(true));
			}
		};
		setupBtn.setSize(40, 20);
		setupBtn.setPos((float) (w - setupBtn.width())/2, menuBtn.top());
		PixelScene.align(setupBtn);
		add(setupBtn);

		saveBtn = new StyledButton(Chrome.Type.GREY_BUTTON, Messages.get(WndSetup.class, "save"), 8){
			@Override
			protected void onClick() {
				super.onClick();
				MusicImplantSPD.scene().add( new WndSetup(false));
			}
		};
		saveBtn.setSize(40, 20);
		saveBtn.setPos((float) (w - saveBtn.width())/2, setupBtn.bottom());
		PixelScene.align(saveBtn);
		add(saveBtn);

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

//		fadeIn();

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
//		btnExit.visible = !MISPDSettings.intro() || Rankings.INSTANCE.totalNumber > 0;
		//do not fade when a window is open
		//
//		for (Object v : members){
//			if (v instanceof Window) resetFade();
//		}
//		if (GamesInProgress.selectedClass != null) {
//			if (uiAlpha > 0f){
//				uiAlpha -= Game.elapsed/4f;
//			}
//			float alpha = GameMath.gate(0f, uiAlpha, 1f);
//			for (StyledButton b : heroBtns){
//				b.alpha(alpha);
//			}
//			startBtn.alpha(alpha);
//			btnExit.icon().alpha(alpha);
//			challengeButton.icon().alpha(alpha);
//			infoButton.icon().alpha(alpha);
//		}
		curDiff(charPoint);
		pointPrompt.text(String.valueOf(charPoint));
		pointPrompt.setPos( ((float)w - pointPrompt.width())/2 , h - pointPrompt.height() - menuBtn.height() - 12f);
		gemPointer.setPos( gemPosition, pointMeter.top() + 4f);
	}

	private void resetFade(){
		//starts fading after 4 seconds, fades over 4 seconds.
		uiAlpha = 2f;
	}

	@Override
	protected void onBackPressed() {
//		if (btnExit.visible){
			MusicImplantSPD.switchScene(TitleScene.class);
//		} else {
//			super.onBackPressed();
//		}
	}

	public void perkOnSelect(LinkedCheckBox cb, Perk perk) {
		if (cb.disabled()){
			StringBuilder conPerk = new StringBuilder();
			if (perk.conflictPerks() != null){
				for (int perkid : perk.conflictPerks()) {
					conPerk.append(Perk.getPerkNameByID(perkid)).append(", ");
				}
			}
			MusicImplantSPD.scene().add(
					new WndMessage(Messages.get(CharacterBuilderScene.class, "conflictperk", conPerk.toString()))
			);
			return;
		}
		//if the button is checked after being pressed, add the character points to scene, else cancel it
		if (cb.checked()){
			if (perk.conflictPerks() != null){
				for (int perkid : perk.conflictPerks()) {
					buttonLinkRef.get(perkid).disabled(true);
				}
			}
			charPoint += perk.isPositive() ? perk.pointCosts() : - perk.pointCosts();
			tempPerks.add(perk);
		} else {
			if (perk.conflictPerks() != null){
				for (int perkid : perk.conflictPerks()) {
					buttonLinkRef.get(perkid).disabled(false);
					//check for every pairing perk, if any is selected, keep locking the conflicting perk
					if (perk.pairingPerks() != null) {
						for (int pairid : perk.pairingPerks()) {
							if (buttonLinkRef.get(pairid).checked()) {
								buttonLinkRef.get(perkid).disabled(true);
							}
						}
					}
				}
			}
			charPoint -= perk.isPositive() ? perk.pointCosts() : - perk.pointCosts();
			tempPerks.remove(perk);
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
