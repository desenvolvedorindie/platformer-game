package com.desenvolvedorindie.platformer.entity.system.physic;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.desenvolvedorindie.platformer.entity.component.physic.CollidableComponent;
import com.desenvolvedorindie.platformer.entity.component.physic.RigidBodyComponent;
import com.desenvolvedorindie.platformer.entity.component.basic.PositionComponent;
import com.desenvolvedorindie.platformer.world.World;

public class MovementSystem extends IteratingSystem {

    private ComponentMapper<PositionComponent> mPosition;
    private ComponentMapper<CollidableComponent> mCollidable;
    private ComponentMapper<RigidBodyComponent> mRigidBody;

    private World world;

    private Array<Rectangle> tiles = new Array<Rectangle>();
    private Vector2 velocity = new Vector2();

    public MovementSystem(World world) {
        super(Aspect.all(
                PositionComponent.class,
                RigidBodyComponent.class
        ));
        this.world = world;
    }

    @Override
    protected void process(int entityId) {
        PositionComponent cPosition = mPosition.get(entityId);
        RigidBodyComponent cRigidBody = mRigidBody.get(entityId);
        CollidableComponent cCollidable = mCollidable.get(entityId);

        if (cRigidBody.isKinematic) {
            float delta = super.world.getDelta();

            if (mCollidable.has(entityId)) {
                cCollidable.onGround = false;
                cCollidable.onCeiling = false;
                cCollidable.onRightWall = false;
                cCollidable.onLeftWall = false;

                velocity.set(cRigidBody.velocity);

                velocity.scl(delta);

                Rectangle rectangle = cCollidable.collisionBox;
                rectangle.setPosition(cPosition.position);

                float startX, startY, endX, endY;

                if (velocity.y > 0) {
                    startY = rectangle.y + rectangle.height;
                    endY = startY + velocity.y;
                } else {
                    startY = rectangle.y + velocity.y;
                    endY = rectangle.y;
                }

                startX = rectangle.x;
                endX = rectangle.x + rectangle.width;

                world.getTilesRectangle(startX, startY, endX, endY, tiles);

                for (int i = 0; i < Math.abs(velocity.y); i++) {
                    boolean found = false;

                    float oldY = rectangle.y;

                    rectangle.y += Math.signum(velocity.y);

                    for (Rectangle tile : tiles) {
                        if (rectangle.overlaps(tile)) {
                            if (velocity.y > 0) {
                                cCollidable.onCeiling = true;
                            } else {
                                cCollidable.onGround = true;
                            }
                            found = true;
                            break;
                        }
                    }

                    if (found) {
                        velocity.y = 0;
                        rectangle.y = oldY;
                        break;
                    }
                }

                if (velocity.x > 0) {
                    startX = rectangle.x + rectangle.width;
                    endX = startX + velocity.x;
                } else {
                    startX = rectangle.x + velocity.x;
                    endX = rectangle.x;
                }
                startY = rectangle.y;
                endY = rectangle.y + rectangle.height;

                world.getTilesRectangle(startX, startY, endX, endY, tiles);

                for (int i = 0; i < Math.abs(velocity.x); i++) {
                    boolean found = false;

                    float oldX = rectangle.x;

                    rectangle.x += Math.signum(velocity.x);

                    for (Rectangle tile : tiles) {
                        if (rectangle.overlaps(tile)) {
                            if (velocity.x > 0) {
                                cCollidable.onRightWall = true;
                            } else if (velocity.x < 0) {
                                cCollidable.onLeftWall = true;
                            }
                            found = true;
                            break;
                        }
                    }

                    if (found) {
                        velocity.x = 0;
                        rectangle.x = oldX;
                        break;
                    }
                }

                cPosition.position.set(rectangle.x, rectangle.y);

                velocity.scl(1 / delta);

                cRigidBody.velocity.set(velocity);
            } else {
                cPosition.position.mulAdd(cRigidBody.velocity, delta);
            }

            if (cRigidBody.useGravity) {
                cRigidBody.velocity.y += world.getGravity() * cRigidBody.gravityMultiplier * delta;
            }
        }
    }

}
