package com.desenvolvedorindie.platformer.entity.system.render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.desenvolvedorindie.platformer.entity.component.base.TransformComponent;
import com.desenvolvedorindie.platformer.entity.component.render.GFXComponent;
import com.desenvolvedorindie.platformer.entity.component.render.SpriterAnimationComponent;
import net.spookygames.gdx.spriter.SpriterAnimator;

public class SpriterAnimationRenderSystem extends IteratingSystem {

    private ComponentMapper<TransformComponent> mTransform;

    private ComponentMapper<SpriterAnimationComponent> mSpriterAnimation;

    private ComponentMapper<GFXComponent> mGFX;

    private OrthographicCamera camera;

    private SpriteBatch batch;

    public SpriterAnimationRenderSystem(OrthographicCamera camera, SpriteBatch batch) {
        super(Aspect.all(TransformComponent.class, SpriterAnimationComponent.class));
        this.camera = camera;
        this.batch = batch;
    }

    @Override
    protected void begin() {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
    }

    public void processEntity(int entityId) {
        process(entityId);
    }

    @Override
    protected void process(int entityId) {
        TransformComponent cTransform = mTransform.get(entityId);
        SpriterAnimationComponent cSpriterAnimation = mSpriterAnimation.get(entityId);
        GFXComponent cGFX = mGFX.get(entityId);

        SpriterAnimator spriterAnimation = cSpriterAnimation.spriterAnimator;

        spriterAnimation.setPivot(cTransform.origin.x, cTransform.origin.y);

        spriterAnimation.setScale(cTransform.scaleX, cTransform.scaleY);

        spriterAnimation.setAngle(cTransform.rotation);

        spriterAnimation.setPosition(cTransform.position.x + 10, cTransform.position.y);

        spriterAnimation.setSpeed(cSpriterAnimation.speed);

        spriterAnimation.update(world.getDelta());

        if (cSpriterAnimation.render) {
            render(spriterAnimation);
        }

        if (mGFX.has(entityId) && cGFX.effect.hasEffects()) {
            cGFX.effect.capture();

            render(spriterAnimation);

            cGFX.effect.render(cGFX.effect.endCapture(), null);
        }
    }

    public void render(SpriterAnimator spriterAnimation) {
        spriterAnimation.draw(batch);
    }

    @Override
    protected void end() {
        batch.end();
    }

}
