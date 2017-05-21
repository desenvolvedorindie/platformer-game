package com.desenvolvedorindie.platformer.world;

import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.desenvolvedorindie.platformer.PlatformerGame;
import com.desenvolvedorindie.platformer.block.Block;
import com.desenvolvedorindie.platformer.dictionary.Blocks;
import com.desenvolvedorindie.platformer.entity.EntitiesFactory;
import com.desenvolvedorindie.platformer.entity.system.SpriteRenderSystem;
import com.desenvolvedorindie.platformer.entity.system.TileRenderSystem;

import net.namekdev.entity_tracker.EntityTracker;
import net.namekdev.entity_tracker.ui.EntityTrackerMainWindow;

public class World {

    private int[][][] map = new int[80][45][2];

    com.artemis.World world;

    public World(OrthographicCamera camera) {
        WorldConfigurationBuilder worldConfigBuilder = new WorldConfigurationBuilder()
                .with(new TileRenderSystem(this, camera))
                .with(new SpriteRenderSystem(camera));

        if(PlatformerGame.DEBUG) {
            worldConfigBuilder.with(new EntityTracker(new EntityTrackerMainWindow()));
        }

        WorldConfiguration config = worldConfigBuilder.build();

        world = new com.artemis.World(config);

        EntitiesFactory.createPlayer(world, 0, 0);
    }

    public void regenerate() {
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                for (int l = 0; l < getLayers(); l++) {
                    if (y < 10) {
                        map[x][y][l] = Blocks.getIdByBlock(Blocks.OBSIDIAN);
                    } else if (y < 20) {
                        map[x][y][l] = Blocks.getIdByBlock(Blocks.COBBLESTONE);
                    } else if (y < 22) {
                        map[x][y][l] = Blocks.getIdByBlock(Blocks.DIRT);
                    }
                }
            }
        }
    }

    public void update(float delta) {
        world.setDelta(delta);
        world.process();
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
