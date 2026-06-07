package cupcake.nikky.imperium.tower.tree.element.exact.edge;

import cupcake.nikky.imperium.tower.attribute.AttributeMap;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.tree.element.TreeElementProcess;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class IncreaseChanceDropWayFragmentElementProcess extends TreeElementProcess {

    private final double chance;

    @Override
    public void onAttributeAdjust(@NotNull AttributeMap attributeMap) {
        attributeMap.find(ImperiumAttribute.INCREASE_DROP_CHANCE_WAY_FRAGMENT).applyBaseValue(chance);
    }

    @Override
    public String description() {
        return String.format("%.1f%% увеличение шанса выпадения осколка Грани", chance);
    }

}
