package cupcake.nikky.imperium.tower.item.modifier.process;

import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.modifier.SpecificModifier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
public class AttributeModifierProcess implements ModifierProcess {

    private final ImperiumAttribute attribute;

    @Override
    public void preGenerate(@NotNull ImperiumItem item, SpecificModifier specificModifier, double key) {
        var multiplier = 1 + quality(item, specificModifier) / 100D;
        item.getAttributes().find(attribute).applyAdditional(key * multiplier);
    }

    @Override
    public Collection<ImperiumAttribute> getAttributes() {
        return Collections.singletonList(attribute);
    }

}
