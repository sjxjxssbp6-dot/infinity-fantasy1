package com.github.NeitExx.bukkit.configuration.api;

import lombok.RequiredArgsConstructor;
import lombok.val;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RequiredArgsConstructor
public class FileHandler {

    private final File dataFolder;
    private final Configuration configuration;

    public File generate() throws IOException {
        val directory = new File(dataFolder, configuration.getDirectory());
        val file = new File(directory, configuration.getConfigurationName());

        if (directory.exists() && file.exists()) return file;

        if (!directory.exists() && directory.isDirectory()) Files.createDirectory(directory.toPath());
        if (!file.exists()) Files.createFile(file.toPath());

        return file;
    }

}
