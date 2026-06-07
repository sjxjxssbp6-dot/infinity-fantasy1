package cupcake.nikky.imperium.tower.tree.element;

import cupcake.nikky.imperium.tower.attribute.AttributeMap;
import org.jetbrains.annotations.NotNull;

public abstract class TreeElementProcess {

    public void onAttributeAdjust(@NotNull final AttributeMap attributeMap) {

    }

    public void onAttributeAdjust(@NotNull final AttributeMap attributeMap, final int level) {
        this.onAttributeAdjust(attributeMap);
    }

    public abstract String description();

}
