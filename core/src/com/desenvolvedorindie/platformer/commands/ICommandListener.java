package com.desenvolvedorindie.platformer.commands;

public interface ICommandListener {

    void notifyListener(ICommandSender sender, ICommand command, int flags, String translationKey, Object... translationArgs);

}
