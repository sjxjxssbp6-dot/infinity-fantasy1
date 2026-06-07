package cupcake.nikky.imperium.tower.tree.element.exact.character;

import cupcake.nikky.imperium.tower.attribute.AttributeMap;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.tree.element.TreeElementProcess;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class AddAccuracyElementProcess extends TreeElementProcess {

    private final double value;

    @Override
    public void onAttributeAdjust(@NotNull AttributeMap attributeMap, int level) {
        attributeMap.find(ImperiumAttribute.ACCURACY).applyBaseValue(value);
    }

    @Override
    public String description() {
        return String.format("+%.0f%% к точности", value);
    }

}