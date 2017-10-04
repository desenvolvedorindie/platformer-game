package com.desenvolvedorindie.platformer.graphics.fx;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import net.spookygames.gdx.gfx.CommonShaders;
import net.spookygames.gdx.gfx.shader.SinglePassShaderEffect;
import org.adrianwalker.multilinestring.Multiline;

public class Greyscale extends SinglePassShaderEffect {

    // @formatter:off
    /**
     #ifdef GL_ES
        #define PRECISION mediump
        precision PRECISION float;
     #else
        #define PRECISION
     #endif

     varying PRECISION vec2 v_texCoords;
     uniform sampler2D u_texture0;
     void main()
     {
        // Greyscale coeffs: 0.2989, 0.5870, 0.1140
        // http://stackoverflow.com/questions/687261/converting-rgb-to-grayscale-intensity
        vec4 c = texture2D(u_texture0, v_texCoords);
        float value = c.r * 0.2989 + c.g * 0.5870 + c.b * 0.1140;
        gl_FragColor.r = value;
        gl_FragColor.g = value;
        gl_FragColor.b = value;
        gl_FragColor.a = c.a;
     }
     */
    @Multiline
    static String Greyscale;
    // @formatter:on

    public Greyscale() {
        super(new ShaderProgram(CommonShaders.Screenspace, Greyscale));
    }

    @Override
    protected void actualRender(Texture source) {
        source.bind(u_texture0);
        super.actualRender(source);
    }

    @Override
    public void dispose() {
        super.dispose();
        program.dispose();
    }
}
