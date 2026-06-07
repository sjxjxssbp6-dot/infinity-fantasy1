package cupcake.nikky.imperium.tower.utility;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.SkullType;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SkullUtility {

    public static SkullType findSkullTypeByEntityType(@NotNull final EntityType entityType) {
        try {
            return SkullType.valueOf(entityType.name());
        } catch (Exception exception) {
            if (entityType.name().contains("WITHER")) return SkullType.WITHER;
            return SkullType.DRAGON;
        }
    }

}
