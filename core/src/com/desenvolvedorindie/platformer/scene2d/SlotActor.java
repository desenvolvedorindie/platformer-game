package com.desenvolvedorindie.platformer.scene2d;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.desenvolvedorindie.platformer.inventory.Slot;
import com.desenvolvedorindie.platformer.inventory.SlotListener;

public class SlotActor extends ImageButton implements SlotListener {

    private Slot slot;

    private Skin skin;

    public SlotActor(Skin skin, Slot slot) {
        super(createStyle(skin, slot));
        this.slot = slot;
        this.skin = skin;

        slot.addListener(this);
    }

    private static ImageButtonStyle createStyle(Skin skin, Slot slot) {
        Drawable icon = null;
        if (slot.getItem() != null) {
            icon = slot.getItem().getIcon();
        }
        ImageButtonStyle style = new ImageButtonStyle(skin.get(ButtonStyle.class));
        style.imageUp = icon;
        style.imageDown = icon;

        return style;
    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);

        SlotTooltip tooltip = new SlotTooltip(slot, skin);
        tooltip.setTouchable(Touchable.disabled); // allows for mouse to hit tooltips in the top-right corner of the screen without flashing
        stage.addActor(tooltip);
        addListener(new TooltipListener(tooltip, true));
    }

    public Slot getSlot() {
        return slot;
    }

    @Override
    public void hasChanged(Slot slot) {
        setStyle(createStyle(skin, slot));
    }

}