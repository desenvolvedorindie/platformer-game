package com.desenvolvedorindie.platformer.graphics.fx;

import com.badlogic.gdx.graphics.Texture;
import com.desenvolvedorindie.platformer.resource.Assets;
import net.spookygames.gdx.gfx.shader.SinglePassShaderEffect;

public class BlackAndWhite extends SinglePassShaderEffect {

    public BlackAndWhite() {
        super(Assets.manager.get(Assets.SHADER_BLACKANDWHITE));
    }

    @Override
    protected void actualRender(Texture source) {
        source.bind(u_texture0);
        super.actualRender(source);
    }

}
