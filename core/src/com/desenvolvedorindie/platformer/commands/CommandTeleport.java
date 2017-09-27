package com.desenvolvedorindie.platformer.commands;

import com.desenvolvedorindie.platformer.world.World;

import java.util.List;

public class CommandTeleport extends Command {

    @Override
    public String getCommandName() {
        return "teleport";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public void execute(World world, ICommandSender sender, String[] args) throws CommandException {

    }

    @Override
    public boolean checkPermission(World world, ICommandSender sender) {
        return false;
    }

    @Override
    public List<String> getTabCompletionOptions(World world, ICommandSender sender, String[] args) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

}
