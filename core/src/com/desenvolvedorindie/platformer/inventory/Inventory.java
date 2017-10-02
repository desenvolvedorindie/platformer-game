package com.desenvolvedorindie.platformer.inventory;

import com.artemis.Entity;
import com.badlogic.gdx.utils.Array;
import com.desenvolvedorindie.platformer.item.Item;
import com.desenvolvedorindie.platformer.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

    protected Array<Slot> slots;

    public Inventory() {
        slots = new Array<>(9);
        for(int i = 0; i < 9; i++) {
            slots.add(new Slot(null, 0));
        }
    }

    public int checkInventory(Item item) {
        int amount = 0;

        for (Slot slot : slots) {
            if (slot.getItem() == item) {
                amount += slot.getAmount();
            }
        }

        return amount;
    }

    public boolean store(Item item, int amount) {
        // first check for a slot with the same item type
        Slot itemSlot = firstSlotWithItem(item);
        if (itemSlot != null) {
            itemSlot.add(item, amount);
            return true;
        } else {
            // now check for an available empty slot
            Slot emptySlot = firstSlotWithItem(null);
            if (emptySlot != null) {
                emptySlot.add(item, amount);
                return true;
            }
        }

        // no slot to add
        return false;
    }

    public Array<Slot> getSlots() {
        return slots;
    }

    private Slot firstSlotWithItem(Item item) {
        for (Slot slot : slots) {
            if (slot.getItem() == item) {
                return slot;
            }
        }

        return null;
    }

}
