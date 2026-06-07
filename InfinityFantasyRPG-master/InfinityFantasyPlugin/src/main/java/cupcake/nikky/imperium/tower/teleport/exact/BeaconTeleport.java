package cupcake.nikky.imperium.tower.teleport.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.beacon.BeaconObjects;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.TeleportIndicatorPacket;
import cupcake.nikky.imperium.tower.currency.CurrencyType;
import cupcake.nikky.imperium.tower.sound.AdvancedSounds;
import cupcake.nikky.imperium.tower.teleport.Teleport;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.user.metadata.MetadataType;
import lombok.RequiredArgsConstructor;
import lombok.val;
import me.anfanik.steda.api.utility.LocationUtility;
import me.func.mod.Anime;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

@RequiredArgsConstructor
public class BeaconTeleport implements Teleport {

    private final Player player;
    private final BeaconObjects targetBeacon;

    private BukkitTask locationTask;
    private BukkitTask teleportTask;

    @Override
    public void process(float seconds, int cost) {
        Fantasy.clientProvider().teleportIndicator(player, Math.round(seconds * 1_000));
        val location = player.getLocation();

        locationTask = Fantasy.runAsyncTimer(() -> {
            if (LocationUtility.isLocationChanged(location, player.getLocation(), false) || !player.hasMetadata("teleport_process")) {
                Fantasy.clientProvider().teleportIndicator(player, 1_000, TeleportIndicatorPacket.TeleportState.SKIP);
                this.cancel(0);
            }
        }, 5);

        teleportTask = Fantasy.runLater(() -> {
            this.cancel(0);
            Fantasy.clientProvider().teleportIndicator(player, 0, TeleportIndicatorPacket.TeleportState.DONE);

            val user = player.<User>getBungeePlayer();

            if (user.getCurrencyHandler().withdrawCurrency(CurrencyType.GRAN, cost)) {
                targetBeacon.forceTeleportToRespawn(player);
                if (cost > 0) Fantasy.bukkitProvider().sound(player, AdvancedSounds.COINS_GIVE);
            } else {
                Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Недостаточно средств", "для перемещения");
            }

            Anime.close(player);
            user.getMetadataStorage().invalidateType(MetadataType.QUEST_CONTEXT);
        }, Math.round(seconds * 20) + 2);
    }

    @Override
    public void cancel(float seconds) {
        if (locationTask != null && !locationTask.isCancelled()) {
            locationTask.cancel();
            locationTask = null;
        }

        if (teleportTask != null && !teleportTask.isCancelled()) {
            teleportTask.cancel();
            teleportTask = null;
        }

        player.removeMetadata("teleport_process", Fantasy.getJavaPlugin());
    }

}
