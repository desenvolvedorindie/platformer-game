package com.desenvolvedorindie.platformer.entity.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.desenvolvedorindie.platformer.entity.component.SpriteComponent;
import com.desenvolvedorindie.platformer.entity.component.TransformComponent;

public class SpriteRenderSystem extends IteratingSystem {

    private ComponentMapper<TransformComponent> mTransform;
    private ComponentMapper<SpriteComponent> mSprite;

    OrthographicCamera camera;
    SpriteBatch batch;

    public SpriteRenderSystem(OrthographicCamera camera) {
        super(Aspect.all(TransformComponent.class, SpriteComponent.class));
        this.camera = camera;
        batch = new SpriteBatch();
    }

    @Override
    protected void begin() {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
    }

    @Override
    protected void process(int entityId) {
        TransformComponent cTransform = mTransform.get(entityId);
        SpriteComponent cSprite = mSprite.get(entityId);

        if(cTransform.originCenter) {
            cSprite.sprite.setOriginCenter();
        } else {
            cSprite.sprite.setOrigin(cTransform.origin.x, cTransform.origin.y);
        }

        cSprite.sprite.setScale(cTransform.scaleX, cTransform.scaleY);

        cSprite.sprite.setRotation(cTransform.rotation);

        cSprite.sprite.setPosition(cTransform.position.x, cTransform.position.y);

        cSprite.sprite.draw(batch);

    }

    @Override
    protected void end() {
        batch.end();
    }


}
