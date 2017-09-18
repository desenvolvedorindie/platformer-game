package com.desenvolvedorindie.platformer.block;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.desenvolvedorindie.platformer.world.World;

public class SlabBlock extends Block {

    public SlabBlock(Texture texture) {
        super(texture);
    }

    public Rectangle getTileRectangle(World world, int x, int y) {
        if (isSolid())
            return new Rectangle(World.mapToWorld(x), World.mapToWorld(y), Block.TILE_SIZE, Block.TILE_SIZE / 2);

        return null;
    }

}
