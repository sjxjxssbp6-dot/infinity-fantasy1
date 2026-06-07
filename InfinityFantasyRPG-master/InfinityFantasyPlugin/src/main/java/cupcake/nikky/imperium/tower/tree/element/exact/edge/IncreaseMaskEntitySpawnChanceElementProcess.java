package cupcake.nikky.imperium.tower.tree.element.exact.edge;

import cupcake.nikky.imperium.tower.attribute.AttributeMap;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.tree.element.TreeElementProcess;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class IncreaseMaskEntitySpawnChanceElementProcess extends TreeElementProcess {

    private final double percent;

    @Override
    public void onAttributeAdjust(@NotNull AttributeMap attributeMap) {
        attributeMap.find(ImperiumAttribute.MASK_ENTITY_SPAWN_CHANCE).applyIncrease(percent / 100D);
    }

    @Override
    public String description() {
        return String.format(
                "%.1f%% увеличение шанса появления Проводника Грани" +
                "%n%n&8(Проводник Грани - монстр, который с некоторым шансом появляется на осколках Грани)" +
                "%n&8(Стандартный шанс появления Проводника Грани - 1%%)" +
                "%n&8(Является событием)", percent
        );
    }

}
