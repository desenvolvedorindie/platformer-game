package com.desenvolvedorindie.platformer.graphics.fx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.desenvolvedorindie.platformer.resource.Assets;
import net.spookygames.gdx.gfx.shader.SinglePassShaderEffect;

public class Emboss extends SinglePassShaderEffect {

    public Emboss() {
        super(Assets.manager.get(Assets.SHADER_EMBOSS));

        registerParameter("u_resolution", new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
    }

}
