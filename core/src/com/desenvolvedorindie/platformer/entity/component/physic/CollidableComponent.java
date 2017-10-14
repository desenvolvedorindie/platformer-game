package com.desenvolvedorindie.platformer.entity.component.physic;

import com.artemis.Component;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class CollidableComponent extends Component {

    public final Rectangle collisionBox = new Rectangle();

    public final Vector2 center = new Vector2();

    public boolean onGround;

    public boolean onCeiling;

    public boolean onLeftWall;

    public boolean onRightWall;

}
