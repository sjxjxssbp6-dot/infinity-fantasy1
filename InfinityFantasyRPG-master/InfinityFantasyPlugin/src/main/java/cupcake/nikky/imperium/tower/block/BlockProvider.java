package cupcake.nikky.imperium.tower.block;

import cupcake.nikky.imperium.tower.bootstrap.exact.enable.MapServiceProcess;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BlockProvider {

    @Getter
    private static final BlockProvider instance = new BlockProvider();

    private final Map<Location, Material> blockData = Map.of(
            new Location(MapServiceProcess.GAME_WORLD, 7, 99, 19, 0, 0), Material.ENDER_CHEST,
            new Location(MapServiceProcess.GAME_WORLD, -5, 93, 805, 0, 0), Material.ENDER_CHEST,
            new Location(MapServiceProcess.GAME_WORLD, 7, 100, 20, 0, 0), Material.WORKBENCH
    );

    public Map<Location, Material> findAll() {
        return blockData;
    }

}
