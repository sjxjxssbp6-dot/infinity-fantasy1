package com.github.NeitExx.bukkit.configuration.api.reflect;

import com.github.NeitExx.bukkit.configuration.api.DefaultConfiguration;
import com.github.NeitExx.bukkit.configuration.api.annotation.ConfigurationPath;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

@Getter
@RequiredArgsConstructor
public class FieldWithPathHandler implements FieldHandler {

    private final DefaultConfiguration configuration;

    @Override
    public void read(@NotNull final Field field) {
        val path = field.getAnnotation(ConfigurationPath.class).path();
        val reflectHandler = getConfiguration().getReflectHandler();

        reflectHandler.executeAccessible(accessibleObject -> {
            getConfiguration().set(path, reflectHandler.getFieldValue(getConfiguration(), accessibleObject));
        }, field);
    }

    @Override
    public void write(@NotNull final Field field) {
        val path = field.getAnnotation(ConfigurationPath.class).path();
        val reflectHandler = getConfiguration().getReflectHandler();

        reflectHandler.executeAccessible(accessibleObject -> {
            reflectHandler.setFieldValue(getConfiguration(), accessibleObject, getConfiguration().get(path));
        }, field);
    }

}
