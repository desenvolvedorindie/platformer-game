package com.desenvolvedorindie.platformer.ai.pathfind.flat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.utils.Array;
import com.desenvolvedorindie.platformer.ai.pathfind.IWorld;
import com.desenvolvedorindie.platformer.ai.pathfind.TiledGraph;

public class FlatTiledGraph implements TiledGraph<FlatTiledNode> {
    public boolean diagonal;
    public FlatTiledNode startNode;
    protected Array<FlatTiledNode> nodes;
    protected IWorld world;

    public FlatTiledGraph(IWorld world) {
        this.world = world;
        this.nodes = new Array<FlatTiledNode>(world.getWidth() * world.getHeight());
        this.diagonal = true;
        this.startNode = null;
    }

    @Override
    public void init() {
        nodes.clear();
        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                nodes.add(new FlatTiledNode(this, x, y, world.getType(x, y), 4));
            }
        }

        // Each node has up to 4 neighbors, therefore no diagonal movement is possible
        for (int x = 0; x < world.getWidth(); x++) {
            int idx = x * world.getHeight();
            for (int y = 0; y < world.getHeight(); y++) {
                FlatTiledNode n = nodes.get(idx + y);
                if (x > 0) {
                    addConnection(n, -1, 0);
                }
                if (y > 0) {
                    addConnection(n, 0, -1);
                }
                if (x < world.getWidth() - 1) {
                    addConnection(n, 1, 0);
                }
                if (y < world.getHeight() - 1) {
                    addConnection(n, 0, 1);
                }
            }
        }
    }

    @Override
    public FlatTiledNode getNode(int x, int y) {
        return nodes.get(x * world.getHeight() + y);
    }

    @Override
    public FlatTiledNode getNode(int index) {
        return nodes.get(index);
    }

    @Override
    public int getWidth() {
        return world.getWidth();
    }

    @Override
    public int getHeight() {
        return world.getHeight();
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

        if (target.type == FlatTiledNode.TILE_FLOOR) {
            n.getConnections().add(new FlatTiledConnection(this, n, target));
        }
    }

}
