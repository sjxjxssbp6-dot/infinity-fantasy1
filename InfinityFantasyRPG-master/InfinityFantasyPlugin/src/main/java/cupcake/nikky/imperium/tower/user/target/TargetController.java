package cupcake.nikky.imperium.tower.user.target;

import cupcake.nikky.imperium.tower.user.User;
import lombok.val;
import org.jetbrains.annotations.NotNull;

public class TargetController {

    public void run(@NotNull final User user) {
        val targetData = user.getTargetEntity().getTargetData();
        val player = user.getSpigot();
        if (targetData == null || player == null) return;

        val monster = targetData.getMonster();

        if (monster == null || !monster.isExist()) {
            targetData.unsubscribe(player.getUniqueId());
            return;
        }

        val spigot = monster.getSpigot();
        if (spigot == null) {
            targetData.unsubscribe(player.getUniqueId());
            return;
        }

        if (!spigot.getWorld().getName().equals(player.getWorld().getName())) {
            targetData.unsubscribe(player.getUniqueId());
            return;
        }

        if (player.getLocation().distanceSquared(spigot.getLocation()) > 16 * 16) {
            targetData.unsubscribe(player.getUniqueId());
        }
    }

}
