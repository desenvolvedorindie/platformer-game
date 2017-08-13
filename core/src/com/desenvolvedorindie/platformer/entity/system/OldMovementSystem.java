package com.desenvolvedorindie.platformer.entity.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.desenvolvedorindie.platformer.block.Block;
import com.desenvolvedorindie.platformer.entity.component.Box2dRigidBodyComponent;
import com.desenvolvedorindie.platformer.entity.component.CollidableComponent;
import com.desenvolvedorindie.platformer.entity.component.RigidBodyComponent;
import com.desenvolvedorindie.platformer.entity.component.TransformComponent;
import com.desenvolvedorindie.platformer.world.World;

public class OldMovementSystem extends IteratingSystem {

    private ComponentMapper<Box2dRigidBodyComponent> mBox2dRigidBody;

    private ComponentMapper<CollidableComponent> mCollidableComponent;

    private ComponentMapper<RigidBodyComponent> mRigidBody;

    private ComponentMapper<TransformComponent> mTransform;

    private World world;

    private OrthographicCamera camera;

    public OldMovementSystem(World world, OrthographicCamera camera) {
        super(Aspect.all(TransformComponent.class, RigidBodyComponent.class));
        this.world = world;
        this.camera = camera;
    }

    @Override
    protected void process(int entityId) {
        Box2dRigidBodyComponent cBox2dRigidBody = mBox2dRigidBody.get(entityId);
        RigidBodyComponent cRigidBody = mRigidBody.get(entityId);
        TransformComponent cTransform = mTransform.get(entityId);
        CollidableComponent cCollidable = mCollidableComponent.get(entityId);

        float delta = super.world.getDelta();

        //cTransform.position.set(cBox2dRigidBody.body.getPosition());
        //cTransform.rotation = MathUtils.radiansToDegrees * cBox2dRigidBody.body.getAngle();

        cRigidBody.velocity.y += world.getGravity() * delta;

        if (mCollidableComponent.has(entityId)) {
            //clamp velocity

            cCollidable.onGround = false;
            cCollidable.onCeiling = false;
            cCollidable.onLeftWall = false;
            cCollidable.onRightWall = false;

            Vector2 oldPos = new Vector2(cTransform.position);
            boolean collisionX = false, collisionY = false, exit;

            cTransform.position.x += cRigidBody.velocity.x * delta;

            cCollidable.collisionBox.setCenter(cTransform.position);

            Vector2 min = cCollidable.collisionBox.getPosition(new Vector2());
            Vector2 max = cCollidable.collisionBox.getSize(new Vector2()).add(min);
            Vector2 size = cCollidable.collisionBox.getSize(new Vector2());
            float pos;

            float centerX = (min.x + max.x) / 2;
            float centerY = (min.y + max.y) / 2;

            // Horizontal Collision
            if (cRigidBody.velocity.x < 0) {
                if (size.y < Block.TILE_SIZE) {
                    //top left
                    collisionX = world.isSolid(min.x, max.y);
                    //bottom left
                    if (!collisionX) {
                        collisionX = world.isSolid(min.x, min.y);
                    }
                } else {
                    // center to up direction in left side
                    exit = false;
                    collisionX = false;
                    pos = centerY;
                    do {
                        if (pos < min.y) {
                            pos = min.y;
                            exit = true;
                        }
                        if (world.isSolid(min.x, pos)) {
                            collisionX = true;
                        }
                        pos -= Block.TILE_SIZE;
                    } while (!collisionX && !exit);

                    // Center to down direction in left side
                    if (!collisionX) {
                        exit = false;
                        pos = centerY + Block.TILE_SIZE;
                        do {
                            if (pos > max.y) {
                                pos = max.y;
                                exit = true;
                            }
                            if (world.isSolid(min.x, pos)) {
                                collisionX = true;
                            }
                            pos += Block.TILE_SIZE;
                        } while (!collisionX && !exit);
                    }
                }

                cCollidable.onLeftWall = collisionX;

                /*
                if (collisionX) {
                    cTransform.position.x = (int) min.x & ~(Block.TILE_SIZE - 1);
                    cTransform.position.x += Block.TILE_SIZE + cTransform.position.x - min.x;
                    cRigidBody.velocity.x = 0;
                }
                */
            } else if (cRigidBody.velocity.x > 0) {
                if (size.y < Block.TILE_SIZE) {
                    //top right
                    collisionX = world.isSolid(max.x, max.y);
                    //bottom right
                    if (!collisionX) {
                        collisionX = world.isSolid(max.x, min.y);
                    }
                } else {
                    // center to up direction in right
                    exit = false;
                    collisionX = false;
                    pos = centerY;
                    do {
                        if (pos < min.y) {
                            pos = min.y;
                            exit = true;
                        }
                        if (world.isSolid(max.x, pos)) {
                            collisionX = true;
                        }
                        pos -= Block.TILE_SIZE;
                    } while (!collisionX && !exit);

                    // Center to down direction in right
                    if (!collisionX) {
                        exit = false;
                        pos = centerY + Block.TILE_SIZE;
                        do {
                            if (pos > max.y) {
                                pos = max.y;
                                exit = true;
                            }
                            if (world.isSolid(max.x, pos)) {
                                collisionX = true;
                            }
                            pos += Block.TILE_SIZE;
                        }  while (!collisionX && !exit);
                    }
                }

                cCollidable.onRightWall = collisionX;

                /*
                if (collisionX) {
                    cTransform.position.x = (int) max.x & ~(Block.TILE_SIZE - 1);
                    cTransform.position.x -= max.x - cTransform.position.x;
                    cRigidBody.velocity.x = 0;
                }
                */
            }

            if (collisionX) {
                cTransform.position.x = oldPos.x;
                cRigidBody.velocity.x = 0;
            }

            cTransform.position.y += cRigidBody.velocity.y * delta;

            cCollidable.collisionBox.setCenter(cTransform.position);

            cCollidable.collisionBox.getPosition(min);
            cCollidable.collisionBox.getSize(max).add(min);
            cCollidable.collisionBox.getSize(size);

            // Vertical Collision
            if (cRigidBody.velocity.y < 0) {
                if (size.x < Block.TILE_SIZE) {
                    //bottom left
                    collisionY = world.isSolid(min.x, min.y);
                    //bottom right
                    if (!collisionY) {
                        collisionY = world.isSolid(max.x, min.y);
                    }
                } else {
                    // center to left direction in bottom
                    exit = false;
                    collisionY = false;
                    pos = centerX;
                    do {
                        if (pos < min.x) {
                            pos = min.x;
                            exit = true;
                        }
                        if (world.isSolid(pos, min.y)) {
                            collisionY = true;
                        }
                        pos -= Block.TILE_SIZE;
                    } while (!collisionY && !exit);

                    // Center to right direction in bottom
                    if (!collisionY) {
                        exit = false;
                        pos = centerX + Block.TILE_SIZE;
                        do {
                            if (pos > max.x) {
                                pos = max.x;
                                exit = true;
                            }
                            if (world.isSolid(pos, min.y)) {
                                collisionY = true;
                            }
                            pos += Block.TILE_SIZE;
                        } while (!collisionY && !exit);
                    }
                }

                cCollidable.onGround = collisionY;

                /*
                if (collisionY) {
                    cTransform.position.y = (int) min.y & ~(Block.TILE_SIZE - 1);
                    cTransform.position.y -= min.y - cTransform.position.y;
                    cRigidBody.velocity.y = 0;
                }
                */
            } else if (cRigidBody.velocity.y > 0) {
                if (size.x <= Block.TILE_SIZE) {
                    //top left
                    collisionY = world.isSolid(min.x, max.y);
                    //top right
                    if (!collisionY) {
                        collisionY = world.isSolid(max.x, max.y);
                    }
                } else {
                    // center to left direction in top
                    exit = false;
                    collisionY = false;
                    pos = centerX;
                    do {
                        if (pos < min.x) {
                            pos = min.x;
                            exit = true;
                        }
                        if (world.isSolid(pos, max.y)) {
                            collisionY = true;
                        }
                        pos -= Block.TILE_SIZE;
                    } while (!collisionY && !exit);

                    // Center to left direction in bottom
                    if (!collisionY) {
                        exit = false;
                        pos = centerX + Block.TILE_SIZE;
                        do {
                            if (pos > max.x) {
                                pos = max.x;
                                exit = true;
                            }
                            if (world.isSolid(pos, max.y)) {
                                collisionY = true;
                            }
                            pos += Block.TILE_SIZE;
                        } while (!collisionY && !exit);
                    }
                }

                cCollidable.onCeiling = collisionY;

                /*
                if (collisionY) {
                    cTransform.position.y = (int) max.y & ~(Block.TILE_SIZE - 1);
                    cTransform.position.y += Block.TILE_SIZE + cTransform.position.y - max.y;
                    cRigidBody.velocity.y = 0;
                }
                */
            }

            if (collisionY) {
                cTransform.position.y = oldPos.y;
                cRigidBody.velocity.y = 0;
            }
        } else {
            cTransform.position.mulAdd(cRigidBody.velocity, delta);
        }
    }

}
