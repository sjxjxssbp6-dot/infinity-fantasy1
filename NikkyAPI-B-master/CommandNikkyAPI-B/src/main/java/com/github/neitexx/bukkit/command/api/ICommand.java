package com.github.neitexx.bukkit.command.api;

import lombok.val;
import me.anfanik.steda.api.access.AccessCheck;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * Created by NeitExx on 15.04.2021
 */

public interface ICommand extends CommandExecutor {

    default ICommand initializeChild(){
        getChildCommands().forEach(command -> {
            command.setChild((byte) (getChild() + 1));
            command.initializeChild();
        });
        return this;
    }

    void setChild(final byte child);

    byte getChild();

    Collection<String> getCommandNames();

    Collection<ICommand> getChildCommands();

    BiFunction<CommandSender, String[], Optional<MessageError>> getExecutor();

    Collection<AccessCheck<CommandSender>> getAccessChecks();

    boolean checkAccess(@NotNull final CommandSender commandSender);

    @Override
    default boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings){
        if(this.checkAccess(commandSender)){
            val message = execute(commandSender, strings);
            message.ifPresent(messageError -> messageError.send(commandSender));
        }
        return true;
    }

    Optional<MessageError> execute(@NotNull final CommandSender sender, @NotNull final String... args);

}
