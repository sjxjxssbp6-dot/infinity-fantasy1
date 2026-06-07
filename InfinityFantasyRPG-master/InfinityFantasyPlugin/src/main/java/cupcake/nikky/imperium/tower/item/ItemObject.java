package cupcake.nikky.imperium.tower.item;

import cupcake.nikky.imperium.tower.incubator.Incubators;
import cupcake.nikky.imperium.tower.item.exact.aspect.exact.*;
import cupcake.nikky.imperium.tower.item.exact.*;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectItem;
import cupcake.nikky.imperium.tower.item.exact.card.StackedDeck;
import cupcake.nikky.imperium.tower.item.exact.fragment.exact.BattleGroundItem;
import cupcake.nikky.imperium.tower.item.exact.card.DivinationCard;
import cupcake.nikky.imperium.tower.item.exact.card.DivinationCardItem;
import cupcake.nikky.imperium.tower.item.exact.fragment.exact.WayFragmentItem;
import cupcake.nikky.imperium.tower.item.exact.gear.armour.*;
import cupcake.nikky.imperium.tower.item.exact.gear.armour.unique.*;
import cupcake.nikky.imperium.tower.item.exact.gear.tool.FishingRodItem;
import cupcake.nikky.imperium.tower.item.exact.gear.tool.PickaxeItem;
import cupcake.nikky.imperium.tower.item.exact.gear.weapon.WeaponType;
import cupcake.nikky.imperium.tower.item.exact.gear.weapon.base.BaseAxe;
import cupcake.nikky.imperium.tower.item.exact.gear.weapon.base.BaseDagger;
import cupcake.nikky.imperium.tower.item.exact.gear.weapon.base.BaseSpear;
import cupcake.nikky.imperium.tower.item.exact.gear.weapon.base.BaseSword;
import cupcake.nikky.imperium.tower.item.exact.gear.weapon.unique.ForestSpear;
import cupcake.nikky.imperium.tower.item.exact.gear.weapon.unique.MortDagger;
import cupcake.nikky.imperium.tower.item.exact.gear.weapon.unique.SkarigaSword;
import cupcake.nikky.imperium.tower.item.exact.harvest.*;
import cupcake.nikky.imperium.tower.item.exact.incubator.IncubatorItem;
import cupcake.nikky.imperium.tower.item.exact.mask.MaskItem;
import cupcake.nikky.imperium.tower.item.exact.mask.Masks;
import cupcake.nikky.imperium.tower.item.exact.modification.ModificationItem;
import cupcake.nikky.imperium.tower.item.exact.modification.exact.*;
import cupcake.nikky.imperium.tower.item.exact.potion.*;
import cupcake.nikky.imperium.tower.item.exact.potion.event.*;
import cupcake.nikky.imperium.tower.item.exact.shard.ProjectionYIBinaShard;
import cupcake.nikky.imperium.tower.item.exact.shard.ProjectionYIHochmaShard;
import cupcake.nikky.imperium.tower.item.modifier.ModifierTag;
import cupcake.nikky.imperium.tower.item.modifier.base.ModifierBaseCreatorType;
import cupcake.nikky.imperium.tower.level.LevelStorage;
import cupcake.nikky.imperium.tower.zone.ZoneList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;

import java.util.*;

@Getter
@RequiredArgsConstructor
public enum ItemObject {

    FIRM_SWORD(new BaseSword(WeaponType.FIRM_SWORD, ModifierBaseCreatorType.FIRM_SWORD), "2000"),
    FIRM_DAGGER(new BaseDagger(WeaponType.FIRM_DAGGER, ModifierBaseCreatorType.FIRM_DAGGER), "2001"),
    FIRM_SPEAR(new BaseSpear(WeaponType.FIRM_SPEAR, ModifierBaseCreatorType.FIRM_SPEAR), "2002"),
    FIRM_AXE(new BaseAxe(WeaponType.FIRM_AXE, ModifierBaseCreatorType.FIRM_AXE), "2003"),
    FIRM_HELMET(new BaseHelmet(BaseType.FIRM, ModifierBaseCreatorType.FIRM_HELMET), "2104"),
    FIRM_BODY(new BaseBody(BaseType.FIRM, ModifierBaseCreatorType.FIRM_BODY), "2105"),
    FIRM_LEGGINGS(new BaseLeggings(BaseType.FIRM, ModifierBaseCreatorType.FIRM_LEGGINGS), "2106"),
    FIRM_BOOTS(new BaseBoots(BaseType.FIRM, ModifierBaseCreatorType.FIRM_BOOTS), "2107"),

    REKN_SWORD(new BaseSword(WeaponType.REKN_SWORD, ModifierBaseCreatorType.REKN_SWORD), "2008"),
    REKN_DAGGER(new BaseDagger(WeaponType.REKN_DAGGER, ModifierBaseCreatorType.REKN_DAGGER), "2009"),
    REKN_SPEAR(new BaseSpear(WeaponType.REKN_SPEAR, ModifierBaseCreatorType.REKN_SPEAR), "2010"),
    REKN_AXE(new BaseAxe(WeaponType.REKN_AXE, ModifierBaseCreatorType.REKN_AXE), "2011"),
    REKN_HELMET(new BaseHelmet(BaseType.REKN, ModifierBaseCreatorType.REKN_HELMET), "2112"),
    REKN_BODY(new BaseBody(BaseType.REKN, ModifierBaseCreatorType.REKN_BODY), "2113"),
    REKN_LEGGINGS(new BaseLeggings(BaseType.REKN, ModifierBaseCreatorType.REKN_LEGGINGS), "2114"),
    REKN_BOOTS(new BaseBoots(BaseType.REKN, ModifierBaseCreatorType.REKN_BOOTS), "2115"),

    SALT_SWORD(new BaseSword(WeaponType.SALT_SWORD, ModifierBaseCreatorType.SALT_SWORD), "2012"),
    SALT_DAGGER(new BaseDagger(WeaponType.SALT_DAGGER, ModifierBaseCreatorType.SALT_DAGGER), "2013"),
    SALT_SPEAR(new BaseSpear(WeaponType.SALT_SPEAR, ModifierBaseCreatorType.SALT_SPEAR), "2014"),
    SALT_AXE(new BaseAxe(WeaponType.SALT_AXE, ModifierBaseCreatorType.SALT_AXE), "2015"),
    SALT_HELMET(new BaseHelmet(BaseType.SALT, ModifierBaseCreatorType.SALT_HELMET), "2116"),
    SALT_BODY(new BaseBody(BaseType.SALT, ModifierBaseCreatorType.SALT_BODY), "2117"),
    SALT_LEGGINGS(new BaseLeggings(BaseType.SALT, ModifierBaseCreatorType.SALT_LEGGINGS), "2118"),
    SALT_BOOTS(new BaseBoots(BaseType.SALT, ModifierBaseCreatorType.SALT_BOOTS), "2119"),

    TARN_SWORD(new BaseSword(WeaponType.TARN_SWORD, ModifierBaseCreatorType.TARN_SWORD), "2016"),
    TARN_DAGGER(new BaseDagger(WeaponType.TARN_DAGGER, ModifierBaseCreatorType.TARN_SWORD), "2017"),
    TARN_SPEAR(new BaseSpear(WeaponType.TARN_SPEAR, ModifierBaseCreatorType.TARN_SPEAR), "2018"),
    TARN_AXE(new BaseAxe(WeaponType.TARN_AXE, ModifierBaseCreatorType.TARN_AXE), "2019"),
    TARN_HELMET(new BaseHelmet(BaseType.TARN, ModifierBaseCreatorType.TARN_HELMET), "2120"),
    TARN_BODY(new BaseBody(BaseType.TARN, ModifierBaseCreatorType.TARN_BODY), "2121"),
    TARN_LEGGINGS(new BaseLeggings(BaseType.TARN, ModifierBaseCreatorType.TARN_LEGGINGS), "2122"),
    TARN_BOOTS(new BaseBoots(BaseType.TARN, ModifierBaseCreatorType.TARN_BOOTS), "2123"),

    KELN_SWORD(new BaseSword(WeaponType.KELN_SWORD, ModifierBaseCreatorType.KELN_SWORD), "2004"),
    KELN_DAGGER(new BaseDagger(WeaponType.KELN_DAGGER, ModifierBaseCreatorType.KELN_DAGGER), "2005"),
    KELN_SPEAR(new BaseSpear(WeaponType.KELN_SPEAR, ModifierBaseCreatorType.KELN_SPEAR), "2006"),
    KELN_AXE(new BaseAxe(WeaponType.KELN_AXE, ModifierBaseCreatorType.KELN_AXE), "2007"),
    KELN_HELMET(new BaseHelmet(BaseType.KELN, ModifierBaseCreatorType.KELN_HELMET), "2108"),
    KELN_BODY(new BaseBody(BaseType.KELN, ModifierBaseCreatorType.KELN_BODY), "2109"),
    KELN_LEGGINGS(new BaseLeggings(BaseType.KELN, ModifierBaseCreatorType.KELN_LEGGINGS), "2110"),
    KELN_BOOTS(new BaseBoots(BaseType.KELN, ModifierBaseCreatorType.KELN_BOOTS), "2111"),

    SKARIGA_SWORD(new SkarigaSword(), "2500"),
    MINER_HELMET(new MinerHelmet(), "2501"),
    HOCHMA_BODY(new BodyHochma(), "2502"),
    BINA_BOOTS(new BootsBina(), "2503"),
    FOREST_SPEAR(new ForestSpear(), "2504"),
    MORT_DAGGER(new MortDagger(), "2505"),
    BATTLE_GROUND_LEGGINGS(new LeggingsBattleGround(), "2506"),
    SYNERGY_BOOTS(new BootsSynergy(), "2507"),

    COAL_HARVEST(new CoalHarvest(), "2400"),
    IRON_HARVEST(new IronHarvest(), "2401"),
    GOLD_HARVEST(new GoldHarvest(), "2402"),
    DIAMOND_HARVEST(new DiamondHarvest(), "2403"),
    EMERALD_HARVEST(new EmeraldHarvest(), "2404"),
    RED_STONE_HARVEST(new RedstoneHarvest(), "2405"),
    FISH_HARVEST_1(new FishHarvest(Material.RAW_FISH, 0, 1, 3, 2), "2410"),
    FISH_HARVEST_2(new FishHarvest(Material.RAW_FISH, 0, 2, 6, 4), "2411"),
    FISH_HARVEST_3(new FishHarvest(Material.RAW_FISH, 0, 3, 8, 6), "2412"),
    FISH_HARVEST_4(new FishHarvest(Material.COOKED_FISH, 0, 4, 16, 8), "2413"),
    FISH_HARVEST_5(new FishHarvest(Material.COOKED_FISH, 0, 5, 26, 11), "2414"),

    HEAL_POTION(new HealPotion(), "1003"),
    POTION_CRITICAL_CHANCE(new CriticalRatePotion(), "1004"),
    POTION_CRITICAL_DAMAGE(new CriticalDamagePotion(), "1005"),
    SPEED_POTION(new SpeedPotion(), "1006"),
    MANA_REGENERATION_POTION(new ManaRegenerationPotion(), "1008"),

    EVENT_ATTACK_POTION(new AttackEventPotion(), "1010"),
    EVENT_CHAOS_RES_POTION(new ChaosResistanceEventPotion(), "1011"),
    EVENT_DEFENSE_POTION(new DefenseEventPotion(), "1012"),
    EVENT_ELEM_RES_POTION(new ElementalResistanceEventPotion(), "1013"),
    EVENT_EXPERIENCE_POTION(new ExperienceEventPotion(), "1014"),
    EVENT_HEALTH_POTION(new HealthEventPotion(), "1015"),

    EPIC_REAGENT_VERGE_MOD_ITEM(new ModificationItem(new EpicReagentVerge()), "1101"),
    MIXED_REAGENT_VERGE_MOD_ITEM(new ModificationItem(new MixedReagentVerge()), "1102"),
    ORB_OF_PANDORA_MOD_ITEM(new ModificationItem(new OrbOfPandora()), "1103"),
    ABSOLUTE_DUST_MOD_ITEM(new ModificationItem(new AbsoluteDust()), "1104"),
    ORB_OF_DISTORTION_MOD_ITEM(new ModificationItem(new OrbOfDistortion()), "1105"),
    ORB_OF_HEPHAESTUS_MOD_ITEM(new ModificationItem(new OrbOfHephaestus()), "1109"),
    FORM_OF_TYCHE_MOD_ITEM(new ModificationItem(new FormOfTyche()), "1110"),
    ESSENCE_OF_TYCHE_MOD_ITEM(new ModificationItem(new EssenceOfTyche()), "1111"),
    SHARD_OF_ABYSS_MOD_ITEM(new ModificationItem(new ShardOfAbyss()), "1112"),
    STAR_OF_CORRUPT_MOD_ITEM(new ModificationItem(new StarOfCorrupt()), "1115"),
    DROP_OF_EDGE_MOD_ITEM(new ModificationItem(new DropOfEdge()), "1117"),
    CLEANSING_TEAR_MOD_ITEM(new ModificationItem(new CleansingTear()), "1118"),
    EXALTED_ORB_MOD_ITEM(new ModificationItem(new ExaltedOrb()), "1119"),
    CATALYST_SPEED_MOD_ITEM(new ModificationItem(new Catalyst(List.of(ModifierTag.SPEED),
            "Катализатор скорости", 1056), "CATALYST_SPEED"), "1120"),
    CATALYST_LIFE_MANA_MOD_ITEM(new ModificationItem(new Catalyst(List.of(ModifierTag.LIFE, ModifierTag.MANA),
            "Катализатор плодородия", 1059), "CATALYST_LIFE_MANA"), "1121"),
    CATALYST_PHYS_CHAOS_MOD_ITEM(new ModificationItem(new Catalyst(List.of(ModifierTag.PHYSICAL, ModifierTag.CHAOS),
            "Катализатор яда", 1054), "CATALYST_PHYS_CHAOS"), "1122"),
    CATALYST_RESISTANCE_MOD_ITEM(new ModificationItem(new Catalyst(List.of(ModifierTag.RESISTANCE),
            "Призматический катализатор", 1058), "CATALYST_RESISTANCE"), "1123"),
    CATALYST_DEFENSE_MOD_ITEM(new ModificationItem(new Catalyst(List.of(ModifierTag.DEFENCE),
            "Катализатор закалки", 1055), "CATALYST_DEFENSE"), "1124"),
    CATALYST_ELEM_DMG_MOD_ITEM(new ModificationItem(new Catalyst(List.of(ModifierTag.ELEMENTAL),
            "Катализатор стихии", 1053), "CATALYST_ELEM_DMG"), "1125"),
    CATALYST_CRIT_MOD_ITEM(new ModificationItem(new Catalyst(List.of(ModifierTag.CRITICAL),
            "Нестабильный катализатор", 1057), "CATALYST_CRIT"), "1126"),

    RESET_ORB(new ResetOrb(), "1200"),
    //SPECIAL_CURRENCY_5(new SpecialCurrencyItem(5), "1300"),

    MASK_ANGER(new MaskItem(Masks.ANGER), "3000"),
    MASK_CONTEMPT(new MaskItem(Masks.CONTEMPT), "3001"),
    MASK_DREAD(new MaskItem(Masks.DREAD), "3003"),
    MASK_ENVY(new MaskItem(Masks.ENVY), "3004"),
    MASK_HATRED(new MaskItem(Masks.HATRED), "3005"),
    MASK_LOATHING(new MaskItem(Masks.LOATHING), "3006"),
    MASK_SCORN(new MaskItem(Masks.SCORN), "3007"),
    MASK_STRENGTHENING(new MaskItem(Masks.STRENGTHENING), "3008"),
    MASK_WRATH(new MaskItem(Masks.WRATH), "3009"),
    MASK_ZEAL(new MaskItem(Masks.ZEAL), "3010"),
    MASK_MISERY(new MaskItem(Masks.MISERY), "3011"),

    PROJECTION_YI_HOCHMA_SHARD(new ProjectionYIHochmaShard(), "4001"),
    PROJECTION_YI_BINA_SHARD(new ProjectionYIBinaShard(), "4002"),

    CHAOS_DAMAGE_ASPECT(new AspectItem(new ChaosDamageAspect()), "5000"),
    FASTER_ATTACK_ASPECT(new AspectItem(new FasterAttackAspect()), "5001"),
    JUMP_ASPECT(new AspectItem(new JumpAspect()), "5002"),
    VOID_MANIPULATION_ASPECT(new AspectItem(new VoidManipulationAspect()), "5003"),
    FIRE_PENETRATION_ASPECT(new AspectItem(new FirePenetrationAspect()), "5004"),
    COLD_PENETRATION_ASPECT(new AspectItem(new ColdPenetrationAspect()), "5005"),
    ARMOUR_AURA_ASPECT(new AspectItem(new ArmourAuraAspect()), "5006"),
    LIGHTNING_PENETRATION_ASPECT(new AspectItem(new LightningPenetrationAspect()), "5007"),
    WIND_KILL_ASPECT(new AspectItem(new WindKillAspect()), "5008"),
    VOLATILITY_ASPECT(new AspectItem(new VolatilityAspect()), "5009"),
    DEFENSE_DISTORTION_ASPECT(new AspectItem(new DefenseDistortionAspect()), "5010"),
    ATTRACTION_ASPECT(new AspectItem(new AttractionAspect()), "5011"),
    TITAN_SHROUD_ASPECT(new AspectItem(new TitanShroudAspect()), "5012"),
    FIRE_AURA_ASPECT(new AspectItem(new FireAuraAspect()), "5014"),
    COLD_AURA_ASPECT(new AspectItem(new ColdAuraAspect()), "5015"),
    LIGHTNING_AURA_ASPECT(new AspectItem(new LightningAuraAspect()), "5016"),
    PRECISION_ASPECT(new AspectItem(new PrecisionAspect()), "5017"),
    ICE_EXPLOSION_ASPECT(new AspectItem(new IceExplosionAspect()), "5018"),
    SPARKS_ASPECT(new AspectItem(new SparksAspect()), "5019"),

    FRAGMENT_BATTLE_1(new BattleGroundItem(61, 1), "7001"),
    FRAGMENT_BATTLE_3(new BattleGroundItem(65, 3), "7002"),
    FRAGMENT_BATTLE_6(new BattleGroundItem(71, 6), "7003"),
    FRAGMENT_BATTLE_8(new BattleGroundItem(75, 8), "7004"),

    WAY_FRAGMENT_1(new WayFragmentItem("Путь: Осколок Беглецов", ZoneList.EDGE_FRAGMENT_1, 1, 1), "7005"),
    WAY_FRAGMENT_2(new WayFragmentItem("Путь: Мертвый осколок", ZoneList.EDGE_FRAGMENT_2, 2, 2), "7006"),
    WAY_FRAGMENT_3(new WayFragmentItem("Путь: Осколок Арахнофоба", ZoneList.EDGE_FRAGMENT_3, 3, 3), "7007"),
    WAY_FRAGMENT_4(new WayFragmentItem("Путь: Смежный осколок", ZoneList.EDGE_FRAGMENT_4, 7, 4), "7008"),
    WAY_FRAGMENT_5(new WayFragmentItem("Путь: Осколок эндермитов", ZoneList.EDGE_FRAGMENT_5, 8, 5), "7009"),
    WAY_FRAGMENT_6(new WayFragmentItem("Путь: Темный осколок", ZoneList.EDGE_FRAGMENT_6, 9, 6), "7010"),
    WAY_FRAGMENT_7(new WayFragmentItem("Путь: Осколок дракона", ZoneList.EDGE_FRAGMENT_7, 19, 7), "7011"),
    WAY_FRAGMENT_8(new WayFragmentItem("Путь: Обитель отшельника", ZoneList.EDGE_FRAGMENT_8, 20, 8), "7012"),

    // weight multiplier = 15, 10, 6, 1
    DIVINATION_CARD_1(new DivinationCardItem(DivinationCard.builder()
            .id("1")
            .weightMultiplier(15)
            .title("Восстановление мира")
            .deckStack(5)
            .entityLevelDrop(3)
            .build().applyReward(ItemObject.RESET_ORB, 5)), "5500"),
    DIVINATION_CARD_2(new DivinationCardItem(DivinationCard.builder()
            .id("2")
            .weightMultiplier(15)
            .title("Эпическое изменение")
            .deckStack(3)
            .entityLevelDrop(12)
            .build().applyReward(ItemObject.EPIC_REAGENT_VERGE_MOD_ITEM, 2)), "5501"),
    DIVINATION_CARD_3(new DivinationCardItem(DivinationCard.builder()
            .id("3")
            .weightMultiplier(15)
            .title("Смешанное изменение")
            .deckStack(3)
            .entityLevelDrop(2)
            .build().applyReward(ItemObject.MIXED_REAGENT_VERGE_MOD_ITEM, 1)), "5502"),
    DIVINATION_CARD_4(new DivinationCardItem(DivinationCard.builder()
            .id("4")
            .weightMultiplier(6)
            .title("Абсолютное изменение")
            .deckStack(6)
            .entityLevelDrop(35)
            .build().applyReward(ItemObject.ABSOLUTE_DUST_MOD_ITEM, 1)), "5503"),
    DIVINATION_CARD_5(new DivinationCardItem(DivinationCard.builder()
            .id("5")
            .weightMultiplier(6)
            .title("Искажающее")
            .deckStack(4)
            .entityLevelDrop(35)
            .build().applyReward(ItemObject.ORB_OF_DISTORTION_MOD_ITEM, 2)), "5504"),
    DIVINATION_CARD_6(new DivinationCardItem(DivinationCard.builder()
            .id("6")
            .weightMultiplier(10)
            .title("Молот Гефеста")
            .deckStack(3)
            .entityLevelDrop(12)
            .build().applyReward(ItemObject.ORB_OF_HEPHAESTUS_MOD_ITEM, 3)), "5505"),
    DIVINATION_CARD_7(new DivinationCardItem(DivinationCard.builder()
            .id("7")
            .weightMultiplier(15)
            .title("Наковальня Гефеста")
            .deckStack(6)
            .entityLevelDrop(8)
            .build().applyReward(ItemObject.ORB_OF_HEPHAESTUS_MOD_ITEM, 1)), "5506"),
    DIVINATION_CARD_8(new DivinationCardItem(DivinationCard.builder()
            .id("8")
            .weightMultiplier(16)
            .title("Случайность")
            .deckStack(10)
            .entityLevelDrop(35)
            .build().applyReward(ItemObject.FORM_OF_TYCHE_MOD_ITEM, 1)), "5507"),
    DIVINATION_CARD_9(new DivinationCardItem(DivinationCard.builder()
            .id("9")
            .weightMultiplier(10)
            .title("Образ случайности")
            .deckStack(1)
            .entityLevelDrop(35)
            .build().applyReward(ItemObject.FORM_OF_TYCHE_MOD_ITEM, 5)), "5508"),
    DIVINATION_CARD_10(new DivinationCardItem(DivinationCard.builder()
            .id("10")
            .weightMultiplier(6)
            .title("Удача")
            .deckStack(5)
            .entityLevelDrop(35)
            .build().applyReward(ItemObject.ESSENCE_OF_TYCHE_MOD_ITEM, 1)), "5509"),
    DIVINATION_CARD_11(new DivinationCardItem(DivinationCard.builder()
            .id("11")
            .weightMultiplier(1)
            .title("Сущность удачи")
            .deckStack(1)
            .entityLevelDrop(LevelStorage.getInstance().getStartShardEdgeLevel())
            .build().applyReward(ItemObject.SHARD_OF_ABYSS_MOD_ITEM, 5)), "5510"),
    DIVINATION_CARD_12(new DivinationCardItem(DivinationCard.builder()
            .id("12")
            .weightMultiplier(6)
            .title("Взгляд из Бездны")
            .deckStack(4)
            .entityLevelDrop(LevelStorage.getInstance().getStartShardEdgeLevel())
            .build().applyReward(ItemObject.SHARD_OF_ABYSS_MOD_ITEM, 1)), "5511"),
    DIVINATION_CARD_15(new DivinationCardItem(DivinationCard.builder()
            .id("15")
            .weightMultiplier(15)
            .title("Осквернение прекрасного")
            .deckStack(10)
            .entityLevelDrop(1)
            .build().applyReward(ItemObject.STAR_OF_CORRUPT_MOD_ITEM, 5)), "5514"),
    DIVINATION_CARD_17(new DivinationCardItem(DivinationCard.builder()
            .id("17")
            .weightMultiplier(1)
            .title("Частица чуждого")
            .deckStack(10)
            .entityLevelDrop(LevelStorage.getInstance().getStartShardEdgeLevel())
            .build().applyReward(ItemObject.DROP_OF_EDGE_MOD_ITEM, 1)), "5516"),
    DIVINATION_CARD_18(new DivinationCardItem(DivinationCard.builder()
            .id("18")
            .weightMultiplier(15)
            .title("Порядок и гармония")
            .deckStack(10)
            .entityLevelDrop(16)
            .build().applyReward(ItemObject.CLEANSING_TEAR_MOD_ITEM, 2)), "5517"),
    DIVINATION_CARD_19(new DivinationCardItem(DivinationCard.builder()
            .id("19")
            .weightMultiplier(1)
            .title("Пробуждение оскверненного")
            .deckStack(10)
            .entityLevelDrop(LevelStorage.getInstance().getStartShardEdgeLevel())
            .build().applyReward(ItemObject.EXALTED_ORB_MOD_ITEM, 1)), "5518"),
    DIVINATION_CARD_20(new DivinationCardItem(DivinationCard.builder()
            .id("20")
            .weightMultiplier(15)
            .title("Слепок чуждого")
            .deckStack(8)
            .entityLevelDrop(LevelStorage.getInstance().getStartShardEdgeLevel())
            .rewardDescription(Collections.singletonList("&f- Случайный &6слепок Грани&f, &65 &fштук"))
            .build()
            .taskFinder(() -> {
                final Map<ItemObject, Integer> result = new EnumMap<>(ItemObject.class);
                Arrays.stream(ItemObject.values()).filter(itemObject -> itemObject.getItem().checkInstance(MaskItem.class)).forEach(itemObject -> result.put(itemObject, 5));
                return result;
            })
    ), "5519"),
    DIVINATION_CARD_21(new DivinationCardItem(DivinationCard.builder()
            .id("21")
            .weightMultiplier(15)
            .title("Аспект от Тихэ")
            .deckStack(16)
            .entityLevelDrop(LevelStorage.getInstance().getStartShardEdgeLevel())
            .rewardDescription(Collections.singletonList("&f- Случайный &6аспект&f, &61 &fштука"))
            .build()
            .taskFinder(() -> {
                final Map<ItemObject, Integer> result = new HashMap<>();
                Arrays.stream(ItemObject.values()).filter(itemObject -> itemObject.getItem().getImperiumType() == ItemType.ASPECT).forEach(itemObject -> result.put(itemObject, 1));
                return result;
            })
    ), "5520"),
    DIVINATION_CARD_23(new DivinationCardItem(DivinationCard.builder()
            .id("23")
            .weightMultiplier(6)
            .title("Видение Скариги")
            .deckStack(12)
            .entityLevelDrop(18)
            .build().applyReward(ItemObject.SKARIGA_SWORD, 1)), "5522"),
    DIVINATION_CARD_24(new DivinationCardItem(DivinationCard.builder()
            .id("24")
            .weightMultiplier(6)
            .title("Видение Шахтера")
            .deckStack(12)
            .entityLevelDrop(60)
            .build().applyReward(ItemObject.MINER_HELMET, 1)), "5523"),
    DIVINATION_CARD_25(new DivinationCardItem(DivinationCard.builder()
            .id("25")
            .weightMultiplier(6)
            .title("Проекционная защиты Бины")
            .deckStack(12)
            .entityLevelDrop(-1)
            .build().applyReward(ItemObject.BINA_BOOTS, 1)), "5524"),
    DIVINATION_CARD_26(new DivinationCardItem(DivinationCard.builder()
            .id("26")
            .weightMultiplier(6)
            .title("Проекционная защиты Хохмы")
            .deckStack(12)
            .entityLevelDrop(-1)
            .build().applyReward(ItemObject.HOCHMA_BODY, 1)), "5525"),
    DIVINATION_CARD_27(new DivinationCardItem(DivinationCard.builder()
            .id("27")
            .weightMultiplier(6)
            .title("Идея Леса")
            .deckStack(12)
            .entityLevelDrop(44)
            .build().applyReward(ItemObject.FOREST_SPEAR, 1)), "5526"),
    DIVINATION_CARD_28(new DivinationCardItem(DivinationCard.builder()
            .id("28")
            .weightMultiplier(6)
            .title("Оружейная удача")
            .deckStack(12)
            .entityLevelDrop(27)
            .build().applyReward(ItemObject.MORT_DAGGER, 1)), "5527"),
    DIVINATION_CARD_29(new DivinationCardItem(DivinationCard.builder()
            .id("29")
            .weightMultiplier(6)
            .title("Наследие")
            .deckStack(12)
            .entityLevelDrop(-1)
            .build().applyReward(ItemObject.BATTLE_GROUND_LEGGINGS, 1)), "5528"),
    DIVINATION_CARD_30(new DivinationCardItem(DivinationCard.builder()
            .id("30")
            .weightMultiplier(10)
            .title("Накопление чуждого")
            .deckStack(12)
            .entityLevelDrop(LevelStorage.getInstance().getStartShardEdgeLevel())
            .rewardDescription(Collections.singletonList("&f- Случайный &6кристалл-инкубатор&f, &62 &fштуки"))
            .build()
            .taskFinder(() -> {
                final Map<ItemObject, Integer> result = new HashMap<>();
                Arrays.stream(ItemObject.values()).filter(itemObject -> itemObject.getItem().checkInstance(IncubatorItem.class)).forEach(itemObject -> result.put(itemObject, 2));
                return result;
            })
    ), "5529"),
    DIVINATION_CARD_31(new DivinationCardItem(DivinationCard.builder()
            .id("31")
            .weightMultiplier(10)
            .title("Приглашение наследия")
            .deckStack(6)
            .entityLevelDrop(LevelStorage.getInstance().getStartShardEdgeLevel())
            .rewardDescription(Collections.singletonList("&f- Случайное &6Приглашение на Поле Битвы"))
            .build()
            .taskFinder(() -> Map.of(
                    ItemObject.FRAGMENT_BATTLE_1, 1,
                    ItemObject.FRAGMENT_BATTLE_3, 1,
                    ItemObject.FRAGMENT_BATTLE_6, 1,
                    ItemObject.FRAGMENT_BATTLE_8, 1
            ))
    ), "5530"),

    STACKED_DECK(new StackedDeck(), "5600"),

    PICKAXE(new PickaxeItem(), "6000"),
    FISHING_ROD(new FishingRodItem(), "6001"),

    INCUBATOR_RANDOM_STANDARD_MATERIAL_1_ITEM(new IncubatorItem(Incubators.INCUBATOR_RANDOM_STANDARD_MATERIAL_1), "7501"),
    INCUBATOR_RANDOM_STANDARD_MATERIAL_2_ITEM(new IncubatorItem(Incubators.INCUBATOR_RANDOM_STANDARD_MATERIAL_2), "7502"),
    INCUBATOR_RANDOM_STANDARD_MATERIAL_3_ITEM(new IncubatorItem(Incubators.INCUBATOR_RANDOM_STANDARD_MATERIAL_3), "7503"),
    INCUBATOR_RANDOM_CATALYST_ITEM(new IncubatorItem(Incubators.INCUBATOR_RANDOM_CATALYST), "7504"),
    INCUBATOR_RANDOM_DOUBLE_DIRECTION_ARMOR_ITEM(new IncubatorItem(Incubators.INCUBATOR_RANDOM_DOUBLE_DIRECTION_ARMOR), "7505"),
    INCUBATOR_RANDOM_DOUBLE_DIRECTION_WEAPON_ITEM(new IncubatorItem(Incubators.INCUBATOR_RANDOM_DOUBLE_DIRECTION_WEAPON), "7506"),
    INCUBATOR_STACKED_DECK_ITEM(new IncubatorItem(Incubators.INCUBATOR_STACKED_DECK), "7507");

    private final ImperiumItem item;
    private final String uniqueId;

    private static List<ItemObject> potions;
    private static List<ItemObject> divinationCards;

    public static List<ItemObject> getPotions() {
        if (potions == null) {
            potions = Arrays.stream(ItemObject.values()).filter(item -> item.getItem().getImperiumType() == ItemType.POTION).toList();
        }
        return potions;
    }

    public static List<ItemObject> getDivinationCards() {
        if (divinationCards == null) {
            divinationCards = Arrays.stream(ItemObject.values()).filter(item -> item.getItem().checkInstance(DivinationCardItem.class)).toList();
        }
        return divinationCards;
    }

}
