package com.jinkeloid.mispd.ui;

import com.jinkeloid.mispd.Chrome;
import com.jinkeloid.mispd.scenes.PixelScene;
import com.jinkeloid.mispd.utils.GLog;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.PointerArea;

public class LinkedCheckBox extends StyledButton{

    private boolean checked = false;
    private boolean disabled = false;
    public int buttonID;
    public int[] linkedButtons;
    //storing these images locally, so they can be used for erasing and adding back
    NinePatch bg_selected = Chrome.get(Chrome.Type.WINDOW);
    NinePatch bg_normal = Chrome.get(Chrome.Type.RED_BUTTON);
    NinePatch bg_disabled = Chrome.get(Chrome.Type.GEM);

    public LinkedCheckBox(String label, int size, int buttonID, int[] linkedButtons) {
        super(Chrome.Type.RED_BUTTON, label, size);
        hotArea.blockLevel = PointerArea.NEVER_BLOCK;
        this.buttonID = buttonID;
        this.linkedButtons = linkedButtons;
    }

    @Override
    protected void layout() {
        super.layout();
        bg_selected.x = bg_normal.x = bg_disabled.x = x;
        bg_selected.y = bg_normal.y = bg_disabled.y = y;
        bg_selected.size(width, height);
        bg_normal.size(width, height);
        bg_disabled.size(width, height);
        float margin = (height - text.height()) / 2;

        text.setPos( x + margin, y + margin);
        PixelScene.align(text);
    }

    public boolean checked() {
        return checked;
    }

    public void checked( boolean value ) {
        if (checked != value) {
            checked = value;
            erase(bg);
            erase(bg_disabled);
            if (checked) {
                erase(bg_normal);
                addToBack(bg_selected);
            } else {
                erase(bg_selected);
                addToBack(bg_normal);
            }
        }
    }

    public boolean disabled() {
        return disabled;
    }

    public void disabled( boolean value ){
        if (disabled != value){
            disabled = value;
            erase(bg);
            erase(bg_selected);
            if (disabled) {
                erase(bg_normal);
                addToBack(bg_disabled);
            } else {
                erase(bg_disabled);
                addToBack(bg_normal);
            }
        }
    }

    @Override
    protected void onClick() {
        if (!disabled){
        super.onClick();
        checked( !checked );
        }
    }
}
