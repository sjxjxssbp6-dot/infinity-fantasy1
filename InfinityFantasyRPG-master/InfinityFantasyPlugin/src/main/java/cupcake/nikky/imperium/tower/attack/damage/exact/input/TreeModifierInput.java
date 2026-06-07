package cupcake.nikky.imperium.tower.attack.damage.exact.input;

import cupcake.nikky.imperium.tower.attack.damage.DamageContainer;
import cupcake.nikky.imperium.tower.attack.damage.DamageProcess;
import cupcake.nikky.imperium.tower.attack.damage.InfluenceType;
import cupcake.nikky.imperium.tower.attack.damage.annotation.InvokeIgnore;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.item.exact.gear.weapon.WeaponItem;
import cupcake.nikky.imperium.tower.item.exact.gear.weapon.WeaponType;
import cupcake.nikky.imperium.tower.item.exact.gear.weapon.base.BaseAxe;
import cupcake.nikky.imperium.tower.item.exact.gear.weapon.base.BaseDagger;
import cupcake.nikky.imperium.tower.item.exact.gear.weapon.base.BaseSpear;
import cupcake.nikky.imperium.tower.item.exact.gear.weapon.base.BaseSword;
import lombok.val;
import org.jetbrains.annotations.NotNull;

public class TreeModifierInput extends DamageProcess {

    @Override
    @InvokeIgnore
    public void invoke(@NotNull DamageContainer container) {
        val imperiumItem = container.getAttacker().getImperiumItemInHand();

        container.getAttacker().entityTypeCallback().ifUserPresent(user -> {
            if (imperiumItem != null && imperiumItem.checkInstance(WeaponItem.class)) {
                val dataStorage = user.findTreeSettings();
                if (dataStorage == null) return;

                double increase = 0D;

                if (imperiumItem.checkInstance(BaseSword.class)) {
                    increase = user.findAttribute(ImperiumAttribute.SWORD_INCREASE_DAMAGE).findValue() / 100D;
                } else if (imperiumItem.checkInstance(BaseDagger.class)) {
                    increase = user.findAttribute(ImperiumAttribute.DAGGER_INCREASE_DAMAGE).findValue() / 100D;
                } else if (imperiumItem.checkInstance(BaseSpear.class)) {
                    increase = user.findAttribute(ImperiumAttribute.SPEAR_INCREASE_DAMAGE).findValue() / 100D;
                } else if (imperiumItem.checkInstance(BaseAxe.class)) {
                    increase = user.findAttribute(ImperiumAttribute.BATTLE_AXE_INCREASE_DAMAGE).findValue() / 100D;
                }

                val physicalDamage = container.findDamageByInfluence(InfluenceType.PHYSICAL);
                val fireDamage = container.findDamageByInfluence(InfluenceType.FIRE);
                val coldDamage = container.findDamageByInfluence(InfluenceType.COLD);
                val lightningDamage = container.findDamageByInfluence(InfluenceType.LIGHTNING);
                val chaosDamage = container.findDamageByInfluence(InfluenceType.CHAOS);

                container.changeDamage(InfluenceType.PHYSICAL, physicalDamage * increase);
                container.changeDamage(InfluenceType.FIRE, fireDamage * increase);
                container.changeDamage(InfluenceType.COLD, coldDamage * increase);
                container.changeDamage(InfluenceType.LIGHTNING, lightningDamage * increase);
                container.changeDamage(InfluenceType.CHAOS, chaosDamage * increase);
            }
        });
    }

}
