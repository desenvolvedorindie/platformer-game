package com.desenvolvedorindie.platformer.inventory;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.desenvolvedorindie.platformer.dictionary.Items;
import com.desenvolvedorindie.platformer.item.Item;

public class Inventory {

    protected Array<Slot> slots;

    public Inventory() {
        int quantity = 9;
        slots = new Array<>(quantity);
        for (int i = 0; i < quantity; i++) {
            slots.add(new Slot(Items.SWORD, 1));
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
