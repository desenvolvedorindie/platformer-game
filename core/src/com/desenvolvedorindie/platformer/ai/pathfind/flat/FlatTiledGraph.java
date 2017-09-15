package com.desenvolvedorindie.platformer.ai.pathfind.flat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.utils.Array;
import com.desenvolvedorindie.platformer.ai.pathfind.TiledGraph;
import com.desenvolvedorindie.platformer.ai.pathfind.TiledNode;
import com.desenvolvedorindie.platformer.world.World;

public class FlatTiledGraph implements TiledGraph<FlatTiledNode> {
    public int sizeX; // 200; //100;
    public int sizeY; // 120; //60;
    public boolean diagonal;
    public FlatTiledNode startNode;
    protected Array<FlatTiledNode> nodes;
    protected World world;

    public FlatTiledGraph(World world) {
        sizeX = world.getWidth();
        sizeY = world.getHeight();
        this.world = world;
        this.nodes = new Array<FlatTiledNode>(sizeX * sizeY);
        this.diagonal = false;
        this.startNode = null;
    }

    @Override
    public void init() {
        nodes.clear();
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                nodes.add(new FlatTiledNode(this, x, y, world.isSolid(x, y) ? TiledNode.TILE_FLOOR : TiledNode.TILE_EMPTY, 4));
            }
        }

        // Each node has up to 4 neighbors, therefore no diagonal movement is possible
        for (int x = 0; x < sizeX; x++) {
            int idx = x * sizeY;
            for (int y = 0; y < sizeY; y++) {
                FlatTiledNode n = nodes.get(idx + y);
                if (x > 0) addConnection(n, -1, 0);
                if (y > 0) addConnection(n, 0, -1);
                if (x < sizeX - 1) addConnection(n, 1, 0);
                if (y < sizeY - 1) addConnection(n, 0, 1);
            }
        }
    }

    @Override
    public FlatTiledNode getNode(int x, int y) {
        return nodes.get(x * sizeY + y);
    }

    @Override
    public FlatTiledNode getNode(int index) {
        return nodes.get(index);
    }

    @Override
    public int getWidth() {
        return sizeX;
    }

    @Override
    public int getHeight() {
        return sizeY;
    }

    @Override
    public int getIndex(FlatTiledNode node) {
        return node.getIndex();
    }

    @Override
    public int getNodeCount() {
        return nodes.size;
    }

    @Override
    public Array<Connection<FlatTiledNode>> getConnections(FlatTiledNode fromNode) {
        return fromNode.getConnections();
    }

    private void addConnection(FlatTiledNode n, int xOffset, int yOffset) {
        FlatTiledNode target = getNode(n.x + xOffset, n.y + yOffset);
        if (target.type == FlatTiledNode.TILE_FLOOR) n.getConnections().add(new FlatTiledConnection(this, n, target));
    }

}
