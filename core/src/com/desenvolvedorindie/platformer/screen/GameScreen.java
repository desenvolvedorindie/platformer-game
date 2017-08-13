package com.desenvolvedorindie.platformer.screen;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.desenvolvedorindie.gdxcamera.constraint.*;
import com.desenvolvedorindie.platformer.PlatformerGame;
import com.desenvolvedorindie.platformer.block.Block;
import com.desenvolvedorindie.platformer.entity.component.CollidableComponent;
import com.desenvolvedorindie.platformer.entity.component.RigidBodyComponent;
import com.desenvolvedorindie.platformer.entity.component.TransformComponent;
import com.desenvolvedorindie.platformer.world.World;

import static com.badlogic.gdx.Input.*;

public class GameScreen extends ScreenAdapter {

    private World world;
    private SpriteBatch batch;
    private OrthographicCamera camera;

    @Override
    public void show() {
        batch = new SpriteBatch();

        camera = new OrthographicCamera(PlatformerGame.SCREEN_WIDTH, PlatformerGame.SCREEN_HEIGHT);
        camera.setToOrtho(false, PlatformerGame.SCREEN_WIDTH, PlatformerGame.SCREEN_HEIGHT);

        world = new World(camera);
        world.regenerate();
        //world.generateTilesBodies();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.update(delta);
    }

    @Override
    public void resize(int width, int height) {
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void dispose() {
        batch.dispose();
        world.dispose();
    }

}
