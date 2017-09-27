package com.desenvolvedorindie.platformer.entity.component;

import com.artemis.Component;
import com.desenvolvedorindie.platformer.inventory.IInventory;
import com.desenvolvedorindie.platformer.inventory.Inventory;

public class PlayerComponent extends Component {

    public IInventory inventory = new Inventory(null, 9);

    public boolean alreadyFalling;

    public boolean canWalk = true;

    public float walkSpeed = 100;

    public boolean godMode;

    //KEYS


}
