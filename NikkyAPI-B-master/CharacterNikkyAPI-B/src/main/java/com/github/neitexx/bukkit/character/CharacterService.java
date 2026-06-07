package com.github.neitexx.bukkit.character;

import com.comphenix.protocol.ProtocolLibrary;
import com.github.neitexx.bukkit.character.api.Visibility;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by NeitExx on 17.08.2020
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CharacterService {

    @Getter private final static CharacterService service = new CharacterService();
    private final CharacterRepository repository = new CharacterRepository();

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public void initialize(@NotNull final JavaPlugin javaPlugin){
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onJoin(PlayerJoinEvent event){
                CharacterService.getService().sendAll(event.getPlayer());
            }

            @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
            public void onTeleport(PlayerTeleportEvent event){
                Bukkit.getScheduler().runTaskLater(javaPlugin, ()-> CharacterService.getService().sendAll(event.getPlayer()), 20);
            }
        }, javaPlugin);

        ProtocolLibrary.getProtocolManager().addPacketListener(new CharacterPacketListener(javaPlugin));

        Bukkit.getScheduler().runTaskTimerAsynchronously(javaPlugin, () -> Bukkit.getOnlinePlayers().forEach(player -> CharacterService.getService().sendAll(player)), 0, 20*30);
    }

    public Character get(final int id) {
        lock.readLock().lock();
        try {
            return this.repository.get(id);
        } finally {
            lock.readLock().unlock();
        }
    }

    public Character register(@NotNull final Character character){
        lock.writeLock().lock();
        try {
            this.repository.put(character);
            return character;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void unregister(final int id){
        lock.writeLock().lock();
        try {
            this.repository.remove(id);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void sendAll(@NotNull final Player player) {
        lock.readLock().lock();
        try {
            this.repository.stream()
                    .filter(character -> {
                        val npcLocation = character.getLocation();
                        return npcLocation != null && npcLocation.getWorld() != null && player.getWorld().getName().equalsIgnoreCase(npcLocation.getWorld().getName());
                    })
                    .filter(character -> !(character instanceof Visibility) || ((Visibility) character).isVisible(player))
                    .forEach(character -> character.send(player));
        } finally {
            lock.readLock().unlock();
        }
    }
}
