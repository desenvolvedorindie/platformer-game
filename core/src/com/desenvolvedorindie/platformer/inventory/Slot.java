package com.desenvolvedorindie.platformer.inventory;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.desenvolvedorindie.platformer.item.ItemStack;

public class Slot extends Actor {

    private int index;

    public IInventory inventory;

    private boolean isLocked;

    public Slot(IInventory inventory, int index) {
        this.index = index;
    }


}
