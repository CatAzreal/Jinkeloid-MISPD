package com.jinkeloid.mispd.ui;

import com.jinkeloid.mispd.Assets;
import com.jinkeloid.mispd.utils.GLog;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;

public class AnimatedTitle extends MovieClip implements MovieClip.Listener{
    public int width = 160;
    public int height = 144;
    public Animation froze;
    public Animation idle;
    public Animation init;
    public Callback callback;
    public AnimatedTitle(){
        listener = this;
        texture(Assets.Sprites.ANIM_TITLE);

        TextureFilm titleStates= new TextureFilm( texture, width, height);
        froze = new Animation( 0, false );
        froze.frames( titleStates, 11);

        init = new Animation( 15, false );
        init.frames( titleStates, 0,1,2,3,4,5,6,7,8,9,10,11);

        idle = new Animation( 15, true );
        idle.frames( titleStates, 11,12,13,14,11,11,11,11,11,11,11,11,15,11,11,11,11,11,11);
    }

    @Override
    public void onComplete(Animation anim) {
        if (callback != null && anim == init){
            this.play(idle);
            Callback executing = callback;
            callback = null;
            executing.call();
        }
    }
}
