package com.desenvolvedorindie.platformer.item;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class Item {

    private final Drawable icon;

    public Item(Drawable icon) {
        this.icon = icon;
    }

    public Drawable getIcon() {
        return icon;
    }
}
