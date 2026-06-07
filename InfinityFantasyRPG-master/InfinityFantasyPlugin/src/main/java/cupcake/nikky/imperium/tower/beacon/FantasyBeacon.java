package cupcake.nikky.imperium.tower.beacon;

import cupcake.nikky.imperium.tower.bootstrap.exact.enable.MapServiceProcess;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;

@RequiredArgsConstructor(staticName = "simple")
public class FantasyBeacon implements Beacon {

    private final Location spawnLocation;

    @Override
    public Location getRespawnLocation() {
        if (spawnLocation.getWorld() == null)
            spawnLocation.setWorld(MapServiceProcess.GAME_WORLD);
        return spawnLocation;
    }

}
