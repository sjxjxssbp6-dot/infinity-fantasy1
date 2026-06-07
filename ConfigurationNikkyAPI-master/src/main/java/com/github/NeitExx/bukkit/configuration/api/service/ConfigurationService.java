package com.github.NeitExx.bukkit.configuration.api.service;

import com.github.NeitExx.bukkit.configuration.api.DefaultConfiguration;
import com.github.NeitExx.bukkit.configuration.api.repository.ConfigurationRepository;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface ConfigurationService {

    ConfigurationService initialize(@NotNull JavaPlugin javaPlugin);

    ConfigurationService initialize(@NotNull JavaPlugin javaPlugin, @Nullable ConfigurationRepository configurationRepository);

    <T extends DefaultConfiguration> Optional<T> findById(@NotNull String id);

    <T extends DefaultConfiguration> Optional<T> findByClass(@NotNull Class<T> tClass);

    ConfigurationService register(@NotNull Class<? extends DefaultConfiguration> configurationClass);

    ConfigurationService unregister(@NotNull String... keys);

    ConfigurationService unregisterAll();

}
