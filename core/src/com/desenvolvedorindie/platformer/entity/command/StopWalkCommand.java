package com.desenvolvedorindie.platformer.entity.command;

import com.artemis.Entity;
import com.desenvolvedorindie.platformer.entity.component.physic.RigidBodyComponent;

public class StopWalkCommand implements Command {

    @Override
    public void execute(Entity entity) {
        RigidBodyComponent cRigidBody = entity.getComponent(RigidBodyComponent.class);
        cRigidBody.velocity.x = 0;
    }
}
