package com.desenvolvedorindie.platformer.entity.component.render;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Pixmap;
import net.spookygames.gdx.gfx.Effect;
import net.spookygames.gdx.gfx.MultiTemporalVisualEffect;

public class GFXComponent extends Component {

    public MultiTemporalVisualEffect effect = new MultiTemporalVisualEffect(Pixmap.Format.RGBA8888, false);

}
