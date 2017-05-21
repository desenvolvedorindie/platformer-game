package com.desenvolvedorindie.platformer.dictionary;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.IntMap;
import com.desenvolvedorindie.platformer.block.Block;

public class Blocks {

    public static final IntMap<Block> REGISTRY = new IntMap<Block>();

    public static final int AIR_ID = 0;

    public static final Block AIR;

    public static final Block DIRT;

    public static final Block COBBLESTONE;

    public static final Block OBSIDIAN;

    public static Block getBlockById(int id) {
        return REGISTRY.get(id);
    }

    public static int getIdByBlock(Block block) {
        return REGISTRY.findKey(block, true, AIR_ID);
    }

    private static Block register(int id, Block block) {
        REGISTRY.put(id, block);
        return block;
    }

    static {
        AIR = register(AIR_ID, new Block(null));
        DIRT = register(1, new Block(new Texture("blocks/dirt.png")));
        COBBLESTONE = register(2, new Block(new Texture("blocks/cobblestone.png")));
        OBSIDIAN = register(3, new Block(new Texture("blocks/obsidian.png")));
    }

}
