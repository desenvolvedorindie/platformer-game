package com.desenvolvedorindie.platformer.entity.system.render;

import com.artemis.BaseSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.desenvolvedorindie.platformer.block.Block;
import com.desenvolvedorindie.platformer.world.World;

public class TileRenderBackgroundSystem extends BaseSystem {

    private World gameWorld;

    private OrthographicCamera camera;

    private SpriteBatch batch;

    private Color tileColor = new Color();

    public TileRenderBackgroundSystem(World world, OrthographicCamera camera, SpriteBatch batch) {
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
        tileColor.set(0xCCCCCCff);
        batch.setColor(tileColor);
        renderLayer(batch, 0, 0, gameWorld.getWidth(), gameWorld.getHeight());
    }

    @Override
    protected void end() {
        batch.end();
    }

    public void renderLayer(Batch batch, int startX, int startY, int endX, int endY) {
        Block block;

        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                block = gameWorld.getBackgroundBlock(x, y);
                block.render(gameWorld, batch, x, y, World.BG);
            }
        }
    }

}
