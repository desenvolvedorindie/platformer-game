package com.desenvolvedorindie.platformer.scene2d.utils;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public abstract class DirectionalInputListener extends InputListener {
    boolean pressed;
    private Action action;

    public DirectionalInputListener(Action action) {
        this.action = action;
    }

    public abstract boolean pressed();

    public abstract void cancel();

    public boolean internalPressed(Actor actor) {
        actor.addAction(action);
        action.restart();
        pressed = true;
        return pressed();
    }

    public void internalCancel(Actor actor) {
        actor.removeAction(action);
        action.restart();
        pressed = false;
        cancel();
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        return internalPressed(event.getListenerActor());
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        internalCancel(event.getListenerActor());
    }

    @Override
    public void touchDragged(InputEvent event, float x, float y, int pointer) {
        if (x < 0 || y < 0 || x > event.getListenerActor().getWidth() || y > event.getListenerActor().getHeight()) {
            internalCancel(event.getListenerActor());
        }
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        if (pointer > -1 && !pressed) {
            internalPressed(event.getListenerActor());
        }
    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        if (pointer > -1 && pressed) {
            internalCancel(event.getListenerActor());
        }
    }
}
