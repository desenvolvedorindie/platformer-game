package com.desenvolvedorindie.platformer.world;

import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.io.JsonArtemisSerializer;
import com.artemis.managers.GroupManager;
import com.artemis.managers.PlayerManager;
import com.artemis.managers.TagManager;
import com.artemis.managers.WorldSerializationManager;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.pfa.PathSmoother;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.desenvolvedorindie.platformer.PlatformerGame;
import com.desenvolvedorindie.platformer.ai.pathfind.*;
import com.desenvolvedorindie.platformer.ai.pathfind.flat.FlatTiledGraph;
import com.desenvolvedorindie.platformer.ai.pathfind.flat.FlatTiledNode;
import com.desenvolvedorindie.platformer.block.Block;
import com.desenvolvedorindie.platformer.block.water.Grid;
import com.desenvolvedorindie.platformer.dictionary.Blocks;
import com.desenvolvedorindie.platformer.entity.EntitiesFactory;
import com.desenvolvedorindie.platformer.entity.system.*;
import net.mostlyoriginal.api.event.common.EventSystem;
import net.namekdev.entity_tracker.EntityTracker;
import net.namekdev.entity_tracker.ui.EntityTrackerMainWindow;
import se.feomedia.orion.system.OperationSystem;

import java.util.Random;

import static com.artemis.WorldConfigurationBuilder.Priority;
import static com.desenvolvedorindie.platformer.block.water.Cell.CellType;

public class World implements IWorld {

    public static final int CHUNK_WIDTH = 128;
    public static final int CHUNK_HEIGHT = 512;

    public static final int BG = 0;
    public static final int FG = 1;

    public static final String NAME_BG = "background";
    public static final String NAME_FG = "foreground";
    private final PathFindingDebugSystem pathFindingDebugSystem;
    public FlatTiledGraph worldMap;
    public TiledSmoothableGraphPath<FlatTiledNode> path;
    public TiledManhattanDistance<FlatTiledNode> heuristic;
    public IndexedAStarPathFinder<FlatTiledNode> pathFinder;
    public PathSmoother<FlatTiledNode, Vector2> pathSmoother;
    public int lastScreenX;
    public int lastScreenY;
    public int lastEndTileX;
    public int lastEndTileY;
    public int startTileX;
    public int startTileY;
    public boolean smooth = false;
    public int[][] mapPath = new int[CHUNK_WIDTH][CHUNK_HEIGHT];
    private EntityTrackerMainWindow entityTrackerWindow;
    private int[][][] map = new int[CHUNK_WIDTH][CHUNK_HEIGHT][2];
    private Rectangle[] collisionBoxes = new Rectangle[CHUNK_WIDTH * CHUNK_HEIGHT];
    private Grid water = new Grid(CHUNK_WIDTH, CHUNK_HEIGHT);
    private com.artemis.World artemis;
    private int seaLevel = 318;
    private float gravity = -576;
    private int player;
    private EntitiesFactory entitiesFactory;
    private CameraSystem cameraSystem;
    private CollisionDebugSystem collisionDebugSystem;
    private EntityDebugSystem entityDebugSystem;

    public World(OrthographicCamera camera, SpriteBatch batch, ShapeRenderer shapeRenderer) {
        WorldConfigurationBuilder worldConfigBuilder = new WorldConfigurationBuilder()
                .with(Priority.HIGH,
                        new GroupManager(),
                        new PlayerManager(),
                        new TagManager(),
                        new EventSystem(),
                        new PlayerControllerSystem(),
                        new MovementSystem(this),
                        new StateSystem(),
                        new OperationSystem(),
                        new DayNightCycleSystem(),
                        new WorldSerializationManager()
                )
                .with(Priority.LOW,
                        new TileRenderSystem(this, camera, batch),
                        new SpriteRenderSystem(camera, batch),
                        new SpriterAnimationRenderSystem(camera, batch),
                        new WaterSystem(this, camera, shapeRenderer),
                        cameraSystem = new CameraSystem(this, camera, shapeRenderer)
                );

        //worldConfigBuilder.with(new EEELPlugin());

        if (PlatformerGame.DEBUG) {
            worldConfigBuilder.with(
                    Priority.LOW,
                    collisionDebugSystem = new CollisionDebugSystem(this, camera, shapeRenderer),
                    pathFindingDebugSystem = new PathFindingDebugSystem(this, camera, shapeRenderer),
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

        JsonArtemisSerializer backend = new JsonArtemisSerializer(artemis);
        backend.prettyPrint(true);
        artemis.getSystem(WorldSerializationManager.class).setSerializer(backend);

        entitiesFactory = new EntitiesFactory(artemis);
        artemis.inject(entitiesFactory);

        player = entitiesFactory.createPlayer(200, mapToWorld(getHeight() - 3));

        if (collisionDebugSystem != null) {
            collisionDebugSystem.setEnabled(true);
        }

        if (entityDebugSystem != null) {
            entityDebugSystem.setEnabled(false);
        }

        if (pathFindingDebugSystem != null) {
            pathFindingDebugSystem.setEnabled(false);
        }

        lastEndTileX = -1;
        lastEndTileY = -1;
        startTileX = 1;
        startTileY = 1;
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
                            block = random.nextInt(100) > 90 ? Blocks.DIRT : Blocks.AIR;
                        }
                    }

                    map[x][y][l] = Blocks.getIdByBlock(block);
                }
            }
        }

        init();
    }

    private boolean isRealFloor(int x, int y) {
        return !isSolid(x, y) && isSolid(x, y - 1);
    }

    private void init() {
        for (int x = 0; x < getWidth(); x++) {
            int idx = x * getHeight();

            for (int y = 0; y < getHeight(); y++) {
                collisionBoxes[idx + y] = getBlock(x, y, FG).getTileRectangle(this, x, y);
            }
        }

        updateWaterCells();

        int jumpSize = 1;

        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                if (isRealFloor(x, y)) {
                    mapPath[x][y] = TiledNode.TILE_FLOOR;
                } else if (!isSolid(x, y)) {
                    boolean isReachable = false;
                    for (int j = y; j >= y - jumpSize; j--) {
                        if (isRealFloor(x, j)) {
                            isReachable = true;
                            break;
                        }
                    }
                    if (isReachable)
                        mapPath[x][y] = TiledNode.TILE_FLOOR;
                }
            }
        }

        int yy;

        /*
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                if ((!isSolid(x + 1, y)) || !isSolid(x - 1, y)) {
                    yy = y;
                    while (yy >= 0) {
                        if (mapPath[x][yy] == TiledNode.TILE_EMPTY && !isSolid(x, yy)) {
                            mapPath[x][yy] = TiledNode.TILE_FLOOR;
                        } else {
                            break;
                        }
                        yy--;
                    }
                }
            }
        }
        */

        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                if (mapPath[x][y] == TiledNode.TILE_FLOOR) {
                    for (int xx = x - 1; xx <= x + 1; xx++) {
                        for (yy = y - 1; yy <= y + 1; yy++) {
                            if (isSolid(xx, yy) && isValid(xx, yy))
                                if (mapPath[xx][yy] == TiledNode.TILE_EMPTY) mapPath[xx][yy] = TiledNode.TILE_WALL;
                        }
                    }
                }
            }
        }

        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                mapPath[x][y] = isSolid(x, y) ? TiledNode.TILE_WALL : TiledNode.TILE_FLOOR;
            }
        }

        worldMap = new FlatTiledGraph(this);
        worldMap.init();

        path = new TiledSmoothableGraphPath<FlatTiledNode>();
        heuristic = new TiledManhattanDistance<FlatTiledNode>();
        pathFinder = new IndexedAStarPathFinder<FlatTiledNode>(worldMap, true);
        pathSmoother = new PathSmoother<FlatTiledNode, Vector2>(new TiledRaycastCollisionDetector<FlatTiledNode>(worldMap));
    }

    public void updateWaterCells() {
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
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
        if (Gdx.input.isKeyJustPressed(Input.Keys.F9)) {
            pathFindingDebugSystem.setEnabled(!pathFindingDebugSystem.isEnabled());
        }

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

    public void updatePath(boolean forceUpdate) {
        int tileX = lastScreenX;
        int tileY = lastScreenY;
        if (forceUpdate || tileX != lastEndTileX || tileY != lastEndTileY && lastEndTileX >= 0 && lastEndTileY >= 0) {
            FlatTiledNode startNode = worldMap.getNode(startTileX, startTileY);
            FlatTiledNode endNode = worldMap.getNode(tileX, tileY);
            if (forceUpdate || endNode.type == FlatTiledNode.TILE_FLOOR) {
                if (endNode.type == FlatTiledNode.TILE_FLOOR) {
                    lastEndTileX = tileX;
                    lastEndTileY = tileY;
                } else {
                    endNode = worldMap.getNode(lastEndTileX, lastEndTileY);
                }
                path.clear();
                worldMap.startNode = startNode;
                long startTime = nanoTime();
                Gdx.app.log("Start Tile", startNode.x + ", " + startNode.y);
                Gdx.app.log("Last Screen", endNode.x + ", " + endNode.y);
                pathFinder.searchNodePath(startNode, endNode, heuristic, path);
                if (pathFinder.metrics != null) {
                    float elapsed = (TimeUtils.nanoTime() - startTime) / 1000000f;
                    System.out.println("----------------- Indexed A* Path Finder Metrics -----------------");
                    System.out.println("Visited nodes................... = " + pathFinder.metrics.visitedNodes);
                    System.out.println("Open list additions............. = " + pathFinder.metrics.openListAdditions);
                    System.out.println("Open list peak.................. = " + pathFinder.metrics.openListPeak);
                    System.out.println("Path finding elapsed time (ms).. = " + elapsed);
                }
                if (smooth) {
                    startTime = nanoTime();
                    pathSmoother.smoothPath(path);
                    if (pathFinder.metrics != null) {
                        float elapsed = (TimeUtils.nanoTime() - startTime) / 1000000f;
                        System.out.println("Path smoothing elapsed time (ms) = " + elapsed);
                    }
                }
            }
        }
    }


    private long nanoTime() {
        return pathFinder.metrics == null ? 0 : TimeUtils.nanoTime();
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

    @Override
    public int getType(int x, int y) {
        return mapPath[x][y];
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
        return collisionBoxes[x * getHeight() + y];
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

    public void load(TiledMap tiledMap) {
        int width = (Integer) tiledMap.getProperties().get("width");
        int height = (Integer) tiledMap.getProperties().get("height");

        map = new int[width][height][2];

        TiledMapTileLayer background = (TiledMapTileLayer) tiledMap.getLayers().get(NAME_BG);
        TiledMapTileLayer foreground = (TiledMapTileLayer) tiledMap.getLayers().get(NAME_FG);

        TiledMapTileLayer.Cell cell;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cell = background.getCell(x, y);
                if (cell != null) {
                    map[x][y][World.BG] = Blocks.getIdByName(String.valueOf(cell.getTile().getProperties().get("name")));
                }
            }
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cell = foreground.getCell(x, y);
                if (cell != null) {
                    map[x][y][World.FG] = Blocks.getIdByName(String.valueOf(cell.getTile().getProperties().get("name")));
                }
            }
        }

        init();
    }

}
