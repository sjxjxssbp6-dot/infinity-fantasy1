package cupcake.nikky.imperium.tower.beacon.context;

import cupcake.nikky.imperium.tower.beacon.BeaconObjects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Getter
@RequiredArgsConstructor
public class BeaconContext {

    private final Player player;
    private final Map<Integer, BeaconObjects> selection = new HashMap<>();
    private final Map<BeaconObjects, Integer> cost = new EnumMap<>(BeaconObjects.class);

    private int id = 0;

    public BeaconContext registerBeacon(@NotNull final BeaconObjects beaconObjects, final int cost) {
        this.selection.put(id++, beaconObjects);
        this.cost.put(beaconObjects, beaconObjects == BeaconObjects.START ? 0 : cost);
        return this;
    }

    public int findCost(@NotNull final BeaconObjects beaconObjects) {
        return this.cost.get(beaconObjects);
    }

    public Optional<BeaconObjects> findBeacon(final int id) {
        return Optional.ofNullable(this.selection.get(id));
    }

}
