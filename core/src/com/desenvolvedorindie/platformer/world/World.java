package com.desenvolvedorindie.platformer.world;

import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.managers.GroupManager;
import com.artemis.managers.PlayerManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.desenvolvedorindie.platformer.PlatformerGame;
import com.desenvolvedorindie.platformer.block.Block;
import com.desenvolvedorindie.platformer.block.water.Grid;
import com.desenvolvedorindie.platformer.dictionary.Blocks;
import com.desenvolvedorindie.platformer.entity.EntitiesFactory;
import com.desenvolvedorindie.platformer.entity.system.*;
import com.desenvolvedorindie.platformer.scene2d.GameHud;
import net.mostlyoriginal.api.event.common.EventSystem;
import net.namekdev.entity_tracker.EntityTracker;
import net.namekdev.entity_tracker.ui.EntityTrackerMainWindow;
import se.feomedia.orion.system.OperationSystem;

import java.util.Random;

import static com.artemis.WorldConfigurationBuilder.Priority;
import static com.desenvolvedorindie.platformer.block.water.Cell.CellType;

public class World {

    private EntityTrackerMainWindow entityTrackerWindow;

    private int[][][] map = new int[80][45][2];

    private Grid water = new Grid(80, 45);

    private com.artemis.World artemis;

    private int seaLevel = 7;

    private float gravity = -576;

    private int player;

    private boolean debug;

    private EntitiesFactory entitiesFactory;

    private CameraSystem cameraSystem;

    private CollisionDebugSystem collisionDebugSystem;

    private EntityDebugSystem entityDebugSystem;

    public World(OrthographicCamera camera, SpriteBatch batch, ShapeRenderer shapeRenderer, GameHud gameHud) {
        WorldConfigurationBuilder worldConfigBuilder = new WorldConfigurationBuilder()
                .with(Priority.HIGH,
                        new GroupManager(),
                        new PlayerManager(),
                        new TagManager(),
                        new EventSystem(),
                        new PlayerControllerSystem(gameHud),
                        new MovementSystem(this),
                        new StateSystem(),
                        new OperationSystem(),
                        new DayNightCycleSystem()
                )
                .with(Priority.LOW,
                        new TileRenderSystem(this, camera, batch),
                        new SpriteRenderSystem(camera, batch),
                        new SpriterAnimationRenderSystem(camera, batch),
                        new WaterSystem(this, camera, shapeRenderer),
                        cameraSystem = new CameraSystem(this, camera, shapeRenderer)
                );

        if (PlatformerGame.DEBUG) {
            worldConfigBuilder.with(
                    Priority.LOW,
                    collisionDebugSystem = new CollisionDebugSystem(this, camera, shapeRenderer),
                    entityDebugSystem = new EntityDebugSystem(camera, 0)
            );

            if (Gdx.app.getType().equals(Application.ApplicationType.Desktop)) {
                worldConfigBuilder.with(Priority.LOW,
                        new EntityTracker(entityTrackerWindow = new EntityTrackerMainWindow(false, false))
                );
            }
        }

        WorldConfiguration config = worldConfigBuilder.build();

        artemis = new com.artemis.World(config);

        entitiesFactory = new EntitiesFactory();
        artemis.inject(entitiesFactory);

        player = entitiesFactory.createPlayer(artemis, 200, mapToWorld(getHeight() - 3));

        if (collisionDebugSystem != null) {
            collisionDebugSystem.setEnabled(false);
        }

        if (entityDebugSystem != null) {
            entityDebugSystem.setEnabled(false);
        }
    }

    public static float mapToWorld(int mapCoordinate) {
        return mapCoordinate * Block.TILE_SIZE;
    }

    public static int worldToMap(float worldCoordinate) {
        return (int) (worldCoordinate / Block.TILE_SIZE);
    }

    public void regenerate() {
        Random random = new Random();

        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                for (int l = 0; l < getLayers(); l++) {
                    Block block;

                    if (y < getSeaLevel() - 5) {
                        block = Blocks.OBSIDIAN;
                    } else if (y < getSeaLevel() - 2) {
                        block = Blocks.COBBLESTONE;
                    } else if (y < getSeaLevel()) {
                        block = Blocks.DIRT;
                    } else {
                        if (l == 0) {
                            block = Blocks.DIRT;
                        } else {
                            block = random.nextInt(100) > 95 ? Blocks.DIRT : Blocks.AIR;
                        }
                    }

                    map[x][y][l] = Blocks.getIdByBlock(block);
                }

                if (isSolid(x, y)) {
                    water.getCell(x, y).setType(CellType.SOLID);
                }
            }
        }
    }

    public void update(float delta) {
        artemis.setDelta(Math.min(delta, 1 / (float) Gdx.graphics.getFramesPerSecond()));
        artemis.process();

        if (PlatformerGame.DEBUG) {
            debug();
        }
    }

    private void debug() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F10)) {
            cameraSystem.setDebug(!cameraSystem.isDebug());
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F11)) {
            if (collisionDebugSystem != null) {
                collisionDebugSystem.setEnabled(!collisionDebugSystem.isEnabled());
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F12)) {
            if (entityDebugSystem != null) {
                entityDebugSystem.setEnabled(!entityDebugSystem.isEnabled());
            }
        }

        if (Gdx.app.getType().equals(Application.ApplicationType.Desktop)) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
                if (entityTrackerWindow != null) {
                    entityTrackerWindow.setVisible(!entityTrackerWindow.isVisible());
                }
            }
        }
    }

    public Block getBlock(int x, int y, int layer) {
        return Blocks.getBlockById(map[x][y][layer]);
    }

    public Block getBlock(float x, float y, int layer) {
        return Blocks.getBlockById(map[worldToMap(x)][worldToMap(y)][layer]);
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

    public boolean isSolid(float x, float y) {
        return isSolid(worldToMap(x), worldToMap(y));
    }

    public boolean isSolid(int x, int y) {
        return !isValid(x, y) || Blocks.getBlockById(map[x][y][1]).isSolid();
    }

    public boolean isValid(int x, int y) {
        return x >= 0 && x < getWidth() && y >= 0 && y < getHeight();
    }

    public void dispose() {
        artemis.dispose();
    }

    public void getTilesRectangle(float startX, float startY, float endX, float endY, Array<Rectangle> tiles) {
        getTilesRectangle(worldToMap(startX), worldToMap(startY), worldToMap(endX), worldToMap(endY), tiles);
    }

    public void getTilesRectangle(int startX, int startY, int endX, int endY, Array<Rectangle> tiles) {
        tiles.clear();

        Rectangle rectangle;

        for (int y = startY; y <= endY; y++) {
            for (int x = startX; x <= endX; x++) {
                if ((rectangle = getTileRectangle(x, y)) != null) {
                    tiles.add(rectangle);
                }
            }
        }
    }

    public Rectangle getTileRectangle(int x, int y) {
        Rectangle rectangle = null;

        if (isValid(x, y) && isSolid(x, y)) {
            return new Rectangle(mapToWorld(x), mapToWorld(y), Block.TILE_SIZE, Block.TILE_SIZE);
        }

        return rectangle;
    }

    public boolean tileCollisionAtPoint(float x, float y) {
        return isSolid(x, y);
    }

    public boolean tileCollisionAtPoints(Vector2... points) {
        boolean found = false;

        for (Vector2 point : points) {
            if (!found) {
                found = tileCollisionAtPoint(point.x, point.y);
            }
        }

        return found;
    }

    public float getGravity() {
        return gravity;
    }

    public int getPlayer() {
        return player;
    }

    public com.artemis.World getArtemis() {
        return artemis;
    }

    public Grid getWater() {
        return water;
    }

}
