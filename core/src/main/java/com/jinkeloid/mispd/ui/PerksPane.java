package com.jinkeloid.mispd.ui;

import com.watabou.noosa.Camera;
import com.watabou.noosa.ui.Component;

public class PerksPane extends ScrollPane{

    int perkSize = 20;

    int w = Camera.main.width;
    int h = Camera.main.height;

    public PerksPane(Component content) {
        super(content);
    }

    public PerksPane() {
        super(new Component());
    }

    @Override
    protected void layout() {
        super.layout();

        float top = 0;

        for (int i = 0; i < perkSize; i++){
            top += 5;
        }

        float bottom = Math.max(height, top + 20);


        content.setSize(100, bottom);
    }
}
