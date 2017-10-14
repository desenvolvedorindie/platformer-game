package com.desenvolvedorindie.platformer.entity.command;

import com.artemis.Entity;
import com.desenvolvedorindie.platformer.entity.component.physic.CollidableComponent;
import com.desenvolvedorindie.platformer.entity.component.physic.JumpComponent;
import com.desenvolvedorindie.platformer.entity.component.physic.RigidBodyComponent;

public class JumpCommand implements Command {

    public JumpCommand() {

    }

    @Override
    public void execute(Entity entity) {
        JumpComponent cJump = entity.getComponent(JumpComponent.class);
        RigidBodyComponent cRigidBody = entity.getComponent(RigidBodyComponent.class);
        CollidableComponent cCollidable = entity.getComponent(CollidableComponent.class);

        if (cJump.canJump) {
            if (cCollidable != null) {
                boolean wallJump = (cCollidable.onLeftWall || cCollidable.onRightWall) && cRigidBody.velocity.y < 0 && cJump.wallJump;

                if (cCollidable.onGround || wallJump) {
                    cRigidBody.velocity.y = cJump.jumpSpeed;
                }
            } else {
                cRigidBody.velocity.y = cJump.jumpSpeed;
            }
        }
    }
}
