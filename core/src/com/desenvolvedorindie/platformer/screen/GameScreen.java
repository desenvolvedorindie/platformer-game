package com.desenvolvedorindie.platformer.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.desenvolvedorindie.platformer.PlatformerGame;
import com.desenvolvedorindie.platformer.entity.component.base.TransformComponent;
import com.desenvolvedorindie.platformer.entity.system.render.SpriterAnimationRenderSystem;
import com.desenvolvedorindie.platformer.entity.system.render.TileRenderSystem;
import com.desenvolvedorindie.platformer.entity.system.world.PlayerControllerSystem;
import com.desenvolvedorindie.platformer.graphics.Light;
import com.desenvolvedorindie.platformer.graphics.fx.*;
import com.desenvolvedorindie.platformer.resource.Assets;
import com.desenvolvedorindie.platformer.scene2d.GameHud;
import com.desenvolvedorindie.platformer.world.World;
import net.spookygames.gdx.gfx.MultiTemporalVisualEffect;
import net.spookygames.gdx.gfx.VisualEffect;

public class GameScreen extends ScreenAdapter {

    int current = 0;
    VisualEffect greyscale = new Greyscale(), invert = new Invert(), vignette = new Vignette(), emboss = new Emboss(), sepia = new Sepia(), blackAndWhite = new BlackAndWhite();
    private MultiTemporalVisualEffect gameEffect, guiEffect;
    private World world;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private OrthographicCamera uiCamera;
    private Stage stage;
    private GameHud gameHud;
    private Skin skin;
    private int lightSize = 512;
    private float upScale = 1f;
    private TextureRegion shadowMap1D;
    private TextureRegion occluders;
    private FrameBuffer shadowMapFBO;
    private FrameBuffer occludersFBO;
    private Array<Light> lights = new Array<Light>();
    private boolean softShadows = false;
    private boolean shadowsBlur = false;


    static Color randomColor() {
        float intensity = (float) Math.random() * 0.5f + 0.5f;
        return new Color((float) Math.random(), (float) Math.random(), (float) Math.random(), intensity);
    }

    @Override
    public void show() {
        TiledMap tiledMap = Assets.manager.get(Assets.DUNGEON_WORLD);

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        camera = new OrthographicCamera(PlatformerGame.GAME_WIDTH, PlatformerGame.GAME_HEIGHT);
        camera.setToOrtho(false, PlatformerGame.GAME_WIDTH, PlatformerGame.GAME_HEIGHT);

        uiCamera = new OrthographicCamera(PlatformerGame.UI_WIDTH, PlatformerGame.UI_HEIGHT);
        uiCamera.setToOrtho(false, PlatformerGame.UI_WIDTH, PlatformerGame.UI_HEIGHT);

        stage = new Stage(new FitViewport(PlatformerGame.UI_WIDTH, PlatformerGame.UI_HEIGHT, uiCamera));
        //stage.setDebugAll(PlatformerGame.DEBUG);

        skin = new Skin(Assets.manager.get(Assets.ui));
        skin.add("font-default", Assets.manager.get(Assets.FONT_HOBO_16));
        skin.load(Gdx.files.internal("ui/skin.json"));

        boolean includeMobile = Gdx.app.getType().equals(Application.ApplicationType.Android) || PlatformerGame.DEBUG;
        gameHud = new GameHud(skin, includeMobile, PlatformerGame.UI_WIDTH, PlatformerGame.UI_HEIGHT);

        stage.addActor(gameHud);

        world = new World(camera, batch, shapeRenderer);
        world.regenerate();
        //world.load(tiledMap);

        TransformComponent cTransform = world.getArtemis().getEntity(world.getPlayer()).getComponent(TransformComponent.class);
        cTransform.position.y = World.mapToWorld(world.getHeightMap(World.worldToMap(cTransform.position.x)) + 1);

        InputProcessor playerInput = world.getArtemis().getSystem(PlayerControllerSystem.class).getPlayerInputAdapter();
        Gdx.input.setInputProcessor(new InputMultiplexer(stage, playerInput, new InputAdapter() {

            public boolean touchDown(int x, int y, int pointer, int button) {
                float mx = x;
                float my = Gdx.graphics.getHeight() - y;
                Light l = new Light(mx, my, randomColor());
                lights.add(l);
                return true;
            }
        }));
        gameHud.setHudListener(world.getArtemis().getSystem(PlayerControllerSystem.class).getPlayerInputAdapter());

        gameEffect = new MultiTemporalVisualEffect(Format.RGBA8888, false);
        guiEffect = new MultiTemporalVisualEffect(Format.RGBA8888, false);

        //Draw

        occludersFBO = new FrameBuffer(Format.RGBA8888, lightSize, lightSize, false);
        occluders = new TextureRegion(occludersFBO.getColorBufferTexture());
        occluders.flip(false, true);

        shadowMapFBO = new FrameBuffer(Format.RGBA8888, lightSize, 1, false);
        Texture shadowMapTex = shadowMapFBO.getColorBufferTexture();

        shadowMapTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        shadowMapTex.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);

        shadowMap1D = new TextureRegion(shadowMapTex);
        shadowMap1D.flip(false, true);


        clearLights();
    }

    void clearLights() {
        lights.clear();
        lights.add(new Light(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(), randomColor()));
    }

    @Override
    public void render(float delta) {
        gameEffect.update(delta);
        guiEffect.update(delta);
        stage.act(delta);

        /* Draw */
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float mx = Gdx.input.getX();
        float my = Gdx.graphics.getHeight() - Gdx.input.getY();


        // GAME
        for (int i = 0; i < lights.size; i++) {
            Light o = lights.get(i);

            if (i == lights.size - 1) {
                o.position.set(mx, my);
            }

            renderLight(o);
        }

        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        batch.setShader(null);
        gameEffect.capture();
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        world.update(delta);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        gameEffect.render(gameEffect.endCapture(), null);

        // DEBUG LIGHT
        batch.begin();
        batch.setShader(null);
        batch.setColor(Color.BLACK);
        batch.draw(occluders, 0, 0);
        batch.setColor(Color.WHITE);
        batch.draw(shadowMap1D, 0, lightSize + 5);
        batch.end();

        // GUI
        guiEffect.capture();
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        guiEffect.render(guiEffect.endCapture(), null);

        if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
            next();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            clearLights();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            softShadows = !softShadows;
            Gdx.app.log("softShadows", String.valueOf(softShadows));
        }
    }

    private void renderLight(Light o) {
        Matrix4 combined = camera.combined.cpy();

        float mx = o.position.x;
        float my = o.position.y;

        //STEP 1. render light region to occluder FBO

        //bind the occluder FBO
        occludersFBO.begin();

        //clear the FBO
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //set the orthographic camera to the size of our FBO
        camera.setToOrtho(false, occludersFBO.getWidth(), occludersFBO.getHeight());

        //translate camera so that light is in the center
        camera.translate(mx - lightSize / 2f, my - lightSize / 2f);
        camera.update();

        int startX = Math.max(0, World.worldToMap(mx - lightSize / 2f));
        int startY = Math.max(0, World.worldToMap(my - lightSize / 2f));
        int endX = Math.min(world.getWidth(), World.worldToMap(mx + lightSize / 2f));
        int endY = Math.min(world.getHeight(), World.worldToMap(my + lightSize / 2f));

        //set up our batch for the occluder pass
        batch.setProjectionMatrix(camera.combined);
        batch.setShader(null); //use default shader
        batch.begin();
        // ... draw any sprites that will cast shadows here ... //
        world.getArtemis().getSystem(TileRenderSystem.class).renderForeground(batch, startX, startY, endX, endY);
        world.getArtemis().getSystem(SpriterAnimationRenderSystem.class).processEntity(world.getPlayer());

        //end the batch before unbinding the FBO
        batch.end();

        //unbind the FBO
        occludersFBO.end();

        //STEP 2. build a 1D shadow map from occlude FBO

        //bind shadow map
        shadowMapFBO.begin();

        //clear it
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //set our shadow map shader
        ShaderProgram shadowMapShader = Assets.manager.get(Assets.SHADER_SHADOWMAP);
        batch.setShader(shadowMapShader);
        batch.begin();
        shadowMapShader.setUniformf("resolution", lightSize, lightSize);
        shadowMapShader.setUniformf("upScale", upScale);

        //reset our projection matrix to the FBO size
        camera.setToOrtho(false, shadowMapFBO.getWidth(), shadowMapFBO.getHeight());
        batch.setProjectionMatrix(camera.combined);

        //draw the occluders texture to our 1D shadow map FBO
        batch.draw(occluders.getTexture(), 0, 0, lightSize, shadowMapFBO.getHeight());

        //flush batch
        batch.end();

        //unbind shadow map FBO
        shadowMapFBO.end();

        //STEP 3. render the blurred shadows

        //reset projection matrix to screen
        camera.setToOrtho(false);
        batch.setProjectionMatrix(camera.combined);

        //set the shader which actually draws the light/shadow
        ShaderProgram shadowRenderShader = Assets.manager.get(Assets.SHADER_SHADOWRENDER);

        batch.setShader(shadowRenderShader);
        batch.begin();

        shadowRenderShader.setUniformf("u_resolution", lightSize, lightSize);
        shadowRenderShader.setUniformf("u_softShadows", softShadows ? 1f : 0f);
        //set color to light
        batch.setColor(o.color);

        float finalSize = lightSize * upScale;

        //draw centered on light position
        batch.draw(shadowMap1D.getTexture(), mx - finalSize / 2f, my - finalSize / 2f, finalSize, finalSize);

        //flush the batch before swapping shaders
        batch.end();

        //reset color
        batch.setColor(Color.WHITE);

        camera.combined.set(combined);
    }

    private void next() {
        gameEffect.clearEffects();

        Gdx.app.log("Effect", String.valueOf(current));

        switch (current) {
            case 0:
                gameEffect.addEffect(greyscale);
                break;
            case 1:
                gameEffect.addEffect(invert);
                break;
            case 2:
                gameEffect.addEffect(vignette);
                break;
            case 3:
                gameEffect.addEffect(emboss);
                break;
            case 4:
                gameEffect.addEffect(sepia);
                break;
            case 5:
                gameEffect.addEffect(blackAndWhite);
                break;
            default:
        }

        current = ++current % 7;
    }

    @Override
    public void resume() {
        gameEffect.rebind();
    }

    @Override
    public void dispose() {
        gameEffect.dispose();
        batch.dispose();
        stage.dispose();
        shapeRenderer.dispose();
        world.dispose();
    }

}
