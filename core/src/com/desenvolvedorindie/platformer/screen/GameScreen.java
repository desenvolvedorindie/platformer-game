package com.desenvolvedorindie.platformer.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.desenvolvedorindie.platformer.PlatformerGame;
import com.desenvolvedorindie.platformer.entity.component.base.TransformComponent;
import com.desenvolvedorindie.platformer.entity.system.world.PlayerControllerSystem;
import com.desenvolvedorindie.platformer.graphics.fx.*;
import com.desenvolvedorindie.platformer.resource.Assets;
import com.desenvolvedorindie.platformer.scene2d.GameHud;
import com.desenvolvedorindie.platformer.world.World;
import net.spookygames.gdx.gfx.MultiTemporalVisualEffect;

public class GameScreen extends ScreenAdapter {

    int current = 0;
    private MultiTemporalVisualEffect gameEffect, guiEffect;
    private World world;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private OrthographicCamera uiCamera;
    private Stage stage;
    private GameHud gameHud;
    private Skin skin;

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
        Gdx.input.setInputProcessor(new InputMultiplexer(stage, playerInput));
        gameHud.setHudListener(world.getArtemis().getSystem(PlayerControllerSystem.class).getPlayerInputAdapter());

        gameEffect = new MultiTemporalVisualEffect(Pixmap.Format.RGBA8888, false);
        guiEffect = new MultiTemporalVisualEffect(Pixmap.Format.RGBA8888, false);

        //Draw
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void render(float delta) {
        gameEffect.update(delta);
        guiEffect.update(delta);
        stage.act(delta);

        /* Draw */

        gameEffect.capture();
        Gdx.gl.glClearColor(0.7f, 0.7f, 1.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        world.update(delta);
        gameEffect.render(gameEffect.endCapture(), null);

        guiEffect.capture();
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        guiEffect.render(guiEffect.endCapture(), null);

        if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
            next();
        }
    }

    private void next() {
        gameEffect.clearEffects();

        switch (current) {
            case 0:
                gameEffect.addEffect(new Greyscale());
                break;
            case 1:
                gameEffect.addEffect(new Invert());
                break;
            case 2:
                gameEffect.addEffect(new Vignette());
                break;
            case 3:
                gameEffect.addEffect(new Emboss());
                break;
            case 4:
                gameEffect.addEffect(new Sepia());
                break;
            default:
        }

        current = ++current % 6;
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
