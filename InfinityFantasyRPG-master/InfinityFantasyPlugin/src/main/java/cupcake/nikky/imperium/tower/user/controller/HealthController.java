package cupcake.nikky.imperium.tower.user.controller;

import com.google.common.util.concurrent.AtomicDouble;
import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.user.death.AsyncFantasyUserDeath;
import cupcake.nikky.imperium.tower.user.User;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.Bukkit;

import java.util.concurrent.atomic.AtomicBoolean;

@RequiredArgsConstructor
public class HealthController {

    private final User user;
    private final AtomicDouble health = new AtomicDouble(1);
    private final AtomicBoolean shouldDeath = new AtomicBoolean(false);

    public double getHealth() {
        return health.get();
    }

    public void setHealth(double health) {
        if (this.isShouldDeath()) return;

        for (;;) {
            val old = this.health.get();
            if (this.health.compareAndSet(old, health)) break;
        }

        if (this.getHealth() <= 0D) {
            this.setShouldDeath(true);

            Fantasy.runLater(user::onRespawn, 1);
            Fantasy.getRespawnAndBeaconExecutor().execute(() -> Bukkit.getPluginManager().callEvent(new AsyncFantasyUserDeath(user.getLastDamageContainer(), user)));
        }

        Fantasy.clientProvider().hotbarHealth(user);
    }

    public void setHealthForce(double health) {
        if (this.isShouldDeath()) return;

        for (;;) {
            val old = this.health.get();
            if (this.health.compareAndSet(old, health)) break;
        }
    }

    public void changeHealthPercent(final double percent) {
        this.changeHealth(user.findAttribute(ImperiumAttribute.HEALTH).findValue() * percent / 100D);
    }

    public void changeHealth(double delta) {
        if (this.isShouldDeath()) return;

        val maxHealth = user.findAttribute(ImperiumAttribute.HEALTH).findValue();
        if (maxHealth <= 0D) return;

        for (;;) {
            val currentHealth = this.getHealth();
            val newHealth = this.getHealth() + delta;

            val nextHealth = Math.clamp(newHealth, 0, maxHealth);
            if (currentHealth == nextHealth) break;

            if (this.health.compareAndSet(currentHealth, nextHealth)) {
                if (nextHealth <= 0D) {
                    this.setShouldDeath(true);

                    Fantasy.runLater(user::onRespawn, 1);
                    Fantasy.getRespawnAndBeaconExecutor().execute(() -> Bukkit.getPluginManager().callEvent(new AsyncFantasyUserDeath(user.getLastDamageContainer(), user)));
                }
                Fantasy.clientProvider().hotbarHealth(user);
                break;
            }
        }
    }

    public boolean isShouldDeath() {
        return shouldDeath.get();
    }

    public void setShouldDeath(final boolean value) {
        for (;;) {
            val old = shouldDeath.get();
            if (shouldDeath.compareAndSet(old, value)) break;
        }
    }

}
