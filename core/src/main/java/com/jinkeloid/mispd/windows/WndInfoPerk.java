package com.jinkeloid.mispd.windows;

import com.jinkeloid.mispd.Assets;
import com.jinkeloid.mispd.actors.hero.Perk;
import com.jinkeloid.mispd.messages.Messages;
import com.jinkeloid.mispd.scenes.PixelScene;
import com.jinkeloid.mispd.ui.RenderedTextBlock;
import com.jinkeloid.mispd.ui.Window;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;

public class WndInfoPerk extends Window {

    private static final float GAP	= 2;

    private static final int WIDTH = 120;

    private SmartTexture icons;
    private TextureFilm film;

    public WndInfoPerk(Perk perk){
        super();

        IconTitle titlebar = new IconTitle();

        icons = TextureCache.get( Assets.Interfaces.TALENT_ICONS );
        film = new TextureFilm( icons, 16, 16 );

        Image perkIcon = new Image( icons );
        perkIcon.frame( film.get(perk.icon()) );

        titlebar.icon( perkIcon );
        titlebar.label( perk.title().toUpperCase(), Window.TITLE_COLOR );
        titlebar.setRect( 0, 0, WIDTH, 0 );
        add( titlebar );

        RenderedTextBlock txtInfo = PixelScene.renderTextBlock(perk.desc(), 6);
        txtInfo.maxWidth(WIDTH);
        txtInfo.setPos(titlebar.left(), titlebar.bottom() + 2*GAP);
        add( txtInfo );

        resize( WIDTH, (int)txtInfo.bottom() + 2 );
    }
}
