package com.desenvolvedorindie.platformer.entity.component.physic;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class RigidBodyComponent extends Component {

    public final Vector2 velocity = new Vector2();

    public boolean useGravity = true;

    public boolean isKinematic = true;

    public float gravityMultiplier = 1;
}
