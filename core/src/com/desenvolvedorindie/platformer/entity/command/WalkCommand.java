package com.desenvolvedorindie.platformer.entity.command;

import com.artemis.Entity;
import com.desenvolvedorindie.platformer.entity.component.WalkerComponent;
import com.desenvolvedorindie.platformer.entity.component.physic.RigidBodyComponent;

public class WalkCommand implements Command {

    private float direction;

    public WalkCommand(float direction) {
        this.direction = direction;
    }

    @Override
    public void execute(Entity entity) {
        RigidBodyComponent cRigidBody = entity.getComponent(RigidBodyComponent.class);
        WalkerComponent cWalker = entity.getComponent(WalkerComponent.class);

        cRigidBody.velocity.x = direction * cWalker.walkSpeed;
    }
}
