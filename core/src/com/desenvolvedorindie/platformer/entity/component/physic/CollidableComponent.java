package com.desenvolvedorindie.platformer.entity.component.physic;

import com.artemis.Component;
import com.badlogic.gdx.math.Rectangle;

public class CollidableComponent extends Component {

    public final Rectangle collisionBox = new Rectangle();

    public boolean onGround;

    public boolean onCeiling;

    public boolean onLeftWall;

    public boolean onRightWall;

}
