package com.jinkeloid.mispd.windows;

import com.jinkeloid.mispd.Chrome;
import com.jinkeloid.mispd.MISPDSettings;
import com.jinkeloid.mispd.MusicImplantSPD;
import com.jinkeloid.mispd.PerkSetups;
import com.jinkeloid.mispd.actors.hero.Perk;
import com.jinkeloid.mispd.messages.Languages;
import com.jinkeloid.mispd.messages.Messages;
import com.jinkeloid.mispd.scenes.CharacterBuilderScene;
import com.jinkeloid.mispd.scenes.PixelScene;
import com.jinkeloid.mispd.ui.GameLog;
import com.jinkeloid.mispd.ui.Icons;
import com.jinkeloid.mispd.ui.LinkedCheckBox;
import com.jinkeloid.mispd.ui.RenderedTextBlock;
import com.jinkeloid.mispd.ui.Window;
import com.jinkeloid.mispd.utils.GLog;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.ui.Button;
import com.watabou.utils.PlatformSupport;

import java.util.ArrayList;

public class WndSetup extends Window {

    private static final int WIDTH_P = 120;
    private static final int WIDTH_L = 144;

    private static final int SLOT_WIDTH = 120;
    private static final int SLOT_HEIGHT = 20;

    private static boolean setup;

    public WndSetup(boolean setup) {
        super();
        WndSetup.setup = setup;
        int w = Camera.main.width;
        int h = Camera.main.height;

        ArrayList<PerkSetups.Info> info = PerkSetups.checkAll();
        int slotGap = 5;
        int slotCount = Math.min(PerkSetups.MAX_SLOTS, info.size()+1);
        int slotsHeight = slotCount*SLOT_HEIGHT + (slotCount-1)* slotGap;

        float yPos = 0;
        for (PerkSetups.Info setupInfo : info) {
            //if info doesn't exist while the window is in setup mode, don't show them
            if (!setupInfo.exist && setup)
                break;
            setupSlots setups;
            if (!setupInfo.exist){
                setups = new setupSlots(setupInfo);
                setups.name.text("Empty Slot");
            } else {
                GLog.i("setup name is: "+setupInfo.setupName);
                setups = new setupSlots(setupInfo);
                setups.name.text(setupInfo.setupName);
            }
            setups.set(setupInfo.slot);
            setups.setRect(0, yPos, SLOT_WIDTH, SLOT_HEIGHT);
            yPos += SLOT_HEIGHT + slotGap;
            add(setups);
        }
        resize( WIDTH_P, (int)yPos );
    }

    private static class setupSlots extends Button {

        private NinePatch bg;

        private RenderedTextBlock name;

        //the alignment will be the same as GIP/startscene,
        //first icon&number: difficulty with character points spent
        //second/third icon&number: pane illustrating the amount of positive and negative perks
        private Image difficulty;
        private BitmapText score;
        private Image posperkCount;
        private Image negperkCount;
        private BitmapText posPerk;
        private BitmapText negPerk;
        private PerkSetups.Info info;

        private boolean confirm;
        private String input;

        private int slot;
        //Now this window cannot appear anywhere else than CBS
        CharacterBuilderScene currentScene = (CharacterBuilderScene) MusicImplantSPD.scene();
        private setupSlots(PerkSetups.Info setupInfo) {
            info = setupInfo;
        }

        @Override
        protected void createChildren() {
            super.createChildren();

            bg = Chrome.get(Chrome.Type.GEM);
            add( bg);

            name = PixelScene.renderTextBlock(9);
            add(name);
        }

        public void set( int slot ){
            this.slot = slot;
            if (!info.exist){
                name.text("Empty Slot");
//                name.text( Messages.get(this, "empty"));
                if (difficulty != null){
                    remove(difficulty);
                    difficulty = null;
                    remove(score);
                    score = null;
                    remove(posperkCount);
                    posperkCount = null;
                    remove(negperkCount);
                    negperkCount = null;
                    remove(posPerk);
                    posPerk = null;
                }
            } else {
                PerkSetups.Info info = PerkSetups.check(slot);
                name.text(info.setupName);
                if (difficulty == null){
                    difficulty = new Image(Icons.get(Icons.SCORE));
                    add(difficulty);
                    score = new BitmapText(PixelScene.pixelFont);
                    add(score);

                    posperkCount = new Image(Icons.get(Icons.POSCONT));
                    add(posperkCount);
                    posPerk = new BitmapText(PixelScene.pixelFont);
                    add(posPerk);
                    negperkCount = new Image(Icons.get(Icons.NEGCONT));
                    add(negperkCount);
                    negPerk = new BitmapText(PixelScene.pixelFont);
                    add(negPerk);
                } else {
                    posperkCount.copy(Icons.get(Icons.POSCONT));
                    negperkCount.copy(Icons.get(Icons.NEGCONT));
                }

                score.text(Integer.toString(info.score));
                score.measure();

                posPerk.text(Integer.toString(info.posCount));
                posPerk.measure();
                negPerk.text(Integer.toString(info.negCount));
                negPerk.measure();
            }

            layout();
        }

        @Override
        protected void layout() {
            super.layout();

            bg.x = x;
            bg.y = y;
            bg.size( width, height );

            if (difficulty != null){
                name.setPos(
                        bg.x+8,
                        bg.y + (height - name.height())/2f
                );
                add(name);

                posperkCount.x = bg.x + width - 40 + (16 - posperkCount.width())/2f;
                posperkCount.y = bg.y + (height - posperkCount.height())/2f;
                add(posperkCount);

                negperkCount.x = bg.x + width - 24 + (16 - negperkCount.width())/2f;
                negperkCount.y = bg.y + (height - negperkCount.height())/2f;
                add(negperkCount);

                posPerk.x = posperkCount.x + (posperkCount.width() - posPerk.width()) / 2f;
                posPerk.y = posperkCount.y + (posperkCount.height() - posPerk.height()) / 2f + 1;
                add(posPerk);

                negPerk.x = negperkCount.x + (negperkCount.width() - negPerk.width()) / 2f;
                negPerk.y = negperkCount.y + (negperkCount.height() - negPerk.height()) / 2f + 1;
                add(negPerk);
                
                difficulty.x = bg.x + width - 56 + (16 - difficulty.width())/2f;
                difficulty.y = bg.y + (height - difficulty.height())/2f;
                add(difficulty);

                score.x = difficulty.x + (difficulty.width() - score.width()) / 2f;
                score.y = difficulty.y + (difficulty.height() - score.height()) / 2f + 1;
                add(score);

            } else {
                name.setPos(
                        x + (width - name.width())/2f,
                        y + (height - name.height())/2f
                );
                add(name);
            }
        }

        @Override
        protected void onClick() {
            super.onClick();
            if (!setup){
                Game.platform.promptTextInput(Messages.get(WndSetup.class, "title"),
                        Messages.get(WndSetup.class, "hinttext"), 40, false,
                        Messages.get(WndSetup.class, "postxt"), Messages.get(WndSetup.class, "negtxt"), new SetupCallBack(this));
                return;
            }
            //loading setups require a full reset
            CharacterBuilderScene.tempPerks.clear();
            CharacterBuilderScene.charPoint = 0;
            for (LinkedCheckBox cb : currentScene.cbList){
                cb.reset();
            }
            LinkedCheckBox cb;
            for (Perk perk : info.Perks){
                cb = currentScene.buttonLinkRef.get(perk.id());
                cb.simulatedClick();
                currentScene.perkOnSelect(cb, perk);
            }
        }

        public void onCallback(setupSlots slots) {
            if (confirm){
                MusicImplantSPD.seamlessResetScene(new Game.SceneChangeCallback() {
                    @Override
                    public void beforeCreate() {
                        GameLog.wipe();
                        Game.platform.resetGenerators();
                    }
                    @Override
                    public void afterCreate() {
                        currentScene = (CharacterBuilderScene) MusicImplantSPD.scene();
                    }
                });
                PerkSetups.saveInfo(CharacterBuilderScene.tempPerks, slot, input);
                //easiest way to refresh the window
                slots.parent.destroy();
                currentScene.add(new WndSetup(false));
                return;
            }
            slots.parent.destroy();
        }


        class SetupCallBack extends PlatformSupport.TextCallback{
            setupSlots slots;
            public SetupCallBack(setupSlots slots) {
                this.slots = slots;
            }

            @Override
            public void onSelect(boolean positive, String text) {
                new Thread(() -> {
                    confirm = positive;
                    input = text;
                    slots.onCallback(slots);
                }).start();
            }
        }
    }
}
