package com.desenvolvedorindie.platformer.entity.system.render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.desenvolvedorindie.platformer.entity.component.basic.OriginComponent;
import com.desenvolvedorindie.platformer.entity.component.basic.RotationComponent;
import com.desenvolvedorindie.platformer.entity.component.basic.ScaleComponent;
import com.desenvolvedorindie.platformer.entity.component.render.SpriteComponent;
import com.desenvolvedorindie.platformer.entity.component.basic.PositionComponent;

public class SpriteRenderSystem extends IteratingSystem {

    private ComponentMapper<PositionComponent> mPosition;
    private ComponentMapper<OriginComponent> mOrigin;
    private ComponentMapper<ScaleComponent> mScale;
    private ComponentMapper<RotationComponent> mRotation;
    private ComponentMapper<SpriteComponent> mSprite;

    private OrthographicCamera camera;

    private SpriteBatch batch;

    public SpriteRenderSystem(OrthographicCamera camera, SpriteBatch batch) {
        super(Aspect.all(
                PositionComponent.class,
                OriginComponent.class,
                ScaleComponent.class,
                RotationComponent.class,
                SpriteComponent.class
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
        PositionComponent cTransform = mPosition.get(entityId);
        SpriteComponent cSprite = mSprite.get(entityId);
        OriginComponent cOrigin = mOrigin.get(entityId);
        ScaleComponent cScale = mScale.get(entityId);
        RotationComponent cRotation = mRotation.get(entityId);

        Sprite sprite = cSprite.sprite;

        if (cOrigin.originCenter) {
            sprite.setOriginCenter();
        } else {
            sprite.setOrigin(cOrigin.origin.x, cOrigin.origin.y);
        }

        sprite.setScale(cScale.scaleX, cScale.scaleY);

        sprite.setRotation(cRotation.rotation);

        sprite.setPosition(cTransform.position.x, cTransform.position.y);

        render(sprite);
    }

    public void render(Sprite sprite) {
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
                sprite.isFlipX(),
                sprite.isFlipY()
        );
    }

    @Override
    protected void end() {
        batch.end();
    }

}
