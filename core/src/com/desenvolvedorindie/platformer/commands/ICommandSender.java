package com.desenvolvedorindie.platformer.commands;

import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.desenvolvedorindie.platformer.block.BlockPos;
import com.desenvolvedorindie.platformer.world.World;

public interface ICommandSender {

    String getName();

    boolean canCommandSenderUseCommand(int permLevel, String commandName);

    BlockPos getPosition();

    Vector2 getPositionVector();

    World getEntityWorld();

    Entity getCommandSenderEntity();

    boolean sendCommandFeedback();

}
