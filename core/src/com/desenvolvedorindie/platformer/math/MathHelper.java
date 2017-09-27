package com.desenvolvedorindie.platformer.math;

public class MathHelper {

    /**
     * http://lolengine.net/blog/2012/4/3/beyond-de-bruijn
     */
    private static final int[] MULTIPLY_DE_BRUIJN_BIT_POSITION;

    /**
     * Rounds the first parameter up to the next interval of the second parameter.
     *
     * For instance, {@code roundUp(1, 4)} returns 4; {@code roundUp(0, 4)} returns 0; and {@code roundUp(4, 4)} returns 4.
     */
    public static int roundUp(int number, int interval) {
        if (interval == 0) {
            return 0;
        } else if (number == 0) {
            return interval;
        } else {
            if (number < 0) {
                interval *= -1;
            }

            int i = number % interval;
            return i == 0 ? number : number + interval - i;
        }
    }

    /**
     * Is the given value a power of two?  (1, 2, 4, 8, 16, ...)
     */
    private static boolean isPowerOfTwo(int value)
    {
        return value != 0 && (value & value - 1) == 0;
    }

    /**
     * Returns the input value rounded up to the next highest power of two.
     */
    public static int smallestEncompassingPowerOfTwo(int value)
    {
        int i = value - 1;
        i = i | i >> 1;
        i = i | i >> 2;
        i = i | i >> 4;
        i = i | i >> 8;
        i = i | i >> 16;
        return i + 1;
    }

    /**
     * Uses a B(2, 5) De Bruijn sequence and a lookup table to efficiently calculate the log-base-two of the given
     * value. Optimized for cases where the input value is a power-of-two. If the input value is not a power-of-two,
     * then subtract 1 from the return value.
     */
    public static int log2DeBruijn(int value)
    {
        value = isPowerOfTwo(value) ? value : smallestEncompassingPowerOfTwo(value);
        return MULTIPLY_DE_BRUIJN_BIT_POSITION[(int)((long)value * 125613361L >> 27) & 31];
    }

    /**
     * Efficiently calculates the floor of the base-2 log of an integer value.  This is effectively the index of the
     * highest bit that is set.  For example, if the number in binary is 0...100101, this will return 5.
     */
    public static int log2(int value)
    {
        return log2DeBruijn(value) - (isPowerOfTwo(value) ? 0 : 1);
    }

    public static int hash(int value)
    {
        value = value ^ value >>> 16;
        value = value * -2048144789;
        value = value ^ value >>> 13;
        value = value * -1028477387;
        value = value ^ value >>> 16;
        return value;
    }

    static
    {
        MULTIPLY_DE_BRUIJN_BIT_POSITION = new int[] {0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9};
    }

}
