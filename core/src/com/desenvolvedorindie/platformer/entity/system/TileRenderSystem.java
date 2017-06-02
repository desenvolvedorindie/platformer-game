package com.desenvolvedorindie.platformer.entity.system;

import com.artemis.BaseSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.desenvolvedorindie.platformer.block.Block;
import com.desenvolvedorindie.platformer.world.World;

public class TileRenderSystem extends BaseSystem {

    private World world;

    private OrthographicCamera camera;

    private SpriteBatch batch;

    public TileRenderSystem(World world, OrthographicCamera camera) {
        this.world = world;
        this.camera = camera;
        this.batch = new SpriteBatch();
    }

    @Override
    protected void begin() {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
    }

    @Override
    protected void processSystem() {
        renderBackground(batch);
        renderForeground(batch);
    }

    @Override
    protected void end() {
        batch.end();
    }

    private void renderBackground(Batch batch) {
        Texture texture = null;

        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                texture = world.getBlock(x, y, 0).texture;

                if (texture != null) {
                    batch.draw(texture, x * Block.TILE_SIZE, y * Block.TILE_SIZE);
                }
            }
        }
    }

    private void renderForeground(Batch batch) {
        Texture texture = null;

        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                texture = world.getBlock(x, y, 1).texture;

                if (texture != null) {
                    batch.draw(texture, x * Block.TILE_SIZE, y * Block.TILE_SIZE);
                }
            }
        }
    }

    @Override
    protected void dispose() {
        batch.dispose();
    }

}
