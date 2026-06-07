package cupcake.nikky.imperium.tower.entity.monster.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.buff.BuffType;
import cupcake.nikky.imperium.tower.entity.monster.FantasyMonster;
import cupcake.nikky.imperium.tower.entity.monster.MonsterType;
import cupcake.nikky.imperium.tower.entity.monster.data.MonsterData;
import lombok.val;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class DreamWalkerSpider extends FantasyMonster {

    public DreamWalkerSpider(@NotNull Location spawnLocation, @NotNull MonsterData monsterData) {
        super(spawnLocation, monsterData, MonsterType.DREAM_WALKER_SPIDER);
    }

    @Override
    public void onAsyncTick() {
        super.onAsyncTick();

        if (this.isExist()) {
            val spigot = this.getSpigot();
            if (spigot == null) return;

            val monsterLocation = spigot.getLocation();

            getAttackers().forEach(player -> {
                val location = player.getLocation();

                if (location.distanceSquared(monsterLocation) < 6 * 6)
                    Fantasy.buffService().appendBuff(player.getUniqueId(), BuffType.IMMOBILITY, 25, 4);
            });
        }
    }
}
