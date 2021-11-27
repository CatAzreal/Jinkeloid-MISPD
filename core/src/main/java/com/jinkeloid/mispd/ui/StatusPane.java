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

package com.jinkeloid.mispd.ui;

import com.jinkeloid.mispd.Assets;
import com.jinkeloid.mispd.Dungeon;
import com.jinkeloid.mispd.MISPDAction;
import com.jinkeloid.mispd.Statistics;
import com.jinkeloid.mispd.actors.hero.Perk;
import com.jinkeloid.mispd.effects.Speck;
import com.jinkeloid.mispd.items.Item;
import com.jinkeloid.mispd.journal.Journal;
import com.jinkeloid.mispd.messages.Messages;
import com.jinkeloid.mispd.scenes.GameScene;
import com.jinkeloid.mispd.scenes.PixelScene;
import com.jinkeloid.mispd.sprites.HeroSprite;
import com.jinkeloid.mispd.utils.GLog;
import com.jinkeloid.mispd.windows.WndGame;
import com.jinkeloid.mispd.windows.WndHero;
import com.jinkeloid.mispd.windows.WndJournal;
import com.jinkeloid.mispd.windows.WndMessage;
import com.watabou.input.GameAction;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.ui.Button;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.ColorMath;

public class StatusPane extends Component {

	private NinePatch bg;
	private Image avatar;
	public static float talentBlink;
	private float warning;

	private int lastTier = 0;

	private Image rawShielding;
	private Image shieldedHP;
	private Image hp;
	private Image hpIndicator;
	//tbh I don't like writing a bunch of code just for two pixels on top of HP bar, but will do at this stage
	private Image hpCover;
	private BitmapText hpText;

	private Image st;
	private Image stIndicator;

	private Image exp;

	private BossHealthBar bossHP;

	private int lastLvl = -1;

	private BitmapText level;
	private BitmapText depth;

	private DangerIndicator danger;
	private BuffIndicator buffs;
	private Compass compass;

	private JournalButton btnJournal;
	private MenuButton btnMenu;

	private Toolbar.PickedUpItem pickedUp;
	
	private BitmapText version;
	private BitmapText pointSpent;

	@Override
	protected void createChildren() {

		bg = new NinePatch( Assets.Interfaces.STATUS, 0, 0, 128, 36, 85, 0, 45, 0 );
		add( bg );

		add( new Button(){
			@Override
			protected void onClick () {
				Camera.main.panTo( Dungeon.hero.sprite.center(), 5f );
				GameScene.show( new WndHero() );
			}
			
			@Override
			public GameAction keyAction() {
				return MISPDAction.HERO_INFO;
			}
		}.setRect( 0, 1, 30, 30 ));

		btnJournal = new JournalButton();
		add( btnJournal );

		btnMenu = new MenuButton();
		add( btnMenu );

		avatar = HeroSprite.avatar( Dungeon.hero.heroClass, lastTier );
		add( avatar );

		talentBlink = 0;

		compass = new Compass( Statistics.amuletObtained ? Dungeon.level.entrance : Dungeon.level.exit );
		add( compass );

		rawShielding = new Image( Assets.Interfaces.SHLD_BAR ,0,0,0,0);
		rawShielding.alpha(0.5f);
		add(rawShielding);

		shieldedHP = new Image( Assets.Interfaces.SHLD_BAR ,0,0,0,0);
		add(shieldedHP);

		hp = new Image( Assets.Interfaces.HP_BAR ,0,0,41,4);
		add( hp );

		hpIndicator = new Image( Assets.Interfaces.HP_BAR ,0,5,6,6);
		add(hpIndicator);

		hpCover = new Image( Assets.Interfaces.HP_BAR ,7,5,1,2);
		add(hpCover);

		st = new Image( Assets.Interfaces.ST_BAR ,0,0,41,4);
		add( st );

		stIndicator = new Image( Assets.Interfaces.ST_BAR ,0,5,6,6);
		add(stIndicator);

		hpText = new BitmapText(PixelScene.pixelFont);
		hpText.alpha(0.6f);
		add(hpText);

		exp = new Image( Assets.Interfaces.XP_BAR );
		add( exp );

		bossHP = new BossHealthBar();
		add( bossHP );

		level = new BitmapText( PixelScene.pixelFont);
		level.hardlight( 0xFFFFAA );
		add( level );

		depth = new BitmapText( Integer.toString( Dungeon.depth ), PixelScene.pixelFont);
		depth.hardlight( 0xCACFC2 );
		depth.measure();
		add( depth );

		danger = new DangerIndicator();
		add( danger );

		buffs = new BuffIndicator( Dungeon.hero );
		add( buffs );

		add( pickedUp = new Toolbar.PickedUpItem());
		
		version = new BitmapText( "v" + Game.version, PixelScene.pixelFont);
		version.alpha( 0.5f );
		add(version);

		pointSpent = new BitmapText( "Ch.Points: " + Dungeon.hero.charPoint, PixelScene.pixelFont);
		pointSpent.alpha( 0.5f );
		add(pointSpent);
	}

	@Override
	protected void layout() {

		height = 32;

		bg.size( width, bg.height );

		avatar.x = bg.x + 15 - avatar.width / 2f;
		avatar.y = bg.y + 16 - avatar.height / 2f;
		PixelScene.align(avatar);

		compass.x = avatar.x + avatar.width / 2f - compass.origin.x;
		compass.y = avatar.y + avatar.height / 2f - compass.origin.y;
		PixelScene.align(compass);

		hpCover.x = hp.x = shieldedHP.x = rawShielding.x = 31;
		hpCover.y = hp.y = shieldedHP.y = rawShielding.y = 3;

		hpIndicator.x = hp.x + hp.width - 1;
		hpIndicator.y = 2;

		//these can be written in a compact way but this way the code is easier to understand
		st.x = 31;
		st.y = 8;
		stIndicator.x = st.x + st.width - 1;
		stIndicator.y = 7;

		hpText.scale.set(PixelScene.align(0.5f));
		hpText.x = hp.x + 1;
		hpText.y = hp.y + (hp.height - (hpText.baseLine()+hpText.scale.y))/2f;
		hpText.y -= 0.001f; //prefer to be slightly higher
		PixelScene.align(hpText);

		bossHP.setPos( 6 + (width - bossHP.width())/2, 20);

		depth.x = width - 35.5f - depth.width() / 2f;
		depth.y = 8f - depth.baseLine() / 2f;
		PixelScene.align(depth);

		danger.setPos( width - danger.width(), 20 );

		buffs.setPos( 31, 12 );

		btnJournal.setPos( width - 42, 1 );

		btnMenu.setPos( width - btnMenu.width(), 1 );
		
		version.scale.set(PixelScene.align(0.5f));
		version.measure();
		version.x = width - version.width();
		version.y = btnMenu.bottom() + (4 - version.baseLine());
		PixelScene.align(version);

		pointSpent.scale.set(PixelScene.align(0.5f));
		pointSpent.measure();
		pointSpent.x = width - pointSpent.width();
		pointSpent.y = version.y + pointSpent.height() - 1;
	}
	
	private static final int[] warningColors = new int[]{0x660000, 0xCC0000, 0x660000};

	@Override
	public void update() {
		super.update();
		
		int health = Dungeon.hero.HP;
		int shield = Dungeon.hero.shielding();
		int max = Dungeon.hero.HT;

		if (!Dungeon.hero.isAlive()) {
			avatar.tint(0x000000, 0.5f);
		} else if ((health/(float)max) < 0.3f) {
			warning += Game.elapsed * 5f *(0.4f - (health/(float)max));
			warning %= 1f;
			avatar.tint(ColorMath.interpolate(warning, warningColors), 0.5f );
		} else if (talentBlink > 0){
			talentBlink -= Game.elapsed;
			avatar.tint(1, 1, 0, (float)Math.abs(Math.sin(2*talentBlink)/2f));
		} else {
			avatar.resetColor();
		}

		Perk.onHeroStatusTrigger();
		if (!Dungeon.hero.hasPerk(Perk.LACK_OF_SENSE)){
			hp.scale.x = Math.max( 0, health/(float)max);
			shieldedHP.scale.x = health/(float)max;
			rawShielding.scale.x = shield/(float)max;
			hpIndicator.x = hp.x + hp.width*Math.max( 0, health/(float)max) - 1;
			if (shield <= 0){
				hpText.text(health + "/" + max);
			} else {
				hpText.text(health + "+" + shield +  "/" + max);
			}
		}else{
			hpText.alpha(0.8f);
			int ratio = (int)(5 * (float)health/max);
			if (health <= 0) ratio = -1;
			switch (ratio){
				case 5: case 4:
					hpText.text("Healthy");
					hpText.hardlight(0x0db53a);
					break;
				case 3:
					hpText.text("Lightly_Damaged");
					hpText.hardlight(0xd7f229);
					break;
				case 2:
					hpText.text("Damaged");
					hpText.hardlight(0xf4f734);
					break;
				case 1:
					hpText.text("Wounded");
					hpText.hardlight(0xe39219);
					break;
				case 0:
					hpText.text("Severely_Wounded");
					hpText.hardlight(0xba0606);
					break;
				case -1:
					hpText.text("Dead");
					hpText.hardlight(0x4a0101);
					break;
				default:
					hpText.text("Healthy??");
					hpText.hardlight(0x001296);
					break;
			}

			hpText.x = hp.x + hp.width/2 - hpText.width/4 - 2f;
			hpText.measure();
			PixelScene.align(hpText);
		}

		exp.scale.x = (width / exp.width) * Dungeon.hero.exp / Dungeon.hero.maxExp();

		if (Dungeon.hero.lvl != lastLvl) {

			if (lastLvl != -1) {
				Emitter emitter = (Emitter)recycle( Emitter.class );
				emitter.revive();
				emitter.pos( 27, 27 );
				emitter.burst( Speck.factory( Speck.STAR ), 12 );
			}

			lastLvl = Dungeon.hero.lvl;
			level.text( Integer.toString( lastLvl ) );
			level.measure();
			level.x = 27.5f - level.width() / 2f;
			level.y = 29.0f - level.baseLine() / 2f;
			PixelScene.align(level);
		}

		int tier = Dungeon.hero.tier();
		if (tier != lastTier) {
			lastTier = tier;
			avatar.copy( HeroSprite.avatar( Dungeon.hero.heroClass, tier ) );
		}
	}

	public void pickup( Item item, int cell) {
		pickedUp.reset( item,
			cell,
			btnJournal.journalIcon.x + btnJournal.journalIcon.width()/2f,
			btnJournal.journalIcon.y + btnJournal.journalIcon.height()/2f);
	}
	
	public void flash(){
		btnJournal.flashing = true;
	}
	
	public void updateKeys(){
		btnJournal.updateKeyDisplay();
	}

	private static class JournalButton extends Button {

		private Image bg;
		private Image journalIcon;
		private KeyDisplay keyIcon;
		
		private boolean flashing;

		public JournalButton() {
			super();

			width = bg.width + 13; //includes the depth display to the left
			height = bg.height + 4;
		}
		
		@Override
		public GameAction keyAction() {
			return MISPDAction.JOURNAL;
		}
		
		@Override
		protected void createChildren() {
			super.createChildren();

			bg = new Image( Assets.Interfaces.MENU, 2, 2, 13, 11 );
			add( bg );
			
			journalIcon = new Image( Assets.Interfaces.MENU, 31, 0, 11, 7);
			add( journalIcon );
			
			keyIcon = new KeyDisplay();
			add(keyIcon);
			updateKeyDisplay();
		}

		@Override
		protected void layout() {
			super.layout();

			bg.x = x + 13;
			bg.y = y + 2;
			
			journalIcon.x = bg.x + (bg.width() - journalIcon.width())/2f;
			journalIcon.y = bg.y + (bg.height() - journalIcon.height())/2f;
			PixelScene.align(journalIcon);
			
			keyIcon.x = bg.x + 1;
			keyIcon.y = bg.y + 1;
			keyIcon.width = bg.width - 2;
			keyIcon.height = bg.height - 2;
			PixelScene.align(keyIcon);
		}

		private float time;
		
		@Override
		public void update() {
			super.update();
			
			if (flashing){
				journalIcon.am = (float)Math.abs(Math.cos( 3 * (time += Game.elapsed) ));
				keyIcon.am = journalIcon.am;
				if (time >= 0.333f*Math.PI) {
					time = 0;
				}
			}
		}

		public void updateKeyDisplay() {
			keyIcon.updateKeys();
			keyIcon.visible = keyIcon.keyCount() > 0;
			journalIcon.visible = !keyIcon.visible;
			if (keyIcon.keyCount() > 0) {
				bg.brightness(.8f - (Math.min(6, keyIcon.keyCount()) / 20f));
			} else {
				bg.resetColor();
			}
		}

		@Override
		protected void onPointerDown() {
			bg.brightness( 1.5f );
			Sample.INSTANCE.play( Assets.Sounds.CLICK );
		}

		@Override
		protected void onPointerUp() {
			if (keyIcon.keyCount() > 0) {
				bg.brightness(.8f - (Math.min(6, keyIcon.keyCount()) / 20f));
			} else {
				bg.resetColor();
			}
		}

		@Override
		protected void onClick() {
			flashing = false;
			time = 0;
			keyIcon.am = journalIcon.am = 1;
			if (!Dungeon.hero.hasPerk(Perk.ILLITERATE)) GameScene.show(new WndJournal());
			else GameScene.show(new WndMessage(Messages.get(Journal.class, "illiterate")));
		}

	}

	private static class MenuButton extends Button {

		private Image image;

		public MenuButton() {
			super();

			width = image.width + 4;
			height = image.height + 4;
		}

		@Override
		protected void createChildren() {
			super.createChildren();

			image = new Image( Assets.Interfaces.MENU, 17, 2, 12, 11 );
			add( image );
		}

		@Override
		protected void layout() {
			super.layout();

			image.x = x + 2;
			image.y = y + 2;
		}

		@Override
		protected void onPointerDown() {
			image.brightness( 1.5f );
			Sample.INSTANCE.play( Assets.Sounds.CLICK );
		}

		@Override
		protected void onPointerUp() {
			image.resetColor();
		}

		@Override
		protected void onClick() {
			GameScene.show( new WndGame() );
		}
	}
}
