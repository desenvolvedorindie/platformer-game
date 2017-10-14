package com.desenvolvedorindie.platformer.entity.effects;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class Blindness extends StatusEffect implements IDebuff {

    public Blindness(float duration) {
        super(duration);
    }

    @Override
    public Drawable getIcon() {
        return null;
    }

}
