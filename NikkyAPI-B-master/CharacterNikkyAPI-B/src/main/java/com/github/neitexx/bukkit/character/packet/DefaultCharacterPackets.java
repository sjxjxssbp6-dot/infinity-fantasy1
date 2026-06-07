package com.github.neitexx.bukkit.character.packet;

import com.github.neitexx.bukkit.character.Character;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Created by NeitExx on 24.10.2021
 */

public class DefaultCharacterPackets {

    @Getter private final CharacterPacket characterInfoPacket, characterSpawnPacket;

    public DefaultCharacterPackets(@NotNull final Character character){
        this.characterInfoPacket = new CharacterInfoPacket(character);
        this.characterSpawnPacket = new CharacterSpawnPacket(character);
    }

    public void sendAll(@NotNull final Player player){
        this.getCharacterInfoPacket().send(player);
        this.getCharacterSpawnPacket().send(player);
    }

}
