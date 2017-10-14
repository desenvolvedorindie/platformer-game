package com.desenvolvedorindie.platformer.entity.system.render;

import com.artemis.BaseSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.desenvolvedorindie.platformer.world.World;

public class ShadownRendererSystem extends BaseSystem {

    private World gameWorld;

    private OrthographicCamera camera;

    private SpriteBatch batch;

    public ShadownRendererSystem(World world, OrthographicCamera camera, SpriteBatch batch) {
        this.gameWorld = world;
        this.camera = camera;
        this.batch = batch;
    }

    @Override
    protected void begin() {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
    }


    @Override
    protected void processSystem() {

    }

    protected void end() {
        batch.end();
    }

}
