package com.desenvolvedorindie.platformer.inventory;

import com.artemis.Entity;
import com.desenvolvedorindie.platformer.item.Item;
import com.desenvolvedorindie.platformer.item.ItemStack;

public interface IInventory {

    int getSize();

    boolean removeItemStack(int index, int count);

    ItemStack removeItemStack(int index);

    ItemStack getItemStack(int index);

    void setItemStack(int index, ItemStack itemStack);

    boolean isItemValidForSlot(int index, ItemStack itemStack);

    boolean hasItem(Item item);

    boolean getItemCount(Item item);

    boolean hasItemCount(Item item, int count);

    boolean isFull();

    void clear();

    void addChangeListener(IInventoryChangedListener inventoryChangedListener);

    void removeChangeListener(IInventoryChangedListener inventoryChangedListener);

    void open(Entity entity);

    void close(Entity entity);

}
