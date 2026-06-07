package com.github.neitexx.bukkit;

import com.github.neitexx.bukkit.character.CharacterService;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by NeitExx on 22.05.2021
 */

public class BootstrapCharacter extends JavaPlugin {

    @Override
    public void onEnable() {
        CharacterService.getService().initialize(this);
    }

    @Override
    public void onDisable() {

    }

}
