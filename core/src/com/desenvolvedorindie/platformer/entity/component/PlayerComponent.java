package com.desenvolvedorindie.platformer.entity.component;

import com.artemis.Component;
import com.desenvolvedorindie.platformer.inventory.IInventory;
import com.desenvolvedorindie.platformer.inventory.Inventory;

public class PlayerComponent extends Component {

    public Inventory inventory = new Inventory();

    public boolean alreadyFalling;

    public boolean canWalk = true;

    public float walkSpeed = 100;

    public boolean godMode;

    //KEYS


}
