package com.desenvolvedorindie.platformer.entity.system.physic;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.desenvolvedorindie.platformer.entity.component.basic.PositionComponent;
import com.desenvolvedorindie.platformer.entity.component.physic.CollidableComponent;
import com.desenvolvedorindie.platformer.entity.component.physic.RigidBodyComponent;
import com.desenvolvedorindie.platformer.world.World;

public class MovementSystem extends IteratingSystem {

    private ComponentMapper<PositionComponent> mPosition;
    private ComponentMapper<CollidableComponent> mCollidable;
    private ComponentMapper<RigidBodyComponent> mRigidBody;

    private World gameWorld;

    private Vector2 velocity = new Vector2(), tempPos = new Vector2();
    private Rectangle tile;

    public MovementSystem(World gameWorld) {
        super(Aspect.all(
                PositionComponent.class,
                RigidBodyComponent.class
        ));
        this.gameWorld = gameWorld;
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
                tempPos.set(cPosition.position).add(cCollidable.center);
                rectangle.setPosition(tempPos);

                int startX, startY, endX, endY;

                if (velocity.x > 0) {
                    startX = World.worldToMap(rectangle.x + rectangle.width);
                    endX = World.worldToMap(rectangle.x + rectangle.width + velocity.x);
                } else {
                    startX = World.worldToMap(rectangle.x + velocity.x);
                    endX = World.worldToMap(rectangle.x);
                }

                startY = World.worldToMap(rectangle.y);
                endY = World.worldToMap(rectangle.y + rectangle.height);

                for (int i = 0; i < Math.abs(velocity.x); i++) {
                    boolean found = false;

                    float oldX = rectangle.x;

                    rectangle.x += Math.abs(velocity.x) < 1f ? velocity.x : Math.signum(velocity.x);

                    for (int x = startX; x <= endX; x++) {
                        for (int y = startY; y <= endY; y++) {
                            if ((tile = gameWorld.getTileRectangle(x, y)) != null && rectangle.overlaps(tile)) {
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
                            break;
                        }
                    }

                    if (found) {
                        velocity.x = 0;
                        rectangle.x = oldX;
                        break;
                    }
                }

                if (velocity.y > 0) {
                    startY = World.worldToMap(rectangle.y + rectangle.height);
                    endY = World.worldToMap(rectangle.y + rectangle.height + velocity.y);
                } else {
                    startY = World.worldToMap(rectangle.y + velocity.y);
                    endY = World.worldToMap(rectangle.y);
                }

                startX = World.worldToMap(rectangle.x);
                endX = World.worldToMap(rectangle.x + rectangle.width);

                for (int i = 0; i < Math.abs(velocity.y); i++) {
                    boolean found = false;

                    rectangle.y += Math.abs(velocity.y) < 1f ? velocity.y : Math.signum(velocity.y);

                    for (int y = startY; y <= endY; y++) {
                        for (int x = startX; x <= endX; x++) {
                            if ((tile = gameWorld.getTileRectangle(x, y)) != null && rectangle.overlaps(tile)) {
                                if (velocity.y > 0) {
                                    cCollidable.onCeiling = true;
                                    rectangle.y = tile.y - rectangle.height;
                                } else {
                                    rectangle.y = tile.y + tile.height;
                                    cCollidable.onGround = true;
                                }
                                found = true;
                                break;
                            }
                        }

                        if (found) {
                            break;
                        }
                    }

                    if (found) {
                        velocity.y = 0;
                        break;
                    }
                }

                cPosition.position.set(rectangle.x - cCollidable.center.x, rectangle.y - cCollidable.center.y);

                velocity.scl(1 / delta);

                cRigidBody.velocity.set(velocity);
            } else {
                cPosition.position.mulAdd(cRigidBody.velocity, delta);
            }

            if (cRigidBody.useGravity) {
                cRigidBody.velocity.y += gameWorld.getGravity() * cRigidBody.gravityMultiplier * delta;
            }
        }
    }

}
