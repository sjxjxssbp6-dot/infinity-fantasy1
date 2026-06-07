package com.github.neitexx.bukkit.command.api;

import com.github.neitexx.bukkit.command.exception.NullCommandException;
import lombok.val;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * Created by NeitExx on 16.04.2021
 */

public interface CommandRegister {

    ICommand getCommand();

    default void register(@NotNull final JavaPlugin plugin) {
        for (String commandName : getCommand().getCommandNames()) {
            val bukkitCommand = plugin.getCommand(commandName);
            if(bukkitCommand == null) throw new NullCommandException();

            bukkitCommand.setExecutor(getCommand());
        }
    }

}
