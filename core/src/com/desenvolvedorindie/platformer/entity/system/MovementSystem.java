package com.desenvolvedorindie.platformer.entity.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.desenvolvedorindie.platformer.entity.component.CollidableComponent;
import com.desenvolvedorindie.platformer.entity.component.RigidBodyComponent;
import com.desenvolvedorindie.platformer.entity.component.TransformComponent;
import com.desenvolvedorindie.platformer.world.World;

public class MovementSystem extends IteratingSystem {

    private ComponentMapper<CollidableComponent> mCollidableComponent;

    private ComponentMapper<RigidBodyComponent> mRigidBody;

    private ComponentMapper<TransformComponent> mTransform;

    private World world;

    private OrthographicCamera camera;

    private Array<Rectangle> tiles = new Array<Rectangle>();

    public MovementSystem(World world, OrthographicCamera camera) {
        super(Aspect.all(TransformComponent.class, RigidBodyComponent.class));
        this.world = world;
        this.camera = camera;
    }

    @Override
    protected void process(int entityId) {
        RigidBodyComponent cRigidBody = mRigidBody.get(entityId);
        TransformComponent cTransform = mTransform.get(entityId);
        CollidableComponent cCollidable = mCollidableComponent.get(entityId);

        float delta = super.world.getDelta();

        cCollidable.onGround = false;
        cCollidable.onCeiling = false;
        cCollidable.onRightWall = false;
        cCollidable.onLeftWall = false;

        cRigidBody.velocity.y += world.getGravity() * cRigidBody.gravityMultiplier * delta;

        Vector2 velocity = new Vector2(cRigidBody.velocity);

        velocity.scl(delta);

        Rectangle rectangle = cCollidable.collisionBox;
        rectangle.setPosition(cTransform.position);

        float startX, startY, endX, endY;
        if (velocity.x > 0) {
            startX = endX = cTransform.position.x + rectangle.width + velocity.x;
        } else {
            startX = endX = cTransform.position.x + velocity.x;
        }
        startY = cTransform.position.y;
        endY = cTransform.position.y + rectangle.height;

        world.getTilesRectangle(startX, startY, endX, endY, tiles);

        rectangle.x += velocity.x;

        for (Rectangle tile : tiles) {
            if (rectangle.overlaps(tile)) {
                if(velocity.x > 0) {
                    cCollidable.onRightWall = true;
                } else if(velocity.x < 0) {
                    cCollidable.onLeftWall = true;
                }
                velocity.x = 0;
                break;
            }
        }

        rectangle.x = cTransform.position.x;

        if (velocity.y > 0) {
            startY = endY = cTransform.position.y + rectangle.height + velocity.y;
        } else {
            startY = endY = cTransform.position.y + velocity.y;
        }

        startX = cTransform.position.x;
        endX = cTransform.position.x + rectangle.width;

        world.getTilesRectangle(startX, startY, endX, endY, tiles);

        rectangle.y += velocity.y;

        for (Rectangle tile : tiles) {
            if (rectangle.overlaps(tile)) {
                if (velocity.y > 0) {
                    cTransform.position.y = tile.y - rectangle.height;
                    cCollidable.onCeiling = true;
                } else {
                    cTransform.position.y = tile.y + tile.height;
                    cCollidable.onGround = true;
                }
                velocity.y = 0;
                break;
            }
        }

        cTransform.position.add(velocity);

        velocity.scl(1 / delta);
        cRigidBody.velocity.set(velocity);

    }

}
