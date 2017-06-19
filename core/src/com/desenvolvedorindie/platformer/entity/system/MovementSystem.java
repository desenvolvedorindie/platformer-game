package com.desenvolvedorindie.platformer.entity.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.desenvolvedorindie.platformer.block.Block;
import com.desenvolvedorindie.platformer.entity.component.CollidableComponent;
import com.desenvolvedorindie.platformer.entity.component.RigidBodyComponent;
import com.desenvolvedorindie.platformer.entity.component.TransformComponent;
import com.desenvolvedorindie.platformer.world.World;

public class MovementSystem extends IteratingSystem {

    private ComponentMapper<TransformComponent> mTransform;

    private ComponentMapper<RigidBodyComponent> mRigidBody;

    private ComponentMapper<CollidableComponent> mCollidable;

    private World world;

    public MovementSystem(World world) {
        super(Aspect.all(TransformComponent.class, RigidBodyComponent.class));
        this.world = world;
    }

    @Override
    protected void process(int entityId) {
        TransformComponent cTransform = mTransform.get(entityId);
        RigidBodyComponent cRigidBody = mRigidBody.get(entityId);
        CollidableComponent cCollidable = mCollidable.get(entityId);

        float delta = super.world.getDelta();

        cTransform.position.mulAdd(cRigidBody.velocity, delta);

        cRigidBody.velocity.y += world.getGravity() * delta;

        if (cCollidable != null) {
            if (cTransform.position.y < world.getSeaLevel() * Block.TILE_SIZE) {
                cRigidBody.velocity.y = 0;
                cTransform.position.y = world.getSeaLevel() * Block.TILE_SIZE;

                cCollidable.onGround = true;

            } else {
                cCollidable.onGround = false;
            }
        }

    }

}
