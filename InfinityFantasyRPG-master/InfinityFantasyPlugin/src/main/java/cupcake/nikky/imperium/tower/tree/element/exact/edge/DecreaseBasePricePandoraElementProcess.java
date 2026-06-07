package cupcake.nikky.imperium.tower.tree.element.exact.edge;

import cupcake.nikky.imperium.tower.attribute.AttributeMap;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.tree.element.TreeElementProcess;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class DecreaseBasePricePandoraElementProcess extends TreeElementProcess {

    private final double chance;

    @Override
    public void onAttributeAdjust(@NotNull AttributeMap attributeMap) {
        attributeMap.find(ImperiumAttribute.PANDORA_BASE_PRICE).applyIncrease(-chance / 100D);
    }

    @Override
    public String description() {
        return String.format("%.1f%% уменьшение базовой цены улучшения ящика Пандоры", chance);
    }

}