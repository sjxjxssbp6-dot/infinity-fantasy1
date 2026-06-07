package cupcake.nikky.imperium.tower.entity.monster.modifier;

import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.entity.monster.Monster;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

@Getter
@RequiredArgsConstructor
public class AttributeModifier {

    private final ImperiumAttribute attribute;
    private final Function<Monster, Double> attributeValue;
    private final ModifierProcess modifierProcess;

    public AttributeModifier(@NotNull final ImperiumAttribute attribute, final double attributeValue, @NotNull final ModifierProcess modifierProcess) {
        this(attribute, monster -> attributeValue, modifierProcess);
    }

    public void applyModifier(@NotNull final Monster monster) {
        modifierProcess.applyAttribute(monster, this);
    }

}
