package com.github.neitexx.bukkit.character.api;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Created by NeitExx on 27.05.2021
 */

public interface Interacting {

    void rightClick(@NotNull final Player player);

    void leftClick(@NotNull final Player player);

}
