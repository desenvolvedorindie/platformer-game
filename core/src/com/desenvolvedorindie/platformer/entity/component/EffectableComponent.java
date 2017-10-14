package com.desenvolvedorindie.platformer.entity.component;

import com.artemis.Component;
import com.badlogic.gdx.utils.Array;
import com.desenvolvedorindie.platformer.entity.effects.IStatusEffect;

public class EffectableComponent extends Component {

    public final Array<IStatusEffect> effects = new Array<IStatusEffect>();

    public void clearFinished() {
        for (int i = effects.size - 1; i >= 0; i--) {
            if (effects.get(i).hasFinished()) {
                effects.removeIndex(i);
            }
        }
    }

}
