package com.github.NeitExx.bukkit.configuration.api;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.function.Function;

public interface PathToDataFolder {

    static PathToDataFolder defaultPath() {
        return JavaPlugin::getDataFolder;
    }

    static PathToDataFolder of(Function<JavaPlugin, File> accepter) {
        return accepter::apply;
    }

    File acceptByPlugin(JavaPlugin javaPlugin);

}
