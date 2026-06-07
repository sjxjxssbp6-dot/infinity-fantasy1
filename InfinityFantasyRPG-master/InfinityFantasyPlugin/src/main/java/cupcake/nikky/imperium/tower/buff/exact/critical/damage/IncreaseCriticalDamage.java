package cupcake.nikky.imperium.tower.buff.exact.critical.damage;

import cupcake.nikky.imperium.tower.buff.FantasyBuff;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;

import java.util.List;

public class IncreaseCriticalDamage extends FantasyBuff {

    private final double value;

    public IncreaseCriticalDamage(final double value) {
        super(60 * 15, "Увеличение крит. урона");
        this.registerMultiplierAttribute(ImperiumAttribute.CRITICAL_DAMAGE, value / 100D);
        this.value = value;
    }

    @Override
    public List<String> getDescription() {
        return List.of("&3%.0f%% &6Увеличение крит. урона".formatted(value));
    }
}
