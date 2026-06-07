package cupcake.nikky.imperium.tower.item.modifier.exact;

import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.item.ItemType;
import cupcake.nikky.imperium.tower.item.exact.gear.armour.BaseBody;
import cupcake.nikky.imperium.tower.item.exact.gear.armour.BaseBoots;
import cupcake.nikky.imperium.tower.item.exact.gear.armour.BaseHelmet;
import cupcake.nikky.imperium.tower.item.exact.gear.armour.BaseLeggings;
import cupcake.nikky.imperium.tower.item.modifier.*;
import cupcake.nikky.imperium.tower.item.modifier.description.ModifierDescriptionOneValue;
import cupcake.nikky.imperium.tower.item.modifier.description.ModifierDescriptionTwoValue;
import cupcake.nikky.imperium.tower.item.modifier.process.*;
import cupcake.nikky.imperium.tower.item.modifier.range.ModifierRange;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ModifierItemListExtended {

    public static final Modifier ADD_PHYSICAL_DAMAGE = Modifier.ofType("add_str_damage", ModifierPlace.PREFIX,
            new ModifierDescriptionTwoValue("%sДобавляет %s%.0f-%.0f %sк физическому урону"),
            new AttributeBaseAmplitudeModifierProcess(ImperiumAttribute.MIN_PHYSICAL_DAMAGE, ImperiumAttribute.MAX_PHYSICAL_DAMAGE),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(1000).range(3, 4).amplitude(1).requireBaseLevel(2),
                    new ModifierTier().tier(2).weight(1000).range(10, 12).amplitude(3).requireBaseLevel(13),
                    new ModifierTier().tier(3).weight(1000).range(20, 24).amplitude(8).requireBaseLevel(21),
                    new ModifierTier().tier(4).weight(1000).range(28, 34).amplitude(6).requireBaseLevel(29),
                    new ModifierTier().tier(5).weight(800).range(35, 40).amplitude(11).requireBaseLevel(36),
                    new ModifierTier().tier(6).weight(600).range(41, 50).amplitude(11).requireBaseLevel(46),
                    new ModifierTier().tier(7).weight(400).range(51, 60).amplitude(13).requireBaseLevel(54),
                    new ModifierTier().tier(8).weight(200).range(61, 72).amplitude(12).requireBaseLevel(65),
                    new ModifierTier().tier(9).exalted(true).weight(1000).range(73, 84).amplitude(6).requireBaseLevel(60)
            ), Arrays.asList(ItemType.WEAPON),
            ModifierTag.DAMAGE, ModifierTag.PHYSICAL, ModifierTag.ATTACK);

    public static final Modifier ADD_FIRE_DAMAGE = Modifier.ofType("add_fire_damage", ModifierPlace.PREFIX,
            new ModifierDescriptionTwoValue("%sДобавляет %s%.0f-%.0f %sк урону огнем"),
            new AttributeBaseAmplitudeModifierProcess(ImperiumAttribute.MIN_FIRE_DAMAGE, ImperiumAttribute.MAX_FIRE_DAMAGE),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(1200).range(4, 6).amplitude(1).requireBaseLevel(1),
                    new ModifierTier().tier(2).weight(1200).range(20, 29).amplitude(5).requireBaseLevel(11),
                    new ModifierTier().tier(3).weight(1200).range(31, 47).amplitude(9).requireBaseLevel(18),
                    new ModifierTier().tier(4).weight(1200).range(44, 65).amplitude(11).requireBaseLevel(26),
                    new ModifierTier().tier(5).weight(1200).range(66, 91).amplitude(15).requireBaseLevel(33),
                    new ModifierTier().tier(6).weight(1200).range(92, 128).amplitude(20).requireBaseLevel(42),
                    new ModifierTier().tier(7).weight(1200).range(129, 172).amplitude(28).requireBaseLevel(51),
                    new ModifierTier().tier(8).weight(960).range(173, 223).amplitude(37).requireBaseLevel(62),
                    new ModifierTier().tier(9).weight(420).range(224, 279).amplitude(46).requireBaseLevel(70),
                    new ModifierTier().tier(10).weight(108).range(280, 335).amplitude(55).requireBaseLevel(75),
                    new ModifierTier().tier(11).exalted(true).weight(1200).range(336, 400).amplitude(40).requireBaseLevel(60)
            ),
            Collections.singletonList(ItemType.WEAPON),
            ModifierTag.DAMAGE, ModifierTag.ELEMENTAL, ModifierTag.ELEMENTAL_DAMAGE, ModifierTag.ATTACK, ModifierTag.FIRE);

    public static final Modifier ADD_COLD_DAMAGE = Modifier.ofType("add_cold_damage", ModifierPlace.PREFIX,
            new ModifierDescriptionTwoValue("%sДобавляет %s%.0f-%.0f %sк урону холодом"),
            new AttributeBaseAmplitudeModifierProcess(ImperiumAttribute.MIN_COLD_DAMAGE, ImperiumAttribute.MAX_COLD_DAMAGE),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(1200).range(4, 6).amplitude(1).requireBaseLevel(1),
                    new ModifierTier().tier(2).weight(1200).range(20, 29).amplitude(5).requireBaseLevel(11),
                    new ModifierTier().tier(3).weight(1200).range(31, 47).amplitude(9).requireBaseLevel(18),
                    new ModifierTier().tier(4).weight(1200).range(44, 65).amplitude(11).requireBaseLevel(26),
                    new ModifierTier().tier(5).weight(1200).range(66, 91).amplitude(15).requireBaseLevel(33),
                    new ModifierTier().tier(6).weight(1200).range(92, 128).amplitude(20).requireBaseLevel(42),
                    new ModifierTier().tier(7).weight(1200).range(129, 172).amplitude(28).requireBaseLevel(51),
                    new ModifierTier().tier(8).weight(960).range(173, 223).amplitude(37).requireBaseLevel(62),
                    new ModifierTier().tier(9).weight(420).range(224, 279).amplitude(46).requireBaseLevel(70),
                    new ModifierTier().tier(10).weight(108).range(280, 335).amplitude(55).requireBaseLevel(75),
                    new ModifierTier().tier(11).exalted(true).weight(1200).range(336, 400).amplitude(40).requireBaseLevel(60)
            ),
            Collections.singletonList(ItemType.WEAPON),
            ModifierTag.DAMAGE, ModifierTag.ELEMENTAL, ModifierTag.ELEMENTAL_DAMAGE, ModifierTag.ATTACK, ModifierTag.COLD);

    public static final Modifier ADD_LIGHTNING_DAMAGE = Modifier.ofType("add_lightning_damage", ModifierPlace.PREFIX,
            new ModifierDescriptionTwoValue("%sДобавляет %s%.0f-%.0f %sк урону молнией"),
            new AttributeBaseAmplitudeModifierProcess(ImperiumAttribute.MIN_LIGHTNING_DAMAGE, ImperiumAttribute.MAX_LIGHTNING_DAMAGE),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(1200).range(4, 6).amplitude(1).requireBaseLevel(1),
                    new ModifierTier().tier(2).weight(1200).range(20, 29).amplitude(5).requireBaseLevel(11),
                    new ModifierTier().tier(3).weight(1200).range(31, 47).amplitude(9).requireBaseLevel(18),
                    new ModifierTier().tier(4).weight(1200).range(44, 65).amplitude(11).requireBaseLevel(26),
                    new ModifierTier().tier(5).weight(1200).range(66, 91).amplitude(15).requireBaseLevel(33),
                    new ModifierTier().tier(6).weight(1200).range(92, 128).amplitude(20).requireBaseLevel(42),
                    new ModifierTier().tier(7).weight(1200).range(129, 172).amplitude(28).requireBaseLevel(51),
                    new ModifierTier().tier(8).weight(960).range(173, 223).amplitude(37).requireBaseLevel(62),
                    new ModifierTier().tier(9).weight(420).range(224, 279).amplitude(46).requireBaseLevel(70),
                    new ModifierTier().tier(10).weight(108).range(280, 335).amplitude(55).requireBaseLevel(75),
                    new ModifierTier().tier(11).exalted(true).weight(1200).range(336, 400).amplitude(40).requireBaseLevel(60)
                    ),
            Collections.singletonList(ItemType.WEAPON),
            ModifierTag.DAMAGE, ModifierTag.ELEMENTAL, ModifierTag.ELEMENTAL_DAMAGE, ModifierTag.ATTACK, ModifierTag.LIGHTNING);

    public static final Modifier ADD_CHAOS_DAMAGE = Modifier.ofType("add_chaos_damage", ModifierPlace.PREFIX,
            new ModifierDescriptionTwoValue("%sДобавляет %s%.0f-%.0f %sк урону хаосом"),
            new AttributeBaseAmplitudeModifierProcess(ImperiumAttribute.MIN_CHAOS_DAMAGE, ImperiumAttribute.MAX_CHAOS_DAMAGE),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(250).range(149, 183).amplitude(90).requireBaseLevel(75),
                    new ModifierTier().tier(2).exalted(true).weight(250).range(184, 240).amplitude(90).requireBaseLevel(60)
            ),
            Collections.singletonList(ItemType.WEAPON),
            ModifierTag.DAMAGE, ModifierTag.ATTACK, ModifierTag.CHAOS);

    public static final Modifier DAMAGE_LEECH_AS_LIFE = Modifier.ofType("leech_as_life", ModifierPlace.PREFIX,
            new ModifierDescriptionOneValue("%s%.1f%% %sурона от атак похищается в виде здоровья"),
            new AttributeModifierProcess(ImperiumAttribute.LEECH_PERCENT),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(1000).range(.4, .8).requireBaseLevel(25),
                    new ModifierTier().tier(2).weight(1000).range(.9, 1.2).requireBaseLevel(50),
                    new ModifierTier().tier(3).weight(500).canGetViaCraft(false).range(1.3, 1.8).requireBaseLevel(61),
                    new ModifierTier().tier(4).weight(500).exalted(true).canGetViaCraft(false).range(2, 3).requireBaseLevel(60)
            ), Arrays.asList(ItemType.WEAPON),
            ModifierTag.LIFE, ModifierTag.PHYSICAL);

    public static final Modifier ADDITIONAL_MAX_HEALTH = Modifier.ofType("add_max_health", ModifierPlace.PREFIX,
            new ModifierDescriptionOneValue("%s+%.0f %sк максимуму здоровья"),
            new AttributeModifierProcess(ImperiumAttribute.HEALTH),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(1000).range(3, 9).requireBaseLevel(1),
                    new ModifierTier().tier(2).weight(1000).range(10, 24).requireBaseLevel(5),
                    new ModifierTier().tier(3).weight(1000).range(25, 39).requireBaseLevel(11),
                    new ModifierTier().tier(4).weight(1000).range(40, 54).requireBaseLevel(18),
                    new ModifierTier().tier(5).weight(1000).range(55, 69).requireBaseLevel(24),
                    new ModifierTier().tier(6).weight(1000).range(70, 84).requireBaseLevel(30),
                    new ModifierTier().tier(7).weight(1000).range(85, 99).requireBaseLevel(36),
                    new ModifierTier().tier(8).weight(1000).range(100, 114).requireBaseLevel(44),
                    new ModifierTier().tier(9).weight(1000).range(115, 129).requireBaseLevel(54),
                    new ModifierTier().tier(10).weight(1000).range(130, 144).requireBaseLevel(64),
                    new ModifierTier().tier(11).weight(1000).range(145, 159).requireBaseLevel(73),
                    new ModifierTier().tier(12).weight(1000).range(160, 174).requireBaseLevel(81),
                    new ModifierTier().tier(13).weight(1000).range(175, 189).requireBaseLevel(86),
                    new ModifierTier().tier(14).exalted(true).weight(1000).range(190, 210).requireBaseLevel(60)
            ), Arrays.asList(ItemType.ARMOR),
            ModifierTag.LIFE);

    public static final Modifier INCREASE_MAX_HEALTH = Modifier.ofType("increase_max_health", ModifierPlace.PREFIX,
            new ModifierDescriptionOneValue("%s%.0f%% %sувеличение максимального здоровья"),
            new AttributeIncreaseModifierProcess(ImperiumAttribute.HEALTH),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(400).range(1, 4).requireBaseLevel(50),
                    new ModifierTier().tier(2).weight(400).range(5, 8).requireBaseLevel(68),
                    new ModifierTier().tier(3).weight(400).range(9, 12).requireBaseLevel(85),
                    new ModifierTier().tier(4).exalted(true).weight(400).range(13, 20).requireBaseLevel(60)
            ), Arrays.asList(ItemType.ARMOR),
            ModifierTag.LIFE);

    public static final Modifier INCREASE_DROP_QUANTITY_OF_ITEMS = Modifier.ofType("increase_drop_quantity", ModifierPlace.SUFFIX,
            new ModifierDescriptionOneValue("%s%.0f%% %sувеличение выпадаемых предметов"),
            new AttributeAdditionalModifierProcess(ImperiumAttribute.MONSTER_QUANTITY_OF_ITEMS, false, true),
            ModifierRange.of(
                    new ModifierTier().tier(1).canGetViaCraft(false).weight(300).range(5, 10).requireBaseLevel(20),
                    new ModifierTier().tier(2).canGetViaCraft(false).weight(300).range(11, 15).requireBaseLevel(32),
                    new ModifierTier().tier(3).canGetViaCraft(false).weight(300).range(16, 20).requireBaseLevel(39),
                    new ModifierTier().tier(4).canGetViaCraft(false).weight(300).range(21, 25).requireBaseLevel(50),
                    new ModifierTier().tier(5).canGetViaCraft(false).weight(300).range(26, 30).requireBaseLevel(62),
                    new ModifierTier().tier(6).exalted(true).canGetViaCraft(false).weight(300).range(31, 40).requireBaseLevel(60)
            ), Arrays.asList(ItemType.ARMOR, ItemType.WEAPON),
            ModifierTag.DROP);

    public static final Modifier DEFENSE = Modifier.ofType("add_defense", ModifierPlace.PREFIX,
            new ModifierDescriptionOneValue("%s+%.0f%% %sк защите"),
            new AttributeModifierProcess(ImperiumAttribute.DEFENSE),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(1000).range(1, 2).requireBaseLevel(1),
                    new ModifierTier().tier(2).weight(1000).range(3, 4).requireBaseLevel(18),
                    new ModifierTier().tier(3).weight(1000).range(5, 6).requireBaseLevel(30),
                    new ModifierTier().tier(4).weight(1000).range(7, 8).requireBaseLevel(38),
                    new ModifierTier().tier(5).weight(1000).range(9, 10).requireBaseLevel(46),
                    new ModifierTier().tier(6).weight(1000).range(11, 12).requireBaseLevel(58),
                    new ModifierTier().tier(7).weight(1000).range(13, 14).requireBaseLevel(69),
                    new ModifierTier().tier(8).exalted(true).weight(1000).range(15, 18).requireBaseLevel(60)
            ), Arrays.asList(ItemType.ARMOR),
            ModifierTag.DEFENCE);

    public static final Modifier DEFENSE_INCREASE = Modifier.ofType("increase_defense", ModifierPlace.PREFIX,
            new ModifierDescriptionOneValue("%s%.1f%% %sувеличение защиты"),
            new AttributeIncreaseModifierProcess(ImperiumAttribute.DEFENSE),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(1000).range(1, 3).requireBaseLevel(3),
                    new ModifierTier().tier(2).weight(1000).range(4, 6).requireBaseLevel(19),
                    new ModifierTier().tier(3).weight(1000).range(7, 9).requireBaseLevel(30),
                    new ModifierTier().tier(4).weight(1000).range(10, 13).requireBaseLevel(44),
                    new ModifierTier().tier(5).weight(1000).range(14, 18).requireBaseLevel(60),
                    new ModifierTier().tier(6).weight(700).range(19, 21).requireBaseLevel(72),
                    new ModifierTier().tier(7).weight(400).range(22, 26).requireBaseLevel(84),
                    new ModifierTier().tier(8).exalted(true).weight(400).range(27, 32).requireBaseLevel(60)
            ), Arrays.asList(ItemType.ARMOR),
            ModifierTag.DEFENCE);

    public static final Modifier ACCURACY = Modifier.ofType("add_accuracy", ModifierPlace.SUFFIX,
            new ModifierDescriptionOneValue("%s+%.0f%% %sк точности"),
            new AttributeModifierProcess(ImperiumAttribute.ACCURACY),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(1000).range(1, 2).requireBaseLevel(1),
                    new ModifierTier().tier(2).weight(1000).range(3, 5).requireBaseLevel(12),
                    new ModifierTier().tier(3).weight(1000).range(6, 8).requireBaseLevel(32),
                    new ModifierTier().tier(4).weight(1000).range(9, 11).requireBaseLevel(46),
                    new ModifierTier().tier(5).weight(1000).range(12, 15).requireBaseLevel(60),
                    new ModifierTier().tier(6).exalted(true).weight(1000).range(16, 20).requireBaseLevel(60)
            ), Arrays.asList(ItemType.WEAPON, ItemType.ARMOR), ModifierTag.ATTACK);

    public static final Modifier ACCURACY_INCREASE = Modifier.ofType("increase_accuracy", ModifierPlace.SUFFIX,
            new ModifierDescriptionOneValue("%s%.1f%% %sувеличение точности"),
            new AttributeIncreaseModifierProcess(ImperiumAttribute.ACCURACY),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(1000).range(1, 3).requireBaseLevel(1),
                    new ModifierTier().tier(2).weight(1000).range(4, 6).requireBaseLevel(12),
                    new ModifierTier().tier(3).weight(1000).range(7, 9).requireBaseLevel(28),
                    new ModifierTier().tier(4).weight(1000).range(10, 13).requireBaseLevel(32),
                    new ModifierTier().tier(5).weight(1000).range(14, 18).requireBaseLevel(44),
                    new ModifierTier().tier(6).weight(1000).range(19, 21).requireBaseLevel(56),
                    new ModifierTier().tier(7).weight(1000).range(22, 26).requireBaseLevel(72),
                    new ModifierTier().tier(8).exalted(true).weight(1000).range(27, 35).requireBaseLevel(60)
            ), Arrays.asList(ItemType.WEAPON, ItemType.ARMOR),
            ModifierTag.ATTACK);

    public static final Modifier MOVEMENT_SPEED = Modifier.ofFilter("mov_speed", ModifierPlace.SUFFIX,
            new ModifierDescriptionOneValue("%s%.1f%% %sувеличение скорости передвижения"),
            new AttributeAdditionalModifierProcess(ImperiumAttribute.MOVEMENT_SPEED),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(1000).range(5, 5).requireBaseLevel(1),
                    new ModifierTier().tier(2).weight(1000).range(10, 10).requireBaseLevel(15),
                    new ModifierTier().tier(3).weight(1000).range(15, 15).requireBaseLevel(30),
                    new ModifierTier().tier(4).weight(1000).range(20, 20).requireBaseLevel(40),
                    new ModifierTier().tier(5).weight(1000).range(25, 25).requireBaseLevel(55),
                    new ModifierTier().tier(6).weight(1000).range(30, 30).requireBaseLevel(86),
                    new ModifierTier().tier(7).exalted(true).weight(1000).range(40, 40).requireBaseLevel(60)
            ), Collections.singletonList(item -> item.checkInstance(BaseBoots.class)),
            ModifierTag.SPEED);

    public static final Modifier GLOBAL_CRITICAL_CHANCE = Modifier.ofType("global_critical_chance", ModifierPlace.SUFFIX,
            new ModifierDescriptionOneValue("%s%.1f%% %sувеличение шанса крит. удара"),
            new AttributeIncreaseModifierProcess(ImperiumAttribute.CRITICAL_RATE),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(1000).range(2, 6).requireBaseLevel(1),
                    new ModifierTier().tier(2).weight(1000).range(7, 11).requireBaseLevel(6),
                    new ModifierTier().tier(3).weight(1000).range(12, 16).requireBaseLevel(20),
                    new ModifierTier().tier(4).weight(1000).range(17, 21).requireBaseLevel(30),
                    new ModifierTier().tier(5).weight(1000).range(22, 26).requireBaseLevel(44),
                    new ModifierTier().tier(6).weight(1000).range(27, 31).requireBaseLevel(59),
                    new ModifierTier().tier(7).weight(1000).range(32, 36).requireBaseLevel(73),
                    new ModifierTier().tier(8).weight(1000).range(37, 44).requireBaseLevel(82),
                    new ModifierTier().tier(9).exalted(true).weight(1000).range(45, 55).requireBaseLevel(60)
            ), Arrays.asList(ItemType.WEAPON),
            ModifierTag.CRITICAL, ModifierTag.ATTACK);

    public static final Modifier GLOBAL_CRITICAL_DAMAGE = Modifier.ofType("global_critical_damage", ModifierPlace.SUFFIX,
            new ModifierDescriptionOneValue("%s%.1f%% %sувеличение крит. урона"),
            new AttributeIncreaseModifierProcess(ImperiumAttribute.CRITICAL_DAMAGE),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(1000).range(5, 10).requireBaseLevel(1),
                    new ModifierTier().tier(2).weight(1000).range(11, 16).requireBaseLevel(21),
                    new ModifierTier().tier(3).weight(1000).range(17, 22).requireBaseLevel(30),
                    new ModifierTier().tier(4).weight(1000).range(23, 28).requireBaseLevel(44),
                    new ModifierTier().tier(5).weight(1000).range(29, 34).requireBaseLevel(59),
                    new ModifierTier().tier(6).weight(1000).range(35, 39).requireBaseLevel(73),
                    new ModifierTier().tier(7).exalted(true).weight(1000).range(40, 46).requireBaseLevel(60)
            ), Arrays.asList(ItemType.WEAPON),
            ModifierTag.CRITICAL, ModifierTag.DAMAGE);

    public static final Modifier MAXIMUM_FIRE_RESISTANCE = Modifier.ofType("add_maximum_fire_resistance", ModifierPlace.PREFIX,
            new ModifierDescriptionOneValue("%s+%.1f%% %sк максимуму сопротивлению огню"),
            new AttributeModifierProcess(ImperiumAttribute.FIRE_RESISTANCE_MAXIMUM),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(500).canGetViaCraft(false).range(1, 1).requireBaseLevel(50),
                    new ModifierTier().tier(2).weight(500).canGetViaCraft(false).range(2, 2).requireBaseLevel(60),
                    new ModifierTier().tier(3).exalted(true).weight(500).canGetViaCraft(false).range(3, 3).requireBaseLevel(60)
            ), Arrays.asList(ItemType.ARMOR),
            ModifierTag.ELEMENTAL, ModifierTag.RESISTANCE, ModifierTag.FIRE);

    public static final Modifier MAXIMUM_COLD_RESISTANCE = Modifier.ofType("add_maximum_cold_resistance", ModifierPlace.PREFIX,
            new ModifierDescriptionOneValue("%s+%.1f%% %sк максимуму сопротивлению холоду"),
            new AttributeModifierProcess(ImperiumAttribute.COLD_RESISTANCE_MAXIMUM),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(500).canGetViaCraft(false).range(1, 1).requireBaseLevel(50),
                    new ModifierTier().tier(2).weight(500).canGetViaCraft(false).range(2, 2).requireBaseLevel(60),
                    new ModifierTier().tier(3).weight(500).exalted(true).canGetViaCraft(false).range(3, 3).requireBaseLevel(60)
            ), Arrays.asList(ItemType.ARMOR),
            ModifierTag.ELEMENTAL, ModifierTag.RESISTANCE, ModifierTag.COLD);

    public static final Modifier MAXIMUM_LIGHTNING_RESISTANCE = Modifier.ofType("add_maximum_lightning_resistance", ModifierPlace.PREFIX,
            new ModifierDescriptionOneValue("%s+%.1f%% %sк максимуму сопротивлению молнии"),
            new AttributeModifierProcess(ImperiumAttribute.LIGHTNING_RESISTANCE_MAXIMUM),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(500).canGetViaCraft(false).range(1, 1).requireBaseLevel(50),
                    new ModifierTier().tier(2).weight(500).canGetViaCraft(false).range(2, 2).requireBaseLevel(60),
                    new ModifierTier().tier(3).exalted(true).weight(500).canGetViaCraft(false).range(3, 3).requireBaseLevel(60)
            ), Arrays.asList(ItemType.ARMOR),
            ModifierTag.ELEMENTAL, ModifierTag.RESISTANCE, ModifierTag.LIGHTNING);

    public static final Modifier MAXIMUM_CHAOS_RESISTANCE = Modifier.ofType("add_maximum_chaos_resistance", ModifierPlace.PREFIX,
            new ModifierDescriptionOneValue("%s+%.1f%% %sк максимуму сопротивлению хаосу"),
            new AttributeModifierProcess(ImperiumAttribute.CHAOS_RESISTANCE_MAXIMUM),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(500).canGetViaCraft(false).range(1, 1).requireBaseLevel(50),
                    new ModifierTier().tier(2).weight(500).canGetViaCraft(false).range(2, 2).requireBaseLevel(60),
                    new ModifierTier().tier(3).exalted(true).weight(500).canGetViaCraft(false).range(3, 3).requireBaseLevel(60)
            ), Arrays.asList(ItemType.ARMOR),
            ModifierTag.ELEMENTAL, ModifierTag.RESISTANCE, ModifierTag.CHAOS);

    public static final Modifier FIRE_RESISTANCE = Modifier.ofType("add_fire_resistance", ModifierPlace.SUFFIX,
            new ModifierDescriptionOneValue("%s+%.1f%% %sк сопротивлению огню"),
            new AttributeAdditionalModifierProcess(ImperiumAttribute.FIRE_RESISTANCE),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(1000).range(5, 10).requireBaseLevel(1),
                    new ModifierTier().tier(2).weight(1000).range(11, 16).requireBaseLevel(12),
                    new ModifierTier().tier(3).weight(1000).range(17, 22).requireBaseLevel(24),
                    new ModifierTier().tier(4).weight(1000).range(23, 28).requireBaseLevel(36),
                    new ModifierTier().tier(5).weight(1000).range(29, 34).requireBaseLevel(48),
                    new ModifierTier().tier(6).weight(1000).range(35, 39).requireBaseLevel(60),
                    new ModifierTier().tier(7).weight(1000).range(40, 44).requireBaseLevel(72),
                    new ModifierTier().tier(8).exalted(true).weight(1000).range(45, 50).requireBaseLevel(60)
            ), Arrays.asList(ItemType.ARMOR),
            ModifierTag.ELEMENTAL, ModifierTag.RESISTANCE, ModifierTag.FIRE);

    public static final Modifier COLD_RESISTANCE = Modifier.ofType("add_cold_resistance", ModifierPlace.SUFFIX,
            new ModifierDescriptionOneValue("%s+%.1f%% %sк сопротивлению холоду"),
            new AttributeAdditionalModifierProcess(ImperiumAttribute.COLD_RESISTANCE),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(1000).range(5, 10).requireBaseLevel(1),
                    new ModifierTier().tier(2).weight(1000).range(11, 16).requireBaseLevel(12),
                    new ModifierTier().tier(3).weight(1000).range(17, 22).requireBaseLevel(24),
                    new ModifierTier().tier(4).weight(1000).range(23, 28).requireBaseLevel(36),
                    new ModifierTier().tier(5).weight(1000).range(29, 34).requireBaseLevel(48),
                    new ModifierTier().tier(6).weight(1000).range(35, 39).requireBaseLevel(60),
                    new ModifierTier().tier(7).weight(1000).range(40, 44).requireBaseLevel(72),
                    new ModifierTier().tier(8).exalted(true).weight(1000).range(45, 50).requireBaseLevel(60)
            ), Arrays.asList(ItemType.ARMOR),
            ModifierTag.ELEMENTAL, ModifierTag.RESISTANCE, ModifierTag.COLD);

    public static final Modifier LIGHTNING_RESISTANCE = Modifier.ofType("add_lightning_resistance", ModifierPlace.SUFFIX,
            new ModifierDescriptionOneValue("%s+%.1f%% %sк сопротивлению молнии"),
            new AttributeAdditionalModifierProcess(ImperiumAttribute.LIGHTNING_RESISTANCE),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(1000).range(5, 10).requireBaseLevel(1),
                    new ModifierTier().tier(2).weight(1000).range(11, 16).requireBaseLevel(12),
                    new ModifierTier().tier(3).weight(1000).range(17, 22).requireBaseLevel(24),
                    new ModifierTier().tier(4).weight(1000).range(23, 28).requireBaseLevel(36),
                    new ModifierTier().tier(5).weight(1000).range(29, 34).requireBaseLevel(48),
                    new ModifierTier().tier(6).weight(1000).range(35, 39).requireBaseLevel(60),
                    new ModifierTier().tier(7).weight(1000).range(40, 44).requireBaseLevel(72),
                    new ModifierTier().tier(8).exalted(true).weight(1000).range(45, 50).requireBaseLevel(60)
            ), Arrays.asList(ItemType.ARMOR),
            ModifierTag.ELEMENTAL, ModifierTag.RESISTANCE, ModifierTag.LIGHTNING);

    public static final Modifier ALL_ELEMENTAL_RESISTANCE = Modifier.ofType("add_all_elem_res", ModifierPlace.SUFFIX,
            new ModifierDescriptionOneValue("%s+%.1f%% %sк сопротивлению всем стихиям"),
            new AttributeAdditionalModifierProcess(List.of(ImperiumAttribute.COLD_RESISTANCE, ImperiumAttribute.FIRE_RESISTANCE, ImperiumAttribute.LIGHTNING_RESISTANCE)),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(1000).range(3, 5).requireBaseLevel(1),
                    new ModifierTier().tier(2).weight(1000).range(6, 8).requireBaseLevel(12),
                    new ModifierTier().tier(3).weight(1000).range(9, 11).requireBaseLevel(28),
                    new ModifierTier().tier(4).weight(1000).range(12, 14).requireBaseLevel(44),
                    new ModifierTier().tier(5).weight(1000).range(15, 16).requireBaseLevel(52),
                    new ModifierTier().tier(6).weight(1000).range(17, 18).requireBaseLevel(60),
                    new ModifierTier().tier(7).exalted(true).weight(1000).range(19, 24).requireBaseLevel(60)
            ), Arrays.asList(ItemType.ARMOR), ModifierTag.ELEMENTAL, ModifierTag.RESISTANCE);

    public static final Modifier ALL_MAXIMUM_ELEMENTAL_RESISTANCE = Modifier.ofFilter("add_maximum_all_elem_res", ModifierPlace.PREFIX,
            new ModifierDescriptionOneValue("%s+%.1f%% %sк максимуму сопротивлению всем стихиям"),
            new AttributeAdditionalModifierProcess(List.of(ImperiumAttribute.COLD_RESISTANCE_MAXIMUM, ImperiumAttribute.FIRE_RESISTANCE_MAXIMUM, ImperiumAttribute.LIGHTNING_RESISTANCE_MAXIMUM)),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(250).canGetViaCraft(false).range(1, 1).requireBaseLevel(40),
                    new ModifierTier().tier(2).weight(250).canGetViaCraft(false).range(2, 2).requireBaseLevel(50),
                    new ModifierTier().tier(3).weight(250).canGetViaCraft(false).range(3, 3).requireBaseLevel(60),
                    new ModifierTier().tier(4).exalted(true).weight(250).canGetViaCraft(false).range(4, 4).requireBaseLevel(60)
            ),
            Collections.singletonList(item -> item.checkInstance(BaseBody.class) || item.checkInstance(BaseLeggings.class)),
            ModifierTag.ELEMENTAL, ModifierTag.RESISTANCE);

    public static final Modifier CHAOS_RESISTANCE = Modifier.ofType("add_chaos_resistance", ModifierPlace.SUFFIX,
            new ModifierDescriptionOneValue("%s+%.1f%% %sк сопротивлению хаосу"),
            new AttributeAdditionalModifierProcess(ImperiumAttribute.CHAOS_RESISTANCE),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(250).range(5, 10).requireBaseLevel(16),
                    new ModifierTier().tier(2).weight(250).range(11, 15).requireBaseLevel(30),
                    new ModifierTier().tier(3).weight(250).range(16, 20).requireBaseLevel(44),
                    new ModifierTier().tier(4).weight(250).range(21, 25).requireBaseLevel(56),
                    new ModifierTier().tier(5).weight(250).range(26, 30).requireBaseLevel(65),
                    new ModifierTier().tier(6).weight(250).range(31, 35).requireBaseLevel(81),
                    new ModifierTier().tier(7).exalted(true).weight(250).range(36, 40).requireBaseLevel(60)
                    ), Arrays.asList(ItemType.ARMOR),
            ModifierTag.ELEMENTAL, ModifierTag.RESISTANCE, ModifierTag.CHAOS);

    public static final Modifier ADDITIONAL_MAX_MANA = Modifier.ofType("add_mana_max", ModifierPlace.PREFIX,
            new ModifierDescriptionOneValue("%s+%.0f %sк максимуму маны"),
            new AttributeModifierProcess(ImperiumAttribute.MANA),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(1000).range(10, 14).requireBaseLevel(1),
                    new ModifierTier().tier(2).weight(1000).range(15, 20).requireBaseLevel(11),
                    new ModifierTier().tier(3).weight(1000).range(21, 25).requireBaseLevel(17),
                    new ModifierTier().tier(4).weight(1000).range(26, 30).requireBaseLevel(23),
                    new ModifierTier().tier(5).weight(1000).range(31, 35).requireBaseLevel(29),
                    new ModifierTier().tier(6).weight(1000).range(36, 40).requireBaseLevel(35),
                    new ModifierTier().tier(7).weight(1000).range(41, 45).requireBaseLevel(42),
                    new ModifierTier().tier(8).weight(1000).range(46, 50).requireBaseLevel(51),
                    new ModifierTier().tier(9).weight(1000).range(51, 55).requireBaseLevel(60),
                    new ModifierTier().tier(10).weight(1000).range(56, 60).requireBaseLevel(69),
                    new ModifierTier().tier(11).weight(1000).range(61, 65).requireBaseLevel(75),
                    new ModifierTier().tier(12).exalted(true).weight(1000).range(66, 70).requireBaseLevel(60)
                    ), Arrays.asList(ItemType.ARMOR),
            ModifierTag.MANA);

    public static final Modifier INCREASED_MAXIMUM_MANA = Modifier.ofFilter("incr_mana_max", ModifierPlace.SUFFIX,
            new ModifierDescriptionOneValue("%s%.0f%% %sувеличение максимума маны"),
            new AttributeIncreaseModifierProcess(ImperiumAttribute.MANA),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(1000).range(1, 4).requireBaseLevel(30),
                    new ModifierTier().tier(2).weight(1000).range(5, 8).requireBaseLevel(50),
                    new ModifierTier().tier(3).weight(1000).range(9, 11).requireBaseLevel(68),
                    new ModifierTier().tier(4).weight(1000).range(12, 15).requireBaseLevel(75),
                    new ModifierTier().tier(5).exalted(true).weight(1000).range(16, 20).requireBaseLevel(60)
                    ), Collections.singletonList(item -> item.checkInstance(BaseBody.class)),
            ModifierTag.MANA);

    public static final Modifier INCREASED_MANA_RESERVE_EFFICIENCY = Modifier.ofFilter("incr_mana_reserve_eff", ModifierPlace.SUFFIX,
            new ModifierDescriptionOneValue("%s%.0f%% %sувеличение эффект. резервации маны"),
            new AttributeAdditionalModifierProcess(ImperiumAttribute.MANA_RESERVATION_EFFICIENCY, false, true),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(1000).range(3, 4).requireBaseLevel(30),
                    new ModifierTier().tier(2).weight(1000).range(5, 6).requireBaseLevel(40),
                    new ModifierTier().tier(3).weight(1000).range(7, 8).requireBaseLevel(50),
                    new ModifierTier().tier(4).weight(1000).range(9, 10).requireBaseLevel(60),
                    new ModifierTier().tier(5).weight(1000).range(11, 13).requireBaseLevel(70),
                    new ModifierTier().tier(6).exalted(true).weight(1000).range(14, 17).requireBaseLevel(60)
            ),
            Collections.singletonList(item -> item.checkInstance(BaseHelmet.class) || item.checkInstance(BaseBoots.class)),
            ModifierTag.MANA);

    public static final Modifier ADDITIONAL_MANA_REGENERATION = Modifier.ofType("add_regen_mana", ModifierPlace.SUFFIX,
            new ModifierDescriptionOneValue("%s+%.0f %sк регенерации маны"),
            new AttributeModifierProcess(ImperiumAttribute.MANA_REGENERATION),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(500).range(1, 2).requireBaseLevel(15),
                    new ModifierTier().tier(2).weight(500).range(3, 4).requireBaseLevel(30),
                    new ModifierTier().tier(3).weight(500).range(5, 6).requireBaseLevel(45),
                    new ModifierTier().tier(4).weight(500).range(7, 8).requireBaseLevel(60),
                    new ModifierTier().tier(5).weight(500).range(9, 10).requireBaseLevel(70),
                    new ModifierTier().tier(6).exalted(true).weight(500).range(11, 12).requireBaseLevel(60)
                    ), Arrays.asList(ItemType.ARMOR),
            ModifierTag.MANA);

    public static final Modifier REGENERATION_IN_FIGHT = Modifier.ofType("add_regen_in_fight", ModifierPlace.SUFFIX,
            new ModifierDescriptionOneValue("%s+%.0f %sк регенерации здоровья в бою"),
            new AttributeModifierProcess(ImperiumAttribute.REGENERATION_HEALTH_IN_FIGHT),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(1000).range(1, 2).requireBaseLevel(1),
                    new ModifierTier().tier(2).weight(1000).range(3, 8).requireBaseLevel(7),
                    new ModifierTier().tier(3).weight(1000).range(9, 16).requireBaseLevel(19),
                    new ModifierTier().tier(4).weight(1000).range(17, 24).requireBaseLevel(31),
                    new ModifierTier().tier(5).weight(900).range(25, 32).requireBaseLevel(44),
                    new ModifierTier().tier(6).weight(600).range(33, 48).requireBaseLevel(55),
                    new ModifierTier().tier(7).weight(500).range(49, 64).requireBaseLevel(68),
                    new ModifierTier().tier(8).exalted(true).weight(500).range(65, 96).requireBaseLevel(60)
                    ), Arrays.asList(ItemType.ARMOR),
            ModifierTag.LIFE);

    public static final Modifier ATTACK_SPEED = Modifier.ofType("attack_speed", ModifierPlace.SUFFIX,
            new ModifierDescriptionOneValue("%s%.0f%% %sувеличение скорости атаки"),
            new AttributeAdditionalModifierProcess(ImperiumAttribute.ATTACK_SPEED),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(1000).range(5, 7).requireBaseLevel(1),
                    new ModifierTier().tier(2).weight(1000).range(8, 10).requireBaseLevel(11),
                    new ModifierTier().tier(3).weight(1000).range(11, 13).requireBaseLevel(22),
                    new ModifierTier().tier(4).weight(1000).range(14, 16).requireBaseLevel(30),
                    new ModifierTier().tier(5).weight(1000).range(17, 19).requireBaseLevel(37),
                    new ModifierTier().tier(6).weight(1000).range(20, 22).requireBaseLevel(45),
                    new ModifierTier().tier(7).weight(1000).range(23, 25).requireBaseLevel(60),
                    new ModifierTier().tier(8).weight(1000).range(26, 27).requireBaseLevel(77),
                    new ModifierTier().tier(9).exalted(true).weight(1000).range(26, 30).requireBaseLevel(60)
            ), Arrays.asList(ItemType.WEAPON),
            ModifierTag.SPEED);

    public static final Modifier INCREASE_DAMAGE = Modifier.ofType("incr_phys_damage", ModifierPlace.PREFIX,
            new ModifierDescriptionOneValue("%s%.0f%% %sувеличение физического урона"),
            new AttributeIncreaseModifierProcess(ImperiumAttribute.MIN_PHYSICAL_DAMAGE, ImperiumAttribute.MAX_PHYSICAL_DAMAGE),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(1000).range(40, 49).requireBaseLevel(1),
                    new ModifierTier().tier(2).weight(1000).range(50, 64).requireBaseLevel(11),
                    new ModifierTier().tier(3).weight(1000).range(65, 84).requireBaseLevel(23),
                    new ModifierTier().tier(4).weight(400).range(85, 109).requireBaseLevel(35),
                    new ModifierTier().tier(5).weight(200).range(110, 134).requireBaseLevel(46),
                    new ModifierTier().tier(6).weight(100).range(135, 154).requireBaseLevel(60),
                    new ModifierTier().tier(7).weight(25).range(155, 169).requireBaseLevel(73),
                    new ModifierTier().tier(8).exalted(true).weight(1000).range(180, 200).requireBaseLevel(60)
            ), Arrays.asList(ItemType.WEAPON),
            ModifierTag.DAMAGE, ModifierTag.PHYSICAL);

    public static final Modifier INCREASE_ELEMENTAL_DAMAGE = Modifier.ofType("incr_elem_damage", ModifierPlace.PREFIX,
            new ModifierDescriptionOneValue("%s%.0f%% %sувеличение стихийного урона"),
            new AttributeIncreaseModifierProcess(
                    ImperiumAttribute.MIN_COLD_DAMAGE, ImperiumAttribute.MAX_COLD_DAMAGE,
                    ImperiumAttribute.MIN_FIRE_DAMAGE, ImperiumAttribute.MAX_FIRE_DAMAGE,
                    ImperiumAttribute.MIN_LIGHTNING_DAMAGE, ImperiumAttribute.MAX_LIGHTNING_DAMAGE
            ),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(1000).range(10, 15).requireBaseLevel(1),
                    new ModifierTier().tier(2).weight(1000).range(16, 20).requireBaseLevel(15),
                    new ModifierTier().tier(3).weight(1000).range(21, 34).requireBaseLevel(20),
                    new ModifierTier().tier(4).weight(1000).range(35, 51).requireBaseLevel(30),
                    new ModifierTier().tier(5).weight(1000).range(53, 61).requireBaseLevel(60),
                    new ModifierTier().tier(6).weight(1000).range(62, 71).requireBaseLevel(81),
                    new ModifierTier().tier(7).weight(1000).range(72, 85).requireBaseLevel(86),
                    new ModifierTier().tier(8).exalted(true).weight(1000).range(86, 100).requireBaseLevel(60)
                    ), Arrays.asList(ItemType.WEAPON),
            ModifierTag.DAMAGE, ModifierTag.ELEMENTAL_DAMAGE, ModifierTag.ELEMENTAL);

}
