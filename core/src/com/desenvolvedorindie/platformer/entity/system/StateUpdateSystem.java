package com.desenvolvedorindie.platformer.entity.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.desenvolvedorindie.platformer.entity.component.StateComponent;

public class StateUpdateSystem extends IteratingSystem {

    private ComponentMapper<StateComponent> mState;

    public StateUpdateSystem() {
        super(Aspect.all(StateComponent.class));
    }

    @Override
    protected void process(int entityId) {
        mState.get(entityId).state.update();
    }

}
