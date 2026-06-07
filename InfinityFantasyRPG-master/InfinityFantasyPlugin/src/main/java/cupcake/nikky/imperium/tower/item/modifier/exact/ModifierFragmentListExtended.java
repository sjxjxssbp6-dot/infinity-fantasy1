package cupcake.nikky.imperium.tower.item.modifier.exact;

import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.item.ItemType;
import cupcake.nikky.imperium.tower.item.modifier.FragmentModifierTier;
import cupcake.nikky.imperium.tower.item.modifier.Modifier;
import cupcake.nikky.imperium.tower.item.modifier.ModifierPlace;
import cupcake.nikky.imperium.tower.item.modifier.description.ModifierDescriptionOneValue;
import cupcake.nikky.imperium.tower.item.modifier.description.ModifierDescriptionWithoutValue;
import cupcake.nikky.imperium.tower.item.modifier.process.AttributeAdditionalModifierProcess;
import cupcake.nikky.imperium.tower.item.modifier.process.AttributeModifierProcess;
import cupcake.nikky.imperium.tower.item.modifier.range.ModifierRange;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ModifierFragmentListExtended {

    public static final Modifier CANNOT_DEAL_CRITICAL_STRIKES = Modifier.ofType("cannot_deal_critical_strikes", ModifierPlace.PREFIX,
            new ModifierDescriptionWithoutValue("%sВы не можете нанести критический удар"),
            new AttributeModifierProcess(ImperiumAttribute.FRAGMENT_CANNOT_DEAL_CRITICAL_STRIKE),
            ModifierRange.of(
                    new FragmentModifierTier().dropQuantity(30).tier(1).weight(500).range(1, 1).requireBaseLevel(7)
            ), Arrays.asList(ItemType.EDGE_FRAGMENT)
    );

    public static final Modifier CANNOT_REGENERATE_HEALTH_AND_MANA = Modifier.ofType("cannot_regen_health_mana", ModifierPlace.SUFFIX,
            new ModifierDescriptionWithoutValue("%sВы не можете регенерировать здоровье и ману"),
            new AttributeModifierProcess(ImperiumAttribute.FRAGMENT_CANNOT_REGENERATE_HEALTH_AND_MANA),
            ModifierRange.of(
                    new FragmentModifierTier().dropQuantity(25).tier(1).weight(500).range(1, 1).requireBaseLevel(4)
            ), Arrays.asList(ItemType.EDGE_FRAGMENT)
    );

    public static final Modifier REFLECT_PHYSICAL_DAMAGE = Modifier.ofType("reflect_phys_dmg", ModifierPlace.PREFIX,
            new ModifierDescriptionOneValue("%s%.0f%% %sотражение физического урона"),
            new AttributeAdditionalModifierProcess(ImperiumAttribute.FRAGMENT_REFLECT_PHYSICAL_DAMAGE),
            ModifierRange.of(
                    new FragmentModifierTier().dropQuantity(15).tier(1).weight(1000).range(13, 13).requireBaseLevel(1),
                    new FragmentModifierTier().dropQuantity(23).tier(2).weight(1000).range(15, 15).requireBaseLevel(4),
                    new FragmentModifierTier().dropQuantity(30).tier(3).weight(1000).range(18, 18).requireBaseLevel(7)
            ), Arrays.asList(ItemType.EDGE_FRAGMENT)
    );

    public static final Modifier REFLECT_ELEMENTAL_DAMAGE = Modifier.ofType("reflect_elem_dmg", ModifierPlace.PREFIX,
            new ModifierDescriptionOneValue("%s%.0f%% %sотражение элементального урона"),
            new AttributeAdditionalModifierProcess(ImperiumAttribute.FRAGMENT_REFLECT_ELEMENTAL_DAMAGE),
            ModifierRange.of(
                    new FragmentModifierTier().dropQuantity(15).tier(1).weight(1000).range(13, 13).requireBaseLevel(1),
                    new FragmentModifierTier().dropQuantity(23).tier(2).weight(1000).range(15, 15).requireBaseLevel(4),
                    new FragmentModifierTier().dropQuantity(30).tier(3).weight(1000).range(18, 18).requireBaseLevel(7)
            ), Arrays.asList(ItemType.EDGE_FRAGMENT)
    );

    public static final Modifier DECREASE_ATTACK_SPEED = Modifier.ofType("decrease_speed_attack", ModifierPlace.PREFIX,
            new ModifierDescriptionOneValue("%s%.0f%% %sуменьшение скорости атаки"),
            new AttributeAdditionalModifierProcess(ImperiumAttribute.ATTACK_SPEED, true),
            ModifierRange.of(
                    new FragmentModifierTier().dropQuantity(10).tier(1).weight(1000).range(5, 10).requireBaseLevel(1),
                    new FragmentModifierTier().dropQuantity(15).tier(2).weight(1000).range(11, 17).requireBaseLevel(4),
                    new FragmentModifierTier().dropQuantity(25).tier(3).weight(1000).range(18, 22).requireBaseLevel(7)
            ), Arrays.asList(ItemType.EDGE_FRAGMENT)
    );

    public static final Modifier DECREASE_MOVEMENT_SPEED = Modifier.ofType("decrease_movement_speed", ModifierPlace.SUFFIX,
            new ModifierDescriptionOneValue("%s%.0f%% %sуменьшение скорости передвижения"),
            new AttributeAdditionalModifierProcess(ImperiumAttribute.MOVEMENT_SPEED, true),
            ModifierRange.of(
                    new FragmentModifierTier().dropQuantity(10).tier(1).weight(1000).range(10, 20).requireBaseLevel(1),
                    new FragmentModifierTier().dropQuantity(15).tier(2).weight(1000).range(21, 28).requireBaseLevel(4),
                    new FragmentModifierTier().dropQuantity(25).tier(3).weight(1000).range(29, 40).requireBaseLevel(7)
            ), Arrays.asList(ItemType.EDGE_FRAGMENT)
    );

    public static final Modifier DECREASE_ALL_DAMAGE = Modifier.ofType("decrease_all_damage", ModifierPlace.PREFIX,
            new ModifierDescriptionOneValue("%s%.0f%% %sуменьшение урона"),
            new AttributeAdditionalModifierProcess(
                    List.of(
                            ImperiumAttribute.MIN_PHYSICAL_DAMAGE, ImperiumAttribute.MAX_PHYSICAL_DAMAGE,
                            ImperiumAttribute.MIN_COLD_DAMAGE, ImperiumAttribute.MAX_COLD_DAMAGE,
                            ImperiumAttribute.MIN_FIRE_DAMAGE, ImperiumAttribute.MAX_FIRE_DAMAGE,
                            ImperiumAttribute.MIN_LIGHTNING_DAMAGE, ImperiumAttribute.MAX_LIGHTNING_DAMAGE,
                            ImperiumAttribute.MIN_CHAOS_DAMAGE, ImperiumAttribute.MAX_CHAOS_DAMAGE
                    ), true),
            ModifierRange.of(
                    new FragmentModifierTier().dropQuantity(10).tier(1).weight(1000).range(5, 10).requireBaseLevel(1),
                    new FragmentModifierTier().dropQuantity(15).tier(2).weight(1000).range(11, 20).requireBaseLevel(4),
                    new FragmentModifierTier().dropQuantity(25).tier(3).weight(1000).range(21, 30).requireBaseLevel(7)
            ), Arrays.asList(ItemType.EDGE_FRAGMENT)
    );

    public static final Modifier CHANCE_GET_DOUBLE_DAMAGE = Modifier.ofType("chance_double_damage", ModifierPlace.SUFFIX,
            new ModifierDescriptionOneValue("%s%.0f%% %sшанс получить двойной урон"),
            new AttributeAdditionalModifierProcess(ImperiumAttribute.FRAGMENT_CHANCE_GET_DOUBLE_DAMAGE),
            ModifierRange.of(
                    new FragmentModifierTier().dropQuantity(10).tier(1).weight(1000).range(5, 7).requireBaseLevel(1),
                    new FragmentModifierTier().dropQuantity(18).tier(2).weight(1000).range(8, 15).requireBaseLevel(4),
                    new FragmentModifierTier().dropQuantity(30).tier(3).weight(1000).range(16, 25).requireBaseLevel(7)
            ), Arrays.asList(ItemType.EDGE_FRAGMENT)
    );

    public static final Modifier INCREASE_INPUT_FIRE_DAMAGE = Modifier.ofType("inc_input_fire_damage", ModifierPlace.PREFIX,
            new ModifierDescriptionOneValue("%s%.0f%% %sот физ. урона наносится дополнительно огнем"),
            new AttributeAdditionalModifierProcess(ImperiumAttribute.FRAGMENT_INCREASE_INPUT_FIRE_DAMAGE),
            ModifierRange.of(
                    new FragmentModifierTier().dropQuantity(16).tier(1).weight(1000).range(50, 69).requireBaseLevel(1),
                    new FragmentModifierTier().dropQuantity(25).tier(2).weight(1000).range(70, 89).requireBaseLevel(4),
                    new FragmentModifierTier().dropQuantity(34).tier(3).weight(1000).range(90, 110).requireBaseLevel(7)
            ), Arrays.asList(ItemType.EDGE_FRAGMENT)
    );

    public static final Modifier INCREASE_INPUT_COLD_DAMAGE = Modifier.ofType("inc_input_cold_damage", ModifierPlace.PREFIX,
            new ModifierDescriptionOneValue("%s%.0f%% %sот физ. урона наносится дополнительно холодом"),
            new AttributeAdditionalModifierProcess(ImperiumAttribute.FRAGMENT_INCREASE_INPUT_COLD_DAMAGE),
            ModifierRange.of(
                    new FragmentModifierTier().dropQuantity(16).tier(1).weight(1000).range(50, 69).requireBaseLevel(1),
                    new FragmentModifierTier().dropQuantity(25).tier(2).weight(1000).range(70, 89).requireBaseLevel(4),
                    new FragmentModifierTier().dropQuantity(34).tier(3).weight(1000).range(90, 110).requireBaseLevel(7)
            ), Arrays.asList(ItemType.EDGE_FRAGMENT)
    );

    public static final Modifier INCREASE_INPUT_LIGHTNING_DAMAGE = Modifier.ofType("inc_input_lightning_damage", ModifierPlace.PREFIX,
            new ModifierDescriptionOneValue("%s%.0f%% %sот физ. урона наносится дополнительно молнией"),
            new AttributeAdditionalModifierProcess(ImperiumAttribute.FRAGMENT_INCREASE_INPUT_LIGHTNING_DAMAGE),
            ModifierRange.of(
                    new FragmentModifierTier().dropQuantity(16).tier(1).weight(1000).range(50, 69).requireBaseLevel(1),
                    new FragmentModifierTier().dropQuantity(25).tier(2).weight(1000).range(70, 89).requireBaseLevel(4),
                    new FragmentModifierTier().dropQuantity(34).tier(3).weight(1000).range(90, 110).requireBaseLevel(7)
            ), Arrays.asList(ItemType.EDGE_FRAGMENT)
    );

    public static final Modifier INCREASE_INPUT_CHAOS_DAMAGE = Modifier.ofType("inc_input_chaos_damage", ModifierPlace.PREFIX,
            new ModifierDescriptionOneValue("%s%.0f%% %sот физ. урона наносится дополнительно хаосом"),
            new AttributeAdditionalModifierProcess(ImperiumAttribute.FRAGMENT_INCREASE_INPUT_CHAOS_DAMAGE),
            ModifierRange.of(
                    new FragmentModifierTier().dropQuantity(16).tier(1).weight(500).range(21, 25).requireBaseLevel(4),
                    new FragmentModifierTier().dropQuantity(26).tier(2).weight(500).range(31, 35).requireBaseLevel(7)
            ), Arrays.asList(ItemType.EDGE_FRAGMENT)
    );

    public static final Modifier DECREASE_FIRE_RESISTANCE = Modifier.ofType("dec_fire_resistance", ModifierPlace.SUFFIX,
            new ModifierDescriptionOneValue("%s%.0f%% %sк сопротивлению огню"),
            new AttributeAdditionalModifierProcess(ImperiumAttribute.FIRE_RESISTANCE),
            ModifierRange.of(
                    new FragmentModifierTier().dropQuantity(11).tier(1).weight(1000).range(-5, -10).requireBaseLevel(1),
                    new FragmentModifierTier().dropQuantity(18).tier(2).weight(1000).range(-11, -17).requireBaseLevel(4),
                    new FragmentModifierTier().dropQuantity(26).tier(3).weight(1000).range(-18, -25).requireBaseLevel(7)
            ), Arrays.asList(ItemType.EDGE_FRAGMENT)
    );

    public static final Modifier DECREASE_COLD_RESISTANCE = Modifier.ofType("dec_cold_resistance", ModifierPlace.SUFFIX,
            new ModifierDescriptionOneValue("%s%.0f%% %sк сопротивлению холоду"),
            new AttributeAdditionalModifierProcess(ImperiumAttribute.COLD_RESISTANCE),
            ModifierRange.of(
                    new FragmentModifierTier().dropQuantity(11).tier(1).weight(1000).range(-5, -10).requireBaseLevel(1),
                    new FragmentModifierTier().dropQuantity(18).tier(2).weight(1000).range(-11, -17).requireBaseLevel(4),
                    new FragmentModifierTier().dropQuantity(26).tier(3).weight(1000).range(-18, -25).requireBaseLevel(7)
            ), Arrays.asList(ItemType.EDGE_FRAGMENT)
    );

    public static final Modifier DECREASE_LIGHTNING_RESISTANCE = Modifier.ofType("dec_lightning_resistance", ModifierPlace.SUFFIX,
            new ModifierDescriptionOneValue("%s%.0f%% %sк сопротивлению молнии"),
            new AttributeAdditionalModifierProcess(ImperiumAttribute.LIGHTNING_RESISTANCE),
            ModifierRange.of(
                    new FragmentModifierTier().dropQuantity(11).tier(1).weight(1000).range(-5, -10).requireBaseLevel(1),
                    new FragmentModifierTier().dropQuantity(18).tier(2).weight(1000).range(-11, -17).requireBaseLevel(4),
                    new FragmentModifierTier().dropQuantity(26).tier(3).weight(1000).range(-18, -25).requireBaseLevel(7)
            ), Arrays.asList(ItemType.EDGE_FRAGMENT)
    );

    public static final Modifier DECREASE_CHAOS_RESISTANCE = Modifier.ofType("dec_chaos_resistance", ModifierPlace.SUFFIX,
            new ModifierDescriptionOneValue("%s%.0f%% %sк сопротивлению хаосу"),
            new AttributeAdditionalModifierProcess(ImperiumAttribute.CHAOS_RESISTANCE),
            ModifierRange.of(
                    new FragmentModifierTier().dropQuantity(11).tier(1).weight(1000).range(-5, -10).requireBaseLevel(1),
                    new FragmentModifierTier().dropQuantity(18).tier(2).weight(1000).range(-11, -17).requireBaseLevel(4),
                    new FragmentModifierTier().dropQuantity(26).tier(3).weight(1000).range(-18, -25).requireBaseLevel(7)
            ), Arrays.asList(ItemType.EDGE_FRAGMENT)
    );

    public static final Modifier DECREASE_ALL_MAXIMUM_RESISTANCES = Modifier.ofType("dec_all_max_resistances", ModifierPlace.SUFFIX,
            new ModifierDescriptionOneValue("%s%.0f%% %sк максимуму всех сопротивлений"),
            new AttributeAdditionalModifierProcess(List.of(
                    ImperiumAttribute.FIRE_RESISTANCE_MAXIMUM, ImperiumAttribute.COLD_RESISTANCE_MAXIMUM,
                    ImperiumAttribute.LIGHTNING_RESISTANCE_MAXIMUM, ImperiumAttribute.CHAOS_RESISTANCE_MAXIMUM
            )),
            ModifierRange.of(
                    new FragmentModifierTier().dropQuantity(18).tier(1).weight(600).range(-5, -8).requireBaseLevel(4),
                    new FragmentModifierTier().dropQuantity(26).tier(2).weight(600).range(-9, -12).requireBaseLevel(7)
            ), Arrays.asList(ItemType.EDGE_FRAGMENT)
    );

    public static final Modifier DECREASE_ALL_RESISTANCES = Modifier.ofType("dec_all_resistances", ModifierPlace.SUFFIX,
            new ModifierDescriptionOneValue("%s%.0f%% %sк максимуму всех сопротивлений"),
            new AttributeAdditionalModifierProcess(List.of(
                    ImperiumAttribute.FIRE_RESISTANCE, ImperiumAttribute.COLD_RESISTANCE,
                    ImperiumAttribute.LIGHTNING_RESISTANCE, ImperiumAttribute.CHAOS_RESISTANCE
            )),
            ModifierRange.of(
                    new FragmentModifierTier().dropQuantity(15).tier(1).weight(1000).range(-5, -8).requireBaseLevel(1),
                    new FragmentModifierTier().dropQuantity(25).tier(2).weight(1000).range(-9, -14).requireBaseLevel(4),
                    new FragmentModifierTier().dropQuantity(34).tier(3).weight(1000).range(-15, -18).requireBaseLevel(7)
            ), Arrays.asList(ItemType.EDGE_FRAGMENT)
    );

}
