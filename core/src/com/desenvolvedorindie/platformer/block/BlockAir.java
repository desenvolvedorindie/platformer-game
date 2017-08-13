package com.desenvolvedorindie.platformer.block;

import com.badlogic.gdx.graphics.Texture;

public class BlockAir extends Block {

    public BlockAir() {
        super(null);
    }

    @Override
    public boolean isSolid() {
        return false;
    }
}
