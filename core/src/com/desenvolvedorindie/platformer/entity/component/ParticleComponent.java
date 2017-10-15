package com.desenvolvedorindie.platformer.entity.component;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.utils.Array;

public class ParticleComponent extends Component {

    Array<ParticleEffectPool.PooledEffect> particles = new Array<>();

}
