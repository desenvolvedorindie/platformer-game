package com.desenvolvedorindie.platformer.scene2d;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.desenvolvedorindie.platformer.inventory.Inventory;
import com.desenvolvedorindie.platformer.inventory.Slot;

public class InventoryActor extends Window {

    public InventoryActor(Inventory inventory, DragAndDrop dragAndDrop, Skin skin) {
        super("", skin);

        // add an "X" button to the top right of the window, and make it hide the inventory
        TextButton closeButton = new TextButton("X", skin);
        closeButton.addListener(new HidingClickListener(this));

        // basic layout
        defaults().space(16).width(16).height(16);
        row().fill().expandX();

        // run through all slots and create SlotActors for each
        for (Slot slot : inventory.getSlots()) {
            SlotActor slotActor = new SlotActor(skin, slot);
            add(slotActor);

            dragAndDrop.addSource(new SlotSource(slotActor));
            dragAndDrop.addTarget(new SlotTarget(slotActor));
        }

        pack();
    }
}