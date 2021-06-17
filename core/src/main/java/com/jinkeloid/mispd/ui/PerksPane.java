package com.jinkeloid.mispd.ui;

import com.jinkeloid.mispd.Assets;
import com.jinkeloid.mispd.Dungeon;
import com.jinkeloid.mispd.actors.hero.Perk;
import com.jinkeloid.mispd.scenes.PixelScene;
import com.jinkeloid.mispd.utils.GLog;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;

    public class PerksPane extends Component{

    private static final int GAP = 2;

    private SmartTexture icons;
    private TextureFilm film;

    private float pos;
    private ScrollPane perkList;
    private ArrayList<PerkSlot> slots = new ArrayList<>();

    @Override
    protected void createChildren() {
        icons = TextureCache.get( Assets.Interfaces.TALENT_ICONS );
        film = new TextureFilm( icons, 16, 16 );

        super.createChildren();

        perkList = new ScrollPane( new Component() ){
            @Override
            public void onClick( float x, float y ) {
                int size = slots.size();
                for (int i=0; i < size; i++) {
                    if (slots.get( i ).onClick( x, y )) {
                        break;
                    }
                }
            }
        };
        add(perkList);
    }

    @Override
    protected void layout() {
        super.layout();
        perkList.setRect(0, 0, width, height);
    }

    public void setupList() {
        Component content = perkList.content();
        for (Perk perk : Dungeon.hero.perks) {
                PerkSlot slot = new PerkSlot(perk);
                slot.setRect(0, pos, width, slot.icon.height());
                content.add(slot);
                slots.add(slot);
                pos += GAP + slot.height();
        }
        content.setSize(perkList.width(), pos);
        perkList.setSize(perkList.width(), perkList.height());
    }

    private class PerkSlot extends Component {

        private Perk perk;

        Image icon;
        RenderedTextBlock txt;

        public PerkSlot(Perk perk ){
            super();
            this.perk = perk;
            int index = perk.icon();

            icon = new Image( icons );
            icon.frame( film.get( index ) );
            icon.hardlight(0.6f, 0.2f, 0.6f);
            icon.y = this.y;
            add( icon );

            txt = PixelScene.renderTextBlock( perk.title(), 8 );
            txt.setPos(
                    icon.width + GAP,
                    this.y + (icon.height - txt.height()) / 2
            );
            PixelScene.align(txt);
            add( txt );
        }

        @Override
        protected void layout() {
            super.layout();
            icon.y = this.y;
            txt.setPos(
                    icon.width + GAP,
                    this.y + (icon.height - txt.height()) / 2
            );
        }

        protected boolean onClick ( float x, float y ) {
            if (inside( x, y )) {
                //TODO: show perk window
//                GameScene.show(new WndInfoBuff(buff));
                return true;
            } else {
                return false;
            }
        }
    }

//    RenderedTextBlock blockText;
//
//    public PerksPane(Component content) {
//        super(content);
//    }
//
//    public PerksPane() {
//        super(new Component());
//    }
//
//    @Override
//    protected void layout() {
//        super.layout();
//
//        float top = 0;
//
//        for (Perk perk : Dungeon.hero.perks) {
//            blockText = PixelScene.renderTextBlock( perk.title(), 6 );
//            content.add(blockText);
//        }
//
//        float bottom = Math.max(height, top + 20);
//
//
//        content.setSize(width, bottom);
//    }
}
