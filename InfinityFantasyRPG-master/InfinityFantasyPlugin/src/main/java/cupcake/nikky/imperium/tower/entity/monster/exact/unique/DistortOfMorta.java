package cupcake.nikky.imperium.tower.entity.monster.exact.unique;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.buff.BuffType;
import cupcake.nikky.imperium.tower.buff.exact.Poison;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.entity.monster.FantasyMonster;
import cupcake.nikky.imperium.tower.entity.monster.MonsterType;
import cupcake.nikky.imperium.tower.entity.monster.damage.cast.InboundDamageCast;
import cupcake.nikky.imperium.tower.entity.monster.data.MonsterData;
import cupcake.nikky.imperium.tower.item.Icons;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.cristalix.wada.Wada;
import ru.cristalix.wada.module.sphere.ISphere;
import ru.cristalix.wada.util.Color;

import java.util.concurrent.atomic.AtomicInteger;

public class DistortOfMorta extends FantasyMonster {

    private ISphere sphere;
    private Location lastSphereLocation;

    private BukkitTask task;
    private AtomicInteger sphereHealth = new AtomicInteger(10);

    private final boolean[] phases = new boolean[3];

    public DistortOfMorta(@NotNull Location spawnLocation, @NotNull MonsterData monsterData) {
        super(spawnLocation, monsterData, MonsterType.DISTORT_OF_MORTA, null, Icons.AWAKENED_DAGGER, Icons.AWAKENED_DAGGER);
        this.lastSphereLocation = getSpawnLocation().clone();
        this.appendCast();
    }

    private void appendCast() {
        this.appendCast(new InboundDamageCast(this) {
            @Override
            public boolean canApply() {
                if (getCurrentHealthPercent() <= 90 && !phases[0]) {
                    phases[0] = true;
                    return true;
                } else if (getCurrentHealthPercent() <= 60 && !phases[1]) {
                    phases[1] = true;
                    return true;
                } else if (getCurrentHealthPercent() <= 30 && !phases[2]) {
                    phases[2] = true;
                    return true;
                }
                return false;
            }

            @Override
            public void apply() {
                startMechanic();
            }
        });
    }

    @Override
    public void onForgotTarget() {
        super.onForgotTarget();
        this.cancelMechanic();
        phases[0] = false;
        phases[1] = false;
        phases[2] = false;
    }

    @Override
    public void onAsyncTick() {
        super.onAsyncTick();
        if (task != null) Fantasy.buffService().appendBuff(getMonsterUniqueId(), BuffType.IMMOBILITY, 0, 2);
    }

    @Override
    public void invalidateEntity() {
        super.invalidateEntity();
        this.cancelMechanic();
    }

    @Override
    public void onDamageWhileInvulnerability(@Nullable final LivingEntity entity) {
        if (this.isInvulnerability()) {
            if (sphereHealth.decrementAndGet() <= 0) {
                Fantasy.bukkitProvider().sound(lastSphereLocation, Sound.BLOCK_GLASS_BREAK);
                this.cancelMechanic();
            } else {
                val damage = (findAttribute(ImperiumAttribute.MAX_CHAOS_DAMAGE).findValue() + findAttribute(ImperiumAttribute.MAX_PHYSICAL_DAMAGE).findValue()) * 0.3;
                getAttackers().forEach(player -> {
                    if (Fantasy.buffService().findById(player.getUniqueId()).map(buffedEntity -> buffedEntity.find(BuffType.POISON).isPresent()).orElse(false)) return;
                    var poison = Fantasy.buffService().appendBuff(player.getUniqueId(), BuffType.POISON, damage, 2);
                    if (poison != null) ((Poison) poison).setInfluence(getSpigot());
                });
                Fantasy.bukkitProvider().sound(lastSphereLocation, Sound.BLOCK_GLASS_PLACE);
            }
        }
    }

    private void startMechanic() {
        val entity = getSpigot();
        if (entity == null) return;

        var location = entity.getLocation();

        sphereHealth.set(10);
        Fantasy.bukkitProvider().sound(location, Sound.ENTITY_ENDERDRAGON_GROWL);
        Fantasy.buffService().appendBuff(getMonsterUniqueId(), BuffType.IMMOBILITY, 0, 2);

        if (sphere == null) {
            this.lastSphereLocation = location;

            sphere = Wada.get().getSphereManager().createSphere(1.7F, location, Color.DARK_GREEN.alpha(.6));
            sphere.send(Bukkit.getOnlinePlayers());
        }

        if (task == null) {
            task = Fantasy.runTimer(() -> {
                if (this.isExist() && this.sphereHealth.get() > 0) {
                    var currentEntity = getSpigot();
                    if (currentEntity == null) {
                        this.cancelMechanic();
                        return;
                    }

                    this.lastSphereLocation = currentEntity.getLocation();
                    sphere.setLocation(lastSphereLocation);

                    this.setInvulnerability(2);
                }
            }, 1);
        }
    }

    private void cancelMechanic() {
        if (sphere != null) Wada.get().getSphereManager().removeSphere(sphere);
        if (task != null && !task.isCancelled()) task.cancel();

        sphere = null;
        task = null;
    }

}
