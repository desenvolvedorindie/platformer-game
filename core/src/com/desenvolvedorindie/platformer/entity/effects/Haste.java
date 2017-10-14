package com.desenvolvedorindie.platformer.entity.effects;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class Haste extends StatusEffect implements IBuff {

    public Haste(float duration) {
        super(duration);
    }

    @Override
    public Drawable getIcon() {
        return null;
    }

}
