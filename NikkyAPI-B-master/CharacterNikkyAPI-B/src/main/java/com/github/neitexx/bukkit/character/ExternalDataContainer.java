package com.github.neitexx.bukkit.character;

import com.github.neitexx.bukkit.character.annotation.ExternalCharacterData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class ExternalDataContainer {

    @Getter
    @Setter
    @Accessors(fluent = true)
    private String name, texture, signature;

    public ExternalDataContainer(@NotNull final ExternalCharacterData data) {
        this.name = data.name();
        this.texture = data.texture();
        this.signature = data.signature();
    }

}
