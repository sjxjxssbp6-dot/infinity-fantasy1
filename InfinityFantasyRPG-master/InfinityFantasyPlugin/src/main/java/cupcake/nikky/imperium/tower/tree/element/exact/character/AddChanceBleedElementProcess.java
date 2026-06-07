package cupcake.nikky.imperium.tower.tree.element.exact.character;

import cupcake.nikky.imperium.tower.attribute.AttributeMap;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.tree.element.TreeElementProcess;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class AddChanceBleedElementProcess extends TreeElementProcess {

    private final double value;

    @Override
    public void onAttributeAdjust(@NotNull AttributeMap attributeMap, int level) {
        attributeMap.find(ImperiumAttribute.CHANCE_TO_CAUSE_BLEEDING).applyBaseValue(value);
    }

    @Override
    public String description() {
        return String.format("+%.0f%% к шансу вызвать кровотечение%n%n&8(Наносит постепенный урон в 70%% от физ. урона атаки,%n&8вызвавшей данное состояние)", value);
    }

}
