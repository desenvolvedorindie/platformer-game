package com.desenvolvedorindie.platformer.dictionary;

import com.badlogic.gdx.utils.IntMap;
import com.desenvolvedorindie.platformer.block.Block;
import com.desenvolvedorindie.platformer.block.BlockAir;
import com.desenvolvedorindie.platformer.block.SlabBlock;
import com.desenvolvedorindie.platformer.block.SlopeBlock;
import com.desenvolvedorindie.platformer.resource.Assets;

import java.util.HashMap;
import java.util.Map;

public class Blocks {

    public static final IntMap<Block> REGISTRY = new IntMap<Block>();

    public static final Map<String, Integer> NAME_TO_ID = new HashMap<String, Integer>();

    public static final Map<Integer, String> ID_TO_NAME = new HashMap<Integer, String>();

    public static final String AIR_NAME = "air";

    public static final int AIR_ID = 0;

    public static final Block AIR;

    public static final Block DIRT;

    public static final Block COBBLESTONE;

    public static final Block OBSIDIAN;

    public static final Block SLAB;

    public static final Block SLOPE;

    static {
        AIR = register(AIR_NAME, AIR_ID, new BlockAir());
        DIRT = register("dirt", 1, new Block(Assets.manager.get(Assets.BLOCK_DIRT)));
        COBBLESTONE = register("cobblestone", 2, new Block(Assets.manager.get(Assets.BLOCK_COBBLESTONE)));
        OBSIDIAN = register("obsidian", 3, new Block(Assets.manager.get(Assets.BLOCK_OBSIDIAN)));
        SLAB = register("slab", 4, new SlabBlock(Assets.manager.get(Assets.BLOCK_SLAB)));
        SLOPE = register("slope", 5, new SlopeBlock(Assets.manager.get(Assets.BLOCK_SLOPE)));
    }

    public static Block getBlockById(int id) {
        return REGISTRY.get(id);
    }

    public static Block getBlockByName(String name) {
        return getBlockById(NAME_TO_ID.getOrDefault(name, AIR_ID));
    }

    public static int getIdByBlock(Block block) {
        return REGISTRY.findKey(block, true, AIR_ID);
    }

    public static int getIdByName(String name) {
        return NAME_TO_ID.getOrDefault(name, AIR_ID);
    }

    public static String getNameById(int id) {
        return ID_TO_NAME.getOrDefault(id, AIR_NAME);
    }

    private static Block register(String name, int id, Block block) {
        REGISTRY.put(id, block);
        NAME_TO_ID.put(name, id);
        ID_TO_NAME.put(id, name);
        return block;
    }

}
