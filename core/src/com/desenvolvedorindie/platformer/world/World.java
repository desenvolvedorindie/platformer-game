package com.desenvolvedorindie.platformer.world;

import com.artemis.Entity;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.desenvolvedorindie.platformer.PlatformerGame;
import com.desenvolvedorindie.platformer.block.Block;
import com.desenvolvedorindie.platformer.dictionary.Blocks;
import com.desenvolvedorindie.platformer.entity.EntitiesFactory;
import com.desenvolvedorindie.platformer.entity.system.MovementSystem;
import com.desenvolvedorindie.platformer.entity.system.PlayerControllerSystem;
import com.desenvolvedorindie.platformer.entity.system.SpriteRenderSystem;
import com.desenvolvedorindie.platformer.entity.system.TileRenderSystem;

import net.namekdev.entity_tracker.EntityTracker;
import net.namekdev.entity_tracker.ui.EntityTrackerMainWindow;

public class World {

    private EntityTrackerMainWindow entityTrackerWindow;

    private int[][][] map = new int[80][45][2];

    private com.artemis.World world;

    private int seaLevel = 7;

    private float gravity = -576;

    private int player;

    private EntitiesFactory entitiesFactory;

    public World(OrthographicCamera camera) {
        WorldConfigurationBuilder worldConfigBuilder = new WorldConfigurationBuilder()
                .with(new PlayerControllerSystem())
                .with(new MovementSystem(this))
                .with(new TileRenderSystem(this, camera))
                .with(new SpriteRenderSystem(camera));

        if(PlatformerGame.DEBUG) {
            if (Gdx.app.getType().equals(Application.ApplicationType.Desktop)) {
                entityTrackerWindow = new EntityTrackerMainWindow(false, false);
                worldConfigBuilder.with(new EntityTracker(entityTrackerWindow));
            }
        }

        WorldConfiguration config = worldConfigBuilder.build();

        world = new com.artemis.World(config);

        entitiesFactory = new EntitiesFactory();
        world.inject(entitiesFactory);

        player = entitiesFactory.createPlayer(world, 0, getHeight() * Block.TILE_SIZE);
    }

    public void regenerate() {
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                for (int l = 0; l < getLayers(); l++) {
                    if (y < getSeaLevel() - 5) {
                        map[x][y][l] = Blocks.getIdByBlock(Blocks.OBSIDIAN);
                    } else if (y < getSeaLevel() - 2) {
                        map[x][y][l] = Blocks.getIdByBlock(Blocks.COBBLESTONE);
                    } else if (y < getSeaLevel()) {
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

    public int getSeaLevel() {
        return seaLevel;
    }

    public void dispose() {
        world.dispose();
    }

    public EntityTrackerMainWindow getEntityTrackerWindow() {
        return entityTrackerWindow;
    }

    public float getGravity() {
        return gravity;
    }

    public int getPlayer() {
        return player;
    }

    public com.artemis.World getWorld() {
        return world;
    }

}
