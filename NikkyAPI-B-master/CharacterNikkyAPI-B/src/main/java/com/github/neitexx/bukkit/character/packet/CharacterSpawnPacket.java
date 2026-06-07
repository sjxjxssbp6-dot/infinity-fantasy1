package com.github.neitexx.bukkit.character.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.github.neitexx.bukkit.character.Character;
import lombok.val;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


/**
 * Created by NeitExx on 24.10.2021
 */

class CharacterSpawnPacket extends PacketContainer implements CharacterPacket {

    CharacterSpawnPacket(@NotNull final Character character){
        super(PacketType.Play.Server.NAMED_ENTITY_SPAWN);
        val location = character.getLocation();

        getModifier().writeDefaults();

        getIntegers().write(0, character.getId());
        getUUIDs().write(0, character.getUuid());

        getDoubles().write(0, location.getX())
                .write(1, location.getY())
                .write(2, location.getZ());

        getBytes().write(0, (byte) (location.getYaw() * 256.0F / 360.0F))
                .write(1, (byte) (location.getPitch() * 256.0F / 360.0F));
    }

    @Override
    public void send(Player player) {
        this.send(player, this);
    }

}
