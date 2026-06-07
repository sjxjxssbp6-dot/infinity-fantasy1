package cupcake.nikky.imperium.tower.tree.element.exact.character;

import cupcake.nikky.imperium.tower.attribute.AttributeMap;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.tree.element.TreeElementProcess;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class AddHealthElementProcess extends TreeElementProcess {

    private final int health;

    @Override
    public void onAttributeAdjust(@NotNull AttributeMap attributeMap) {
        attributeMap.find(ImperiumAttribute.HEALTH).applyBaseValue(health);
    }

    @Override
    public String description() {
        return String.format("+%d к здоровью", health);
    }

}
