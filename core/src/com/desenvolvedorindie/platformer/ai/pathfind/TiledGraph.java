package com.desenvolvedorindie.platformer.ai.pathfind;

import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.desenvolvedorindie.platformer.world.World;

public interface TiledGraph<N extends TiledNode<N>> extends IndexedGraph<N> {

    public void init();

    public N getNode(int x, int y);

    public N getNode(int index);

    public int getWidth();

    public int getHeight();

}
