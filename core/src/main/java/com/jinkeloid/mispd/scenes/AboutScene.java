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
import com.jinkeloid.mispd.MusicImplantSPD;
import com.jinkeloid.mispd.effects.Flare;
import com.jinkeloid.mispd.ui.ExitButton;
import com.jinkeloid.mispd.ui.Icons;
import com.jinkeloid.mispd.ui.RenderedTextBlock;
import com.jinkeloid.mispd.ui.ScrollPane;
import com.jinkeloid.mispd.ui.Window;
import com.jinkeloid.mispd.utils.GLog;
import com.watabou.input.PointerEvent;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.PointerArea;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.DeviceCompat;

import javax.management.OperationsException;

public class AboutScene extends PixelScene {

	private static final float SCROLL_SPEED	= 30f;
	public static ScrollPane list;
	private float shift = -40;

	@Override
	public void create() {
		super.create();

		final float colWidth = 120;
		final float fullWidth = colWidth * (landscape() ? 2 : 1);

		int w = Camera.main.width;
		int h = Camera.main.height;

		list = new ScrollPane( new Component() );
		add( list );

		Component content = list.content();
		content.clear();

		//*** MISPD Credits ***

		CreditsBlock mispd = new CreditsBlock(true, Window.MISPD_COLOR,
				"Music Implanted SPD",
				Icons.SHPX.get(),
				"Developer: _Jinkeloid_\n" +
						"Based on Shattered Pixel Dungeon's open source\n\n" +
						"Additional UI: Jinkeloid\n" +
						"Additional Artwork: xxx\n" +
						"Sound Effects: xxx\n\n"
				,
				null,
				null);
		mispd.setRect((w - fullWidth)/2f, 120, 120, 0);
		content.add(mispd);

		//*** Musics Used ***

		CreditsBlock musicUsed = new CreditsBlock(true, Window.MISPD_COLOR,
				"Music:\n",
				null,
				"Placeholders\nPlaceholders\nPlaceholders\nPlaceholders\nPlaceholders\nPlaceholders\nPlaceholders\n"
				,
				null,
				null);
		musicUsed.setRect((w - fullWidth)/2f, mispd.bottom()+6, 120, 0);
		content.add(musicUsed);

		//*** Special Thanks ***

		CreditsBlock specialThanks = new CreditsBlock(true, Window.MISPD_COLOR,
				"Special Thanks to:\n",
				null,
				"Evan Debenham\n\n ConsideredHamster\n\n Omicronrg\n\n" +
						"Players from Chinese PD community\n who supported me and this mod back from 2016\n\n" +
						"All the kind souls on PD Discord\n\n" +
						"...\n" +
						"_And you._\n"
				,
				null,
				null);
		specialThanks.setRect((w - fullWidth)/2f, musicUsed.bottom()+6, 120, 0);
		content.add(specialThanks);

		//*** Shattered Pixel Dungeon Credits ***

		CreditsBlock shpx = new CreditsBlock(true, Window.SHPX_COLOR,
				"Shattered Pixel Dungeon",
				Icons.SHPX.get(),
				"Developer: _Evan Debenham_\n" +
						"Based on Pixel Dungeon's open source\n\n" +
						"Hero Art & Design: Alexsandar Komitov\n" +
						"Sound Effects: Charlie\n\n" +
						"Powered by libGDX\n" +
						"libGDX Made by: Edu García\n" +
						"Shattered GDX Help: Kevin MacMartin"
				,
				null,
				null);
		shpx.setRect((w - fullWidth)/2f, specialThanks.bottom()+6, 120, 0);
		content.add(shpx);

		addLine(shpx.top() - 4, content);

		//*** Freesound Credits ***

		CreditsBlock freesound = new CreditsBlock(true,
				Window.TITLE_COLOR,
				"_Freesound.org_ Sound Effects:\n",
				null,
						"_SFX ATTACK SWORD 001.wav_ by _JoelAudio_\n" +
						"_Pack: Slingshots and Longbows_ by _saturdaysoundguy_\n" +
						"_Cracking/Crunching, A.wav_ by _InspectorJ_\n" +
						"_Extracting a sword.mp3_ by _Taira Komori_\n" +
						"_Pack: Uni Sound Library_ by _timmy h123_\n" +
						"_Pack: Movie Foley: Swords_ by _Black Snow_\n" +
						"_machine gun shot 2.flac_ by _qubodup_\n" +
						"_m240h machine gun burst 4.flac_ by _qubodup_\n" +
						"_Pack: Onomatopoeia_ by _Adam N_\n" +
						"_Pack: Watermelon_ by _lolamadeus_\n" +
						"_metal chain_ by _Mediapaja2009_\n" +
						"_Pack: Sword Clashes Pack_ by _JohnBuhr_\n" +
						"_Pack: Metal Clangs and Pings_ by _wilhellboy_\n" +
						"_Pack: Stabbing Stomachs & Crushing Skulls_ by _TheFilmLook_\n" +
						"_Sheep bleating_ by _zachrau_\n" +
						"_Lemon,Juicy,Squeeze,Fruit.wav_ by _Filipe Chagas_\n" +
						"_Lemon,Squeeze,Squishy,Fruit.wav_ by _Filipe Chagas_",
				null,
				null);
		freesound.setRect((Camera.main.width - colWidth)/2f-10, shpx.bottom() + 8, colWidth+20, 0);
		content.add(freesound);

		//*** Pixel Dungeon Credits ***

		final int WATA_COLOR = 0x55AAFF;
		CreditsBlock wata = new CreditsBlock(true, WATA_COLOR,
				"Pixel Dungeon",
				Icons.WATA.get(),
				"Developed by: _Watabou_\nInspired by Brian Walker's Brogue\n" +
						"Music: Cube Code",
				null,
				null);
		if (landscape()){
			wata.setRect(shpx.left(), freesound.bottom() + 8, colWidth, 0);
		} else {
			wata.setRect(shpx.left(), freesound.bottom() + 8, colWidth, 0);
		}
		content.add(wata);

		addLine(wata.top() - 4, content);

		//*** Some Words ***

		CreditsBlock thanks = new CreditsBlock(true, Window.TITLE_COLOR,
				"Thank you for playing!",
				null,
				null,
				null,
				null);
		if (landscape()){
			thanks.setRect(shpx.left(), wata.bottom() + 40, colWidth, 0);
		} else {
			thanks.setRect(shpx.left(), wata.bottom() + 40, colWidth, 0);
		}
		content.add(thanks);

		content.setSize( fullWidth, thanks.bottom() + 10 );

		list.setRect( 0, 0, w, thanks.bottom() - h/2f );
		list.scrollTo(0, 0);

		ExitButton btnExit = new ExitButton();
		btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
		add( btnExit );

		fadeIn();
	}

	@Override
	public void update() {
		super.update();
		ScrollPane list = AboutScene.list;
		list.disableThumb();
		shift += Game.elapsed * SCROLL_SPEED;
		list.scrollTo(0, Math.min(Math.max(shift, 0), list.height()));
//		throw new RuntimeException("custom exception");
	}

	@Override
	protected void onBackPressed() {
		MusicImplantSPD.switchScene(TitleScene.class, new Game.SceneChangeCallback() {
			@Override
			public void beforeCreate() {
				shift = -40;
			}

			@Override
			public void afterCreate() {
			}
		});
	}

	private void addLine( float y, Group content ){
		ColorBlock line = new ColorBlock(Camera.main.width, 1, 0xFF333333);
		line.y = y;
		content.add(line);
	}

	private static class CreditsBlock extends Component {

		boolean large;
		RenderedTextBlock title;
		Image avatar;
		Flare flare;
		RenderedTextBlock body;

		RenderedTextBlock link;
		ColorBlock linkUnderline;
		PointerArea linkButton;

		//many elements can be null, but body is assumed to have content.
		private CreditsBlock(boolean large, int highlight, String title, Image avatar, String body, String linkText, String linkUrl){
			super();

			this.large = large;

			if (title != null) {
				this.title = PixelScene.renderTextBlock(title, large ? 8 : 6);
				if (highlight != -1) this.title.hardlight(highlight);
				add(this.title);
			}

			if (avatar != null){
				this.avatar = avatar;
				add(this.avatar);
			}

			if (large && highlight != -1 && this.avatar != null){
				this.flare = new Flare( 7, 24 ).color( highlight, true ).show(this.avatar, 0);
				this.flare.angularSpeed = 20;
			}

			this.body = PixelScene.renderTextBlock(body, 6);
			if (highlight != -1) this.body.setHightlighting(true, highlight);
			if (large) this.body.align(RenderedTextBlock.CENTER_ALIGN);
			add(this.body);

			if (linkText != null && linkUrl != null){

				int color = 0xFFFFFFFF;
				if (highlight != -1) color = 0xFF000000 | highlight;
				this.linkUnderline = new ColorBlock(1, 1, color);
				add(this.linkUnderline);

				this.link = PixelScene.renderTextBlock(linkText, 6);
				if (highlight != -1) this.link.hardlight(highlight);
				add(this.link);

				linkButton = new PointerArea(0, 0, 0, 0){
					@Override
					protected void onClick( PointerEvent event ) {
						DeviceCompat.openURI( linkUrl );
					}
				};
				add(linkButton);
			}

		}

		@Override
		protected void layout() {
			super.layout();

			float topY = top();

			if (title != null){
				title.maxWidth((int)width());
				title.setPos( x + (width() - title.width())/2f, topY);
				topY += title.height() + (large ? 2 : 1);
			}

			if (large){

				if (avatar != null){
					avatar.x = x + (width()-avatar.width())/2f;
					avatar.y = topY;
					PixelScene.align(avatar);
					if (flare != null){
						flare.point(avatar.center());
					}
					topY = avatar.y + avatar.height() + 2;
				}

				body.maxWidth((int)width());
				body.setPos( x + (width() - body.width())/2f, topY);
				topY += body.height() + 2;

			} else {

				if (avatar != null){
					avatar.x = x;
					body.maxWidth((int)(width() - avatar.width - 1));

					if (avatar.height() > body.height()){
						avatar.y = topY;
						body.setPos( avatar.x + avatar.width() + 1, topY + (avatar.height() - body.height())/2f);
						topY += avatar.height() + 1;
					} else {
						avatar.y = topY + (body.height() - avatar.height())/2f;
						PixelScene.align(avatar);
						body.setPos( avatar.x + avatar.width() + 1, topY);
						topY += body.height() + 2;
					}

				} else {
					topY += 1;
					body.maxWidth((int)width());
					body.setPos( x, topY);
					topY += body.height()+2;
				}

			}

			if (link != null){
				if (large) topY += 1;
				link.maxWidth((int)width());
				link.setPos( x + (width() - link.width())/2f, topY);
				topY += link.height() + 2;

				linkButton.x = link.left()-1;
				linkButton.y = link.top()-1;
				linkButton.width = link.width()+2;
				linkButton.height = link.height()+2;

				linkUnderline.size(link.width(), PixelScene.align(0.49f));
				linkUnderline.x = link.left();
				linkUnderline.y = link.bottom()+1;

			}

			topY -= 2;

			height = Math.max(height, topY - top());
		}
	}
}
