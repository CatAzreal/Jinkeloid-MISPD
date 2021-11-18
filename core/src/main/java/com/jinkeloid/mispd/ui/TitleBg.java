package com.jinkeloid.mispd.ui;

import com.jinkeloid.mispd.Assets;
import com.jinkeloid.mispd.MusicImplantSPD;
import com.jinkeloid.mispd.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.tweeners.Tweener;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Callback;
import com.watabou.utils.Signal;

public class TitleBg extends Component {

    private static final float SCROLL_SPEED	= 64f;

    private Image titleBG;

    public Callback callback;

    private boolean shiftTrigger;

    public TitleBg(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected void createChildren() {

        titleBG = new Image(Assets.Interfaces.TITLE_BG);

        add(titleBG);
    }

    @Override
    protected void layout() {
        float scale = Camera.main.width/titleBG.width;
          titleBG.scale.set(scale);
          titleBG.y = Game.startTrigger ? (Camera.main.height - titleBG.height*scale) : 0;
    }

    @Override
    public void update() {

        super.update();

        //if there's a frame taking over 80ms, we might as well wait for the device to load a bit
        if (Game.elapsed < 0.08f && (Game.startTrigger || shiftTrigger)) {
            shiftTrigger = true;
            Game.startTrigger = false;
            //Background scroll speed gradually slows down, to a minimum of 16
            float shift = Game.elapsed * SCROLL_SPEED * Math.max(titleBG.y / (Camera.main.height - titleBG.height * Camera.main.width / titleBG.width), 0.25f);
            if (titleBG.y < 0)
                titleBG.y += shift;
            else{
                onComplete();
                shiftTrigger = false;
                titleBG.y = 0;
            }
        }
    }

    public void onComplete() {
        if (callback != null){
            Callback executing = callback;
            callback = null;
            executing.call();
        }
    }
}
