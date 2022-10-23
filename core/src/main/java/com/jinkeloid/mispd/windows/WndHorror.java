package com.jinkeloid.mispd.windows;

import com.jinkeloid.mispd.Dungeon;
import com.jinkeloid.mispd.actors.buffs.Buff;
import com.jinkeloid.mispd.actors.buffs.Horror;
import com.jinkeloid.mispd.actors.hero.Perk;
import com.jinkeloid.mispd.messages.Messages;
import com.jinkeloid.mispd.scenes.PixelScene;
import com.jinkeloid.mispd.ui.HorrorGauge;
import com.jinkeloid.mispd.ui.Icons;
import com.jinkeloid.mispd.ui.RenderedTextBlock;
import com.jinkeloid.mispd.ui.Window;
import com.watabou.gltextures.SmartTexture;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;

public class WndHorror extends Window {

    private static final float GAP	= 2;

    private static final int WIDTH = 120;

    private SmartTexture icons;
    private TextureFilm film;

    private Image icon;

    public WndHorror(){
        super();

        IconTitle titlebar = new IconTitle();

        String title;
        String desc;
        String appendKey;
        float horrorVal = Buff.affect(Dungeon.hero, Horror.class).horror();
        int horrorState = (int) (horrorVal / 25f);
        switch (horrorState){
            case 0:
                appendKey = "uneasy";
                icon = Icons.get(HorrorGauge.horrorIcon.UNEASY);
                break;
            case 1:
                appendKey = "frightened";
                icon = Icons.get(HorrorGauge.horrorIcon.FRIGHTENED);
                break;
            case 2:
                appendKey = "horrified";
                icon = Icons.get(HorrorGauge.horrorIcon.HORRIFIED);
                break;
            case 3:
                appendKey = "trembling";
                icon = Icons.get(HorrorGauge.horrorIcon.TREMBLING);
                break;
            default:
                appendKey = "error";
                icon = Icons.get(HorrorGauge.horrorIcon.UNEASY);
                break;
        }
        title = Messages.get(this, "title_"+appendKey);
        desc = Messages.get(this, "desc_"+appendKey);

        if (Dungeon.hero.hasPerk(Perk.ADRENALINE))
            desc += "\n" + Messages.get(this, "desc_"+appendKey+"_positive", (int)Horror.movBonus(), (int)Horror.evaBonus(), (int)Horror.atkspeedBonus());
        desc += "\n" + Messages.get(this, "desc_"+appendKey+"_generic", (int)Horror.accPenalty());
        desc += "\n\n" + Messages.get(this, "desc_horror");
        titlebar.icon(icon);
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
