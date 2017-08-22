package com.desenvolvedorindie.platformer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.desenvolvedorindie.platformer.graphics.FPSLogger;
import com.desenvolvedorindie.platformer.resource.Assets;
import com.desenvolvedorindie.platformer.screen.PreloadScreen;

public class PlatformerGame extends Game {

    public static final boolean DEBUG = true;
    public static final int GAME_WIDTH = 1280;
    public static final int GAME_HEIGHT = 720;
    public static final int UI_WIDTH = 426;
    public static final int UI_HEIGHT = 240;
    private static PlatformerGame instance;
    private FPSLogger fps;

    private PlatformerGame() {
        Box2D.init();
    }

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
