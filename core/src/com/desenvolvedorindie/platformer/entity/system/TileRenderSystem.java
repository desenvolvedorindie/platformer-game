package com.desenvolvedorindie.platformer.entity.system;

import com.artemis.BaseSystem;
import com.badlogic.gdx.graphics.Color;
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

    private Color tileColor = new Color();

    public TileRenderSystem(World world, OrthographicCamera camera, SpriteBatch batch) {
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
        tileColor.set(0xCCCCCCff);
        batch.setColor(tileColor);
        renderBackground(batch);

        tileColor.set(Color.WHITE);
        batch.setColor(Color.WHITE);
        renderForeground(batch);
    }

    @Override
    protected void end() {
        batch.end();
    }

    private void renderBackground(Batch batch) {
        Block block;

        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                block = world.getBlock(x, y, World.BG);
                block.render(world, batch, x, y, World.BG);
            }
        }
    }

    private void renderForeground(Batch batch) {
        Block block;

        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                block = world.getBlock(x, y, World.FG);
                block.render(world, batch, x, y, World.FG);
            }
        }
    }

}
