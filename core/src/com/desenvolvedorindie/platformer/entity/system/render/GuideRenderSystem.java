package com.desenvolvedorindie.platformer.entity.system.render;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.desenvolvedorindie.platformer.resource.Assets;
import com.desenvolvedorindie.platformer.world.World;

public class GuideRenderSystem extends BaseSystem {

    boolean[][] selectedBlocks;
    TextureRegion[][] masks;
    private int[][] bitmask;
    private World world;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private TextureAtlas.AtlasRegion grid;
    private TextureAtlas.AtlasRegion selected;
    private Vector3 mouse = new Vector3();

    public GuideRenderSystem(World world, OrthographicCamera camera, SpriteBatch batch) {
        this.world = world;
        this.camera = camera;
        this.batch = batch;
        grid = Assets.manager.get(Assets.ATLAS_GUIDES).findRegion("grid");
        selected = Assets.manager.get(Assets.ATLAS_GUIDES).findRegion("selected");
        selectedBlocks = new boolean[world.getWidth()][world.getHeight()];

        bitmask = new int[world.getWidth()][world.getWidth()];

        masks = TextureRegion.split(Assets.manager.get(Assets.TEXTURE_SELECTION), 16, 16);

        calculate();
    }

    @Override
    protected void begin() {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
    }

    @Override
    protected void processSystem() {
        mouse.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mouse);

        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                batch.draw(grid, World.mapToWorld(x), World.mapToWorld(y));
            }
        }

        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                if (bitmask[x][y] != 15 && selectedBlocks[x][y]) {
                    batch.draw(masks[0][bitmask[x][y]], World.mapToWorld(x), World.mapToWorld(y));
                }
            }
        }


        int x = World.worldToMap(mouse.x);
        int y = World.worldToMap(mouse.y);

        batch.draw(selected, World.mapToWorld(x), World.mapToWorld(y));


        if (Gdx.input.justTouched()) {
            selectedBlocks[x][y] = !selectedBlocks[x][y];
            calculate();
        }

    }

    protected void end() {
        batch.end();
    }

    private void calculate() {
        boolean north, south, west, east;
        int index;
        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                north = isSelected(x, y + 1);
                south = isSelected(x, y - 1);
                west = isSelected(x - 1, y);
                east = isSelected(x + 1, y);

                index = toInt(north) + 2 * toInt(west) + 4 * toInt(east) + 8 * toInt(south);
                Gdx.app.log("index", String.valueOf(index));
                bitmask[x][y] = index;
            }
        }
    }

    private boolean isSelected(int x, int y) {
        return isValid(x, y) && selectedBlocks[x][y];
    }

    private boolean isValid(int x, int y) {
        return x >= 0 && x < world.getWidth() && y >= 0 && y < world.getHeight();
    }

    private int toInt(boolean value) {
        return value ? 1 : 0;
    }
}
