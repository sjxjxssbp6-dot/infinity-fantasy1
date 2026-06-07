package com.github.NeitExx.bukkit.configuration.api;

import com.github.NeitExx.bukkit.configuration.api.annotation.ConfigurationPath;
import lombok.*;
import com.github.NeitExx.bukkit.configuration.api.reflect.DefaultReflectHandler;
import com.github.NeitExx.bukkit.configuration.api.reflect.FieldHandler;
import com.github.NeitExx.bukkit.configuration.api.reflect.FieldWithPathHandler;
import com.github.NeitExx.bukkit.configuration.api.reflect.ReflectHandler;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public abstract class DefaultConfiguration extends YamlConfiguration implements Configuration {

    @Getter private File file;
    @Getter @Setter(AccessLevel.PROTECTED) private ReflectHandler reflectHandler;
    @Getter @Setter(AccessLevel.PROTECTED) private FieldHandler fieldHandler;

    public DefaultConfiguration initialize(@NotNull final JavaPlugin javaPlugin) {
        if(getFile() != null) throw new RuntimeException(String.format("Configuration <%s> already initialized", this.getClass().getSimpleName()));

        if (getReflectHandler() == null) this.reflectHandler = new DefaultReflectHandler();
        if (getFieldHandler() == null) this.fieldHandler = new FieldWithPathHandler(this);

        this.createIfNotExist(javaPlugin);

        if(getFile() == null) throw new RuntimeException("Error in configuration initializing");

        this.handleFields(FieldExecuteType.WRITE);
        this.afterInitializing();

        return this;
    }

    public abstract void afterInitializing();

    private void createIfNotExist(@NotNull final JavaPlugin javaPlugin) {
        val handler = new FileHandler(getDataFolder().acceptByPlugin(javaPlugin), this);

        try {
            this.file = handler.generate();
            this.load(file);
        } catch (IOException | InvalidConfigurationException exception) {
            throw new RuntimeException(String.format("Some trouble with loading <%s> configuration class", this.getClass().getSimpleName()), exception);
        }
    }

    @SneakyThrows
    private void handleFields(@NotNull final FieldExecuteType executeType) {
        synchronized (this) {
            Arrays.stream(this.getClass().getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(ConfigurationPath.class))
                    .forEach(field -> {
                        switch (executeType) {
                            case WRITE: getFieldHandler().write(field);
                            case READ: getFieldHandler().read(field);
                        }
                    });
        }
    }

    public void save() {
        this.handleFields(FieldExecuteType.READ);

        try {
            this.save(getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
