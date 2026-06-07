package com.github.neitexx.bukkit.character;

import com.github.neitexx.bukkit.character.annotation.ExternalCharacterData;
import com.github.neitexx.bukkit.character.packet.DefaultCharacterPackets;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.anfanik.steda.api.utility.MathUtility;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.stream.Stream;

/**
 * Created by NeitExx on 16.08.2020
 */

@RequiredArgsConstructor
public abstract class Character {

    @Getter private final int id = MathUtility.randomInRange(1, 1_000_000);
    @Getter private final UUID uuid = UUID.randomUUID();

    @Getter private ExternalDataContainer externalData = new ExternalDataContainer("&7Unknown", "", "");
    private DefaultCharacterPackets defaultCharacterPackets;

    @Getter private Location location;

    public void initialize(@NotNull final Location location){
        this.externalData = Stream.of(this.getClass().getAnnotations())
                .filter(annotation -> annotation instanceof ExternalCharacterData)
                .findFirst().map(annotation -> (ExternalCharacterData) annotation).map(ExternalDataContainer::new)
                .orElse(externalData);
        this.location = location;

        this.defaultCharacterPackets = new DefaultCharacterPackets(this); // need location and external data
    }

    public void send(Player player){
        this.defaultCharacterPackets.sendAll(player);
    }

}
