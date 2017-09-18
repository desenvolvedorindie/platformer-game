package com.desenvolvedorindie.platformer.dictionary;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.IntMap;
import com.desenvolvedorindie.platformer.block.Block;
import com.desenvolvedorindie.platformer.item.Item;
import com.desenvolvedorindie.platformer.item.ItemBlock;
import com.desenvolvedorindie.platformer.resource.Assets;

import java.util.HashMap;
import java.util.Map;

public class Items {

    public static final IntMap<Item> REGISTRY = new IntMap<Item>();

    public static final Map<Block, Item> BLOCK_TO_ITEM = new HashMap<Block, Item>();

    public static final Item SWORD;

    static {
        SWORD = register(1, new Item(new SpriteDrawable(new Sprite(Assets.manager.get(Assets.ITEM_SWORD)))));
    }

    public static Item getBlockById(int id) {
        return REGISTRY.get(id);
    }

    public static int getIdByBlock(Item block) {
        return REGISTRY.findKey(block, true, 0);
    }

    private static Item register(int id, Item item) {
        REGISTRY.put(id, item);


        return item;
    }

    private static void registerItemBlock(Block block) {
        registerItemBlock(block, new ItemBlock(block));
    }

    private static void registerItemBlock(Block block, Item item) {
        BLOCK_TO_ITEM.put(block, item);
    }

}
