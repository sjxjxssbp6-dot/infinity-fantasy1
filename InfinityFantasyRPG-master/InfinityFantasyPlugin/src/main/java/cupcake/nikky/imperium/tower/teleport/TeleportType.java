package cupcake.nikky.imperium.tower.teleport;

import cupcake.nikky.imperium.tower.beacon.BeaconObjects;
import cupcake.nikky.imperium.tower.teleport.exact.BeaconTeleport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.bukkit.entity.Player;

import java.util.function.BiFunction;

@Getter
@RequiredArgsConstructor
@Accessors(fluent = true)
public enum TeleportType {

    BEACON(BeaconTeleport::new);

    private final BiFunction<Player, BeaconObjects, Teleport> teleportGenerator;

}
