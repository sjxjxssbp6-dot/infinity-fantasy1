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
public final class ModifierToolListExtended {

    public static final Modifier CHANCE_TO_DOUBLE_EXP = Modifier.ofType("tool_chance_to_double_exp", ModifierPlace.SUFFIX,
            new ModifierDescriptionOneValue("%s%.1f%% %sшанс получить двойной опыт инструментом"),
            new AttributeModifierProcess(ImperiumAttribute.INCREASE_TOOL_CHANCE_TO_DOUBLE_EXP),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(1000).range(1, 5).requireBaseLevel(2),
                    new ModifierTier().tier(2).weight(1000).range(6, 10).requireBaseLevel(3),
                    new ModifierTier().tier(3).weight(1000).range(11, 15).requireBaseLevel(4),
                    new ModifierTier().tier(4).weight(1000).range(16, 25).requireBaseLevel(4)),
            Collections.singletonList(ItemType.TOOL));

    public static final Modifier CHANCE_TO_DOUBLE_CURRENCY = Modifier.ofType("tool_chance_to_double_currency", ModifierPlace.PREFIX,
            new ModifierDescriptionOneValue("%s%.1f%% %sшанс получить двойную добычу"),
            new AttributeModifierProcess(ImperiumAttribute.INCREASE_TOOL_CHANCE_TO_DOUBLE_CURRENCY),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(1000).range(1, 5).requireBaseLevel(2),
                    new ModifierTier().tier(2).weight(1000).range(6, 10).requireBaseLevel(3),
                    new ModifierTier().tier(3).weight(1000).range(11, 15).requireBaseLevel(4),
                    new ModifierTier().tier(4).weight(1000).range(16, 25).requireBaseLevel(4)),
            Collections.singletonList(ItemType.TOOL));

    public static final Modifier INCREASE_EXP = Modifier.ofType("tool_increase_exp", ModifierPlace.SUFFIX,
            new ModifierDescriptionOneValue("%s%.1f%% %sувеличение получаемого опыта инструментом"),
            new AttributeModifierProcess(ImperiumAttribute.INCREASE_TOOL_EXP),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(1000).range(1, 5).requireBaseLevel(2),
                    new ModifierTier().tier(2).weight(1000).range(6, 10).requireBaseLevel(2),
                    new ModifierTier().tier(3).weight(1000).range(11, 15).requireBaseLevel(3),
                    new ModifierTier().tier(4).weight(1000).range(16, 25).requireBaseLevel(3),
                    new ModifierTier().tier(5).weight(1000).range(26, 35).requireBaseLevel(4),
                    new ModifierTier().tier(6).weight(1000).range(36, 50).requireBaseLevel(4)),
            Collections.singletonList(ItemType.TOOL));

    public static final Modifier INCREASE_CURRENCY = Modifier.ofType("tool_increase_currency", ModifierPlace.PREFIX,
            new ModifierDescriptionOneValue("%s%.1f%% %sувеличение получаемой добычи"),
            new AttributeModifierProcess(ImperiumAttribute.INCREASE_TOOL_CURRENCY),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(1000).range(20, 50).requireBaseLevel(2),
                    new ModifierTier().tier(2).weight(1000).range(51, 65).requireBaseLevel(2),
                    new ModifierTier().tier(3).weight(1000).range(66, 75).requireBaseLevel(3),
                    new ModifierTier().tier(4).weight(1000).range(76, 85).requireBaseLevel(3),
                    new ModifierTier().tier(5).weight(1000).range(86, 95).requireBaseLevel(4),
                    new ModifierTier().tier(6).weight(1000).range(96, 110).requireBaseLevel(4)),
            Collections.singletonList(ItemType.TOOL));

    public static final Modifier INCREASE_TOOL_POWER = Modifier.ofType("tool_increase_power", ModifierPlace.SUFFIX,
            new ModifierDescriptionOneValue("%s+%.0f %sк силе инструмента"),
            new AttributeModifierProcess(ImperiumAttribute.INCREASE_TOOL_POWER),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(250).range(1, 1).requireBaseLevel(2),
                    new ModifierTier().tier(2).weight(250).range(2, 2).requireBaseLevel(3),
                    new ModifierTier().tier(3).weight(250).range(3, 3).requireBaseLevel(4),
                    new ModifierTier().tier(4).weight(250).range(4, 4).requireBaseLevel(4)),
            Collections.singletonList(ItemType.TOOL));

}
