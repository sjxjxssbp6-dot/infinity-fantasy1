package cupcake.nikky.imperium.tower.tree.element.exact.character;

import cupcake.nikky.imperium.tower.attribute.AttributeMap;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.tree.element.TreeElementProcess;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class IncreaseElementalDamageElementProcess extends TreeElementProcess {

    private final double percent;

    @Override
    public void onAttributeAdjust(@NotNull AttributeMap attributeMap) {
        attributeMap.find(ImperiumAttribute.MIN_FIRE_DAMAGE).applyIncrease(percent / 100D);
        attributeMap.find(ImperiumAttribute.MAX_FIRE_DAMAGE).applyIncrease(percent / 100D);
        attributeMap.find(ImperiumAttribute.MIN_COLD_DAMAGE).applyIncrease(percent / 100D);
        attributeMap.find(ImperiumAttribute.MAX_COLD_DAMAGE).applyIncrease(percent / 100D);
        attributeMap.find(ImperiumAttribute.MIN_LIGHTNING_DAMAGE).applyIncrease(percent / 100D);
        attributeMap.find(ImperiumAttribute.MAX_LIGHTNING_DAMAGE).applyIncrease(percent / 100D);
    }

    @Override
    public String description() {
        return String.format("%.1f%% увеличение элементального урона при ударе", percent);
    }

}
