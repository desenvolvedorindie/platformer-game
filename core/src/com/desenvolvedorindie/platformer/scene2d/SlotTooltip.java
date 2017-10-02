package com.desenvolvedorindie.platformer.scene2d;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.desenvolvedorindie.platformer.inventory.Slot;
import com.desenvolvedorindie.platformer.inventory.SlotListener;

public class SlotTooltip extends Window implements SlotListener {

    private Skin skin;

    private Slot slot;

    public SlotTooltip(Slot slot, Skin skin) {
        super("Tooltip...", skin);
        this.slot = slot;
        this.skin = skin;
        hasChanged(slot);
        slot.addListener(this);
        setVisible(false);
    }

    @Override
    public void hasChanged(Slot slot) {
        if (slot.isEmpty()) {
            setVisible(false);
            return;
        }

        // title displays the amount
        getTitleLabel().setText(slot.getAmount() + "x " + slot.getItem());
        clear();
        Label label = new Label("Super awesome description of " + slot.getItem(), skin);
        add(label);
        pack();
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        // the listener sets this to true in case the slot is hovered
        // however, we don't want that in case the slot is empty
        if (slot.isEmpty()) {
            super.setVisible(false);
        }
    }

}