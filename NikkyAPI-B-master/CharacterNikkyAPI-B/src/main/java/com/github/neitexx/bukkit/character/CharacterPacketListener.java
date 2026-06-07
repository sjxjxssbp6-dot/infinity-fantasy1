package com.github.neitexx.bukkit.character;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.github.neitexx.bukkit.character.api.Interacting;
import com.github.neitexx.bukkit.character.event.CharacterInteractEvent;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * Created by NeitExx on 27.05.2021
 */

class CharacterPacketListener extends PacketAdapter {

    public CharacterPacketListener(@NotNull final JavaPlugin plugin) {
        super(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.USE_ENTITY);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        if (event.getPacketType() != PacketType.Play.Client.USE_ENTITY) return;

        val player = event.getPlayer();

        val packet = event.getPacket();
        val id = packet.getIntegers().read(0);

        val character = CharacterService.getService().get(id);
        if (character == null) return;

        val action = event.getPacket().getEntityUseActions().read(0);
        if (action != EnumWrappers.EntityUseAction.INTERACT_AT) return;

        if(character instanceof Interacting) {
            val characterInteract = (Interacting) character;

            val hand = event.getPacket().getHands().read(0);

            if(hand == EnumWrappers.Hand.MAIN_HAND) {
                Bukkit.getScheduler().runTask(plugin, () -> {
                    val interactEvent = new CharacterInteractEvent(player, character, hand);
                    Bukkit.getPluginManager().callEvent(interactEvent);
                    if (interactEvent.isCancelled()) return;

                    characterInteract.rightClick(player);
                });
            } else if(hand == EnumWrappers.Hand.OFF_HAND){
                Bukkit.getScheduler().runTask(plugin, () -> {
                    val interactEvent = new CharacterInteractEvent(player, character, hand);
                    Bukkit.getPluginManager().callEvent(interactEvent);
                    if (interactEvent.isCancelled()) return;

                    characterInteract.leftClick(player);
                });
            }
        }
    }

}
