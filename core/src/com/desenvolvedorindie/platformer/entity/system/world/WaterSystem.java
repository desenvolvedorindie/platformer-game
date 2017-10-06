package com.desenvolvedorindie.platformer.entity.system.world;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.desenvolvedorindie.platformer.block.Block;
import com.desenvolvedorindie.platformer.block.water.Cell;
import com.desenvolvedorindie.platformer.world.World;

import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class WaterSystem extends BaseSystem {

    private Vector3 mousePos = new Vector3();

    private Camera camera;

    private World world;

    private ShapeRenderer shapeRenderer;

    private boolean mouseDebug;

    public WaterSystem(World world, Camera camera, ShapeRenderer shapeRenderer) {
        this.world = world;
        this.camera = camera;
        this.shapeRenderer = shapeRenderer;
    }

    @Override
    protected void begin() {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeType.Filled);
    }

    @Override
    protected void processSystem() {
        world.getWater().update();

        if (mouseDebug) {
            if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
                mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(mousePos);

                int x = (int) (mousePos.x / Block.TILE_SIZE);
                int y = (int) (mousePos.y / Block.TILE_SIZE);

                if ((x > 0 && x < world.getWater().getWidth()) && (y > 0 && y < world.getWater().getHeight())) {
                    world.getWater().getCell(x, y).addLiquid(5);
                }
            }
        }

        for (int x = 0; x < world.getWater().getWidth(); x++) {
            for (int y = 0; y < world.getWater().getHeight(); y++) {
                Cell c = world.getWater().getCell(x, y);
                c.update();

                if (c.size > 0) {
                    shapeRenderer.setColor(c.color);
                    shapeRenderer.rect(x * Block.TILE_SIZE, (y) * Block.TILE_SIZE, Block.TILE_SIZE, Block.TILE_SIZE);
                }
            }
        }
    }

    @Override
    protected void end() {
        shapeRenderer.end();
    }

    public boolean isMouseDebug() {
        return mouseDebug;
    }

    public void setMouseDebug(boolean mouseDebug) {
        this.mouseDebug = mouseDebug;
    }
}
