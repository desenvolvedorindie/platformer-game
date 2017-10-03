package com.desenvolvedorindie.platformer.scene2d;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.desenvolvedorindie.platformer.inventory.Slot;
import com.desenvolvedorindie.platformer.item.Item;

public class SlotSource extends DragAndDrop.Source {

    private Slot sourceSlot;

    public SlotSource(SlotActor actor) {
        super(actor);
        this.sourceSlot = actor.getSlot();
    }

    @Override
    public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
        if (sourceSlot.getAmount() == 0) {
            return null;
        }

        DragAndDrop.Payload payload = new DragAndDrop.Payload();
        Slot payloadSlot = new Slot(sourceSlot.getItem(), sourceSlot.getAmount());
        sourceSlot.take(sourceSlot.getAmount());
        payload.setObject(payloadSlot);

        Drawable icon = payloadSlot.getItem().getIcon();

        Actor dragActor = new Image(icon);
        dragActor.setSize(16, 16);
        payload.setDragActor(dragActor);

        Actor validDragActor = new Image(icon);
        validDragActor.setSize(16, 16);
        validDragActor.setColor(0, 1, 0, 1);
        payload.setValidDragActor(validDragActor);

        Actor invalidDragActor = new Image(icon);
        invalidDragActor.setColor(1, 0, 0, 1);
        payload.setInvalidDragActor(invalidDragActor);

        return payload;
    }

    @Override
    public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target) {
        Slot payloadSlot = (Slot) payload.getObject();
        if (target != null) {
            Slot targetSlot = ((SlotActor) target.getActor()).getSlot();
            if (targetSlot.getItem() == payloadSlot.getItem() || targetSlot.getItem() == null) {
                targetSlot.add(payloadSlot.getItem(), payloadSlot.getAmount());
            } else {
                Item targetType = targetSlot.getItem();
                int targetAmount = targetSlot.getAmount();
                targetSlot.take(targetAmount);
                targetSlot.add(payloadSlot.getItem(), payloadSlot.getAmount());
                sourceSlot.add(targetType, targetAmount);
            }
        } else {
            sourceSlot.add(payloadSlot.getItem(), payloadSlot.getAmount());
        }
    }
}