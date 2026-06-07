package com.github.neitexx.bukkit.character.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.*;
import com.github.neitexx.bukkit.character.Character;
import lombok.val;
import me.anfanik.steda.api.utility.TextUtility;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

/**
 * Created by NeitExx on 24.10.2021
 */

class CharacterInfoPacket extends PacketContainer implements CharacterPacket {

    CharacterInfoPacket(@NotNull final Character character){
        super(PacketType.Play.Server.PLAYER_INFO);
        val externalData = character.getExternalData();

        val name = TextUtility.colorize(externalData.name());

        getModifier().writeDefaults();
        getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.ADD_PLAYER);

        val profile = new WrappedGameProfile(character.getUuid(), name);
        profile.getProperties().put("textures", new WrappedSignedProperty("textures", externalData.texture(), externalData.signature()));

        getPlayerInfoDataLists().write(0, Collections.singletonList(
                new PlayerInfoData(profile, 0,
                        EnumWrappers.NativeGameMode.CREATIVE,
                        WrappedChatComponent.fromText(name))));
    }

    @Override
    public void send(Player player) {
        this.send(player, this);
    }

}
