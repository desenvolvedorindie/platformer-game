package com.desenvolvedorindie.platformer.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.desenvolvedorindie.platformer.resource.Assets;

public class Light implements Disposable {

    public final Vector2 position = new Vector2();
    public final Color color = new Color();
    public float step = 1.0f, start = (float) (-Math.PI), end = (float) (Math.PI);
    private FrameBuffer occludersFBO, shadowMapFBO;
    private TextureRegion occluders, shadowMap1D;
    private int lightSize;
    public boolean softShadows = false;

    private OrthographicCamera camera;
    private Batch batch;
    private Color colorTemp;
    private Matrix4 combinedTemp;

    public Light(float x, float y, Color color, int lightSize) {
        this.position.set(x, y);
        this.color.set(color);
        this.lightSize = lightSize;
        init();
    }

    private void init() {
        occludersFBO = new FrameBuffer(Pixmap.Format.RGBA8888, lightSize, lightSize, false);
        occluders = new TextureRegion(occludersFBO.getColorBufferTexture());
        occluders.flip(false, true);

        shadowMapFBO = new FrameBuffer(Pixmap.Format.RGBA8888, lightSize, 1, false);

        Texture shadowMapTex = shadowMapFBO.getColorBufferTexture();

        shadowMapTex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        shadowMapTex.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        shadowMap1D = new TextureRegion(shadowMapTex);
        shadowMap1D.flip(false, true);
    }

    public Vector2 getPosition() {
        return position;
    }

    public Color getColor() {
        return color;
    }

    public FrameBuffer getOccludersFBO() {
        return occludersFBO;
    }

    public TextureRegion getOccluders() {
        return occluders;
    }

    public FrameBuffer getShadowMapFBO() {
        return shadowMapFBO;
    }

    public TextureRegion getShadowMap1D() {
        return shadowMap1D;
    }

    public float getLightSize() {
        return lightSize;
    }

    public void setLightSize(int lightSize) {
        this.lightSize = lightSize;
        dispose();
        init();
    }

    @Override
    public String toString() {
        return "Light{" +
                "position=" + position +
                ", color=" + color +
                ", step=" + step +
                ", start=" + start +
                ", end=" + end +
                ", lightSize=" + lightSize +
                ", softShadows=" + softShadows +
                '}';
    }

    @Override
    public void dispose() {
        occludersFBO.dispose();
        shadowMapFBO.dispose();
        occluders.getTexture().dispose();
        shadowMap1D.getTexture().dispose();
    }

    public void startOccluder(OrthographicCamera camera, Batch batch) {
        colorTemp = batch.getColor().cpy();
        combinedTemp = camera.combined.cpy();

        this.camera = camera;
        this.batch = batch;

        float mx = this.position.x;
        float my = this.position.y;

        //STEP 1. render light region to occluder FBO

        //bind the occluder FBO
        getOccludersFBO().begin();

        //clear the FBO
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //set the orthographic camera to the size of our FBO
        camera.setToOrtho(false, getOccludersFBO().getWidth(), getOccludersFBO().getHeight());

        //translate camera so that light is in the center
        camera.translate(mx - getLightSize() / 2f, my - getLightSize() / 2f);
        camera.update();

        //set up our batch for the occluder pass
        batch.setProjectionMatrix(camera.combined);
    }

    public void endOccluder() {
        //unbind the FBO
        this.getOccludersFBO().end();
    }


    public void renderShadowMap() {
        //STEP 2. build a 1D shadow map from occlude FBO

        //bind shadow map
        this.getShadowMapFBO().begin();

        //clear it
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //set our shadow map shader
        ShaderProgram shadowMapShader = Assets.manager.get(Assets.SHADER_SHADOWMAP);
        batch.setShader(shadowMapShader);
        batch.begin();
        shadowMapShader.setUniformf("resolution", this.getLightSize(), getLightSize());
        shadowMapShader.setUniformf("u_step", step);

        //reset our projection matrix to the FBO size
        camera.setToOrtho(false, this.getShadowMapFBO().getWidth(), getShadowMapFBO().getHeight());
        batch.setProjectionMatrix(camera.combined);

        //draw the occluders texture to our 1D shadow map FBO
        batch.draw(getOccluders().getTexture(), 0, 0, getLightSize(), getShadowMapFBO().getHeight());

        //flush batch
        batch.end();

        //unbind shadow map FBO
        getShadowMapFBO().end();

        //STEP 3. render the blurred shadows

        //reset projection matrix to screen
        camera.setToOrtho(false);
        camera.combined.set(combinedTemp);
        batch.setProjectionMatrix(camera.combined);

        //set the shader which actually draws the light/shadow
        ShaderProgram shadowRenderShader = Assets.manager.get(Assets.SHADER_SHADOWRENDER);

        batch.setShader(shadowRenderShader);
        batch.begin();

        shadowRenderShader.setUniformf("u_resolution", getLightSize(), getLightSize());
        shadowRenderShader.setUniformf("u_softShadows", softShadows ? 1f : 0f);
        shadowRenderShader.setUniformf("u_start", start);
        shadowRenderShader.setUniformf("u_end", end);
        //set color to light
        batch.setColor(color);

        //draw centered on light position
        batch.draw(getShadowMap1D().getTexture(), position.x - getLightSize() / 2f, position.y - getLightSize() / 2f, getLightSize(), getLightSize());

        //flush the batch before swapping shaders
        batch.end();

        //reset
        batch.setColor(colorTemp);
    }
}