package com.github.neitexx.bukkit.character.packet;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import lombok.val;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by NeitExx on 24.10.2021
 */

interface CharacterPacket {

    void send(Player player);

    default void send(Player player, PacketContainer packetContainer) {
        val protocolManager = ProtocolLibrary.getProtocolManager();

        try {
            protocolManager.sendServerPacket(player, packetContainer);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
