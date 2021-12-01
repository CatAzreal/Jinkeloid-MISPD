package com.jinkeloid.mispd.scenes;

import com.jinkeloid.mispd.Chrome;
import com.jinkeloid.mispd.Dungeon;
import com.jinkeloid.mispd.GamesInProgress;
import com.jinkeloid.mispd.MISPDSettings;
import com.jinkeloid.mispd.actors.hero.HeroClass;
import com.jinkeloid.mispd.messages.Messages;
import com.jinkeloid.mispd.ui.ActionIndicator;
import com.jinkeloid.mispd.ui.Icons;
import com.jinkeloid.mispd.ui.StyledButton;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;

public class ShopScene extends PixelScene{

    private Image background;
    private StyledButton startBtn;

    @Override
    public void create() {
        super.create();

        int w = Camera.main.width;
        int h = Camera.main.height;

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
        startBtn.setPos((w - startBtn.width())/2f ,(h + 2 - startBtn.height()));
        add(startBtn);
    }
}
