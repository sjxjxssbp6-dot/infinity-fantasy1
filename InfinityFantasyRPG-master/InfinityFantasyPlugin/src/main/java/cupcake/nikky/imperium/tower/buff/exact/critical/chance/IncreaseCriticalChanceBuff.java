package cupcake.nikky.imperium.tower.buff.exact.critical.chance;

import cupcake.nikky.imperium.tower.buff.FantasyBuff;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;

import java.util.List;

public class IncreaseCriticalChanceBuff extends FantasyBuff {

    private final double value;

    public IncreaseCriticalChanceBuff(final double value) {
        super(60 * 15, "Увеличение шанса крит. удара");
        this.registerMultiplierAttribute(ImperiumAttribute.CRITICAL_RATE, value / 100D);
        this.value = value;
    }

    @Override
    public List<String> getDescription() {
        return List.of("&3%.0f%% &6Увеличение шанса крит. удара".formatted(value));
    }

}
