package com.github.NeitExx.bukkit.configuration.api.repository;

import com.github.NeitExx.bukkit.configuration.api.DefaultConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

public interface ConfigurationRepository {

    void setJavaPlugin(@NotNull JavaPlugin javaPlugin);

    boolean isDataFolderNull();

    <T extends DefaultConfiguration> Optional<T> findById(@NotNull String id);

    <T extends DefaultConfiguration> Optional<T> findByClass(@NotNull Class<T> tClass);

    Collection<DefaultConfiguration> findAll();

    void save(@NotNull String key);

    void save(@NotNull Class<? extends DefaultConfiguration> tClass);

    void saveAll();

    boolean append(@NotNull Class<? extends DefaultConfiguration> configurationClass);

    void invalidate(@NotNull String key);

    void invalidate(@NotNull Class<? extends DefaultConfiguration> tClass);

    void invalidateAll();

}
