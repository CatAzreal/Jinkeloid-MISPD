package com.jinkeloid.mispd.ui;

import com.jinkeloid.mispd.Assets;
import com.jinkeloid.mispd.Dungeon;
import com.jinkeloid.mispd.MISPDAction;
import com.jinkeloid.mispd.scenes.GameScene;
import com.jinkeloid.mispd.scenes.PixelScene;
import com.jinkeloid.mispd.utils.GLog;
import com.jinkeloid.mispd.windows.WndHero;
import com.watabou.input.GameAction;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.ui.Button;
import com.watabou.noosa.ui.Component;

public class HorrorGauge extends Component {
    private Image mild;
    private Image horror;
    private Image trembling;
    private Image ancient;

    private static final int COLOR_MILD = 0xBFB8C200;
    private static final int COLOR_HORROR = 0xBFC77E00;
    private static final int COLOR_TREMBLING = 0xBFD40F0F;
    private static final int COLOR_ANCIENT = 0xBF6A0093;

    private ColorBlock horrorBar;

    private float horrorVal;

    protected void createChildren() {

        mild =  new Image( Assets.Interfaces.HORROR, 0, 0, 17, 42 );
        horror =  new Image( Assets.Interfaces.HORROR, 17, 0, 17, 42 );
        trembling =  new Image( Assets.Interfaces.HORROR, 34, 0, 17, 42 );
        ancient =  new Image( Assets.Interfaces.HORROR, 51, 0, 17, 42 );
        add(mild);
        add(horror);
        add(trembling);
        add(ancient);

        add( new Button(){
            @Override
            protected void onClick () {
                GLog.i("horror window placeholder");
//                GameScene.show( new WndHero() );
            }
        }.setRect( 0, 40, 17, 41 ));

        horrorBar = new ColorBlock( 3, 15, COLOR_MILD );
        add(horrorBar);
    }

    @Override
    protected void layout() {
        mild.y = horror.y = trembling.y = ancient.y = 40;
        horrorBar.y = mild.y + 11;
        horrorBar.x = mild.x + 7;
    }

    @Override
    public void update() {
        super.update();
        horrorVal = Dungeon.hero.Horror / 25;
        int horrorState = (int)horrorVal;
        switch (horrorState){
            case 0:
                mild.visible = true;
                horror.visible = false;
                trembling.visible = false;
                ancient.visible = false;
                horrorBar.color(COLOR_MILD);
                break;
            case 1:
                mild.visible = false;
                horror.visible = true;
                trembling.visible = false;
                ancient.visible = false;
                horrorBar.color(COLOR_HORROR);
                horrorVal -= 1;
                break;
            case 2:
                mild.visible = false;
                horror.visible = false;
                trembling.visible = true;
                ancient.visible = false;
                horrorBar.color(COLOR_TREMBLING);
                horrorVal -= 2;
                break;
            case 3:
                mild.visible = false;
                horror.visible = false;
                trembling.visible = false;
                ancient.visible = true;
                horrorBar.color(COLOR_ANCIENT);
                horrorVal -= 3;
                break;
        }
        horrorBar.scale.y = 15 * horrorVal;
    }
}
