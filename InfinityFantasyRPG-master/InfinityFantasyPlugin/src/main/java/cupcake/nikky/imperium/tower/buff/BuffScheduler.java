package cupcake.nikky.imperium.tower.buff;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.protocol.packet.ImperiumBuffPacket;
import cupcake.nikky.imperium.tower.user.User;
import lombok.val;
import org.bukkit.Bukkit;

public class BuffScheduler implements Runnable {

    @Override
    public void run() {
        Fantasy.buffService().findAll().forEach(entity -> {
            val uuid = entity.getUuid();

            val monster = Fantasy.monsterProvider().findByUUID(uuid).orElse(null);
            val player = Bukkit.getPlayer(uuid);

            if (player == null && monster == null) return;

            val livingEntity = monster == null ? player : monster.getSpigot();
            if (livingEntity == null) return;

            if (player != null) {
                var user = player.<User>getBungeePlayer();
                if (user == null || user.isInvalidated() || !user.isLoaded()) return;
            }

            entity.getAll().forEach(buff -> {
                buff.onTick(livingEntity);

                if (buff.shouldInvalidate()) {
                    buff.onCompletion(livingEntity);

                    Fantasy.buffService().invalidateBuff(uuid, buff.getBuffType());
                } else if (player != null) {
                    new ImperiumBuffPacket(player.getBungeePlayer(), buff).notice(1);
                }
            });
        });
    }

}
