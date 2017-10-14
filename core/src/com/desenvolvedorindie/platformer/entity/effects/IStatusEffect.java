package com.desenvolvedorindie.platformer.entity.effects;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public interface IStatusEffect {

    Drawable getIcon();

    void update(float delta);

    boolean hasFinished();

}
