package com.desenvolvedorindie.platformer.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.desenvolvedorindie.platformer.PlatformerGame;
import com.desenvolvedorindie.platformer.entity.component.SpriterAnimationComponent;
import com.desenvolvedorindie.platformer.entity.component.TransformComponent;
import com.desenvolvedorindie.platformer.entity.system.PlayerControllerSystem;
import com.desenvolvedorindie.platformer.entity.system.SpriterAnimationRenderSystem;
import com.desenvolvedorindie.platformer.graphics.fx.Outline;
import com.desenvolvedorindie.platformer.resource.Assets;
import com.desenvolvedorindie.platformer.scene2d.GameHud;
import com.desenvolvedorindie.platformer.world.World;
import net.spookygames.gdx.gfx.MultiTemporalVisualEffect;
import net.spookygames.gdx.spriter.SpriterAnimator;

public class GameScreen extends ScreenAdapter {

    MultiTemporalVisualEffect effect;
    Rectangle viewportRectangle = new Rectangle();
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

        stage = new Stage(new FitViewport(PlatformerGame.UI_WIDTH, PlatformerGame.UI_HEIGHT, uiCamera), batch);
        stage.setDebugAll(PlatformerGame.DEBUG);
        skin = new Skin(Assets.manager.get(Assets.ui));
        boolean includeMobile = Gdx.app.getType().equals(Application.ApplicationType.Android) || PlatformerGame.DEBUG;
        gameHud = new GameHud(skin, includeMobile);

        stage.addActor(gameHud);

        world = new World(camera, batch, shapeRenderer);
        world.regenerate();
        //world.load(tiledMap);

        TransformComponent cTransform = world.getArtemis().getEntity(world.getPlayer()).getComponent(TransformComponent.class);
        cTransform.position.y = World.mapToWorld(world.getHeightMap(World.worldToMap(cTransform.position.x)) + 1);

        InputProcessor playerInput = world.getArtemis().getSystem(PlayerControllerSystem.class).getPlayerInputAdapter();
        Gdx.input.setInputProcessor(new InputMultiplexer(stage, playerInput));
        gameHud.setHudListener(world.getArtemis().getSystem(PlayerControllerSystem.class).getPlayerInputAdapter());

        /*
        effect = new MultiTemporalVisualEffect(Pixmap.Format.RGBA8888, false);

        viewportRectangle.set(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        effect.getCombinedBuffer().setViewport(viewportRectangle);

        effect.addEffect(new Outline(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 1280, 720) {
            {
                setThickness(2f);
                setColor(Color.BLUE);
            }

            @Override
            public void renderOutlined() {
                batch.begin();
                SpriterAnimator spriterAnimator = world.getArtemis().getEntity(world.getPlayer()).getComponent(SpriterAnimationComponent.class).spriterAnimator;
                world.getArtemis().getSystem(SpriterAnimationRenderSystem.class).render(spriterAnimator);
                batch.end();
            }

            @Override
            public String toString() {
                return "Outline";
            }

        });
        */
    }

    @Override
    public void render(float delta) {
        //Matrix4 c = camera.combined.cpy();

        //effect.update(delta);
        stage.act(delta);

        Gdx.gl.glClearColor(0.7f, 0.7f, 1.0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.update(delta);

        //stage.draw();

        /*
        effect.capture();

        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        effect.render(effect.endCapture(), null);
        */
    }

    /*
    @Override
    public void resume() {
        effect.rebind();
    }

    @Override
    public void resize(int width, int height) {
        batch.setProjectionMatrix(camera.combined);
    }
    */

    @Override
    public void dispose() {
        //effect.dispose();
        batch.dispose();
        shapeRenderer.dispose();
        world.dispose();
    }

}
