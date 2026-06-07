package com.github.NeitExx.bukkit.configuration.api;

import com.github.NeitExx.bukkit.configuration.api.reflect.FieldHandler;
import com.github.NeitExx.bukkit.configuration.api.reflect.ReflectHandler;

import java.io.File;

public interface Configuration {

    File getFile();

    ReflectHandler getReflectHandler();

    FieldHandler getFieldHandler();

    String getConfigurationName();

    default String getDirectory() {
        return "";
    }

    default PathToDataFolder getDataFolder() {
        return PathToDataFolder.defaultPath();
    }

}
