package com.desenvolvedorindie.platformer.entity.component;

import com.artemis.Component;

public class JumpComponent extends Component {

    public boolean canJump = true;

    public float jumpSpeed = 300;

    public float minJumpSpeed = 100;

    public boolean wallJump = false;

    public int jumps = 0;

    public int maxJumps = 1;

}
