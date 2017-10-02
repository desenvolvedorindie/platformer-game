package com.desenvolvedorindie.platformer.scene2d;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class HidingClickListener extends ClickListener {

    private Actor actor;

    public HidingClickListener(Actor actor) {
        this.actor = actor;
    }

    @Override
    public void enter (InputEvent event, float x, float y, int pointer, Actor fromActor) {
        Window window = (Window) actor;
        window.setMovable(false);
    }

    @Override
    public void exit (InputEvent event, float x, float y, int pointer, Actor toActor) {
        Window window = (Window) actor;
        window.setMovable(true);
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        actor.setVisible(false);
    }

}