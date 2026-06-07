package cupcake.nikky.imperium.tower.tree.element.exact.character;

import cupcake.nikky.imperium.tower.attribute.AttributeMap;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.tree.element.TreeElementProcess;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class IncreaseChaosDamageHitElementProcess extends TreeElementProcess {

    private final double percent;

    @Override
    public void onAttributeAdjust(@NotNull AttributeMap attributeMap) {
        attributeMap.find(ImperiumAttribute.MIN_CHAOS_DAMAGE).applyIncrease(percent / 100D);
        attributeMap.find(ImperiumAttribute.MAX_CHAOS_DAMAGE).applyIncrease(percent / 100D);
    }

    @Override
    public String description() {
        return String.format("%.1f%% увеличение урона хаосом при ударе", percent);
    }

}
