package com.github.neitexx.bukkit.command.impl;

import com.github.neitexx.bukkit.command.api.ICommand;
import com.github.neitexx.bukkit.command.api.MessageError;
import lombok.*;
import me.anfanik.steda.api.access.AccessCheck;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * Created by NeitExx on 16.04.2021
 */

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class BasicCommand implements ICommand {

    private final BiFunction<CommandSender, String[], Optional<MessageError>> executor;

    private final Collection<AccessCheck<CommandSender>> accessChecks;
    private final Collection<ICommand> childCommands;

    private final Collection<String> commandNames;

    private byte child;

    @Override
    public boolean checkAccess(@NotNull final CommandSender commandSender) {
        for (AccessCheck<CommandSender> accessCheck : accessChecks) {
            if(!accessCheck.checkAccess(commandSender)) {
                commandSender.sendMessage(accessCheck.getErrorMessage(commandSender));
                return false;
            }
        }
        return true;
    }

    public Optional<ICommand> getChildCommand(@NotNull final String name){
        for (ICommand childCommand : childCommands) {
            for (String commandName : childCommand.getCommandNames()) {
                if(name.equalsIgnoreCase(commandName)) return Optional.of(childCommand);
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<MessageError> execute(@NotNull final CommandSender sender, @NotNull final String... args) {
        if(args.length > (child + 1)){
            val childCommand = getChildCommand(args[this.child + 1]).orElse(null);

            if(childCommand != null && childCommand.checkAccess(sender)) {
                return childCommand.execute(sender, args);
            }
        }

        return executor.apply(sender, args);
    }

}
