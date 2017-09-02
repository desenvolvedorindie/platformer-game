package com.desenvolvedorindie.platformer.entity.component;

import com.artemis.Component;
import com.badlogic.gdx.utils.Array;
import com.desenvolvedorindie.platformer.entity.effects.IStatusEffect;

public class EffectableComponent extends Component {

    Array<IStatusEffect> effects = new Array<IStatusEffect>();

    public void addEffect(IStatusEffect statusEffect) {

    }

    public Array<IStatusEffect> getEffects() {
        return effects;
    }

}
