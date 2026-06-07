package cupcake.nikky.imperium.tower.bukkit;

import cupcake.nikky.imperium.tower.chat.ChatType;
import cupcake.nikky.imperium.tower.entity.item.FantasyEntityItem;
import cupcake.nikky.imperium.tower.sound.AdvancedSounds;
import lombok.val;
import me.anfanik.steda.api.utility.TextUtility;
import me.func.mod.ui.MultiChat;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftItem;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.function.Consumer;

public final class BukkitProvider {

    public CraftItem spawnItem(@NotNull final Location location, @Nullable final UUID targetUuid, @NotNull final ItemStack itemStack) {
        val world = (CraftWorld) location.getWorld();
        val worldServer = world.getHandle();

        val entityItem = new FantasyEntityItem(worldServer, location.getX(), location.getY(), location.getZ(), targetUuid, CraftItemStack.asNMSCopy(itemStack));
        entityItem.pickupDelay = 5;

        return (CraftItem) this.spawn(location, entityItem, null);
    }

    public CraftItem spawnItem(@NotNull final Location location, @NotNull final ItemStack itemStack) {
        return this.spawnItem(location, null, itemStack);
    }

    public org.bukkit.entity.Entity spawn(@NotNull final Location location, @NotNull final EntityType entityType, @Nullable final Consumer<Entity> handler) {
        val craftWorld = (CraftWorld) location.getWorld();
        Entity entity = craftWorld.createEntity(location, entityType.getEntityClass());
        return this.spawn(location, entity, handler);
    }

    public org.bukkit.entity.Entity spawn(@NotNull final Location location, @NotNull final Entity entity, @Nullable final Consumer<Entity> handler) {
        val world = (CraftWorld) location.getWorld();
        val worldServer = world.getHandle();

        if (entity instanceof EntityInsentient entityInsentient)
            entityInsentient.prepare(worldServer.D(new BlockPosition(entity)), null);

        if (handler != null) handler.accept(entity);

        worldServer.addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return entity.getBukkitEntity();
    }

    public void sound(@NotNull final Player player, @NotNull final Sound sound) {
        player.playSound(player.getLocation(), sound, 1F, 1F);
    }

    public void sound(@NotNull final Player player, @NotNull final Location location, @NotNull final Sound sound) {
        player.playSound(location, sound, 1F, 1F);
    }

    public void sound(@NotNull final Location location, @NotNull final Sound sound) {
        location.getWorld().playSound(location, sound, 1F, 1F);
    }

    public void sound(@NotNull final Player player, @NotNull final String sound) {
        player.playSound(player.getLocation(), sound, 1F, 1F);
    }

    public void sound(@NotNull final Player player, @NotNull final AdvancedSounds advancedSounds) {
        player.playSound(player.getLocation(), advancedSounds.getSoundName(), advancedSounds.getCategory(), 1F, 1F);
    }

    public void sound(@NotNull final Location location, @NotNull final AdvancedSounds advancedSounds) {
        location.getWorld().playSound(location, advancedSounds.getSoundName(), advancedSounds.getCategory(), 1F, 1F);
    }

    public void sound(@NotNull final Player player, @NotNull final Location location, @NotNull final AdvancedSounds advancedSounds) {
        player.playSound(location, advancedSounds.getSoundName(), advancedSounds.getCategory(), 1F, 1F);
    }

    public void sound(@NotNull final Player player, @NotNull final Location location, @NotNull final String sound) {
        player.playSound(location, sound, 1F, 1F);
    }

    public void sound(@NotNull final Location location, @NotNull final String sound) {
        location.getWorld().playSound(location, sound, 1F, 1F);
    }

    public void sendMessage(@NotNull final Player player, @NotNull final ChatType chatType, @NotNull final String... message) {
        for (String s : message) {
            if (chatType != ChatType.NONE)
                MultiChat.sendMessage(player, chatType.name().toLowerCase(), TextUtility.colorize(chatType.getPrefix() + " " + s));

            val chatPrefix = chatType.getPrefix();
            player.sendMessage(TextUtility.colorize(chatPrefix + (chatPrefix.isEmpty() ? "" : " ") + s));
        }
    }

    public void sendMessage(@NotNull final ChatType chatType, @NotNull final String message, @NotNull final Player... players) {
        this.sendMessage(chatType, message, Arrays.asList(players));
    }

    public void sendMessage(@NotNull final ChatType chatType, @NotNull final String message, @NotNull final Collection<? extends Player> players) {
        val chatPrefix = chatType.getPrefix();
        val line = TextUtility.colorize(chatPrefix + (chatPrefix.isEmpty() ? "" : " ") + message);

        if (chatType != ChatType.NONE)
            MultiChat.broadcast(players, chatType.name().toLowerCase(), line);

        for (Player player : players) {
            player.sendMessage(line);
        }
    }

    public void sendMessageToExactChat(@NotNull final ChatType chatType, @NotNull final String message, @NotNull final Collection<? extends Player> players) {
        val chatPrefix = chatType.getPrefix();
        val line = TextUtility.colorize(chatPrefix + (chatPrefix.isEmpty() ? "" : " ") + message);

        if (chatType != ChatType.NONE) MultiChat.broadcast(players, chatType.name().toLowerCase(), line);
    }

    public void broadCast(@NotNull final ChatType chatType, @NotNull final String... messages) {
        if (chatType == ChatType.NONE) return;

        val chatPrefix = chatType.getPrefix();
        for (String message : messages) {
            MultiChat.broadcast(chatType.name().toLowerCase(), TextUtility.colorize(chatPrefix + (chatPrefix.isEmpty() ? "" : " ") + message));
        }
    }

}
