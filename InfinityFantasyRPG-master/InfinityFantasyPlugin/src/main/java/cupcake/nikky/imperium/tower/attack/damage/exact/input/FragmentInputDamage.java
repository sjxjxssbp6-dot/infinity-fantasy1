package cupcake.nikky.imperium.tower.attack.damage.exact.input;

import cupcake.nikky.imperium.tower.attack.damage.DamageContainer;
import cupcake.nikky.imperium.tower.attack.damage.DamageProcess;
import cupcake.nikky.imperium.tower.attack.damage.InfluenceType;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.user.User;
import org.jetbrains.annotations.NotNull;

public class FragmentInputDamage extends DamageProcess {

    @Override
    public void invoke(@NotNull DamageContainer container) {
        container.getDefender().entityTypeCallback().ifAttributablePresent(attributable -> {
            var fire = attributable.findAttribute(ImperiumAttribute.FRAGMENT_INCREASE_INPUT_FIRE_DAMAGE).findValue() / 100D;
            var cold = attributable.findAttribute(ImperiumAttribute.FRAGMENT_INCREASE_INPUT_COLD_DAMAGE).findValue() / 100D;
            var lightning = attributable.findAttribute(ImperiumAttribute.FRAGMENT_INCREASE_INPUT_LIGHTNING_DAMAGE).findValue() / 100D;
            var chaos = attributable.findAttribute(ImperiumAttribute.FRAGMENT_INCREASE_INPUT_CHAOS_DAMAGE).findValue() / 100D;

            var physical = container.findDamageByInfluence(InfluenceType.PHYSICAL);

            container.changeDamage(InfluenceType.FIRE, physical * fire);
            container.changeDamage(InfluenceType.COLD, physical * cold);
            container.changeDamage(InfluenceType.LIGHTNING, physical * lightning);
            container.changeDamage(InfluenceType.CHAOS, physical * chaos);

            if (!(attributable instanceof User user)) return;

            var fragmentSettings = user.findFragmentSettings();
            if (!fragmentSettings.isInFragment()) return;

            var corruption = fragmentSettings.getFragmentCorruption();
            if (corruption == 0) return;

            for (InfluenceType value : InfluenceType.values()) {
                container.setDamage(value, container.findDamageByInfluence(value) * (1 + (corruption / 100D)));
            }
        });
    }

}
