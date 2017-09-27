package com.desenvolvedorindie.platformer.block;


import com.desenvolvedorindie.platformer.math.Vector2i;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public enum Facing {
    TOP(0, 1, 0, 1, "top", new Vector2i(0, 1)),
    BOTTOM(1, 0, 0, -1, "bottom", new Vector2i(0, -1)),
    LEFT(2, 3, -1, 0, "left", new Vector2i(-1, 0)),
    RIGHT(3, 2, 1, 0, "right", new Vector2i(1, 0));

    private static final Map<String, Facing> NAME_LOOKUP = new HashMap<>();

    private static final Facing[] VALUES = new Facing[4];

    private final int index;

    private final int opposite;

    private final int frontOffsetX;

    private final int frontOffsetY;

    private final String name;

    private final Vector2i direction;

    Facing(int index, int opposite, int frontOffsetX, int frontOffsetY, String name, Vector2i direction) {
        this.index = index;
        this.opposite = opposite;
        this.frontOffsetX = frontOffsetX;
        this.frontOffsetY = frontOffsetY;
        this.name = name;
        this.direction = direction;
    }

    public int getIndex() {
        return this.index;
    }

    public int getFrontOffsetX() {
        return frontOffsetX;
    }

    public int getFrontOffsetY() {
        return frontOffsetY;
    }

    public String getName() {
        return this.name;
    }

    public Vector2i getDirection() {
        return direction;
    }

    public Facing getOpposite() {
        return getFront(this.opposite);
    }

    public String toString() {
        return this.name;
    }

    public static Facing random(Random rand) {
        return values()[rand.nextInt(values().length)];
    }

    public static Facing getFacingFromVector(float x, float y) {
        Facing result = TOP;
        float f = Float.MIN_VALUE;

        for (Facing facing : values()) {
            float f1 = x * (float) facing.direction.getX() + y * (float) facing.direction.getY();

            if (f1 > f) {
                f = f1;
                result = facing;
            }
        }

        return result;
    }

    public static Facing getByName(String name) {
        return name == null ? null : NAME_LOOKUP.get(name.toLowerCase(Locale.ROOT));
    }

    public static Facing getFront(int index) {
        return VALUES[Math.abs(index % VALUES.length)];
    }

    static {
        for (Facing facing : values()) {
            VALUES[facing.index] = facing;
            NAME_LOOKUP.put(facing.getName().toLowerCase(Locale.ROOT), facing);
        }
    }

}