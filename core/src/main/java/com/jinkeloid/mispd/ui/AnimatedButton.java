package com.jinkeloid.mispd.ui;

import com.watabou.noosa.Camera;
import com.watabou.noosa.ui.Button;

//A button with customized animation and nothing else
public class AnimatedButton extends Button {

    public AnimatedSprite sprite;
    public float scale;

    public AnimatedButton(AnimatedSprite sprite) {
        this.sprite = sprite;
        scale = Camera.main.width/(float)sprite.spriteWidthRaw / 1.2f;
        add(sprite);
    }

    //without scalemod the button would occupy 5/6 of the screen width
    public AnimatedButton(AnimatedSprite sprite, float scaleMod) {
        this.sprite = sprite;
        scale = Camera.main.width/(float)sprite.spriteWidthRaw / scaleMod;
        add(sprite);
    }

    @Override
    protected void layout() {
        sprite.x = this.x - sprite.paddingW * scale;
        sprite.y = this.y - sprite.paddingH * scale;
        sprite.scale.set(scale);
        super.layout();
    }

    @Override
    protected void onPointerDown() {
        sprite.play(sprite.pressed);
    }
    @Override
    protected void onPointerUp() {sprite.play(sprite.unpressed); }
    @Override
    protected void onClick() {
        sprite.play(sprite.press_released);
    }
    protected boolean onLongClick() {
        return false;
    }
}
