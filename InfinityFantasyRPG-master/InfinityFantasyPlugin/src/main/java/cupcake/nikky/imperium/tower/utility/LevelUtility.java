package cupcake.nikky.imperium.tower.utility;

import cupcake.nikky.imperium.tower.Fantasy;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class LevelUtility {

    public static int getLevel(@NotNull final LivingEntity entity) {
        if (entity instanceof Player) {
            val user = Fantasy.userService().findById(entity.getUniqueId());
            if (user.isPresent()) return user.get().getLevel();
        } else {
            val monster = Fantasy.monsterProvider().findByUUID(entity.getUniqueId());
            if (monster.isPresent()) return monster.get().getLevel();
        }

        return -1;
    }

}
