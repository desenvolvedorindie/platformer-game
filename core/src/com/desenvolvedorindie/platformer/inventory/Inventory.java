package com.desenvolvedorindie.platformer.inventory;

import com.artemis.Entity;
import com.badlogic.gdx.utils.Array;
import com.desenvolvedorindie.platformer.item.Item;
import com.desenvolvedorindie.platformer.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Inventory implements IInventory {

    protected final Array<ItemStack> inventory;

    protected List<IInventoryChangedListener> changeListeners;

    private String title;

    public Inventory(String title, int slotsCount) {
        this.title = title;

        inventory = new Array<>(slotsCount);

        for (int i = 0; i < slotsCount; i++) {
            inventory.add(new ItemStack(null, 0));
        }
    }

    @Override
    public int getSize() {
        return inventory.size;
    }

    @Override
    public boolean removeItemStack(int index, int count) {
        ItemStack itemStack = inventory.get(index);
        if (itemStack.stack < count) {
            return false;
        }
        itemStack.stack -= count;
        return true;
    }

    @Override
    public ItemStack removeItemStack(int index) {
        ItemStack itemStack = inventory.get(index);
        itemStack.setItem(null);
        return itemStack;
    }

    @Override
    public ItemStack getItemStack(int index) {
        return null;
    }

    @Override
    public void setItemStack(int index, ItemStack itemStack) {
        inventory.set(index, itemStack);
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack itemStack) {
        return true;
    }

    @Override
    public boolean hasItem(Item item) {
        return false;
    }

    @Override
    public boolean getItemCount(Item item) {
        return false;
    }

    @Override
    public boolean hasItemCount(Item item, int count) {
        return false;
    }

    @Override
    public boolean isFull() {
        for (int i = 0; i < getSize(); i++) {
            if (inventory.get(i) == null) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void clear() {
        for (int i = 0; i < getSize(); i++) {
            inventory.set(i, null);
        }
    }

    @Override
    public void addChangeListener(IInventoryChangedListener inventoryChangedListener) {
        if (changeListeners == null)
            changeListeners = new ArrayList<IInventoryChangedListener>();

        changeListeners.add(inventoryChangedListener);
    }

    @Override
    public void removeChangeListener(IInventoryChangedListener inventoryChangedListener) {
        if (changeListeners != null)
            changeListeners.remove(inventoryChangedListener);
    }

    @Override
    public void open(Entity entity) {

    }

    @Override
    public void close(Entity entity) {

    }

}
