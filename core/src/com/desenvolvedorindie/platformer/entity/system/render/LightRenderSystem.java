package com.desenvolvedorindie.platformer.entity.system.render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.desenvolvedorindie.platformer.entity.component.basic.PositionComponent;
import com.desenvolvedorindie.platformer.entity.component.render.LightComponent;
import com.desenvolvedorindie.platformer.entity.component.render.SpriterAnimationComponent;
import com.desenvolvedorindie.platformer.graphics.Light;
import com.desenvolvedorindie.platformer.world.World;

public class LightRenderSystem extends IteratingSystem {

    private ComponentMapper<PositionComponent> mPosition;
    private ComponentMapper<LightComponent> mLight;

    private World gameWorld;

    private OrthographicCamera camera;

    private SpriteBatch batch;

    private Array<Light> lights = new Array<Light>();

    private boolean debug = false;

    private Vector3 u = new Vector3();

    public LightRenderSystem(World gameWorld, OrthographicCamera camera, SpriteBatch batch) {
        super(Aspect.all(
                PositionComponent.class,
                LightComponent.class
        ));

        this.gameWorld = gameWorld;
        this.camera = camera;
        this.batch = batch;
        clearLights();

        /*
        Gdx.input.setInputProcessor(new InputMultiplexer(stage, playerInput, new InputAdapter() {

            public boolean touchDown(int x, int y, int pointer, int button) {
                u.set(x, y, 0);
                camera.unproject(u);
                Light l = new Light(u.x, u.y, randomColor(), random.nextInt(1024));
                //lights.add(l);
                return true;
            }
        }));
        */
    }

    void clearLights() {
        for (Light light : lights) {
            light.dispose();
        }
        lights.clear();
        lights.add(new Light(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(), Color.WHITE, 1024));
    }

    @Override
    protected void begin() {
        u.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(u);

        for (int i = 0; i < lights.size; i++) {
            Light o = lights.get(i);

            if (i == lights.size - 1) {
                o.position.set(u.x, u.y);
            }

            renderLight(o);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.L)) {
            clearLights();
        }

        if (debug) {
            Light lastLight = lights.peek();
            batch.begin();
            batch.setShader(null);
            batch.setColor(Color.BLACK);
            batch.draw(lastLight.getOccluders(), 0, 0);
            batch.setColor(Color.WHITE);
            batch.draw(lastLight.getShadowMap1D(), 0, lastLight.getLightSize() + 5);
            batch.end();
        }
    }

    @Override
    protected void process(int entityId) {
        PositionComponent cPosition = mPosition.get(entityId);
        LightComponent cLight = mLight.get(entityId);

        for (Light light : cLight.lights) {

        }
    }

    private void renderLight(Light light) {
        int startX = Math.max(0, World.worldToMap(light.position.x - light.getLightSize() / 2f));
        int startY = Math.max(0, World.worldToMap(light.position.y - light.getLightSize() / 2f));
        int endX = Math.min(gameWorld.getWidth(), World.worldToMap(light.position.x + light.getLightSize() / 2f));
        int endY = Math.min(gameWorld.getHeight(), World.worldToMap(light.position.y + light.getLightSize() / 2f));

        light.startOccluder(camera, batch);

        batch.setShader(null); //use default shader

        batch.begin();

        gameWorld.getArtemis().getSystem(TileRenderForegroundSystem.class).renderLayer(batch, startX, startY, endX, endY);
        gameWorld.getArtemis().getSystem(SpriterAnimationRenderSystem.class).render(world.getEntity(gameWorld.getPlayer()).getComponent(SpriterAnimationComponent.class).spriterAnimator);

        batch.end();

        light.endOccluder();

        light.renderShadowMap();
    }
}
