package cupcake.nikky.imperium.tower.corpse;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CorpsePoints {

    /* key = location of corpse, value = currency reward */
    private final Map<Location, Integer> points = new HashMap<>();

    @Getter
    private static final CorpsePoints instance = new CorpsePoints();
    @Getter
    @Setter(value = AccessLevel.PRIVATE)
    private World corpseWorld;

    public void initialize(@NotNull final World world) {
        this.setCorpseWorld(world);
        CorpsePoints.getInstance().points.put(new Location(world, 37, 104, 173), 5); // 2-1
        CorpsePoints.getInstance().points.put(new Location(world, -47, 98, 171), 5); // 2-2
        CorpsePoints.getInstance().points.put(new Location(world, 48, 93, 278), 5); // 3-1
        CorpsePoints.getInstance().points.put(new Location(world, 19, 93, 357), 5); // 3-2
        CorpsePoints.getInstance().points.put(new Location(world, 47, 85, 470), 5); // 4-1
        CorpsePoints.getInstance().points.put(new Location(world, 16, 113, 469), 5); // 4-2
        CorpsePoints.getInstance().points.put(new Location(world, -47, 90, 606), 5); // 5-1
        CorpsePoints.getInstance().points.put(new Location(world, -35, 87, 604), 5); // 5-2
        CorpsePoints.getInstance().points.put(new Location(world, 22, 93, 707), 5); // 6-1
        CorpsePoints.getInstance().points.put(new Location(world, -40, 110, 736), 5); // 6-2
        CorpsePoints.getInstance().points.put(new Location(world, -33, 86, 860), 5); // 7-1
        CorpsePoints.getInstance().points.put(new Location(world, -35, 89, 921), 5); // 7-2
        CorpsePoints.getInstance().points.put(new Location(world, -21, 97, 873), 5); // 7-3
        CorpsePoints.getInstance().points.put(new Location(world, -59, 94, 1039), 5); // 8-1
        CorpsePoints.getInstance().points.put(new Location(world, 38, 89, 1052), 5); // 8-2
        CorpsePoints.getInstance().points.put(new Location(world, -28, 90, 1051), 5); // 8-3
        CorpsePoints.getInstance().points.put(new Location(world, 41, 89, 1126), 5); // 9-1
        CorpsePoints.getInstance().points.put(new Location(world, -18, 93, 1173), 5); // 9-2
        CorpsePoints.getInstance().points.put(new Location(world, -38, 89, 1269), 5); // 10-1
        CorpsePoints.getInstance().points.put(new Location(world, 8, 108, 1276), 5); // 10-2
        CorpsePoints.getInstance().points.put(new Location(world, -31, 90, 1424), 5); // 11-1
        CorpsePoints.getInstance().points.put(new Location(world, -18, 92, 1391), 5); // 11-2
        CorpsePoints.getInstance().points.put(new Location(world, 25, 89, 1536), 5); // 12-1
        CorpsePoints.getInstance().points.put(new Location(world, -22, 89, 1586), 5); // 12-2
        CorpsePoints.getInstance().points.put(new Location(world, -14, 90, 1673), 5); // 13-1
        CorpsePoints.getInstance().points.put(new Location(world, 37, 89, 1689), 5); // 13-2
        CorpsePoints.getInstance().points.put(new Location(world, 45, 91, 1785), 5); // 14-1
        CorpsePoints.getInstance().points.put(new Location(world, -13, 93, 1786), 5); // 14-2
        CorpsePoints.getInstance().points.put(new Location(world, -26, 95, 1908), 5); // 15-1
        CorpsePoints.getInstance().points.put(new Location(world, 3, 94, 1952), 5); // 15-2
        CorpsePoints.getInstance().points.put(new Location(world, -16, 90, 2041), 5); // 16-1
        CorpsePoints.getInstance().points.put(new Location(world, -30, 107, 2019), 5); // 16-2
        CorpsePoints.getInstance().points.put(new Location(world, 16, 80, 2171), 5); // 17-1
        CorpsePoints.getInstance().points.put(new Location(world, 12, 90, 2142), 5); // 17-2
        CorpsePoints.getInstance().points.put(new Location(world, 29, 113, 2284), 5); // 18-1
        CorpsePoints.getInstance().points.put(new Location(world, -27, 90, 2293), 5); // 18-2
        CorpsePoints.getInstance().points.put(new Location(world, -44, 94, 2414), 5); // 19-1
        CorpsePoints.getInstance().points.put(new Location(world, 15, 92, 2380), 5); // 19-2
        CorpsePoints.getInstance().points.put(new Location(world, -22, 93, 2533), 5); // 20-1
        CorpsePoints.getInstance().points.put(new Location(world, 25, 95, 2523), 5); // 20-2
        CorpsePoints.getInstance().points.put(new Location(world, -3, 92, 2594), 5); // 21-1
        CorpsePoints.getInstance().points.put(new Location(world, 19, 91, 2636), 5); // 21-2
        CorpsePoints.getInstance().points.put(new Location(world, -31, 93, 2715), 5); // 22-1
        CorpsePoints.getInstance().points.put(new Location(world, 28, 89, 2748), 5); // 22-2
        CorpsePoints.getInstance().points.put(new Location(world, -22, 94, 2865), 5); // 23-1
        CorpsePoints.getInstance().points.put(new Location(world, -34, 93, 2809), 5); // 23-2
        CorpsePoints.getInstance().points.put(new Location(world, -25, 90, 2946), 5); // 24-1
        CorpsePoints.getInstance().points.put(new Location(world, 10, 90, 2927), 5); // 24-2
        CorpsePoints.getInstance().points.put(new Location(world, -10, 93, 3054), 5); // 25-1
        CorpsePoints.getInstance().points.put(new Location(world, -31, 88, 3040), 5); // 25-2
        CorpsePoints.getInstance().points.put(new Location(world, -23, 93, 3162), 5); // 26-1
        CorpsePoints.getInstance().points.put(new Location(world, 22, 91, 3116), 5); // 26-2
        CorpsePoints.getInstance().points.put(new Location(world, 20, 92, 3229), 5); // 27-1
        CorpsePoints.getInstance().points.put(new Location(world, -34, 91, 3253), 5); // 27-2
        CorpsePoints.getInstance().points.put(new Location(world, 17, 92, 3379), 5); // 28-1
        CorpsePoints.getInstance().points.put(new Location(world, -11, 93, 3373), 5); // 28-2
        CorpsePoints.getInstance().points.put(new Location(world, -12, 87, 3464), 5); // 29-1
        CorpsePoints.getInstance().points.put(new Location(world, 39, 83, 3449), 5); // 29-2
        CorpsePoints.getInstance().points.put(new Location(world, 28, 89, 3543), 5); // 30-1
        CorpsePoints.getInstance().points.put(new Location(world, -27, 89, 3523), 5); // 30-2
        CorpsePoints.getInstance().points.put(new Location(world, -27, 94, 3552), 5); // 30-3
        CorpsePoints.getInstance().points.put(new Location(world, -36, 91, 3617), 5); // 31-1
        CorpsePoints.getInstance().points.put(new Location(world, 4, 101, 3635), 5); // 31-2

        CorpsePoints.getInstance().points.put(new Location(world, 1378, 104, 143), 5); // 2-1
        CorpsePoints.getInstance().points.put(new Location(world, 1462, 110, 144), 5); // 2-2

        CorpsePoints.getInstance().points.put(new Location(world, 1454, 96, 314), 5); // 3-1
        CorpsePoints.getInstance().points.put(new Location(world, 1425, 96, 394), 5); // 3-2

        CorpsePoints.getInstance().points.put(new Location(world, 1440, 86, 580), 5); // 4-1
        CorpsePoints.getInstance().points.put(new Location(world, 1408, 114, 579), 5); // 4-2

        CorpsePoints.getInstance().points.put(new Location(world, 1377, 88, 781), 5); // 5-1
        CorpsePoints.getInstance().points.put(new Location(world, 1365, 91, 783), 5); // 5-2

        CorpsePoints.getInstance().points.put(new Location(world, 1417, 94, 956), 5); // 6-1
        CorpsePoints.getInstance().points.put(new Location(world, 1354, 111, 986), 5); // 6-2

        CorpsePoints.getInstance().points.put(new Location(world, 1399, 97, 1212), 5); // 7-1
        CorpsePoints.getInstance().points.put(new Location(world, 1386, 87, 1199), 5); // 7-2
        CorpsePoints.getInstance().points.put(new Location(world, 1384, 90, 1260), 5); // 7-3

        CorpsePoints.getInstance().points.put(new Location(world, 1471, 96, 1470), 5); // 8-1
        CorpsePoints.getInstance().points.put(new Location(world, 1374, 100, 1457), 5); // 8-2
        CorpsePoints.getInstance().points.put(new Location(world, 1405, 96, 1469), 5); // 8-3
    }

    public boolean isCorpse(@NotNull final Location location) {
        return points.containsKey(location);
    }

    public int findReward(@NotNull final Location location) {
        return points.getOrDefault(location, -1);
    }

}
