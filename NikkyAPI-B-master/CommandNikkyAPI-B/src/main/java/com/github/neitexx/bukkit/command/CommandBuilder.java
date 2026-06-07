package com.github.neitexx.bukkit.command;

import com.github.neitexx.bukkit.command.api.CommandRegister;
import com.github.neitexx.bukkit.command.api.ICommand;
import com.github.neitexx.bukkit.command.api.MessageError;
import com.github.neitexx.bukkit.command.impl.BasicCommand;
import com.github.neitexx.bukkit.command.impl.BasicCommandRegistry;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import me.anfanik.steda.api.access.AccessCheck;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiFunction;

/**
 * Created by NeitExx on 15.04.2021
 */

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommandBuilder {

    private final List<String> commandNames = new ArrayList<>();
    private final List<AccessCheck<CommandSender>> accessChecks = new ArrayList<>();
    private final List<ICommand> childCommands = new ArrayList<>();

    private BiFunction<CommandSender, String[], Optional<MessageError>> executor;

    public static CommandBuilder get(){
        return new CommandBuilder();
    }

    public CommandBuilder addCommandName(@NotNull final String... commandNames){
        Collections.addAll(this.commandNames, commandNames);
        return this;
    }

    public CommandBuilder addChildCommand(@NotNull final ICommand... childCommands){
        return this.addChildCommand(Arrays.asList(childCommands));
    }

    public CommandBuilder addChildCommand(@NotNull final Collection<ICommand> childCommands){
        this.childCommands.addAll(childCommands);
        return this;
    }

    public final CommandBuilder addAccessCheck(@NotNull final Collection<AccessCheck<CommandSender>> accessChecks){
        this.accessChecks.addAll(accessChecks);
        return this;
    }

    @SafeVarargs
    public final CommandBuilder addAccessCheck(@NotNull final AccessCheck<CommandSender>... accessChecks){
        return this.addAccessCheck(Arrays.asList(accessChecks));
    }

    public CommandBuilder setExecutor(@NotNull final BiFunction<CommandSender, String[], Optional<MessageError>> executor){
        this.executor = executor;
        return this;
    }

    public CommandRegister build(){
        val basicCommand = new BasicCommand(executor, accessChecks, childCommands, commandNames, (byte) -1).initializeChild();
        return new BasicCommandRegistry(basicCommand);
    }

    public ICommand buildCommand(){
        return new BasicCommand(executor, accessChecks, childCommands, commandNames, (byte) -1).initializeChild();
    }

}
