package com.desenvolvedorindie.platformer.commands;

import com.desenvolvedorindie.platformer.world.World;

import java.util.List;

public interface ICommand {

    String getCommandName();

    String getCommandUsage(ICommandSender sender);

    void execute(World world, ICommandSender sender, String[] args) throws CommandException;

    boolean checkPermission(World world, ICommandSender sender);

    List<String> getTabCompletionOptions(World world, ICommandSender sender, String[] args);

    boolean isUsernameIndex(String[] args, int index);

}
