package cupcake.nikky.imperium.tower.beacon.context;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.beacon.BeaconObjects;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.protocol.packet.ImperiumTeleportContextPacket;
import cupcake.nikky.imperium.tower.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class BeaconContextProvider {

    @Getter
    private static final BeaconContextProvider instance = new BeaconContextProvider();

    private final Map<UUID, BeaconContext> data = new ConcurrentHashMap<>(32, 0.75F, 2);

    public void registerContext(@NotNull final Player player) {
        val context = new BeaconContext(player);

        val user = player.<User>getBungeePlayer();
        val questedPlayer = user.findQuestSettings();

        Arrays.stream(BeaconObjects.values())
                .forEach(beaconObjects -> {
                    val isBlocked = !beaconObjects.getBeaconSettings().handlerUser().test(user) || !beaconObjects.getBeaconSettings().handlerQuest().test(questedPlayer);
                    val price = isBlocked ? 0 : beaconObjects.getBeaconSettings().priceFunction().applyAsInt(beaconObjects);
                    context.registerBeacon(beaconObjects, price);
                });

        if (context.getSelection().isEmpty()) {
            Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Нет доступных мест для телепорта");
            return;
        } else if (Fantasy.combatProvider().isInCombat(player.getUniqueId())) {
            Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Меню телепортов нельзя открывать", "находясь в бою");
            return;
        }

        this.data.put(player.getUniqueId(), context);

        new ImperiumTeleportContextPacket(context).notice(0);
    }

    public Optional<BeaconContext> findByUuid(@NotNull final UUID uuid) {
        return Optional.ofNullable(this.data.get(uuid));
    }

    public void unregister(@NotNull final UUID uuid) {
        this.data.remove(uuid);
    }

}
