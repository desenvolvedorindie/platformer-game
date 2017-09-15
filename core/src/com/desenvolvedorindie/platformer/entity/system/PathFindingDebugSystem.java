package com.desenvolvedorindie.platformer.entity.system;

import com.artemis.BaseSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.desenvolvedorindie.platformer.ai.pathfind.flat.FlatTiledNode;
import com.desenvolvedorindie.platformer.block.Block;
import com.desenvolvedorindie.platformer.world.World;

public class PathFindingDebugSystem extends BaseSystem {

    private World gameWorld;

    private Camera camera;

    private ShapeRenderer shapeRenderer;

    private Color COLOR_FLOOR = new Color(0xffffff55);
    private Color COLOR_WALL = new Color(0x7f7f7f55);

    public PathFindingDebugSystem(World world, Camera camera, ShapeRenderer shapeRenderer) {
        this.gameWorld = world;
        this.camera = camera;
        this.shapeRenderer = shapeRenderer;
    }

    @Override
    protected void processSystem() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int x = 0; x < gameWorld.worldMap.getWidth(); x++) {
            for (int y = 0; y < gameWorld.worldMap.getHeight(); y++) {
                if (gameWorld.worldMap.getNode(x, y).type == FlatTiledNode.TILE_FLOOR) {
                    shapeRenderer.setColor(COLOR_FLOOR);
                    shapeRenderer.rect(x * Block.TILE_SIZE, y * Block.TILE_SIZE, Block.TILE_SIZE, Block.TILE_SIZE);
                } else if (gameWorld.worldMap.getNode(x, y).type == FlatTiledNode.TILE_WALL) {
                    shapeRenderer.setColor(COLOR_WALL);
                    shapeRenderer.rect(x * Block.TILE_SIZE, y * Block.TILE_SIZE, Block.TILE_SIZE, Block.TILE_SIZE);
                }
            }
        }

        shapeRenderer.setColor(Color.RED);
        int nodeCount = gameWorld.path.getCount();
        for (int i = 0; i < nodeCount; i++) {
            FlatTiledNode node = gameWorld.path.nodes.get(i);
            shapeRenderer.rect(node.x * Block.TILE_SIZE, node.y * Block.TILE_SIZE, Block.TILE_SIZE, Block.TILE_SIZE);
        }

        if (gameWorld.smooth) {
            shapeRenderer.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            float hw = Block.TILE_SIZE / 2f;
            if (nodeCount > 0) {
                FlatTiledNode prevNode = gameWorld.path.nodes.get(0);
                for (int i = 1; i < nodeCount; i++) {
                    FlatTiledNode node = gameWorld.path.nodes.get(i);
                    shapeRenderer.line(node.x * Block.TILE_SIZE + hw, node.y * Block.TILE_SIZE + hw, prevNode.x * Block.TILE_SIZE + hw, prevNode.y * Block.TILE_SIZE + hw);
                    prevNode = node;
                }
            }
        }
        shapeRenderer.end();
    }
}
