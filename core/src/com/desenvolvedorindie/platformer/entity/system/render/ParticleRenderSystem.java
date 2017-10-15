package com.desenvolvedorindie.platformer.entity.system.render;

import com.artemis.Aspect;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.desenvolvedorindie.platformer.entity.component.ParticleComponent;
import com.desenvolvedorindie.platformer.resource.Assets;

public class ParticleRenderSystem extends IteratingSystem {

    Array<ParticleEffectPool.PooledEffect> particles = new Array<>();
    ParticleEffectPool fireEffectPool;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Vector3 u = new Vector3();

    public ParticleRenderSystem(OrthographicCamera camera, SpriteBatch batch) {
        super(Aspect.all(
                ParticleComponent.class
        ));

        this.camera = camera;
        this.batch = batch;

        fireEffectPool = new ParticleEffectPool(Assets.manager.get(Assets.PARTICLE_FIRE), 1, 10);

        ParticleEffectPool.PooledEffect effect = fireEffectPool.obtain();

        particles.add(effect);

        for (ParticleEffect particle : particles) {
            particle.start();
        }
    }

    @Override
    protected void begin() {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        u.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(u);

        particles.peek().setPosition(u.x, u.y);

        if (Gdx.input.justTouched()) {
            ParticleEffectPool.PooledEffect effect = fireEffectPool.obtain();
            particles.add(effect);
        }

        for (int i = particles.size - 1; i >= 0; i--) {
            ParticleEffectPool.PooledEffect effect = particles.get(i);
            effect.draw(batch, world.getDelta());

            if (effect.isComplete()) {
                if (i != particles.size - 1) {
                    effect.free();
                    particles.removeIndex(i);
                } else {
                    effect.reset();
                }
            }
        }
    }

    @Override
    protected void process(int entityId) {

    }

    protected void end() {
        batch.end();
    }

}
