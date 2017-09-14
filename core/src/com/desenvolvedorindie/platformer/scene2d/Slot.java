package com.desenvolvedorindie.platformer.scene2d;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.desenvolvedorindie.platformer.item.ItemStack;

public class Slot extends Actor {

    private ItemStack itemStack;

    private int index;

    private boolean isLocked;

    public Slot(int index) {
        this.index = index;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (itemStack != null) {
            itemStack.getItem().getIcon().draw(batch, getX(), getY(), 16, 16);
        }
    }

}
