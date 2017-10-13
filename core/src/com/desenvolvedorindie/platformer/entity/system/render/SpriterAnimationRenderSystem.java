package com.desenvolvedorindie.platformer.entity.system.render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.desenvolvedorindie.platformer.entity.component.basic.OriginComponent;
import com.desenvolvedorindie.platformer.entity.component.basic.PositionComponent;
import com.desenvolvedorindie.platformer.entity.component.basic.RotationComponent;
import com.desenvolvedorindie.platformer.entity.component.basic.ScaleComponent;
import com.desenvolvedorindie.platformer.entity.component.render.GFXComponent;
import com.desenvolvedorindie.platformer.entity.component.render.SpriterAnimationComponent;
import net.spookygames.gdx.spriter.SpriterAnimator;

public class SpriterAnimationRenderSystem extends IteratingSystem {

    private ComponentMapper<PositionComponent> mPosition;
    private ComponentMapper<OriginComponent> mOrigin;
    private ComponentMapper<ScaleComponent> mScale;
    private ComponentMapper<RotationComponent> mRotation;
    private ComponentMapper<SpriterAnimationComponent> mSpriterAnimation;
    private ComponentMapper<GFXComponent> mGFX;

    private OrthographicCamera camera;

    private SpriteBatch batch;

    public SpriterAnimationRenderSystem(OrthographicCamera camera, SpriteBatch batch) {
        super(Aspect.all(
                PositionComponent.class,
                OriginComponent.class,
                ScaleComponent.class,
                SpriterAnimationComponent.class
        ));
        this.camera = camera;
        this.batch = batch;
    }

    @Override
    protected void begin() {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
    }

    @Override
    protected void process(int entityId) {
        PositionComponent cPosition = mPosition.get(entityId);
        OriginComponent cOrigin = mOrigin.get(entityId);
        ScaleComponent cScale = mScale.get(entityId);
        RotationComponent cRotation = mRotation.get(entityId);
        SpriterAnimationComponent cSpriterAnimation = mSpriterAnimation.get(entityId);
        GFXComponent cGFX = mGFX.get(entityId);

        SpriterAnimator spriterAnimation = cSpriterAnimation.spriterAnimator;

        spriterAnimation.setPivot(cOrigin.origin.x, cOrigin.origin.y);

        spriterAnimation.setScale(cScale.scaleX, cScale.scaleY);

        spriterAnimation.setAngle(cRotation.rotation);

        spriterAnimation.setPosition(cPosition.position.x + 10, cPosition.position.y);

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
