package com.desenvolvedorindie.platformer.scene2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.desenvolvedorindie.platformer.inventory.Slot;

public class SlotTarget extends DragAndDrop.Target {

    private Slot targetSlot;

    public SlotTarget(SlotActor actor) {
        super(actor);
        targetSlot = actor.getSlot();
        getActor().setColor(Color.LIGHT_GRAY);
    }

    @Override
    public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
        Slot payloadSlot = (Slot) payload.getObject();
        if (targetSlot.getItem() == payloadSlot.getItem() || targetSlot.getItem() == null) {
            getActor().setColor(Color.WHITE);
            return true;
        } else {
            getActor().setColor(Color.DARK_GRAY);
            return false;
        }
    }

    @Override
    public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
    }

    @Override
    public void reset(DragAndDrop.Source source, DragAndDrop.Payload payload) {
        getActor().setColor(Color.LIGHT_GRAY);
    }
}