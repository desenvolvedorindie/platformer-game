package com.desenvolvedorindie.platformer.entity.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.desenvolvedorindie.platformer.entity.component.SpriteComponent;
import com.desenvolvedorindie.platformer.entity.component.SpriterAnimationComponent;
import com.desenvolvedorindie.platformer.entity.component.TransformComponent;
import net.spookygames.gdx.spriter.SpriterAnimator;

public class SpriterAnimationRenderSystem extends IteratingSystem {

    private ComponentMapper<TransformComponent> mTransform;

    private ComponentMapper<SpriterAnimationComponent> mSpriterAnimation;

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

    @Override
    protected void process(int entityId) {
        TransformComponent cTransform = mTransform.get(entityId);
        SpriterAnimationComponent cSpriterAnimation = mSpriterAnimation.get(entityId);

        SpriterAnimator spriterAnimation = cSpriterAnimation.spriterAnimator;

        spriterAnimation.setPivot(cTransform.origin.x, cTransform.origin.y);

        spriterAnimation.setScale(cTransform.scaleX, cTransform.scaleY);

        spriterAnimation.setAngle(cTransform.rotation);

        spriterAnimation.setPosition(cTransform.position.x + 10, cTransform.position.y);

        spriterAnimation.setSpeed(cSpriterAnimation.speed);

        spriterAnimation.update(world.getDelta());

        batch.setShader(cSpriterAnimation.shader);
        render(spriterAnimation);
        batch.setShader(null);
    }

    public void render(SpriterAnimator spriterAnimation) {
        spriterAnimation.draw(batch);
    }

    @Override
    protected void end() {
        batch.end();
    }
}
