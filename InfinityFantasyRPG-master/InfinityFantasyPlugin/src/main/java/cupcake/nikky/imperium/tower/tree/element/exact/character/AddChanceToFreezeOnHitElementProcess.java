package cupcake.nikky.imperium.tower.tree.element.exact.character;

import cupcake.nikky.imperium.tower.attribute.AttributeMap;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.tree.element.TreeElementProcess;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class AddChanceToFreezeOnHitElementProcess extends TreeElementProcess {

    private final double value;

    @Override
    public void onAttributeAdjust(@NotNull AttributeMap attributeMap) {
        attributeMap.find(ImperiumAttribute.CHANCE_TO_FREEZE_ON_HIT).applyBaseValue(value);
    }

    @Override
    public String description() {
        return String.format("+%.1f%% к шансу наложить заморозку при ударе", value);
    }

}
