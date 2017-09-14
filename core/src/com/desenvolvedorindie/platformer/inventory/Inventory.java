package com.desenvolvedorindie.platformer.inventory;

import com.artemis.Entity;
import com.desenvolvedorindie.platformer.item.Item;
import com.desenvolvedorindie.platformer.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Inventory implements IInventory {

    protected final ItemStack[] inventory;

    protected List<IInventoryChangedListener> changeListeners;

    public Inventory(int slotsCount) {
        inventory = new ItemStack[slotsCount];
        for (int i = 0; i < slotsCount; i++) {
            inventory[i] = new ItemStack(null, 0);
        }
    }

    @Override
    public int getSize() {
        return inventory.length;
    }

    @Override
    public boolean removeItemStack(int index, int count) {
        ItemStack itemStack = inventory[index];
        if (itemStack.stack < count) {
            return false;
        }
        itemStack.stack -= count;
        return true;
    }

    @Override
    public ItemStack removeItemStack(int index) {
        ItemStack itemStack = inventory[index];
        inventory[index] = null;
        return itemStack;
    }

    @Override
    public ItemStack getItemStack(int index) {
        return null;
    }

    @Override
    public void setItemStack(int index, ItemStack itemStack) {
        inventory[index] = itemStack;
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
            if (inventory[i] == null) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void clear() {
        for (int i = 0; i < getSize(); i++) {
            inventory[i] = null;
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
