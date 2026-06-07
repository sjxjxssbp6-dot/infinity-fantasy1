package cupcake.nikky.imperium.tower.item.modifier.process;

import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.modifier.SpecificModifier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class AttributeIncreaseModifierProcess implements ModifierProcess {

    @Getter private final List<ImperiumAttribute> attributes;

    public AttributeIncreaseModifierProcess(@NotNull final ImperiumAttribute attribute) {
        this.attributes = Collections.singletonList(attribute);
    }

    public AttributeIncreaseModifierProcess(@NotNull final ImperiumAttribute... attributes) {
        this.attributes = Arrays.asList(attributes);
    }

    @Override
    public void preGenerate(@NotNull ImperiumItem item, SpecificModifier specificModifier, double key) {
        getAttributes().forEach(attribute -> {
            var multiplier = 1 + quality(item, specificModifier) / 100D;
            item.getAttributes().find(attribute).applyIncrease(key / 100D * multiplier);
        });
    }

}
