package com.desenvolvedorindie.platformer.entity.system.debug;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.desenvolvedorindie.platformer.ai.pathfind.flat.FlatTiledNode;
import com.desenvolvedorindie.platformer.block.Block;
import com.desenvolvedorindie.platformer.entity.component.base.TransformComponent;
import com.desenvolvedorindie.platformer.world.World;

public class PathFindingDebugSystem extends BaseSystem {

    Vector3 mousePos = new Vector3();
    private World gameWorld;
    private Camera camera;
    private ShapeRenderer shapeRenderer;
    private Color COLOR_FLOOR = new Color(0xffffff22);
    private Color COLOR_WALL = new Color(0x7f7f7f22);

    public PathFindingDebugSystem(World world, Camera camera, ShapeRenderer shapeRenderer) {
        this.gameWorld = world;
        this.camera = camera;
        this.shapeRenderer = shapeRenderer;
    }

    @Override
    protected void processSystem() {
        if (Gdx.input.justTouched() && Gdx.input.isTouched(0)) {
            if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
                mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(mousePos);

                gameWorld.lastScreenX = World.worldToMap(mousePos.x);
                gameWorld.lastScreenY = World.worldToMap(mousePos.y);
                gameWorld.startTileX = Math.round(gameWorld.getArtemis().getEntity(gameWorld.getPlayer()).getComponent(TransformComponent.class).position.x / Block.TILE_SIZE);
                gameWorld.startTileY = Math.round(gameWorld.getArtemis().getEntity(gameWorld.getPlayer()).getComponent(TransformComponent.class).position.y / Block.TILE_SIZE);
                gameWorld.updatePath(false);
            }
        }

        float hw = Block.TILE_SIZE / 2f;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (int x = 0; x < gameWorld.worldMap.getWidth(); x++) {
            for (int y = 0; y < gameWorld.worldMap.getHeight(); y++) {
                FlatTiledNode node = gameWorld.worldMap.getNode(x, y);
                if (node.type == FlatTiledNode.TILE_FLOOR) {
                    shapeRenderer.setColor(COLOR_FLOOR);
                    shapeRenderer.rect(World.mapToWorld(x), World.mapToWorld(y), Block.TILE_SIZE, Block.TILE_SIZE);

                    shapeRenderer.setColor(Color.LIGHT_GRAY);
                } else if (node.type == FlatTiledNode.TILE_WALL) {
                    shapeRenderer.setColor(COLOR_WALL);
                    shapeRenderer.rect(World.mapToWorld(x), World.mapToWorld(y), Block.TILE_SIZE, Block.TILE_SIZE);
                }

                for (Connection<FlatTiledNode> connection : node.getConnections()) {
                    FlatTiledNode to = connection.getToNode();

                    shapeRenderer.line(World.mapToWorld(node.x) + hw, World.mapToWorld(node.y) + hw, World.mapToWorld(to.x) + hw, World.mapToWorld(to.y) + hw);
                }
            }
        }

        shapeRenderer.setColor(Color.RED);
        int nodeCount = gameWorld.path.getCount();
        for (int i = 0; i < nodeCount; i++) {
            FlatTiledNode node = gameWorld.path.nodes.get(i);
            shapeRenderer.rect(World.mapToWorld(node.x), World.mapToWorld(node.y), Block.TILE_SIZE, Block.TILE_SIZE);
        }

        if (gameWorld.smooth) {
            if (nodeCount > 0) {
                FlatTiledNode prevNode = gameWorld.path.nodes.get(0);
                for (int i = 1; i < nodeCount; i++) {
                    FlatTiledNode node = gameWorld.path.nodes.get(i);
                    shapeRenderer.line(World.mapToWorld(node.x) + hw, World.mapToWorld(node.y) + hw, World.mapToWorld(prevNode.x) + hw, World.mapToWorld(prevNode.y) + hw);
                    prevNode = node;
                }
            }
        }

        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(World.mapToWorld(gameWorld.lastEndTileX), World.mapToWorld(gameWorld.lastEndTileY), Block.TILE_SIZE, Block.TILE_SIZE);

        shapeRenderer.end();
    }
}
