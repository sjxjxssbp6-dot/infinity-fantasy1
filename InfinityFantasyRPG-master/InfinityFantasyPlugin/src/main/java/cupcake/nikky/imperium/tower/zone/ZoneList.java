package cupcake.nikky.imperium.tower.zone;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.bootstrap.exact.enable.MapServiceProcess;
import cupcake.nikky.imperium.tower.chat.ChatType;
import cupcake.nikky.imperium.tower.protocol.fraction.FractionType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.wada.Wada;
import ru.cristalix.wada.util.Color;

import java.util.Arrays;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ZoneList {

    public static final Zone FARM_ZONE = new Zone(-150, 150, 75, 270, -100F, 950, false, 5000, true)
            .addSpawnLocation("default", 4.5F, 99, 9.5F);
    public static final Zone FARM_ZONE_WITH_PVP = new Zone(-150, 150, 75, 270, 951, 3588, true, 5000, true)
            .addSpawnLocation("default", 4.5F, 99, 9.5F);
    public static final Zone REKT_MINE_ZONE = new Zone(288, 400, 50, 270, 3593, 3711, false, 5000, true)
            .addSpawnLocation("default", 353, 103.5F, 3606.8F);
    public static final Zone POLYGON_BINA = new Zone(1463, 1549, 30, 270, 1879, 1966, false, 14000, true)
            .addSpawnLocation("default", 1469, 105, 1915, -25F, 11.6F);
    public static final Zone POLYGON_HOCHMA = new Zone(-1531, -1458, 50, 270, 1844, 1975, false, 14000, true)
            .addSpawnLocation("default", -1478.5F, 104, 1893.7F, 41.4F, 15.4F);
    public static final Zone BATTLE_GROUND_ZONE = new Zone(802, 1157, 40, 270, 663, 1034, false, 15000, false)
            .addSpawnLocation("spawn1", 919, 93, 771.9F, -40, 1)
            .addSpawnLocation("spawn3", 930, 93, 953, -142, 3)
            .addSpawnLocation("spawn6", 1088.8F, 90, 809.6F, 54, -2)
            .addSpawnLocation("spawn8", 1068.6F, 96, 940.4F, 152, 9);

    public static final Zone FRACTION_ZONE = new Zone(-1100, -900, 50, 300, -150, 150, true, 1000, true)
            .addSpawnLocation(FractionType.NONE.name(), -997, 218, -10.1F, -14, 0)
            .addSpawnLocation(FractionType.CHAOS.name(), -997, 126, -6, -72, 0)
            .addSpawnLocation(FractionType.ORDER.name(), -1030.5F, 184.5F, -9.5F, -90, 0);

    public static final Zone EDGE_FRAGMENT_1 = new Zone(1329, 1484, 40, 270, -180, -20, false, 23000, false)
            .addSpawnLocation("default", 1407, 103, -103);
    public static final Zone EDGE_FRAGMENT_2 = new Zone(1355, 1491, 40, 270, 65, 203, false, 23000, false)
            .addSpawnLocation("default", 1421, 109, 134.2F);
    public static final Zone EDGE_FRAGMENT_3 = new Zone(1342, 1500, 40, 270, 268, 428, false, 23000, false)
            .addSpawnLocation("default", 1401, 92, 350.76F);
    public static final Zone EDGE_FRAGMENT_4 = new Zone(1314, 1474, 40, 270, 500, 637, false, 23000, false)
            .addSpawnLocation("default", 1393, 97, 571);
    public static final Zone EDGE_FRAGMENT_5 = new Zone(1316, 1494, 40, 270, 700, 849, false, 23000, false)
            .addSpawnLocation("default", 1407, 93, 771.3F);
    public static final Zone EDGE_FRAGMENT_6 = new Zone(1307, 1494, 40, 270, 916, 1047, false, 23000, false)
            .addSpawnLocation("default", 1404, 102, 986.3F);
    public static final Zone EDGE_FRAGMENT_7 = new Zone(1325, 1542, 40, 270, 1147, 1295, false, 23000, false)
            .addSpawnLocation("default", 1427.3F, 143, 1204.6F);
    public static final Zone EDGE_FRAGMENT_8 = new Zone(1352, 1496, 40, 270, 1363, 1511, false, 23000, false)
            .addSpawnLocation("default", 1431, 101, 1438.1F);

    public static void processZones() {
        for (Zone zone : findAll()) {
            zone.spawns().values().forEach(location -> {
                if (location.getWorld() == null)
                    location.setWorld(MapServiceProcess.GAME_WORLD);
            });
        }
        Arrays.stream(findAll()).filter(zone -> !zone.timeless() || zone == FRACTION_ZONE).forEach(zone -> zone.allSpawnLocations().forEach(location -> {
            var zoneLocation = location.clone();
            if (zone != FRACTION_ZONE) zoneLocation.setY(zoneLocation.getBlockY());
            var zoneManager = Wada.get().getZoneManager();
            zoneManager.addZone(zoneManager.zoneBuilder()
                    .location(zoneLocation)
                    .size(3)
                    .color(Color.GREEN)
                    .onCollisionStateChange((player, state) -> {
                        if (zone.pvp() && state) {
                            Fantasy.bukkitProvider().sendMessage(ChatType.NOTIFICATION, "&aВы вошли в безопасную зону!", player);
                        } else if (zone.pvp() && player.getLocation().distanceSquared(location) <= 3 * 3) {
                            Fantasy.bukkitProvider().sendMessage(ChatType.NOTIFICATION, "&cВы вошли в опасную зону!", player);
                        }
                    })
                    .build());
        }));
    }

    public static Zone[] findAll() {
        return new Zone[] {
                FARM_ZONE, POLYGON_BINA, POLYGON_HOCHMA, REKT_MINE_ZONE, BATTLE_GROUND_ZONE, FRACTION_ZONE,
                EDGE_FRAGMENT_1, EDGE_FRAGMENT_2, EDGE_FRAGMENT_3, EDGE_FRAGMENT_4, EDGE_FRAGMENT_5,
                EDGE_FRAGMENT_6, EDGE_FRAGMENT_7, EDGE_FRAGMENT_8, FARM_ZONE_WITH_PVP
        };
    }

    public static Zone[] findEdgeFragments() {
        return new Zone[] {
                BATTLE_GROUND_ZONE,
                EDGE_FRAGMENT_1, EDGE_FRAGMENT_2, EDGE_FRAGMENT_3, EDGE_FRAGMENT_4, EDGE_FRAGMENT_5,
                EDGE_FRAGMENT_6, EDGE_FRAGMENT_7, EDGE_FRAGMENT_8
        };
    }

    public static Zone findByLocation(@NotNull final Location location) {
        for (Zone zone : ZoneList.findAll()) {
            if (zone.isIn(location))
                return zone;
        }

        return FARM_ZONE;
    }

}
