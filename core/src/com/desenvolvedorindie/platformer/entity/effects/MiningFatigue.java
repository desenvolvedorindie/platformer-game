package com.desenvolvedorindie.platformer.entity.effects;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class MiningFatigue extends StatusEffect implements IDebuff {

    public MiningFatigue(float duration) {
        super(duration);
    }

    @Override
    public Drawable getIcon() {
        return null;
    }

}
