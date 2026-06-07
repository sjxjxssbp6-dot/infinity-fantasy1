package com.github.NeitExx.bukkit.configuration.api.reflect;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.function.Consumer;

public class DefaultReflectHandler implements ReflectHandler {

    @Override
    public void setFieldValue(@NotNull final Object object, @NotNull final Field field, @Nullable final Object value) {
        try {
            field.set(object, value);
        } catch (IllegalAccessException e){
            e.printStackTrace();
        }
    }

    @Override
    public Object getFieldValue(@NotNull final Object object, @NotNull final Field field) {
        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T extends AccessibleObject> void executeAccessible(@NotNull final Consumer<T> execute, @NotNull final T object) {
        object.setAccessible(true);
        execute.accept(object);
        object.setAccessible(false);
    }

}
