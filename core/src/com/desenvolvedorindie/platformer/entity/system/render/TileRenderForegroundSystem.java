package com.desenvolvedorindie.platformer.entity.system.render;

import com.artemis.BaseSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.desenvolvedorindie.platformer.block.Block;
import com.desenvolvedorindie.platformer.world.World;

public class TileRenderForegroundSystem extends BaseSystem {

    private World world;

    private OrthographicCamera camera;

    private SpriteBatch batch;

    private Color tileColor = new Color();

    public TileRenderForegroundSystem(World world, OrthographicCamera camera, SpriteBatch batch) {
        this.world = world;
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
        tileColor.set(Color.WHITE);
        batch.setColor(Color.WHITE);
        renderLayer(batch, 0, 0, world.getWidth(), world.getHeight());
    }

    @Override
    protected void end() {
        batch.end();
    }


    public void renderLayer(Batch batch, int startX, int startY, int endX, int endY) {
        Block block;

        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                block = world.getForegroundBlock(x, y);
                block.render(world, batch, x, y, World.FG);
            }
        }
    }

}
