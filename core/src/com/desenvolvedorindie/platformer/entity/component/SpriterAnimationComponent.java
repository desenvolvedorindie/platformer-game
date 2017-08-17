package com.desenvolvedorindie.platformer.entity.component;

import com.artemis.Component;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import net.spookygames.gdx.spriter.SpriterAnimator;

public class SpriterAnimationComponent extends Component {

    public SpriterAnimator spriterAnimator;

    public ShaderProgram shader;

    public float speed = 1f;

}
