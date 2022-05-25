package com.jinkeloid.mispd.ui;

import com.jinkeloid.mispd.Chrome;
import com.jinkeloid.mispd.scenes.PixelScene;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.PointerArea;

public class LinkedCheckBox extends StyledButton{

    private boolean checked = false;
    private boolean disabled = false;
    public int buttonID;
    public int[] linkedButtons;
    //storing these images locally, so they can be used for erasing and adding back
    NinePatch bg_selected_pos = Chrome.get(Chrome.Type.GEM_GREEN);
    NinePatch bg_selected_neg = Chrome.get(Chrome.Type.GEM_ORANGE);
    NinePatch bg_normal = Chrome.get(Chrome.Type.GEM_WB);
    NinePatch bg_disabled = Chrome.get(Chrome.Type.GEM_GREY);
    public boolean positive;

    public LinkedCheckBox(String label, int size, int buttonID, int[] linkedButtons, boolean positive) {
        super(Chrome.Type.GEM_WB, label, size);
        erase(bg);
        layout();
        addToBack(bg_normal);
        hotArea.blockLevel = PointerArea.NEVER_BLOCK;
        this.buttonID = buttonID;
        this.linkedButtons = linkedButtons;
        this.positive = positive;
    }

    @Override
    protected void layout() {
        super.layout();
        bg_selected_neg.x = bg_selected_pos.x = bg_normal.x = bg_disabled.x = x;
        bg_selected_neg.y = bg_selected_pos.y = bg_normal.y = bg_disabled.y = y;
        bg_selected_neg.height = bg_selected_pos.height = bg_normal.height = bg_disabled.height = height*2;
        bg_disabled.scale.set(0.5f);
        bg_normal.scale = bg_selected_pos.scale = bg_selected_neg.scale = bg_disabled.scale;
        bg_selected_pos.size(width*2, height*2);
        bg_selected_neg.size(width*2, height*2);
        bg_normal.size(width*2, height*2);
        bg_disabled.size(width*2, height*2);

        float margin = (height - text.height()) / 2;

        text.setPos( x + margin, y + margin);
        PixelScene.align(text);
    }

    public boolean checked() {
        return checked;
    }

    public void checked( boolean value, boolean positive ) {
        if (checked != value) {
            checked = value;
            erase(bg);
            erase(bg_disabled);
            if (checked) {
                erase(bg_normal);
                if (positive) {
                    addToBack(bg_selected_pos);
                } else {
                    addToBack(bg_selected_neg);
                }
            } else {
                if (positive) {
                    erase(bg_selected_pos);
                } else {
                    erase(bg_selected_neg);
                }
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
            erase(bg_selected_pos);
            erase(bg_selected_neg);
            if (disabled) {
                text.hardlight(606060);
                erase(bg_normal);
                addToBack(bg_disabled);
            } else {
                text.resetColor();
                //call this method twice in order to recolor the text(can use rebuild but I'm just that lazy)
                text.setHightlighting(false);
                text.setHightlighting(true, Window.TITLE_COLOR);
                erase(bg_disabled);
                addToBack(bg_normal);
            }
        }
    }

    //reset used for setup loading
    public void reset(){
        disabled = checked = false;
        erase(bg);
        erase(bg_selected_pos);
        erase(bg_selected_neg);
        erase(bg_disabled);
        addToBack(bg_normal);
        text.resetColor();
        text.setHightlighting(false);
        text.setHightlighting(true, Window.TITLE_COLOR);
    }

    @Override
    protected void onClick() {
        if (!disabled){
        super.onClick();
        checked( !checked, positive);
        }
    }
    //simulated click used for loading setups
    public void simulatedClick() {
        if (!disabled){
            checked( !checked, positive);
        }
    }
}
