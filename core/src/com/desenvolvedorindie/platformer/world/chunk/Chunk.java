package com.desenvolvedorindie.platformer.world.chunk;

import com.desenvolvedorindie.platformer.world.ChunkManager;

public class Chunk {

    public static int CHUNK_SIZE = 50;

    int[][][] blocks = new int[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE];

    private float[][] lightMap = new float[CHUNK_SIZE][CHUNK_SIZE];

    private ChunkManager chunkManager;

    private int startX;

    private int startY;

    private boolean isGenerated;

    private boolean topChunk;

    public Chunk(ChunkManager chunkManager, int startX, int startY, boolean topChunk) {
        this.startX = startX;
        this.startY = startY;
        this.chunkManager = chunkManager;
        this.topChunk = topChunk;
    }

}
