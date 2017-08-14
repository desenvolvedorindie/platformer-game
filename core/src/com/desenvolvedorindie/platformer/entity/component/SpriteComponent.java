package com.desenvolvedorindie.platformer.entity.component;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class SpriteComponent extends Component {

    public Sprite sprite;

    public boolean flipX;

    public boolean flipY;

    public ShaderProgram shader;

}
