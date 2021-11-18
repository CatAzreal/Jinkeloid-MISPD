package com.jinkeloid.mispd.ui;

import com.jinkeloid.mispd.MISPDSettings;
import com.jinkeloid.mispd.messages.Languages;
import com.jinkeloid.mispd.utils.GLog;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;

public class AnimatedSprite extends MovieClip implements MovieClip.Listener{
    public int spriteWidthRaw;
    public int spriteHeightRaw;
    public int paddingW;
    public int paddingH;
    public Animation unpressed;
    public Animation pressed;
    public Animation press_released;
    public Animation disabled;
    public Callback callback;
    public boolean disable = false;
    public AnimatedSprite(String sprite, int[] wh, int[] aniFrameStates){
        listener = this;
        spriteWidthRaw = wh[0];
        spriteHeightRaw = wh[1];
        paddingW = wh[2];
        paddingH = wh[3];
        int[] fo = {0,2,4,6,8,10,12};
        if (MISPDSettings.language() == Languages.CHINESE){
            for (int i = 0; i < fo.length; i++) {
                fo[i] = 2 * i + 1;
            }
        } else {
            for (int i = 0; i < fo.length; i++) {
                fo[i] = 2 * i;
            }
        }
        texture(sprite);

        TextureFilm buttonStates= new TextureFilm( texture, spriteWidthRaw, spriteHeightRaw);

        unpressed = new Animation( 0, true );
        unpressed.frames( buttonStates, fo[0]);

        pressed = new Animation( 0, true );
        pressed.frames( buttonStates, fo[1]);

        press_released = new Animation( 30, false );
        press_released.frames( buttonStates, fo[2],fo[2],fo[3],fo[3],fo[4],fo[5]);

        disabled = new Animation( 0, true );
        disabled.frames( buttonStates, fo[6]);
    }


    @Override
    public void onComplete(Animation anim) {
        if (callback != null){
            Callback executing = callback;
            callback = null;
            executing.call();
        }
        this.play(unpressed);
    }

    public float spriteWidth(float scale){
        return (spriteWidthRaw - paddingW * 2) * scale;
    }

    public float spriteHeight(float scale){
        return (spriteHeightRaw - paddingH * 2) * scale;
    }
}
