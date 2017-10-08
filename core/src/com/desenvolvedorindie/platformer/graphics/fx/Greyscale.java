package com.desenvolvedorindie.platformer.graphics.fx;

import com.desenvolvedorindie.platformer.resource.Assets;
import net.spookygames.gdx.gfx.shader.SinglePassShaderEffect;

public class Greyscale extends SinglePassShaderEffect {

    public Greyscale() {
        super(Assets.manager.get(Assets.SHADER_GREYSCALE));
    }

}
