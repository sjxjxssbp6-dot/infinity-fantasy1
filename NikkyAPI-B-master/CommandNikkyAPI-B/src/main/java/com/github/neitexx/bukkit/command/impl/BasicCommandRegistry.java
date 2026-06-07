package com.github.neitexx.bukkit.command.impl;

import com.github.neitexx.bukkit.command.api.CommandRegister;
import com.github.neitexx.bukkit.command.api.ICommand;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * Created by NeitExx on 16.04.2021
 */

@RequiredArgsConstructor
public class BasicCommandRegistry implements CommandRegister {

    @Getter @NotNull
    private final ICommand command;

}
