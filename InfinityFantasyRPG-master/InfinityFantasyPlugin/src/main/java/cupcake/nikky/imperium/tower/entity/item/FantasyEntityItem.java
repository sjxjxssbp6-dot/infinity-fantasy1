package cupcake.nikky.imperium.tower.entity.item;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.client.protocol.cursor.CursorPromptData;
import lombok.val;
import me.anfanik.steda.api.utility.TextUtility;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FantasyEntityItem extends EntityItem {

    private final Map<UUID, Integer> humanPickupDelayData = new HashMap<>();
    private final UUID targetUuid;

    public FantasyEntityItem(World world, double d0, double d1, double d2) {
        super(world, d0, d1, d2);
        this.targetUuid = null;
    }

    public FantasyEntityItem(World world, double d0, double d1, double d2, ItemStack itemstack) {
        super(world, d0, d1, d2, itemstack);
        this.targetUuid = null;
    }

    public FantasyEntityItem(World world, double d0, double d1, double d2, UUID targetUuid, ItemStack itemstack) {
        super(world, d0, d1, d2, itemstack);
        this.targetUuid = targetUuid;
    }

    public FantasyEntityItem(World world) {
        super(world);
        this.targetUuid = null;
    }

    @Override
    public void die() {
        super.die();
        humanPickupDelayData.clear();
    }

    @Override
    public void d(EntityHuman entityhuman) {
        if (targetUuid != null && !entityhuman.getUniqueID().equals(targetUuid)) return;

        val entityPickupDelay = humanPickupDelayData.getOrDefault(entityhuman.getUniqueID(), 0);
        if (entityPickupDelay - ticksLived > 0) return;
        humanPickupDelayData.put(entityhuman.getUniqueID(), ticksLived + 20);

        ItemStack itemStack = this.getItemStack();
        int i = itemStack.getCount();
        Item item = itemStack.getItem();
        int canHold = entityhuman.inventory.canHold(itemStack);
        int remaining = i - canHold;

        boolean flyAtPlayer = false;

        if (this.pickupDelay <= 0) {
            PlayerAttemptPickupItemEvent attemptEvent = new PlayerAttemptPickupItemEvent((Player)entityhuman.getBukkitEntity(), (org.bukkit.entity.Item)this.getBukkitEntity(), remaining);
            this.world.getServer().getPluginManager().callEvent(attemptEvent);
            flyAtPlayer = attemptEvent.getFlyAtPlayer();
            if (attemptEvent.isCancelled()) {
                if (flyAtPlayer) {
                    entityhuman.receive(this, i);
                }
                return;
            }
        }

        if (this.pickupDelay <= 0 && canHold > 0) {
            itemStack.setCount(canHold);
            PlayerPickupItemEvent playerEvent = new PlayerPickupItemEvent((Player)entityhuman.getBukkitEntity(), (org.bukkit.entity.Item)this.getBukkitEntity(), remaining);
            playerEvent.setCancelled(!entityhuman.canPickUpLoot);
            this.world.getServer().getPluginManager().callEvent(playerEvent);
            flyAtPlayer = playerEvent.getFlyAtPlayer();
            if (playerEvent.isCancelled()) {
                if (flyAtPlayer) {
                    entityhuman.receive(this, i);
                }

                return;
            }

            itemStack.setCount(canHold + remaining);
            this.pickupDelay = 0;
            val data = new CursorPromptData(TextUtility.colorize(itemStack.getName() + " &fX &6" + canHold), "");
            Fantasy.clientProvider().cursorNotify((Player) entityhuman.getBukkitEntity(), data);
        }

        if (this.pickupDelay == 0 && entityhuman.inventory.pickup(itemStack)) {
            if (flyAtPlayer) {
                entityhuman.receive(this, i);
            }

            if (itemStack.isEmpty()) {
                this.die();
                itemStack.setCount(i);
            }
        }
    }

    @Override
    public boolean tryPutInHopper() {
        return false;
    }
}
