package com.desenvolvedorindie.platformer;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.desenvolvedorindie.platformer.graphics.FPSLogger;
import com.desenvolvedorindie.platformer.input.GameInput;
import com.desenvolvedorindie.platformer.resource.Assets;
import com.desenvolvedorindie.platformer.screen.PreloadScreen;

public class PlatformerGame extends Game {

    public static final String NAME = "Plataformer";
    public static final boolean DEBUG = true;
    public static final int GAME_WIDTH = 1280;
    public static final int GAME_HEIGHT = 720;
    public static final int UI_WIDTH = 426;
    public static final int UI_HEIGHT = 240;
    public static GameInput input;
    private static PlatformerGame instance;
    private FPSLogger fps;

    public static PlatformerGame instance() {
        return instance;
    }

    @Override
    public void create() {
        instance = this;

        input = new GameInput();

        if (DEBUG) {
            fps = new FPSLogger(NAME, false, true);
        }

        this.setScreen(new PreloadScreen());
    }

    @Override
    public void render() {
        input.process();

        super.render();

        if (DEBUG) {
            fps.log();

            if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
                if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
                    getScreen().show();
                }
            }
        }
    }

    @Override
    public void dispose() {
        Assets.manager.dispose();
    }

}
