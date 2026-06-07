package cupcake.nikky.imperium.tower.attack.damage.exact.output;

import cupcake.nikky.imperium.tower.attack.damage.DamageContainer;
import cupcake.nikky.imperium.tower.attack.damage.DamageProcess;
import cupcake.nikky.imperium.tower.attack.damage.annotation.InvokeIgnore;
import cupcake.nikky.imperium.tower.entity.monster.modifier.ModifierCollection;
import cupcake.nikky.imperium.tower.item.exact.gear.weapon.base.BaseAxe;
import cupcake.nikky.imperium.tower.item.exact.gear.weapon.base.BaseSpear;
import cupcake.nikky.imperium.tower.knockback.KnockBackApplier;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicReference;

public class KnockBackOutput extends DamageProcess {

    private final KnockBackApplier baseKnockBackApplier = new KnockBackApplier(-.4);
    private final KnockBackApplier advancedKnockBackApplier = new KnockBackApplier(-.2);

    @Override
    @InvokeIgnore(ifIgnoreKnockBack = true)
    public void invoke(@NotNull DamageContainer container) {
        val attacker = container.getAttacker().getLivingEntity();
        val defender = container.getDefender().getLivingEntity();

        if (attacker == null || defender == null) return;

        val attackerWeapon = container.getAttacker().getImperiumItemInHand();
        if (attackerWeapon != null && attackerWeapon.checkInstance(BaseAxe.class)) return;

        val knockBackReference = new AtomicReference<>(baseKnockBackApplier);

        container.getDefender().entityTypeCallback().ifMonsterPresent(monster -> {
            val collection = monster.getModifierEngine().getCurrentModifiers();
            if (collection == ModifierCollection.EPIC || collection == ModifierCollection.UNIQUE)
                knockBackReference.set(advancedKnockBackApplier);
        });

        container.getAttackInfluenceCallback().ifUserPresent(user -> {
            if (attackerWeapon != null && attackerWeapon.checkInstance(BaseSpear.class)) {
                knockBackReference.get().setMultiplier(2D);
            } else knockBackReference.get().setMultiplier(1D);

            val direction = attacker.getLocation().getDirection();
            knockBackReference.get().apply(direction, defender);
        });
    }

}
