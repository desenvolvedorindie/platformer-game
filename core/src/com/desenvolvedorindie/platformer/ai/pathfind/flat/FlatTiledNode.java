/*******************************************************************************
 * Copyright 2014 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.desenvolvedorindie.platformer.ai.pathfind.flat;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.utils.Array;
import com.desenvolvedorindie.platformer.ai.pathfind.TiledGraph;
import com.desenvolvedorindie.platformer.ai.pathfind.TiledNode;

/** A node for a {@link FlatTiledGraph}.
 *
 * @author davebaol */
public class FlatTiledNode extends TiledNode<FlatTiledNode> {

	private FlatTiledGraph tiledGraph;

	public FlatTiledNode (FlatTiledGraph tiledGraph, int x, int y, int type, int connectionCapacity) {
		super(x, y, type, new Array<Connection<FlatTiledNode>>(connectionCapacity));
		this.tiledGraph = tiledGraph;
	}

	@Override
	public int getIndex () {
		return x * tiledGraph.getHeight() + y;
	}

}
