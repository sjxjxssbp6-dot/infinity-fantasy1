package cupcake.nikky.imperium.tower.item.modifier.exact;

import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.item.ItemType;
import cupcake.nikky.imperium.tower.item.modifier.Modifier;
import cupcake.nikky.imperium.tower.item.modifier.ModifierPlace;
import cupcake.nikky.imperium.tower.item.modifier.ModifierTier;
import cupcake.nikky.imperium.tower.item.modifier.description.ModifierDescriptionOneValue;
import cupcake.nikky.imperium.tower.item.modifier.process.AttributeModifierProcess;
import cupcake.nikky.imperium.tower.item.modifier.range.ModifierRange;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collections;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ModifierToolListDirection {

    public static final Modifier CHANCE_TO_DOUBLE_EXP = Modifier.ofType("tool_chance_to_double_exp_dir", ModifierPlace.DIRECTION,
            new ModifierDescriptionOneValue("%s%.1f%% %sшанс получить двойной опыт инструментом"),
            new AttributeModifierProcess(ImperiumAttribute.INCREASE_TOOL_CHANCE_TO_DOUBLE_EXP),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(750).range(15, 15).requireBaseLevel(5),
                    new ModifierTier().tier(2).weight(750).range(25, 25).requireBaseLevel(6)),
            Collections.singletonList(ItemType.TOOL));

    public static final Modifier CHANCE_TO_DOUBLE_CURRENCY = Modifier.ofType("tool_chance_to_double_currency_dir", ModifierPlace.DIRECTION,
            new ModifierDescriptionOneValue("%s%.1f%% %sшанс получить двойную добычу"),
            new AttributeModifierProcess(ImperiumAttribute.INCREASE_TOOL_CHANCE_TO_DOUBLE_CURRENCY),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(750).range(15, 15).requireBaseLevel(5),
                    new ModifierTier().tier(2).weight(750).range(25, 25).requireBaseLevel(6)),
            Collections.singletonList(ItemType.TOOL));

    public static final Modifier INCREASE_TOOL_POWER = Modifier.ofType("tool_increase_power_dir", ModifierPlace.DIRECTION,
            new ModifierDescriptionOneValue("%s+%.0f %sк силе инструмента"),
            new AttributeModifierProcess(ImperiumAttribute.INCREASE_TOOL_POWER),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(500).range(1, 1).requireBaseLevel(5),
                    new ModifierTier().tier(2).weight(500).range(2, 2).requireBaseLevel(6)),
            Collections.singletonList(ItemType.TOOL));

}
