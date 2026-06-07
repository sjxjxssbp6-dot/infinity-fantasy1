package cupcake.nikky.imperium.tower.attack.damage.exact.input;

import com.google.common.util.concurrent.AtomicDouble;
import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.attack.damage.DamageContainer;
import cupcake.nikky.imperium.tower.attack.damage.DamageFlag;
import cupcake.nikky.imperium.tower.attack.damage.DamageProcess;
import cupcake.nikky.imperium.tower.attack.damage.annotation.InvokeIgnore;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.sound.AdvancedSounds;
import lombok.val;
import me.anfanik.steda.api.utility.MathUtility;
import org.jetbrains.annotations.NotNull;

public class EvasionProcess extends DamageProcess {

    @Override
    @InvokeIgnore(ifEvasion = false)
    public void invoke(@NotNull DamageContainer container) {
        val evasion = new AtomicDouble(0);

        container.getAttackInfluenceCallback().ifUserPresent(user -> container.getDefender().entityTypeCallback().ifMonsterPresent(monster -> {
            val deltaLevel = Math.max(monster.getLevel() - user.getLevel(), 0);
            evasion.set(deltaLevel * 5);
        }));

        container.getDefender().entityTypeCallback().ifAttributablePresent(attributable -> evasion.addAndGet(attributable.findAttribute(ImperiumAttribute.EVASION).findValueWithLimit()));

        container.getAttackInfluenceCallback().ifAttributablePresent(attributable -> {
            val range = evasion.get() - (attributable.findAttribute(ImperiumAttribute.ACCURACY).findValueWithLimit() - 100);
            if (range <= 0) return;

            if (MathUtility.randomInRange(0, 100) <= range)
                this.doEvasion(container);
        });
    }

    private void doEvasion(final DamageContainer damageContainer) {
        val defender = damageContainer.getDefender().getLivingEntity();
        if (defender == null) return;

        damageContainer.applyDamageFlag(DamageFlag.EVASION);
        Fantasy.bukkitProvider().sound(defender.getLocation(), AdvancedSounds.EVASION);
    }

}
