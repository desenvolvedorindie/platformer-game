package com.desenvolvedorindie.platformer.block;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.desenvolvedorindie.platformer.world.World;

public class Block {

    public static final int TILE_SIZE = 16;

    public final Texture texture;

    public Block(Texture texture) {
        this.texture = texture;
    }

    public boolean isSolid() {
        return true;
    }

    public void render(World world, Batch batch, int x, int y, int layer) {
        if (texture != null) {
            batch.draw(texture, World.mapToWorld(x), World.mapToWorld(y), Block.TILE_SIZE, Block.TILE_SIZE);
        }
    }

    public Rectangle getTileRectangle(World world, int x, int y) {
        if (isSolid())
            return new Rectangle(World.mapToWorld(x), World.mapToWorld(y), Block.TILE_SIZE, Block.TILE_SIZE);

        return null;
    }

}
