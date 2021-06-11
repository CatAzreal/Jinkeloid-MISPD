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

package com.jinkeloid.mispd.windows;

import com.jinkeloid.mispd.Assets;
import com.jinkeloid.mispd.actors.hero.Perk;
import com.jinkeloid.mispd.messages.Messages;
import com.jinkeloid.mispd.scenes.PixelScene;
import com.jinkeloid.mispd.ui.Icons;
import com.jinkeloid.mispd.ui.RedButton;
import com.jinkeloid.mispd.ui.RenderedTextBlock;
import com.jinkeloid.mispd.ui.Window;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;

public class WndInfoTalent extends Window {

	private static final float GAP	= 2;

	private static final int WIDTH = 120;

	private SmartTexture icons;
	private TextureFilm film;

	public WndInfoTalent(Perk perk, int points, Callback onUpgradeButton){
		super();

		IconTitle titlebar = new IconTitle();

		icons = TextureCache.get( Assets.Interfaces.TALENT_ICONS );
		film = new TextureFilm( icons, 16, 16 );

		Image buffIcon = new Image( icons );
		buffIcon.frame( film.get(perk.icon()) );

		titlebar.icon( buffIcon );
		String title = Messages.titleCase(perk.title());
		if (points > 0){
			title += " +" + points;
		}
		titlebar.label( title, Window.TITLE_COLOR );
		titlebar.setRect( 0, 0, WIDTH, 0 );
		add( titlebar );

		RenderedTextBlock txtInfo = PixelScene.renderTextBlock(perk.desc(), 6);
		txtInfo.maxWidth(WIDTH);
		txtInfo.setPos(titlebar.left(), titlebar.bottom() + 2*GAP);
		add( txtInfo );

		resize( WIDTH, (int)(txtInfo.bottom() + GAP) );

		if (onUpgradeButton != null) {
			RedButton upgrade = new RedButton( Messages.get(this, "upgrade") ) {
				@Override
				protected void onClick() {
					super.onClick();
					hide();
					onUpgradeButton.call();
				}
			};
			upgrade.icon(Icons.get(Icons.TALENT));
			upgrade.setRect(0, txtInfo.bottom() + 2*GAP, WIDTH, 18);
			add(upgrade);
			resize( WIDTH, (int)upgrade.bottom()+1 );
		}


	}

}
