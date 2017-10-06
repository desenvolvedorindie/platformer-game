package com.desenvolvedorindie.platformer.graphics.fx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.desenvolvedorindie.platformer.resource.Assets;
import net.spookygames.gdx.gfx.shader.SinglePassShaderEffect;

public class Invert extends SinglePassShaderEffect {

    public Invert() {
        super(Assets.manager.get(Assets.SHADER_INVERT));
    }
}
