package cupcake.nikky.imperium.tower.world;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.block.BlockProvider;
import cupcake.nikky.imperium.tower.box.world.CaseWorldProvider;
import cupcake.nikky.imperium.tower.storage.PlayerGlobalStorage;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.utility.PermissionUtility;
import lombok.val;
import me.anfanik.steda.api.listener.DefaultListener;
import net.diamondden.api.event.ChunkSaveEvent;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.event.world.WorldUnloadEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class GeneralListener implements DefaultListener {

    private static final Cache<UUID, Boolean> cancelledArmorEquip = CacheBuilder.newBuilder()
            .concurrencyLevel(2)
            .expireAfterWrite(200, TimeUnit.MILLISECONDS)
            .build();

    public static void appendCachedArmorAccess(@NotNull final UUID uuid, final boolean access) {
        cancelledArmorEquip.put(uuid, access);
    }

    @EventHandler
    public void onTeleport(EntityTeleportEvent event) {
        if (event.getEntityType() == EntityType.ENDERMAN) event.setCancelled(true);
    }

    @EventHandler
    public void onChunkLoad(ChunkSaveEvent event) {
        if (event.getWorld().getName().equals("stage-start")) event.setCancelled(true);
    }

    @EventHandler
    public void onWorldUnload(WorldUnloadEvent event) {
        if (event.getWorld().getName().equals("stage-start")) return;
        event.getWorld().getLivingEntities().stream().filter(entity -> !(entity instanceof Player) && !(entity instanceof ArmorStand)).forEach(Entity::remove);
    }

    @EventHandler
    public void onPhysics(BlockPhysicsEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onInteractBlocks(PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        event.setCancelled(true);
        if (PermissionUtility.isOperator(event.getPlayer()) && event.getPlayer().getGameMode() == GameMode.CREATIVE)
            event.setCancelled(false);
    }

    @EventHandler
    public void onBlockDamage(BlockDamageEvent event) {
        if (event.getInstaBreak()) {
            event.setInstaBreak(false);
            event.setCancelled(true);
        } else if (event.getBlock() != null && !event.getBlock().getType().name().endsWith("ORE")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        event.setCancelled(true);
        if (PermissionUtility.isOperator(event.getPlayer()) && event.getPlayer().getGameMode() == GameMode.CREATIVE)
            event.setCancelled(false);
    }

    @EventHandler
    public void onArmorDestroy(PlayerItemDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockUse(PlayerInteractEvent event) {
        val clickedBlock = event.getClickedBlock();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            event.setUseInteractedBlock(Event.Result.DENY);

            if (clickedBlock != null) {
                var location = clickedBlock.getLocation().toBlockLocation();
                location.setYaw(0F);
                location.setPitch(0F);

                var fantasyBlock = BlockProvider.getInstance().findAll().get(location);
                if (fantasyBlock == null) {
                    val pairedData = CaseWorldProvider.getInstance().getCases().get(location);
                    if (pairedData == null) return;

                    event.getPlayer().<User>getBungeePlayer().findCaseSettings().openCase(pairedData.getFirst());
                    event.setUseItemInHand(Event.Result.DENY);
                    return;
                }

                if (fantasyBlock == Material.ENDER_CHEST) {
                    new PlayerGlobalStorage(event.getPlayer().getBungeePlayer()).open();
                    event.setUseItemInHand(Event.Result.DENY);
                } else if (fantasyBlock == Material.WORKBENCH) {
                    event.getPlayer().<User>getBungeePlayer().findSyncCraftBenchSettings().openScreen();
                    event.setUseItemInHand(Event.Result.DENY);
                }
            }
        }
    }

    @EventHandler
    public void onBurn(EntityCombustEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onInteractArmor(PlayerInteractEvent event) {
        val cached = cancelledArmorEquip.getIfPresent(event.getPlayer().getUniqueId());
        if (cached != null && !cached) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onArmorDrag(InventoryDragEvent event) {
        val cached = cancelledArmorEquip.getIfPresent(event.getWhoClicked().getUniqueId());
        if (cached != null && !cached) event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onArmorEquip(InventoryClickEvent event) {
        val cached = cancelledArmorEquip.getIfPresent(event.getWhoClicked().getUniqueId());
        if (cached != null && !cached) event.setCancelled(true);
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onInteractAtEntity(PlayerInteractAtEntityEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onFeed(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onRegeneration(EntityRegainHealthEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onAirChange(EntityAirChangeEvent event) {
        event.setCancelled(true);
        event.setAmount(20);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        ((CraftPlayer) event.getPlayer()).getHandle().checkMove = false;
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof Item || event.getEntity() instanceof Player || event.getEntity() instanceof Projectile) return;

        if (!event.getEntity().getMetadata("mount").isEmpty() || !event.getEntity().getMetadata("fantasy_monster").isEmpty()) {
            event.getEntity().removeMetadata("mount", Fantasy.getJavaPlugin());
            event.getEntity().removeMetadata("fantasy_monster", Fantasy.getJavaPlugin());
            return;
        }
        event.setCancelled(true);
    }

}
