package com.desenvolvedorindie.platformer.ai.pathfind;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.utils.Array;

public abstract class TiledNode<N extends TiledNode<N>> {

	public static final int TILE_EMPTY = 0;

	public static final int TILE_FLOOR = 1;

	public static final int TILE_WALL = 2;

	public final int x;

	public final int y;

	public final int type;

	protected Array<Connection<N>> connections;

	public TiledNode (int x, int y, int type, Array<Connection<N>> connections) {
		this.x = x;
		this.y = y;
		this.type = type;
		this.connections = connections;
	}

	public abstract int getIndex ();

	public Array<Connection<N>> getConnections () {
		return this.connections;
	}

}
