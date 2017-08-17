package com.desenvolvedorindie.platformer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.desenvolvedorindie.platformer.graphics.FPSLogger;
import com.desenvolvedorindie.platformer.screen.PreloadScreen;

public class PlatformerGame extends Game {

    private static PlatformerGame instance;

    public static final boolean DEBUG = true;

    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;

    private PlatformerGame() {
        Box2D.init();
    }

    private FPSLogger fps;

    public static PlatformerGame getInstance() {
        if (instance == null) {
            instance = new PlatformerGame();
        }
        return instance;
    }

    @Override
    public void create() {
        if (DEBUG) {
            fps = new FPSLogger(PlatformerGame.class.getSimpleName(), false, true);
        }

        this.setScreen(new PreloadScreen());
    }

    @Override
    public void render() {
        super.render();

        if (DEBUG) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
                if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
                    getScreen().show();
                }

                fps.log();
            }
        }
    }


}
