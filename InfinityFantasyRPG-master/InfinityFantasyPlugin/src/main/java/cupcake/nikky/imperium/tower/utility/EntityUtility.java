package cupcake.nikky.imperium.tower.utility;

import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.entity.monster.Monster;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.zone.Zone;
import cupcake.nikky.imperium.tower.zone.ZoneList;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EntityUtility {

    public static double getHealthPercentage(final double current, final double max) {
        return current / max * 100D;
    }

    public static double getHealthPercentage(@NotNull final Monster monster) {
        return getHealthPercentage(monster.getHealth(), monster.findAttribute(ImperiumAttribute.HEALTH).findValue());
    }

    public static double getHealthPercentage(@NotNull final User user) {
        return getHealthPercentage(user.getHealth(), user.findAttribute(ImperiumAttribute.HEALTH).findValue());
    }

    public static List<LivingEntity> findInRadius(@NotNull final Location sourceLocation, final double radius) {
        return findInRadius(sourceLocation, radius, radius, radius);
    }

    public static List<LivingEntity> findInRadius(@NotNull final Location sourceLocation, final double radiusX, final double radiusY, final double radiusZ) {
        val sourcePvp = ZoneList.findByLocation(sourceLocation).pvp();
        return sourceLocation.getNearbyLivingEntities(radiusX, radiusY, radiusZ).stream().filter(entity -> {
            if (entity instanceof Player player) {
                return sourcePvp && Optional.of(player.<User>getBungeePlayer().getCurrentZone()).map(Zone::pvp).orElse(false);
            } else return true;
        }).toList();
    }

}
