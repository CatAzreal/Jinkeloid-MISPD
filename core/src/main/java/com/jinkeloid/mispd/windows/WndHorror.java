package com.jinkeloid.mispd.windows;

import com.jinkeloid.mispd.Assets;
import com.jinkeloid.mispd.Dungeon;
import com.jinkeloid.mispd.actors.hero.Perk;
import com.jinkeloid.mispd.messages.Messages;
import com.jinkeloid.mispd.scenes.PixelScene;
import com.jinkeloid.mispd.ui.Icons;
import com.jinkeloid.mispd.ui.RenderedTextBlock;
import com.jinkeloid.mispd.ui.Window;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;

public class WndHorror extends Window {

    private static final float GAP	= 2;

    private static final int WIDTH = 120;

    private SmartTexture icons;
    private TextureFilm film;

    public WndHorror(){
        super();

        IconTitle titlebar = new IconTitle();

        Image perkIcon = Icons.JINKELOID.get();
        String title;
        String desc;
        int horrorVal = (int)(Dungeon.hero.Horror / 25);
        switch (horrorVal){
            case 0:
                title = Messages.get(this, "title_uneasy");
                desc = Messages.get(this, "desc_uneasy");
                break;
            case 1:
                title = Messages.get(this, "title_frightened");
                desc = Messages.get(this, "desc_frightened");
                break;
            case 2:
                title = Messages.get(this, "title_horrified");
                desc = Messages.get(this, "desc_horrified");
                break;
            case 3:
                title = Messages.get(this, "title_trembling");
                desc = Messages.get(this, "desc_trembling");
                break;
            default:
                title = Messages.get(this, "title_error");
                desc = Messages.get(this, "desc_error");
                break;
        }
        desc += Messages.get(this, "desc_additional_info");
        titlebar.icon( perkIcon );
        titlebar.label( title.toUpperCase(), Window.TITLE_COLOR );
        titlebar.setRect( 0, 0, WIDTH, 0 );
        add( titlebar );

        RenderedTextBlock txtInfo = PixelScene.renderTextBlock(desc, 6);
        txtInfo.maxWidth(WIDTH);
        txtInfo.setPos(titlebar.left(), titlebar.bottom() + 2*GAP);
        add( txtInfo );

        resize( WIDTH, (int)txtInfo.bottom() + 2 );
    }
}
