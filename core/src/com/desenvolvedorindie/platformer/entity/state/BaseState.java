package com.desenvolvedorindie.platformer.entity.state;

import com.artemis.Entity;
import com.badlogic.gdx.ai.fsm.State;

public abstract class BaseState implements State<Entity> {

    private boolean hasinit;

    @Override
    public void enter(Entity entity) {
        init(entity);
    }

    public void init(Entity entity) {
        if (!hasinit) {
            entity.getWorld().inject(this);
            hasinit = true;
        }
    }
}
