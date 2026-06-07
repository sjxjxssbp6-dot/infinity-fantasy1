package cupcake.nikky.imperium.tower.bootstrap.exact.enable;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.bootstrap.BootstrapProcess;
import cupcake.nikky.imperium.tower.bootstrap.exact.enable.listener.ArmorClickListener;
import cupcake.nikky.imperium.tower.bootstrap.exact.enable.listener.ArmorInteractListener;
import cupcake.nikky.imperium.tower.character.CharacterProvider;
import cupcake.nikky.imperium.tower.character.event.CharacterInteractEvent;
import cupcake.nikky.imperium.tower.corpse.CorpseInteract;
import cupcake.nikky.imperium.tower.user.User;
import lombok.val;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class BukkitPacketListenerProcess implements BootstrapProcess {

    private final Cache<UUID, Boolean> delayInteract = CacheBuilder.newBuilder()
            .concurrencyLevel(4)
            .expireAfterWrite(200, TimeUnit.MILLISECONDS)
            .build();

    @Override
    public void invoke(@NotNull JavaPlugin javaPlugin) {
        Fantasy.protocolProvider().addOutboundListener((entityPlayer, packet) -> {
            if (packet instanceof PacketPlayOutSetSlot packetPlayOutSetSlot) {
                val slot = packetPlayOutSetSlot.b;
                if (slot >= 36 && slot <= 44) {
                    Fantasy.userService().findById(entityPlayer.getUniqueID()).ifPresent(user -> user.getFantasyInventory().setUpdateHeldItem(true));
                }
            } else if (packet instanceof PacketPlayOutHeldItemSlot) {
                Fantasy.userService().findById(entityPlayer.getUniqueID()).ifPresent(user -> user.getFantasyInventory().setUpdateHeldItem(true));
            } else if (packet instanceof PacketPlayOutEntityMetadata packetEntityMetadata) {
                val entityId = packetEntityMetadata.a;
                val entity = entityPlayer.world.world.getHandle().getEntity(entityId);
                if (entity == null) return false;

                val dataWatcher = packetEntityMetadata.b.stream().filter(item -> item.object.equals(Entity.aB)).findAny().orElse(null);
                if (dataWatcher == null) return false;

                val customNameDataWatcher = (DataWatcher.Item<String>) dataWatcher;

                Fantasy.monsterProvider().findByUUID(entity.getUniqueID()).ifPresent(monster -> customNameDataWatcher.b = monster.getNameForPlayer(entityPlayer.getUniqueID()));
            } else if (packet instanceof PacketPlayOutBlockChange packetPlayOutBlockChange) {
                var material = Material.byId.get(Block.REGISTRY_ID.getId(packetPlayOutBlockChange.block));

                val position = BlockPosition.fromLong(packetPlayOutBlockChange.positionKey);
                val blockLocation = new Location(MapServiceProcess.GAME_WORLD, position.getX(), position.getY(), position.getZ());

                return entityPlayer.getBukkitEntity().<User>getBungeePlayer().findPandoraSettings().findDataByLocation(blockLocation) != null && material == Material.AIR;
            }

            return false;
        });
        Fantasy.protocolProvider().addInboundListener((entityPlayer, packet) -> {
            if (packet instanceof PacketPlayInUseEntity packetUse) {
                val entity = packetUse.a(entityPlayer.world);
                if (entity instanceof EntityArmorStand) {
                    new CorpseInteract(entityPlayer.getBukkitEntity(), new Location(MapServiceProcess.GAME_WORLD, entity.getX(), entity.getY(), entity.getZ())).onCorpseInteract();
                    return true;
                }

                if (delayInteract.getIfPresent(entityPlayer.getUniqueID()) != null) return true;

                val character = CharacterProvider.getInstance().findById(packetUse.getEntityId());

                if (character != null) {
                    val player = entityPlayer.getBukkitEntity();
                    val user = player.<User>getBungeePlayer();

                    if (user.isInvalidated() || !user.isLoaded()) return true;

                    val interactEvent = new CharacterInteractEvent(entityPlayer.getBukkitEntity(), character);
                    Bukkit.getPluginManager().callEvent(interactEvent);

                    delayInteract.put(player.getUniqueId(), true);
                    if (interactEvent.isCancelled()) return true;

                    val interacting = character.getInteracting();
                    if (interacting != null) {
                        Fantasy.runLater(() -> {
                            player.closeInventory();
                            interacting.interact(player);
                        });
                        return true;
                    }
                }
            } else if (packet instanceof PacketPlayInBlockPlace packetPlayInBlockPlace) {
                if (delayInteract.getIfPresent(entityPlayer.getUniqueID()) != null) return true;
                return new ArmorInteractListener(entityPlayer).armorAirClick(packetPlayInBlockPlace);
            } else if (packet instanceof PacketPlayInUseItem packetPlayInUseItem) {
                if (delayInteract.getIfPresent(entityPlayer.getUniqueID()) != null) return true;
                return new ArmorInteractListener(entityPlayer).armorBlockClick(packetPlayInUseItem);
            } else if (packet instanceof PacketPlayInHeldItemSlot) {
                Fantasy.userService().findById(entityPlayer.getUniqueID()).ifPresent(user -> user.getFantasyInventory().setUpdateHeldItem(true));
            } else if (packet instanceof PacketPlayInWindowClick windowClickPacket) {
                val resultClick = new ArmorClickListener(entityPlayer).clickWindow(windowClickPacket);

                if (windowClickPacket.slot >= 36 && windowClickPacket.slot <= 44) {
                    Fantasy.userService().findById(entityPlayer.getUniqueID()).ifPresent(user -> user.getFantasyInventory().setUpdateHeldItem(true));
                } else if (windowClickPacket.slot >= 5 && windowClickPacket.slot <= 8) {
                    Fantasy.userService().findById(entityPlayer.getUniqueID()).ifPresent(user -> user.getFantasyInventory().setUpdateArmor(true));
                }

                return resultClick;
            }

            return false;
        });
    }

}
