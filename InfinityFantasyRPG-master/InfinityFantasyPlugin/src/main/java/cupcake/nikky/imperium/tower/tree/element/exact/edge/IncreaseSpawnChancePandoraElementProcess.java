package cupcake.nikky.imperium.tower.tree.element.exact.edge;

import cupcake.nikky.imperium.tower.attribute.AttributeMap;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.tree.element.TreeElementProcess;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class IncreaseSpawnChancePandoraElementProcess extends TreeElementProcess {

    private final double chance;

    @Override
    public void onAttributeAdjust(@NotNull AttributeMap attributeMap) {
        attributeMap.find(ImperiumAttribute.PANDORA_CHANCE).applyIncrease(chance / 100D);
    }

    @Override
    public String description() {
        return String.format("%.1f%% увеличение шанса появления ящика Пандоры" +
                "%n%n&8(Ящик Пандоры - сундук, что появляется с некоторым шансом при убийстве монстров)" +
                "%n&8(Стандартный шанс появления ящика Пандоры - 1%%)" +
                "%n&8(Является событием)", chance
        );
    }

}
