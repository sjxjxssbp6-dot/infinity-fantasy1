package cupcake.nikky.imperium.tower.item.modifier.exact;

import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.item.ItemType;
import cupcake.nikky.imperium.tower.item.exact.gear.armour.BaseBody;
import cupcake.nikky.imperium.tower.item.exact.gear.armour.BaseLeggings;
import cupcake.nikky.imperium.tower.item.modifier.Modifier;
import cupcake.nikky.imperium.tower.item.modifier.ModifierPlace;
import cupcake.nikky.imperium.tower.item.modifier.ModifierTag;
import cupcake.nikky.imperium.tower.item.modifier.ModifierTier;
import cupcake.nikky.imperium.tower.item.modifier.description.ModifierDescriptionOneValue;
import cupcake.nikky.imperium.tower.item.modifier.process.*;
import cupcake.nikky.imperium.tower.item.modifier.range.ModifierRange;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ModifierItemListDirection {

    public static final Modifier DAMAGE_LEECH_AS_LIFE = Modifier.ofType("dir_str_leech_as_life", ModifierPlace.DIRECTION,
            new ModifierDescriptionOneValue("%s%.1f%% %sурона от атак похищается в виде здоровья"),
            new AttributeModifierProcess(ImperiumAttribute.LEECH_PERCENT),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(1000).range(.2, .4).requireBaseLevel(15),
                    new ModifierTier().tier(2).weight(1000).range(.5, .7).requireBaseLevel(40),
                    new ModifierTier().tier(3).weight(1000).range(.9, 1.2).requireBaseLevel(61)), Arrays.asList(ItemType.WEAPON),
            ModifierTag.LIFE, ModifierTag.PHYSICAL);

    public static final Modifier FLAT_CRITICAL_CHANCE = Modifier.ofType("dir_flat_crit_chance", ModifierPlace.DIRECTION,
            new ModifierDescriptionOneValue("%s+%.1f%% %sк шансу крит. удара"),
            new AttributeModifierProcess(ImperiumAttribute.CRITICAL_RATE),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(1000).range(.5, 1).requireBaseLevel(1),
                    new ModifierTier().tier(2).weight(1000).range(1.1, 1.5).requireBaseLevel(15),
                    new ModifierTier().tier(3).weight(1000).range(1.6, 2).requireBaseLevel(30),
                    new ModifierTier().tier(4).weight(1000).range(2.1, 2.5).requireBaseLevel(50),
                    new ModifierTier().tier(5).weight(1000).range(2.6, 3).requireBaseLevel(66)), Arrays.asList(ItemType.WEAPON),
            ModifierTag.CRITICAL);

    public static final Modifier INCREASE_DROP_QUANTITY_OF_ITEMS = Modifier.ofType("dir_increase_drop_quantity", ModifierPlace.DIRECTION,
           new ModifierDescriptionOneValue("%s%.0f%% %sувеличение выпадаемых предметов"),
            new AttributeAdditionalModifierProcess(ImperiumAttribute.MONSTER_QUANTITY_OF_ITEMS, false, true),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(300).range(3, 6).requireBaseLevel(15),
                    new ModifierTier().tier(2).weight(300).range(7, 10).requireBaseLevel(30),
                    new ModifierTier().tier(3).weight(300).range(11, 14).requireBaseLevel(45),
                    new ModifierTier().tier(4).weight(300).range(15, 18).requireBaseLevel(60),
                    new ModifierTier().tier(5).weight(300).range(19, 22).requireBaseLevel(72)), Arrays.asList(ItemType.ARMOR, ItemType.WEAPON),
            ModifierTag.DROP);

    public static final Modifier GLOBAL_CRITICAL_CHANCE = Modifier.ofType("dir_global_critical_chance", ModifierPlace.DIRECTION,
            new ModifierDescriptionOneValue("%s%.1f%% %sувеличение шанса крит. удара"),
            new AttributeIncreaseModifierProcess(ImperiumAttribute.CRITICAL_RATE),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(1000).range(5, 10).requireBaseLevel(1),
                    new ModifierTier().tier(2).weight(1000).range(11, 15).requireBaseLevel(15),
                    new ModifierTier().tier(3).weight(1000).range(16, 20).requireBaseLevel(40)), Collections.emptyList(), ModifierTag.CRITICAL);

    public static final Modifier GLOBAL_CRITICAL_DAMAGE = Modifier.ofType("dir_global_critical_damage", ModifierPlace.DIRECTION,
            new ModifierDescriptionOneValue("%s%.1f%% %sувеличение крит. урона"),
            new AttributeIncreaseModifierProcess(ImperiumAttribute.CRITICAL_DAMAGE),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(1000).range(5, 10).requireBaseLevel(1),
                    new ModifierTier().tier(2).weight(1000).range(11, 15).requireBaseLevel(15),
                    new ModifierTier().tier(3).weight(1000).range(16, 20).requireBaseLevel(40)), Collections.emptyList(), ModifierTag.CRITICAL, ModifierTag.DAMAGE);

    public static final Modifier FIRE_RESISTANCE = Modifier.ofType("dir_add_fire_resistance", ModifierPlace.DIRECTION,
            new ModifierDescriptionOneValue("%s+%.1f%% %sк сопротивлению огню"),
            new AttributeAdditionalModifierProcess(ImperiumAttribute.FIRE_RESISTANCE),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(1000).range(5, 10).requireBaseLevel(6),
                    new ModifierTier().tier(2).weight(1000).range(11, 15).requireBaseLevel(19),
                    new ModifierTier().tier(3).weight(1000).range(16, 20).requireBaseLevel(44)), Collections.emptyList(), ModifierTag.ELEMENTAL, ModifierTag.RESISTANCE, ModifierTag.FIRE);

    public static final Modifier COLD_RESISTANCE = Modifier.ofType("dir_add_cold_resistance", ModifierPlace.DIRECTION,
            new ModifierDescriptionOneValue("%s+%.1f%% %sк сопротивлению холоду"),
            new AttributeAdditionalModifierProcess(ImperiumAttribute.COLD_RESISTANCE),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(1000).range(5, 10).requireBaseLevel(6),
                    new ModifierTier().tier(2).weight(1000).range(11, 15).requireBaseLevel(19),
                    new ModifierTier().tier(3).weight(1000).range(16, 20).requireBaseLevel(44)), Collections.emptyList(), ModifierTag.ELEMENTAL, ModifierTag.RESISTANCE, ModifierTag.COLD);

    public static final Modifier LIGHTNING_RESISTANCE = Modifier.ofType("dir_add_lightning_resistance", ModifierPlace.DIRECTION,
            new ModifierDescriptionOneValue("%s+%.1f%% %sк сопротивлению молнии"),
            new AttributeAdditionalModifierProcess(ImperiumAttribute.LIGHTNING_RESISTANCE),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(1000).range(5, 10).requireBaseLevel(6),
                    new ModifierTier().tier(2).weight(1000).range(11, 15).requireBaseLevel(19),
                    new ModifierTier().tier(3).weight(1000).range(16, 20).requireBaseLevel(44)), Collections.emptyList(), ModifierTag.ELEMENTAL, ModifierTag.RESISTANCE, ModifierTag.LIGHTNING);

    public static final Modifier CHAOS_RESISTANCE = Modifier.ofType("dir_add_chaos_resistance", ModifierPlace.DIRECTION,
            new ModifierDescriptionOneValue("%s+%.1f%% %sк сопротивлению хаосу"),
            new AttributeAdditionalModifierProcess(ImperiumAttribute.CHAOS_RESISTANCE),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(1000).range(5, 10).requireBaseLevel(6),
                    new ModifierTier().tier(2).weight(1000).range(11, 15).requireBaseLevel(19),
                    new ModifierTier().tier(3).weight(1000).range(16, 20).requireBaseLevel(44)), Collections.emptyList(), ModifierTag.ELEMENTAL, ModifierTag.RESISTANCE, ModifierTag.CHAOS);

    public static final Modifier ALL_ELEMENTAL_RESISTANCE = Modifier.ofType("dir_add_all_elem_res", ModifierPlace.DIRECTION,
            new ModifierDescriptionOneValue("%s+%.1f%% %sк сопротивлению всем стихиям"),
            new AttributeAdditionalModifierProcess(List.of(ImperiumAttribute.COLD_RESISTANCE, ImperiumAttribute.FIRE_RESISTANCE, ImperiumAttribute.LIGHTNING_RESISTANCE)),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(1000).range(1, 3).requireBaseLevel(3),
                    new ModifierTier().tier(2).weight(1000).range(4, 7).requireBaseLevel(18),
                    new ModifierTier().tier(3).weight(1000).range(8, 11).requireBaseLevel(44)), Collections.emptyList(), ModifierTag.ELEMENTAL, ModifierTag.RESISTANCE);

    public static final Modifier ADDITIONAL_MAX_HEALTH = Modifier.ofType("dir_add_max_health", ModifierPlace.DIRECTION,
            new ModifierDescriptionOneValue("%s+%.0f %sк максимуму здоровья"),
            new AttributeModifierProcess(ImperiumAttribute.HEALTH),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(1000).range(10, 15).requireBaseLevel(6),
                    new ModifierTier().tier(2).weight(1000).range(30, 50).requireBaseLevel(28),
                    new ModifierTier().tier(3).weight(1000).range(60, 100).requireBaseLevel(60)), Collections.emptyList(), ModifierTag.LIFE);

    public static final Modifier INCREASED_MANA_RESERVE_EFFICIENCY = Modifier.ofFilter("dir_incr_mana_reserve_eff", ModifierPlace.DIRECTION,
            new ModifierDescriptionOneValue("%s%.0f%% %sувеличение эффект. резервации маны"),
            new AttributeAdditionalModifierProcess(ImperiumAttribute.MANA_RESERVATION_EFFICIENCY, false, true),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(1000).range(2, 3).requireBaseLevel(30),
                    new ModifierTier().tier(2).weight(1000).range(4, 6).requireBaseLevel(72)),
            Collections.singletonList(item -> item.checkInstance(BaseBody.class)),
            ModifierTag.MANA);

    public static final Modifier ADDITIONAL_MANA_REGENERATION = Modifier.ofType("dir_add_regen_mana", ModifierPlace.DIRECTION,
            new ModifierDescriptionOneValue("%s+%.0f %sк регенерации маны"),
            new AttributeModifierProcess(ImperiumAttribute.MANA_REGENERATION),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(500).range(1, 2).requireBaseLevel(24),
                    new ModifierTier().tier(2).weight(500).range(3, 4).requireBaseLevel(44),
                    new ModifierTier().tier(3).weight(500).range(5, 6).requireBaseLevel(66)), Arrays.asList(ItemType.ARMOR),
            ModifierTag.MANA);

    public static final Modifier ADDITIONAL_MAX_MANA = Modifier.ofType("dir_add_mana_max", ModifierPlace.DIRECTION,
            new ModifierDescriptionOneValue("%s+%.0f %sк максимуму маны"),
            new AttributeModifierProcess(ImperiumAttribute.MANA),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(1000).range(10, 14).requireBaseLevel(15),
                    new ModifierTier().tier(2).weight(1000).range(15, 20).requireBaseLevel(30),
                    new ModifierTier().tier(3).weight(1000).range(21, 25).requireBaseLevel(45)), Arrays.asList(ItemType.ARMOR),
            ModifierTag.MANA);

    public static final Modifier INCREASED_MAXIMUM_MANA = Modifier.ofFilter("dir_incr_mana_max", ModifierPlace.DIRECTION,
            new ModifierDescriptionOneValue("%s%.0f%% %sувеличение максимума маны"),
            new AttributeIncreaseModifierProcess(ImperiumAttribute.MANA),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(500).range(1, 4).requireBaseLevel(15),
                    new ModifierTier().tier(2).weight(500).range(5, 8).requireBaseLevel(44)),
            Collections.singletonList(item -> item.checkInstance(BaseLeggings.class)),
            ModifierTag.MANA);

    public static final Modifier ELEMENTAL_DAMAGE = Modifier.ofType("dir_incr_elem_damage", ModifierPlace.DIRECTION,
            new ModifierDescriptionOneValue("%s%.1f%% %sувеличение стихийного урона"),
            new AttributeIncreaseModifierProcess(
                    ImperiumAttribute.MIN_COLD_DAMAGE, ImperiumAttribute.MAX_COLD_DAMAGE,
                    ImperiumAttribute.MIN_FIRE_DAMAGE, ImperiumAttribute.MAX_FIRE_DAMAGE,
                    ImperiumAttribute.MIN_LIGHTNING_DAMAGE, ImperiumAttribute.MAX_LIGHTNING_DAMAGE
            ),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(1000).range(5, 10).requireBaseLevel(6),
                    new ModifierTier().tier(2).weight(1000).range(11, 18).requireBaseLevel(28),
                    new ModifierTier().tier(3).weight(1000).range(19, 25).requireBaseLevel(44)), Collections.emptyList(), ModifierTag.ELEMENTAL, ModifierTag.ELEMENTAL_DAMAGE, ModifierTag.DAMAGE);

    public static final Modifier DEFENSE = Modifier.ofType("dir_add_defense", ModifierPlace.DIRECTION,
            new ModifierDescriptionOneValue("%s+%.0f%% %sк защите"),
            new AttributeModifierProcess(ImperiumAttribute.DEFENSE),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(1000).range(1, 2).requireBaseLevel(1),
                    new ModifierTier().tier(2).weight(1000).range(3, 4).requireBaseLevel(15),
                    new ModifierTier().tier(3).weight(1000).range(5, 6).requireBaseLevel(30),
                    new ModifierTier().tier(4).weight(1000).range(7, 8).requireBaseLevel(50)), Collections.emptyList(), ModifierTag.DEFENCE);

    public static final Modifier REGENERATION_IN_FIGHT = Modifier.ofType("dir_add_regen_in_fight", ModifierPlace.DIRECTION,
            new ModifierDescriptionOneValue("%s+%.0f %sк регенерации здоровья в бою"),
            new AttributeModifierProcess(ImperiumAttribute.REGENERATION_HEALTH_IN_FIGHT),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(1000).range(1, 2).requireBaseLevel(1),
                    new ModifierTier().tier(2).weight(1000).range(3, 8).requireBaseLevel(7),
                    new ModifierTier().tier(3).weight(1000).range(9, 16).requireBaseLevel(19),
                    new ModifierTier().tier(4).weight(900).range(17, 24).requireBaseLevel(31),
                    new ModifierTier().tier(5).weight(600).range(25, 32).requireBaseLevel(44),
                    new ModifierTier().tier(6).weight(500).range(33, 48).requireBaseLevel(61)
            ), Collections.emptyList(), ModifierTag.LIFE);

}
