package com.desenvolvedorindie.platformer.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Light {

    public final Vector2 position = new Vector2();
    public final Color color = new Color();

    public Light(float x, float y, Color color) {
        this.position.set(x, y);
        this.color.set(color);
    }

    @Override
    public String toString() {
        return "Light{" +
                "position=" + position +
                ", color=" + color +
                '}';
    }
}