package cupcake.nikky.imperium.tower.tree.element.exact.character;

import cupcake.nikky.imperium.tower.attribute.AttributeMap;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.tree.element.TreeElementProcess;
import org.jetbrains.annotations.NotNull;

public class ConvertElementStrToStrElementProcess extends TreeElementProcess {

    @Override
    public void onAttributeAdjust(@NotNull AttributeMap attributeMap) {
        attributeMap.find(ImperiumAttribute.CONVERT_ELEMENT_DAMAGE_INTO_PHYS_DAMAGE).applyBaseValue(1);
    }

    @Override
    public String description() {
        return "Конвертирует элементальный урон в физический";
    }

}
