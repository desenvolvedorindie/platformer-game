package com.desenvolvedorindie.platformer.screen;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.desenvolvedorindie.platformer.PlatformerGame;
import com.desenvolvedorindie.platformer.entity.component.RigidBodyComponent;
import com.desenvolvedorindie.platformer.entity.component.TransformComponent;
import com.desenvolvedorindie.platformer.world.World;

public class GameScreen extends ScreenAdapter {

    protected OrthographicCamera camera;
    protected World world;
    SpriteBatch batch;

    protected final Vector3 screenCoordinate = new Vector3();

    @Override
    public void show() {
        batch = new SpriteBatch();

        camera = new OrthographicCamera(PlatformerGame.SCREEN_WIDTH, PlatformerGame.SCREEN_HEIGHT);
        camera.setToOrtho(false, PlatformerGame.SCREEN_WIDTH, PlatformerGame.SCREEN_HEIGHT);

        world = new World(camera);
        world.regenerate();

        if (PlatformerGame.DEBUG) {
            Gdx.input.setInputProcessor(new InputAdapter() {
                @Override
                public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                    screenCoordinate.set(screenX, screenY, 0);
                    camera.unproject(screenCoordinate);

                    int player = world.getPlayer();

                    world.getWorld().getEntity(player).getComponent(TransformComponent.class).position.set(screenCoordinate.x, screenCoordinate.y);
                    world.getWorld().getEntity(player).getComponent(RigidBodyComponent.class).velocity.set(0, 0);

                    return true;
                }
            });
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.update(delta);

        if (PlatformerGame.DEBUG) {
            if (Gdx.app.getType().equals(Application.ApplicationType.Desktop)) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
                    if (world.getEntityTrackerWindow() != null) {
                        world.getEntityTrackerWindow().setVisible(!world.getEntityTrackerWindow().isVisible());
                    }
                }
            }
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void dispose() {
        batch.dispose();
        world.dispose();
    }

}
