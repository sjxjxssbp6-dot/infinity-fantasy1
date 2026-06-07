package cupcake.nikky.imperium.tower.attack.damage.provider;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.attack.damage.DamageContainer;
import cupcake.nikky.imperium.tower.attack.damage.DamageFlag;
import cupcake.nikky.imperium.tower.attack.damage.DamageType;
import cupcake.nikky.imperium.tower.attack.damage.InfluenceType;
import cupcake.nikky.imperium.tower.attack.event.FantasyDamageEvent;
import cupcake.nikky.imperium.tower.attribute.AttributeInstance;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;

@RequiredArgsConstructor(staticName = "of")
public class DamageSubProcess {

    private final DamageContainer damageContainer;

    public DamageSubProcess setAttackInfluence(@NotNull final LivingEntity entity) {
        if (damageContainer != null) {
            damageContainer.setAttackInfluence(entity);
        }

        return this;
    }

    public DamageSubProcess setDamageType(@NotNull final DamageType damageType) {
        if (damageContainer != null) {
            damageContainer.setDamageType(damageType);
        }

        return this;
    }

    public DamageSubProcess setAttribute(@NotNull final ImperiumAttribute attribute, @NotNull final AttributeInstance attributeInstance) {
        if (damageContainer != null) {
            damageContainer.getAdditionalAttributes().put(attribute, attributeInstance);
        }

        return this;
    }

    public DamageSubProcess setDamage(@NotNull final InfluenceType type, final double damage) {
        if (damageContainer != null) {
            damageContainer.setDamage(type, damage);
        }

        return this;
    }

    public DamageSubProcess setDamageByPercentOfDefenderHealth(@NotNull final InfluenceType type, final double percent) {
        if (damageContainer != null) {
            damageContainer.getDefender().entityTypeCallback().ifAttributablePresent(attributable -> {
                damageContainer.setDamage(type, attributable.findAttribute(ImperiumAttribute.HEALTH).findValue() * (percent / 100D));
            });
        }

        return this;
    }

    public DamageSubProcess applyFlag(@NotNull final DamageFlag flag) {
        if (damageContainer != null) {
            damageContainer.applyDamageFlag(flag);
        }

        return this;
    }

    public DamageResult apply() {
        if (damageContainer == null) return DamageResult.SKIP;

        val cancel = new AtomicBoolean(false);

        damageContainer.getAttacker().entityTypeCallback().ifUserPresent(user -> cancel.set(user.isShouldDeath()));
        if (cancel.get()) return DamageResult.CANCELLED;

        damageContainer.getDefender().entityTypeCallback().ifUserPresent(user -> {
            if (user.isShouldDeath()) cancel.set(true);
        }).ifMonsterPresent(monster -> {
            if (monster.isShouldDeath()) {
                cancel.set(true);
            } else {
                val targetTracker = monster.getTargetTracker();
                if (targetTracker.isForgotTarget() && !monster.isInvulnerability()) {
                    targetTracker.reset();
                }
            }
        });

        if (cancel.get() || !damageContainer.getDefender().entityTypeCallback().isPresent()) return DamageResult.CANCELLED;

        val fantasyDamageEvent = new FantasyDamageEvent(damageContainer);

        Fantasy.damageProvider().process(damageContainer);
        Bukkit.getPluginManager().callEvent(fantasyDamageEvent);

        return DamageResult.NOT_CANCELLED;
    }

}
