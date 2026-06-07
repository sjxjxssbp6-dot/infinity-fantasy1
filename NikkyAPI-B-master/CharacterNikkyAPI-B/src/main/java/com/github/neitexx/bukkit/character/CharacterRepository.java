package com.github.neitexx.bukkit.character;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by NeitExx on 24.10.2021
 */

public class CharacterRepository {

    private final Map<Integer, Character> data = new HashMap<>();

    public void put(@NotNull final Character character){
        this.data.put(character.getId(), character);
    }

    public Character get(final int id){
        return this.data.get(id);
    }

    public void remove(final int id){
        this.data.remove(id);
    }

    public Stream<Character> stream(){
        return data.values().stream();
    }

}
