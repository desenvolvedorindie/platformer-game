package com.desenvolvedorindie.platformer.inventory;

import com.artemis.Entity;

public interface IInventoryChangedListener {

    void onInventoryChanged(IInventory invetory);

    void onInventoryOpened(Inventory inventory, Entity entity);

    void onInventoryClosed(Inventory inventory, Entity entity);

}
