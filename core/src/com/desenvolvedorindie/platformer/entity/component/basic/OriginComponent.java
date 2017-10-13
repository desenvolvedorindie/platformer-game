package com.desenvolvedorindie.platformer.entity.component.basic;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class OriginComponent extends Component {

    public final Vector2 origin = new Vector2();

    public boolean originCenter = false;

}
