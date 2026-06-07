package com.github.neitexx.bukkit.command.api;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Created by NeitExx on 16.04.2021
 */

public interface MessageError {

    String getMessage();

    default void send(@NotNull final CommandSender sender){
        sender.sendMessage(getMessage());
    }

}
