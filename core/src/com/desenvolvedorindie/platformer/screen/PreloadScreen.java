package com.desenvolvedorindie.platformer.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.desenvolvedorindie.platformer.PlatformerGame;
import com.desenvolvedorindie.platformer.resource.Assets;

public class PreloadScreen extends ScreenAdapter {

    @Override
    public void show() {
        Assets.load();
    }

    @Override
    public void render(float delta) {
        Gdx.app.log("Progress", Assets.manager.getProgress() * 100 + "%");

        if (Assets.manager.update()) {
            PlatformerGame.instance().setScreen(new GameScreen());
        }
    }
}
