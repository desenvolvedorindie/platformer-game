package com.desenvolvedorindie.platformer.block.water;


import com.badlogic.gdx.graphics.Color;

public class Cell {

    public static final Color LIGHT_COLOR = new Color(0.6f, 0.8f, 1f, 1);

    public static final Color DARK_COLOR = new Color(0, 0.5f, 0.9f, 1);

    private static final boolean SHOW_FLOW = true;

    private static final boolean RENDER_FLOATING_LIQUID = true;

    private static final boolean RENDER_DOWN_FLOWING_LIQUID = true;

    public final boolean[] flowDirections = new boolean[4];

    public final Color color = new Color(1);

    public float liquid;

    public int settleCount;

    public CellType type = CellType.BLANK;

    // Neighboring cells
    public Cell top;
    public Cell bottom;
    public Cell left;
    public Cell right;

    // Shows flow direction of cell
    public int bitmask;

    public float size;

    private boolean settled;

    public boolean isSettled() {
        return settled;
    }

    public void setSettled(boolean settled) {
        this.settled = settled;
        if (!settled)
            settleCount = 0;
    }

    public void setType(CellType type) {
        this.type = type;
        if (type == CellType.SOLID) {
            liquid = 0;
        }
        unsettleNeighbors();
    }

    public void addLiquid(float amount) {
        liquid += amount;
        setSettled(false);
    }

    public void resetFlowDirections() {
        //Arrays.fill(flowDirections, false);
        flowDirections[0] = false;
        flowDirections[1] = false;
        flowDirections[2] = false;
        flowDirections[3] = false;
    }

    public void unsettleNeighbors() {
        if (top != null)
            top.setSettled(false);
        if (bottom != null)
            bottom.setSettled(false);
        if (left != null)
            left.setSettled(false);
        if (right != null)
            right.setSettled(false);
    }

    public void update() {
        // Update bitmask based on flow directions
        bitmask = 0;
        if (flowDirections[(int) FlowDirection.TOP.id])
            bitmask += 1;
        if (flowDirections[(int) FlowDirection.RIGHT.id])
            bitmask += 2;
        if (flowDirections[(int) FlowDirection.BOTTOM.id])
            bitmask += 4;
        if (flowDirections[(int) FlowDirection.LEFT.id])
            bitmask += 8;

        if (SHOW_FLOW) {
            // Show flow direction of this cell
            //FlowSprite.sprite = FlowSprites [Bitmask];
        } else {
            //FlowSprite.sprite = FlowSprites [0];
        }

        // Set size of Liquid sprite based on liquid value
        size = Math.min(1, liquid);

        // Optional rendering flags
        if (!RENDER_FLOATING_LIQUID) {
            // Remove "Floating" liquids
            if (bottom != null && bottom.type != CellType.SOLID && bottom.liquid <= 0.99f) {
                size = 0;
            }
        }
        if (RENDER_DOWN_FLOWING_LIQUID) {
            // Fill out cell if cell above it has liquid
            if (type == CellType.BLANK && top != null && (top.liquid > 0.05f || top.bitmask == 4)) {
                size = 1;
            }
        }

        // Set color based on pressure in cell
        this.color.set(LIGHT_COLOR);
        this.color.lerp(DARK_COLOR, liquid / 4f);
    }

    public enum CellType {
        BLANK,
        SOLID,
    }

    public enum FlowDirection {
        TOP(0),
        RIGHT(1),
        BOTTOM(2),
        LEFT(3);

        public final int id;

        FlowDirection(int id) {
            this.id = id;
        }
    }

}
