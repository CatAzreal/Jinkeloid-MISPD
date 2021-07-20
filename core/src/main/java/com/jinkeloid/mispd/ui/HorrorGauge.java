package com.jinkeloid.mispd.ui;

import com.jinkeloid.mispd.Assets;
import com.jinkeloid.mispd.Dungeon;
import com.jinkeloid.mispd.utils.GLog;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Button;
import com.watabou.noosa.ui.Component;

public class HorrorGauge extends Component {
    private Image uneasy;
    private Image frightened;
    private Image horrified;
    private Image trembling;

    private static final int COLOR_UNEASY = 0xBFB8C200;
    private static final int COLOR_FRIGHTENED = 0xBFC77E00;
    private static final int COLOR_HORRIFIED = 0xBFD40F0F;
    private static final int COLOR_TREMBLED = 0xBF6A0093;

    private ColorBlock horrorBar;

    protected void createChildren() {

        uneasy =  new Image( Assets.Interfaces.HORROR, 0, 0, 17, 42 );
        frightened =  new Image( Assets.Interfaces.HORROR, 17, 0, 17, 42 );
        horrified =  new Image( Assets.Interfaces.HORROR, 34, 0, 17, 42 );
        trembling =  new Image( Assets.Interfaces.HORROR, 51, 0, 17, 42 );
        add(uneasy);
        add(frightened);
        add(horrified);
        add(trembling);

        add( new Button(){
            @Override
            protected void onClick () {
                GLog.i("horror window placeholder");
//                GameScene.show( new WndHero() );
            }
        }.setRect( 0, 40, 17, 41 ));

        horrorBar = new ColorBlock( 3, 15, COLOR_UNEASY);
        add(horrorBar);
    }

    @Override
    protected void layout() {
        uneasy.y = frightened.y = horrified.y = trembling.y = 40;
        horrorBar.y = uneasy.y + 11;
        horrorBar.x = uneasy.x + 7;
    }

    @Override
    public void update() {
        super.update();
        float horrorVal = Dungeon.hero.Horror / 25;
        int horrorState = (int) horrorVal;
        switch (horrorState){
            case 0:
                uneasy.visible = true;
                frightened.visible = false;
                horrified.visible = false;
                trembling.visible = false;
                horrorBar.color(COLOR_UNEASY);
                break;
            case 1:
                uneasy.visible = false;
                frightened.visible = true;
                horrified.visible = false;
                trembling.visible = false;
                horrorBar.color(COLOR_FRIGHTENED);
                horrorVal -= 1;
                break;
            case 2:
                uneasy.visible = false;
                frightened.visible = false;
                horrified.visible = true;
                trembling.visible = false;
                horrorBar.color(COLOR_HORRIFIED);
                horrorVal -= 2;
                break;
            case 3:
                uneasy.visible = false;
                frightened.visible = false;
                horrified.visible = false;
                trembling.visible = true;
                horrorBar.color(COLOR_TREMBLED);
                horrorVal -= 3;
                break;
            default:
                horrorVal = 0;
                break;
        }
        horrorBar.scale.y = 15 * horrorVal;
    }
}
