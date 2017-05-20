package com.desenvolvedorindie.platformer.world;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.desenvolvedorindie.platformer.block.Block;
import com.desenvolvedorindie.platformer.dictionary.Blocks;

public class World {

    public int[][][] map = new int[80][45][2];

    public World() {

    }

    public void regenerate() {
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                if (y < 10) {
                    map[x][y][1] = Blocks.getIdByBlock(Blocks.OBSIDIAN);
                } else if (y < 20) {
                    map[x][y][1] = Blocks.getIdByBlock(Blocks.COBBLESTONE);
                } else if (y < 22) {
                    map[x][y][1] = Blocks.getIdByBlock(Blocks.DIRTY);
                }
            }
        }
    }

    public void render(Batch batch) {
        Texture texture = null;

        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                texture = Blocks.getBlockById(map[x][y][1]).texture;

                if (texture != null) {
                    batch.draw(texture, x * Block.TILE_SIZE, y * Block.TILE_SIZE);
                }
            }
        }
    }

    public Block getBlock(int x, int y, int layer) {
        return Blocks.getBlockById(map[x][y][layer]);
    }

    public int getWidth() {
        return map.length;
    }

    public int getHeight() {
        return map[0].length;
    }

    public int getLayers() {
        return map[0][0].length;
    }

}
