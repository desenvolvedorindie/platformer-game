package com.desenvolvedorindie.platformer.entity.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.desenvolvedorindie.platformer.entity.component.SpriteComponent;
import com.desenvolvedorindie.platformer.entity.component.TransformComponent;

public class SpriteRenderSystem extends IteratingSystem {

    private ComponentMapper<TransformComponent> mTransform;

    private ComponentMapper<SpriteComponent> mSprite;

    private OrthographicCamera camera;

    private SpriteBatch batch;

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

        Sprite sprite = cSprite.sprite;

        if(cTransform.originCenter) {
            sprite.setOriginCenter();
        } else {
            sprite.setOrigin(cTransform.origin.x, cTransform.origin.y);
        }

        sprite.setScale(cTransform.scaleX, cTransform.scaleY);

        sprite.setRotation(cTransform.rotation);

        sprite.setPosition(cTransform.position.x, cTransform.position.y);

        batch.setShader(cSprite.shader);

        batch.draw(
                sprite.getTexture(),
                sprite.getX() - sprite.getOriginX(),
                sprite.getY() - sprite.getOriginY(),
                sprite.getOriginX(),
                sprite.getOriginY(),
                sprite.getWidth(),
                sprite.getHeight(),
                sprite.getScaleX(),
                sprite.getScaleY(),
                sprite.getRotation(),
                sprite.getRegionX(),
                sprite.getRegionY(),
                sprite.getRegionWidth(),
                sprite.getRegionHeight(),
                cSprite.flipX,
                cSprite.flipY
        );

        batch.setShader(null);
    }

    @Override
    protected void end() {
        batch.end();
    }


}
