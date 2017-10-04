package com.desenvolvedorindie.platformer.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.desenvolvedorindie.platformer.PlatformerGame;
import com.desenvolvedorindie.platformer.entity.component.PlayerComponent;
import com.desenvolvedorindie.platformer.entity.component.SpriterAnimationComponent;
import com.desenvolvedorindie.platformer.entity.component.TransformComponent;
import com.desenvolvedorindie.platformer.entity.system.PlayerControllerSystem;
import com.desenvolvedorindie.platformer.entity.system.SpriterAnimationRenderSystem;
import com.desenvolvedorindie.platformer.graphics.fx.Greyscale;
import com.desenvolvedorindie.platformer.graphics.fx.Outline;
import com.desenvolvedorindie.platformer.resource.Assets;
import com.desenvolvedorindie.platformer.scene2d.GameHud;
import com.desenvolvedorindie.platformer.world.World;
import net.spookygames.gdx.gfx.MultiTemporalVisualEffect;
import net.spookygames.gdx.spriter.SpriterAnimator;

public class GameScreen extends ScreenAdapter {

    private final Color color = new Color();
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
    private Outline outline;

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

        effect = new MultiTemporalVisualEffect(Pixmap.Format.RGBA8888, false);

        effect.getCombinedBuffer().setViewport(viewportRectangle);

        effect.clearEffects();

        effect.addEffect(outline = new Outline(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera.viewportWidth, camera.viewportHeight) {
            {
                setThickness(2f);
                setColor(color);
            }
            @Override
            public void renderOutlined() {
                SpriterAnimationRenderSystem sRender = world.getArtemis().getSystem(SpriterAnimationRenderSystem.class);

                SpriterAnimationComponent cSpriterAnimation = world.getArtemis().getEntity(world.getPlayer()).getComponent(SpriterAnimationComponent.class);

                batch.begin();
                sRender.render(cSpriterAnimation.spriterAnimator);
                batch.end();
            }
        });
    }

    @Override
    public void render(float delta) {
        PlayerComponent playerComponent = world.getArtemis().getEntity(world.getPlayer()).getComponent(PlayerComponent.class);
        color.set(playerComponent.r, playerComponent.g, playerComponent.b, 1);
        outline.setColor(color);

        effect.update(delta);
        stage.act(delta);

        /* Draw */

        Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        effect.capture();

        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.update(delta);

        stage.draw();

        effect.render(effect.endCapture(), null);
    }

    @Override
    public void resize(int width, int height) {
        Viewport viewport = stage.getViewport();
        viewport.update(width, height, true);
        viewportRectangle.set(viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());
    }

    @Override
    public void resume() {
        effect.rebind();
    }

    @Override
    public void dispose() {
        effect.dispose();
        batch.dispose();
        stage.dispose();
        shapeRenderer.dispose();
        world.dispose();
    }

}
