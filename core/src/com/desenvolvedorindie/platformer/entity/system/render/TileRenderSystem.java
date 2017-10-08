package com.desenvolvedorindie.platformer.entity.system.render;

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
        renderBackground(batch, 0, 0, world.getWidth(), world.getHeight());

        tileColor.set(Color.WHITE);
        batch.setColor(Color.WHITE);
        renderForeground(batch, 0, 0, world.getWidth(), world.getHeight());
    }

    @Override
    protected void end() {
        batch.end();
    }

    public void renderBackground(Batch batch, int startX, int startY, int endX, int endY) {
        Block block;

        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                block = world.getBlock(x, y, World.BG);
                block.render(world, batch, x, y, World.BG);
            }
        }
    }

    public void renderForeground(Batch batch, int startX, int startY, int endX, int endY) {
        Block block;

        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                block = world.getBlock(x, y, World.FG);
                block.render(world, batch, x, y, World.FG);
            }
        }
    }

}
