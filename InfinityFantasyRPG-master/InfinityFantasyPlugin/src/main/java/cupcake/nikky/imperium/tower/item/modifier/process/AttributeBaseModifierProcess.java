package cupcake.nikky.imperium.tower.item.modifier.process;

import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.modifier.SpecificModifier;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
@AllArgsConstructor
public class AttributeBaseModifierProcess implements ModifierProcess {

    private final ImperiumAttribute attribute;
    private boolean divide;

    @Override
    public void preGenerate(@NotNull ImperiumItem item, SpecificModifier specificModifier, double key) {
        var multiplier = 1 + quality(item, specificModifier) / 100D;
        item.getAttributes().find(attribute).applyBaseValue(key / (divide ? 100 : 1) * multiplier);
    }

    @Override
    public Collection<ImperiumAttribute> getAttributes() {
        return Collections.singletonList(attribute);
    }

}
