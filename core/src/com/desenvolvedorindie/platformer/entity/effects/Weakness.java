package com.desenvolvedorindie.platformer.entity.effects;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class Weakness extends StatusEffect implements IDebuff {

    public Weakness(float duration) {
        super(duration);
    }

    @Override
    public Drawable getIcon() {
        return null;
    }

}
