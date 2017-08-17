package com.desenvolvedorindie.platformer.block;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.*;

import static com.badlogic.gdx.physics.box2d.BodyDef.BodyType.StaticBody;

public class Block {

    public static final int TILE_SIZE = 16;

    public final Texture texture;

    public Block(Texture texture) {
        this.texture = texture;
    }

    public boolean isSolid() {
        return true;
    }

}
