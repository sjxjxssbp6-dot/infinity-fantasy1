package cupcake.nikky.imperium.tower.item.modifier.process;

import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.modifier.SpecificModifier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
public class AttributeAdditionalModifierProcess implements ModifierProcess {

    @Getter private final List<ImperiumAttribute> attributes;
    private final boolean isNegative;
    private boolean divide;

    public AttributeAdditionalModifierProcess(@NotNull final ImperiumAttribute attribute) {
        this.attributes = Collections.singletonList(attribute);
        this.isNegative = false;
    }

    public AttributeAdditionalModifierProcess(@NotNull final ImperiumAttribute attribute, final boolean isNegative) {
        this.attributes = Collections.singletonList(attribute);
        this.isNegative = isNegative;
    }

    public AttributeAdditionalModifierProcess(@NotNull final ImperiumAttribute attribute, final boolean isNegative, final boolean divide) {
        this.attributes = Collections.singletonList(attribute);
        this.isNegative = isNegative;
        this.divide = divide;
    }

    public AttributeAdditionalModifierProcess(@NotNull final List<ImperiumAttribute> attribute) {
        this.attributes = attribute;
        this.isNegative = false;
    }

    @Override
    public void preGenerate(@NotNull ImperiumItem item, SpecificModifier specificModifier, double key) {
        getAttributes().forEach(currentAttribute -> {
            var multiplier = 1 + quality(item, specificModifier) / 100D;
            item.getAttributes().find(currentAttribute).applyAdditional((isNegative ? -1 : 1) * key / (divide ? 100 : 1) * multiplier);
        });
    }

}
