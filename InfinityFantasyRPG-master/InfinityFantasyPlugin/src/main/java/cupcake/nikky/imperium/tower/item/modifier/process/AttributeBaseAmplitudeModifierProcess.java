package cupcake.nikky.imperium.tower.item.modifier.process;

import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.modifier.SpecificModifier;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class AttributeBaseAmplitudeModifierProcess implements ModifierProcess {

    @Getter
    private final List<ImperiumAttribute> attributes;

    public AttributeBaseAmplitudeModifierProcess(@NotNull final ImperiumAttribute minAttribute, @NotNull final ImperiumAttribute maxAttribute) {
        this.attributes = Arrays.asList(minAttribute, maxAttribute);
    }

    public AttributeBaseAmplitudeModifierProcess(@NotNull final ImperiumAttribute... attributes) {
        this.attributes = Arrays.asList(attributes);
        if (getAttributes().size() % 2 != 0) {
            throw new RuntimeException("AttributeBaseMagnitudeModifierProcess size error");
        }
    }

    @Override
    public void preGenerate(@NotNull ImperiumItem item, SpecificModifier specificModifier, double key) {
        var amplitude = specificModifier.getModifier().getRange().findByNumericTier(specificModifier.findTier()).amplitude();

        for (int i = 0; i < getAttributes().size(); i += 2) {
            var min = getAttributes().get(i);
            var max = getAttributes().get(i + 1);

            var multiplier = 1 + quality(item, specificModifier) / 100D;
            item.getAttributes().find(min).applyBaseValue((key - amplitude) * multiplier);
            item.getAttributes().find(max).applyBaseValue((key + amplitude) * multiplier);
        }
    }

}
