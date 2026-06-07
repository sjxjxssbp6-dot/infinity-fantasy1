package com.github.neitexx.bukkit.character.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by NeitExx on 27.05.2021
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ExternalCharacterData {

    String name();

    String texture() default "";

    String signature() default "";

}
