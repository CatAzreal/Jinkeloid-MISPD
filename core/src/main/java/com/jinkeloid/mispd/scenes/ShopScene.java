package com.jinkeloid.mispd.scenes;

import com.jinkeloid.mispd.Chrome;
import com.jinkeloid.mispd.Dungeon;
import com.jinkeloid.mispd.GamesInProgress;
import com.jinkeloid.mispd.MISPDSettings;
import com.jinkeloid.mispd.MusicImplantSPD;
import com.jinkeloid.mispd.actors.hero.HeroClass;
import com.jinkeloid.mispd.messages.Messages;
import com.jinkeloid.mispd.ui.ActionIndicator;
import com.jinkeloid.mispd.ui.IconButton;
import com.jinkeloid.mispd.ui.Icons;
import com.jinkeloid.mispd.ui.RenderedTextBlock;
import com.jinkeloid.mispd.ui.StyledButton;
import com.jinkeloid.mispd.ui.Window;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;

public class ShopScene extends PixelScene{

    private Image background;
    private StyledButton startBtn;
    private StyledButton perkBtn;
    private StyledButton descendBtn;

    private RenderedTextBlock prompt;
    private RenderedTextBlock wipPrompt;
    private IconButton logo;
    int w = Camera.main.width;
    int h = Camera.main.height;

    @Override
    public void create() {
        super.create();

        logo = new IconButton(Icons.get(Icons.SHOP));
        add(logo);
        prompt = PixelScene.renderTextBlock(Messages.get(this, "title"), 12);
        prompt.hardlight(Window.TITLE_COLOR);
        add(prompt);
        //logo and title
        logo.setPos( (w - 25 - prompt.width())/2f, 16);
        prompt.setPos( (w + 25 - prompt.width())/2f, prompt.height());

        wipPrompt = PixelScene.renderTextBlock(Messages.get(this, "wip"), 8);
        add(wipPrompt);
        wipPrompt.maxWidth( w - 20);
        wipPrompt.setPos( (w - wipPrompt.width())/2f, h/2f);

        descendBtn = new StyledButton(Chrome.Type.TAG_BIG, Messages.get(this, "descend"), 9){
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
        descendBtn.icon(Icons.get(Icons.SEWER));
        descendBtn.setSize(48, 40);
        descendBtn.setPos(w - descendBtn.width(), (h - descendBtn.height()));
        add(descendBtn);

        perkBtn = new StyledButton(Chrome.Type.TAG_BIG, Messages.get(this, "perk"), 9, true){
            @Override
            protected void onClick() {
                super.onClick();
                Game.switchScene( CharacterBuilderScene.class );
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
        perkBtn.icon(Icons.get(Icons.TRAIT));
        perkBtn.setSize(48, 40);
        perkBtn.setPos(0, (h - perkBtn.height()));
        add(perkBtn);
    }

    @Override
    protected void onBackPressed() {
        MusicImplantSPD.switchScene(CharacterBuilderScene.class);
    }
}
