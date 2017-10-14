package com.desenvolvedorindie.platformer.entity.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.desenvolvedorindie.platformer.entity.component.EffectableComponent;
import com.desenvolvedorindie.platformer.entity.effects.IStatusEffect;

public class StatusEffectSystem extends IteratingSystem {

    private ComponentMapper<EffectableComponent> mEffectable;

    public StatusEffectSystem() {
        super(Aspect.all(
                EffectableComponent.class
        ));
    }

    @Override
    protected void process(int entityId) {
        EffectableComponent cEffectagle = mEffectable.get(entityId);

        for (IStatusEffect effect : cEffectagle.effects) {
            effect.update(world.getDelta());
        }

        cEffectagle.clearFinished();
    }
}
