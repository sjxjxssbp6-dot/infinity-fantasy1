package cupcake.nikky.imperium.tower.filter.listener;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.trade.player.TradeInventoryUpdate;
import cupcake.nikky.imperium.tower.trade.player.TradePlayerProvider;
import cupcake.nikky.imperium.tower.utility.NbtUtility;
import lombok.val;
import me.anfanik.steda.api.listener.DefaultListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ItemFilterListener implements DefaultListener {

    @EventHandler
    public void dropSpawn(@NotNull final ItemSpawnEvent event) {
        val item = event.getEntity();
        val perPlayer = NbtUtility.getTagCompound(item.getItemStack()).getString("uniquePerPlayer");
        val perSession = NbtUtility.getTagCompound(item.getItemStack()).getString("uniquePerSession");
        val incubatorItem = NbtUtility.getTagCompound(item.getItemStack()).getBoolean("incubator");

        item.setItemStack(NbtUtility.clearSessionTags(item.getItemStack()));

        if (incubatorItem) {
            item.setMetadata("incubator", new FixedMetadataValue(Fantasy.getJavaPlugin(), true));
        }

        if(perPlayer != null && !perPlayer.isBlank()) {
            item.setMetadata("uniquePerPlayer", new FixedMetadataValue(Fantasy.getJavaPlugin(), perPlayer));
        }

        if(perSession != null && !perSession.isBlank()) {
            item.setMetadata("uniquePerSession", new FixedMetadataValue(Fantasy.getJavaPlugin(), perSession));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryAddItem(@NotNull final PlayerPickupItemEvent event) {
        val trader = TradePlayerProvider.getInstance().findSession(event.getPlayer().getUniqueId());
        if (trader == null) return;

        Fantasy.userService().findById(event.getPlayer().getUniqueId())
                .ifPresent(user -> Fantasy.runLater(() -> user.sendPacket(new TradeInventoryUpdate()), 2));
    }

    @EventHandler(ignoreCancelled = true)
    public void onPickupDrop(@NotNull final PlayerAttemptPickupItemEvent event) {
        val item = event.getItem();
        val player = event.getPlayer();

        val incubator = item.getMetadata("incubator");
        item.removeMetadata("incubator", Fantasy.getJavaPlugin());
        if (incubator != null && !incubator.isEmpty() && incubator.getFirst().asBoolean()) {
            return;
        }

        val sessionMetadata = item.getMetadata("uniquePerSession");
        if (sessionMetadata == null) return;

        item.removeMetadata("uniquePerSession", Fantasy.getJavaPlugin());
        if (sessionMetadata.isEmpty()) return;

        val user = Fantasy.userService().findById(player.getUniqueId())
                .filter(user1 -> user1.isLoaded() && !user1.isInvalidated())
                .orElse(null);

        val itemStack = item.getItemStack();
        val imperiumItem = Fantasy.itemProvider().find(itemStack);

        if (user == null || imperiumItem == null) {
            if (imperiumItem == null) item.remove();
            event.setCancelled(true);
            event.setFlyAtPlayer(false);
            return;
        }

        val uuid = UUID.fromString(sessionMetadata.getFirst().asString());
        val acceptSession = user.getSession().getSessionUuid().equals(uuid);

        if (!acceptSession && !imperiumItem.getDropSettings().droppable()) {
            event.setCancelled(true);
            event.setFlyAtPlayer(false);
            return;
        }

        if (!acceptSession && item.getTicksLived() < 20 * 15) {
            event.setCancelled(true);
            event.setFlyAtPlayer(false);
            return;
        }

        if (imperiumItem.getDropSettings().droppable()) {
            val filter = user.findLootFilterSettings();
            val isValid = filter.getItemFilterHandler().isValidToRemove(imperiumItem);
            if (isValid) {
                event.setCancelled(true);
                event.setFlyAtPlayer(true);
            }

            filter.getItemFilterHandler().process(item, imperiumItem, itemStack.getAmount(), isValid);
        }
    }

}
