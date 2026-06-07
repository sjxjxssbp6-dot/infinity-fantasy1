package cupcake.nikky.imperium.tower.entity.monster.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.buff.BuffType;
import cupcake.nikky.imperium.tower.entity.monster.FantasyMonster;
import cupcake.nikky.imperium.tower.entity.monster.MonsterType;
import cupcake.nikky.imperium.tower.entity.monster.data.MonsterData;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class CharmedGolem extends FantasyMonster {

    private static BukkitTask slownessTask;
    private static final List<FantasyMonster> charmedGolem = new CopyOnWriteArrayList<>();

    private final AtomicLong velocityCooldown = new AtomicLong(Instant.now().toEpochMilli());

    public CharmedGolem(@NotNull final Location spawnLocation, @NotNull final MonsterData monsterData) {
        super(spawnLocation, monsterData, MonsterType.CHARMED_GOLEM);
        charmedGolem.add(this);

        if (slownessTask != null) return;
        slownessTask = Fantasy.runAsyncTimer(() -> charmedGolem.forEach(monster -> {
            if (!monster.isExist() || monster.getTargetTracker().goalTarget() == null) return;
            monster.getAttackers().stream()
                    .filter(player -> player.getLocation().distanceSquared(monster.getSpigot().getLocation()) < 5 * 5)
                    .forEach(player -> Fantasy.buffService().appendBuff(player.getUniqueId(), BuffType.SLOWNESS, 25, 4));
        }), 10);
    }

    @Override
    public void onSpawn() {
        super.onSpawn();
    }

    @Override
    public double onOutboundDamage(@NotNull LivingEntity entity, double damage) {
        if (velocityCooldown.get() > Instant.now().toEpochMilli()) return super.onOutboundDamage(entity, damage);
        entity.setVelocity(new Vector(0D, 0.9D, 0D));
        velocityCooldown.set(Instant.now().toEpochMilli() + TimeUnit.SECONDS.toMillis(1));
        return super.onOutboundDamage(entity, damage);
    }

}
