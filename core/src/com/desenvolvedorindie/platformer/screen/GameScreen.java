package com.desenvolvedorindie.platformer.screen;

import com.artemis.managers.PlayerManager;
import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.desenvolvedorindie.platformer.PlatformerGame;
import com.desenvolvedorindie.platformer.entity.component.basic.PositionComponent;
import com.desenvolvedorindie.platformer.entity.system.world.PlayerControllerSystem;
import com.desenvolvedorindie.platformer.graphics.fx.*;
import com.desenvolvedorindie.platformer.network.GameClient;
import com.desenvolvedorindie.platformer.network.data.Login;
import com.desenvolvedorindie.platformer.network.data.MovePlayer;
import com.desenvolvedorindie.platformer.resource.Assets;
import com.desenvolvedorindie.platformer.scene2d.GameHud;
import com.desenvolvedorindie.platformer.world.World;
import net.spookygames.gdx.gfx.MultiTemporalVisualEffect;
import net.spookygames.gdx.gfx.VisualEffect;

import java.util.Random;

public class GameScreen extends ScreenAdapter {

    int current = 0;
    VisualEffect greyscale = new Greyscale(), invert = new Invert(), vignette = new Vignette(), emboss = new Emboss(), sepia = new Sepia(), blackAndWhite = new BlackAndWhite();
    GameClient client;

    private MultiTemporalVisualEffect gameEffect, guiEffect;
    private World world;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private OrthographicCamera uiCamera;
    private Stage stage;
    private GameHud gameHud;
    private Skin skin;
    private MovePlayer movePlayer;

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

        skin = new Skin(Assets.manager.get(Assets.ATLAS_UI));
        skin.add("font-default", Assets.manager.get(Assets.FONT_HOBO_16));
        skin.load(Gdx.files.internal("ui/skin.json"));

        boolean includeMobile = Gdx.app.getType().equals(Application.ApplicationType.Android) || PlatformerGame.DEBUG;
        gameHud = new GameHud(skin, includeMobile, PlatformerGame.UI_WIDTH, PlatformerGame.UI_HEIGHT);

        stage.addActor(gameHud);

        world = new World(camera, batch, shapeRenderer);
        world.regenerate();
        //world.load(tiledMap);

        PositionComponent cTransform = world.getArtemis().getEntity(world.getPlayer()).getComponent(PositionComponent.class);
        cTransform.position.y = World.mapToWorld(world.getHeightMap(World.worldToMap(cTransform.position.x)) + 1);

        final Random random = new Random();

        InputProcessor playerInput = world.getArtemis().getSystem(PlayerControllerSystem.class).getPlayerInputAdapter();
        gameHud.setHudListener(world.getArtemis().getSystem(PlayerControllerSystem.class).getPlayerInputAdapter());

        Gdx.input.setInputProcessor(new InputMultiplexer(stage, playerInput));

        gameEffect = new MultiTemporalVisualEffect(Format.RGBA8888, false);
        guiEffect = new MultiTemporalVisualEffect(Format.RGBA8888, false);

        //Draw
    }


    @Override
    public void render(float delta) {
        gameEffect.update(delta);
        guiEffect.update(delta);
        stage.act(delta);

        /* Draw */
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setShader(null);
        gameEffect.capture();
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        world.update(delta);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        gameEffect.render(gameEffect.endCapture(), null);

        // GUI
        guiEffect.capture();
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        guiEffect.render(guiEffect.endCapture(), null);

        if (Gdx.input.isKeyJustPressed(Keys.N)) {
            next();
        }

        if (Gdx.input.isKeyJustPressed(Keys.C) && client == null) {
            String name = world.getArtemis().getSystem(PlayerManager.class).getPlayer(world.getArtemis().getEntity(world.getPlayer()));
            client = new GameClient(world, name);
            Login login = new Login();
            login.name = name;
            client.login(login);
        }

        if (client != null && client.isConnected()) {
            PositionComponent cTransform = world.getArtemis().getEntity(world.getPlayer()).getComponent(PositionComponent.class);
            movePlayer = new MovePlayer();
            movePlayer.x = cTransform.position.x;
            movePlayer.y = cTransform.position.y;
            client.movePlayer(movePlayer);
        }
    }

    private void next() {
        guiEffect.clearEffects();

        Gdx.app.log("StatusEffect", String.valueOf(current));

        switch (current) {
            case 0:
                guiEffect.addEffect(greyscale);
                break;
            case 1:
                guiEffect.addEffect(invert);
                break;
            case 2:
                guiEffect.addEffect(vignette);
                break;
            case 3:
                guiEffect.addEffect(emboss);
                break;
            case 4:
                guiEffect.addEffect(sepia);
                break;
            case 5:
                guiEffect.addEffect(blackAndWhite);
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
