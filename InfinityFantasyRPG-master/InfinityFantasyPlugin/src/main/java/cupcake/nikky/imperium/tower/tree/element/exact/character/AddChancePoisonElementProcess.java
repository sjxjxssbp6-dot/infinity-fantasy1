package cupcake.nikky.imperium.tower.tree.element.exact.character;

import cupcake.nikky.imperium.tower.attribute.AttributeMap;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.tree.element.TreeElementProcess;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class AddChancePoisonElementProcess extends TreeElementProcess {

    private final double value;

    @Override
    public void onAttributeAdjust(@NotNull AttributeMap attributeMap, int level) {
        attributeMap.find(ImperiumAttribute.CHANCE_TO_POISON).applyBaseValue(value);
    }

    @Override
    public String description() {
        return String.format("+%.0f%% к шансу отравить%n%n&8(Наносит постепенный урон в 30%% от физ. урона%n&8и хаос урона атаки, вызвавшей данное состояние)", value);
    }

}
