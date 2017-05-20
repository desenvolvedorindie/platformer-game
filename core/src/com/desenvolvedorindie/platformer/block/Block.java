package com.desenvolvedorindie.platformer.block;

import com.badlogic.gdx.graphics.Texture;

public class Block {

    public static final int TILE_SIZE = 16;

    public final Texture texture;

    public Block(Texture texture) {
        this.texture = texture;
    }

}
