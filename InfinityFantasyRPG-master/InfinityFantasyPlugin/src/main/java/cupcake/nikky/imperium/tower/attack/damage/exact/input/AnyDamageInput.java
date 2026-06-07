package cupcake.nikky.imperium.tower.attack.damage.exact.input;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.attack.damage.DamageContainer;
import cupcake.nikky.imperium.tower.attack.damage.DamageFlag;
import cupcake.nikky.imperium.tower.attack.damage.DamageProcess;
import cupcake.nikky.imperium.tower.attack.damage.InfluenceType;
import cupcake.nikky.imperium.tower.attack.damage.annotation.InvokeIgnore;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.entity.monster.goal.exact.ImmobilizedGoalUpdater;
import cupcake.nikky.imperium.tower.sound.AdvancedSounds;
import lombok.val;
import me.anfanik.steda.api.utility.MathUtility;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;

public class AnyDamageInput extends DamageProcess {

    @Override
    @SuppressWarnings("all")
    @InvokeIgnore
    public void invoke(@NotNull DamageContainer container) {
        val imperiumItem = container.getAttacker().getImperiumItemInHand();
        val cancel = new AtomicBoolean();
        val isDoubleDamage = new AtomicBoolean();

        container.getAttacker().entityTypeCallback().ifUserPresent(user -> {
            if (imperiumItem != null && imperiumItem.getGeneralSettings().requiredLevel() > user.getLevel()) {
                var player = (Player) container.getAttacker().getLivingEntity();
                container.applyDamageFlag(DamageFlag.NOT_AVAILABLE);
                Fantasy.bukkitProvider().sound(player, AdvancedSounds.CONFUSION);
                Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Не соблюдены требования", "уровня для этого оружия");
                cancel.set(true);
            }

            user.getCurrentMountData().tryUnmount();
        });

        if (cancel.get()) return;

        container.getAttacker().entityTypeCallback().ifAttributablePresent(attacker -> {
            val attackerAttributes = container.getAttacker().getAttributes();

            container.setDamage(InfluenceType.PHYSICAL, MathUtility.randomInRange(
                    attackerAttributes.find(ImperiumAttribute.MIN_PHYSICAL_DAMAGE).getValue(),
                    attackerAttributes.find(ImperiumAttribute.MAX_PHYSICAL_DAMAGE).getValue()
            ));
            container.setDamage(InfluenceType.FIRE, MathUtility.randomInRange(
                    attackerAttributes.find(ImperiumAttribute.MIN_FIRE_DAMAGE).getValue(),
                    attackerAttributes.find(ImperiumAttribute.MAX_FIRE_DAMAGE).getValue()
            ));
            container.setDamage(InfluenceType.COLD, MathUtility.randomInRange(
                    attackerAttributes.find(ImperiumAttribute.MIN_COLD_DAMAGE).getValue(),
                    attackerAttributes.find(ImperiumAttribute.MAX_COLD_DAMAGE).getValue()
            ));
            container.setDamage(InfluenceType.LIGHTNING, MathUtility.randomInRange(
                    attackerAttributes.find(ImperiumAttribute.MIN_LIGHTNING_DAMAGE).getValue(),
                    attackerAttributes.find(ImperiumAttribute.MAX_LIGHTNING_DAMAGE).getValue()
            ));
            container.setDamage(InfluenceType.CHAOS, MathUtility.randomInRange(
                    attackerAttributes.find(ImperiumAttribute.MIN_CHAOS_DAMAGE).getValue(),
                    attackerAttributes.find(ImperiumAttribute.MAX_CHAOS_DAMAGE).getValue()
            ));
        });

        container.getDefender().entityTypeCallback().ifAttributablePresent(attributable -> {
            val doubleDamageChance = attributable.findAttribute(ImperiumAttribute.FRAGMENT_CHANCE_GET_DOUBLE_DAMAGE).findValue();
            if (doubleDamageChance > 0D) {
                isDoubleDamage.set(MathUtility.randomInRange(1, 100) <= doubleDamageChance);
            }
        });

        container.getDefender().entityTypeCallback().ifMonsterPresent(monster -> {
            if (monster.getGoalUpdater() instanceof ImmobilizedGoalUpdater || monster.getMonsterData().getSpeed() <= -100) {
                container.applyDamageFlag(DamageFlag.IGNORE_KNOCK_BACK);
            }
        });

        container.getAttacker().entityTypeCallback().ifAttributablePresent(attributable -> {
            val moreDamage = 1 + attributable.findAttribute(ImperiumAttribute.MORE_DAMAGE).findValue() / 100D;
            val isDouble = isDoubleDamage.get();

            for (InfluenceType value : InfluenceType.values()) {
                val damage = container.findDamageByInfluence(value);
                container.setDamage(value, damage * moreDamage * (isDouble ? 2D : 1D));
            }
        });

        container.getAttacker().entityTypeCallback().ifUserPresent(user -> {
            if (imperiumItem == null || !imperiumItem.getDamageSettings().isDamageable())
                container.resetDamage();
        });
    }

}
