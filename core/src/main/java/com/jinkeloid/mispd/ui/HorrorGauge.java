package com.jinkeloid.mispd.ui;

import com.jinkeloid.mispd.Assets;
import com.jinkeloid.mispd.Dungeon;
import com.jinkeloid.mispd.actors.buffs.Buff;
import com.jinkeloid.mispd.actors.buffs.Horror;
import com.jinkeloid.mispd.actors.buffs.Satiation;
import com.jinkeloid.mispd.effects.particles.PurpleParticle;
import com.jinkeloid.mispd.effects.particles.ShadowParticle;
import com.jinkeloid.mispd.scenes.GameScene;
import com.jinkeloid.mispd.utils.GLog;
import com.jinkeloid.mispd.windows.WndHorror;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Image;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.ui.Button;
import com.watabou.noosa.ui.Component;

public class HorrorGauge extends Component {
    private static Image uneasy;
    private static Image frightened;
    private static Image horrified;
    private static Image trembling;
    static Emitter emitter;
    public enum horrorIcon {
        UNEASY,
        FRIGHTENED,
        HORRIFIED,
        TREMBLING;
    }

    private static final int COLOR_UNEASY = 0xBFB8C200;
    private static final int COLOR_FRIGHTENED = 0xBFC77E00;
    private static final int COLOR_HORRIFIED = 0xBFD40F0F;
    private static final int COLOR_TREMBLED = 0xBF6A0093;

    private static ColorBlock horrorBar;

    protected void createChildren() {

        uneasy =  new Image( Assets.Interfaces.HORROR, 0, 0, 18, 42 );
        frightened =  new Image( Assets.Interfaces.HORROR, 17, 0, 18, 42 );
        horrified =  new Image( Assets.Interfaces.HORROR, 34, 0, 18, 42 );
        trembling =  new Image( Assets.Interfaces.HORROR, 51, 0, 18, 42 );
        add(uneasy);
        add(frightened);
        add(horrified);
        add(trembling);

        add( new Button(){
            @Override
            protected void onClick () {
                GameScene.show( new WndHorror() );
            }
        }.setRect( 0, 40, 17, 41 ));

        horrorBar = new ColorBlock( 3, 15, COLOR_UNEASY);
        emitter = GameScene.emitter();
        add(horrorBar);
        if (emitter != null) {
//            emitter.pos( horrorBar.x, horrorBar.y + horrorBar.height);
            add(emitter);
        }
        updateGauge();
    }

    @Override
    protected void layout() {
        uneasy.y = frightened.y = horrified.y = trembling.y = 40;
        horrorBar.y = uneasy.y + 13;
        horrorBar.x = uneasy.x + 8;
    }


    public static void updateGauge(){
        updateGaugeVisual();
    }

    public static void updateGauge(int diff){
        if (diff > 0 && emitter != null){
            emitter.pos( horrorBar.x, horrorBar.y + 15);
            emitter.revive();
            emitter.start( ShadowParticle.UP, 0.05f, 20 );
        }
        updateGaugeVisual();
    }

    public static void updateGaugeVisual(){
        float horrorVal = Buff.affect(Dungeon.hero, Horror.class).horror() / 25f;
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

    @Override
    public void update() {
        super.update();
    }
}
