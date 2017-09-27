package com.desenvolvedorindie.platformer.entity.component;

import com.artemis.Component;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.desenvolvedorindie.platformer.resource.Assets;
import net.spookygames.gdx.spriter.SpriterAnimator;

public class SpriterAnimationComponent extends Component {

    public SpriterAnimator spriterAnimator;

    public ShaderProgram shader/* = Assets.manager.get(Assets.SHADER_PASSTHROUGH)*/;

    public float speed = 1f;

}
