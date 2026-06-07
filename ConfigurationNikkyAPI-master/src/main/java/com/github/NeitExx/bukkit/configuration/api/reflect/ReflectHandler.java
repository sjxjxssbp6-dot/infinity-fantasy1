package com.github.NeitExx.bukkit.configuration.api.reflect;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.function.Consumer;

public interface ReflectHandler {

    void setFieldValue(@NotNull Object object, @NotNull Field field, @Nullable Object value);

    Object getFieldValue(@NotNull Object object, @NotNull Field field);

    <T extends AccessibleObject> void executeAccessible(@NotNull Consumer<T> execute, @NotNull T object);

}
