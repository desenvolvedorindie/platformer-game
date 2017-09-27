package com.desenvolvedorindie.platformer.block;


import com.desenvolvedorindie.platformer.math.MathHelper;
import com.desenvolvedorindie.platformer.math.Vector2i;

public class BlockPos extends Vector2i {

    public static final BlockPos ORIGIN = new BlockPos(0, 0);

    private static final int NUM_X_BITS = 1 + MathHelper.log2(MathHelper.smallestEncompassingPowerOfTwo(30000000));
    private static final int NUM_Z_BITS = NUM_X_BITS;
    private static final int NUM_Y_BITS = 64 - NUM_X_BITS - NUM_Z_BITS;
    private static final int Y_SHIFT = 0 + NUM_Z_BITS;
    private static final int X_SHIFT = Y_SHIFT + NUM_Y_BITS;
    private static final long X_MASK = (1L << NUM_X_BITS) - 1L;
    private static final long Y_MASK = (1L << NUM_Y_BITS) - 1L;

    private final boolean wall;

    public BlockPos(int x, int y, boolean wall) {
        super(x, y);
        this.wall = wall;
    }

    public BlockPos(int x, int y) {
        this(x, y, false);
    }

    public BlockPos(BlockPos blockPos) {
        this(blockPos.getX(), blockPos.getY(), blockPos.isWall());
    }

    public boolean isWall() {
        return wall;
    }

    public BlockPos opposite() {
        return new BlockPos(this.getX(), this.getY(), !this.isWall());
    }

    public BlockPos up() {
        return this.up(1);
    }

    public BlockPos up(int n) {
        return this.offset(Facing.TOP, n);
    }

    public BlockPos down() {
        return this.down(1);
    }

    public BlockPos down(int n) {
        return this.offset(Facing.BOTTOM, n);
    }

    public BlockPos left() {
        return this.left(1);
    }

    public BlockPos left(int n) {
        return this.offset(Facing.LEFT, n);
    }

    public BlockPos right() {
        return this.left(1);
    }

    public BlockPos right(int n) {
        return this.offset(Facing.RIGHT, n);
    }

    public BlockPos offset(Facing facing) {
        return this.offset(facing, 1);
    }

    public BlockPos offset(Facing facing, int n) {
        return n == 0 ? this : new BlockPos(this.getX() + facing.getFrontOffsetX() * n, this.getY() + facing.getFrontOffsetY() * n, this.isWall());
    }

    public long toLong() {
        return ((long) this.getX() & X_MASK) << X_SHIFT | ((long) this.getY() & Y_MASK) << Y_SHIFT;
    }

}
