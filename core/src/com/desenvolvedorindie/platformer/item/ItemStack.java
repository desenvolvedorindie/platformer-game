package com.desenvolvedorindie.platformer.item;

public class ItemStack {

    public int stack;
    private Item item;
    private int meta;

    public ItemStack(Item item, int amount) {
        this.item = item;
        this.stack = amount;
    }

    public ItemStack(Item item) {
        this(item, 1);
    }

    public ItemStack(Item item, int amount, int meta) {
        this.item = item;
        this.stack = amount;
        this.meta = meta;

        if (this.meta < 0) {
            this.meta = 0;
        }
    }

    public int getStack() {
        return stack;
    }

    public void setStack(int stack) {
        this.stack = stack;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getMeta() {
        return meta;
    }

    public void setMeta(int meta) {
        this.meta = meta;
    }
}
