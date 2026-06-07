package cupcake.nikky.imperium.tower.tree.element.exact.character;

import cupcake.nikky.imperium.tower.attribute.AttributeMap;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.tree.element.TreeElementProcess;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class IncreaseIgniteDamageElementProcess extends TreeElementProcess {

    private final double percent;

    @Override
    public void onAttributeAdjust(@NotNull AttributeMap attributeMap) {
        attributeMap.find(ImperiumAttribute.IGNITE_DAMAGE).applyIncrease(percent / 100D);
    }

    @Override
    public String description() {
        return String.format("%.1f%% увеличение урона от поджога%n%n&8(Базовый урон поджога: 50%% от урона огнем при ударе)", percent);
    }

}
