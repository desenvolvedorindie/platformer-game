package com.desenvolvedorindie.platformer.graphics;

import com.badlogic.gdx.graphics.Color;

public class Colors {

    static {
        reset();
    }

    public static void reset () {
        com.badlogic.gdx.graphics.Colors.reset();
        com.badlogic.gdx.graphics.Colors.put("ASD", new Color());
    }

}
