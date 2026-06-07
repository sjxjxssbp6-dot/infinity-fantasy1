package cupcake.nikky.imperium.tower.incubator;

import cupcake.nikky.imperium.tower.incubator.reward.*;
import cupcake.nikky.imperium.tower.incubator.reward.value.RandomIntValue;
import cupcake.nikky.imperium.tower.incubator.reward.value.StableIntValue;
import cupcake.nikky.imperium.tower.item.ItemObject;
import cupcake.nikky.imperium.tower.item.exact.gear.armour.ArmorItem;
import cupcake.nikky.imperium.tower.item.exact.gear.weapon.WeaponItem;
import cupcake.nikky.imperium.tower.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum Incubators {

    INCUBATOR_RANDOM_STANDARD_MATERIAL_1(
            IncubatorRequirement.create().deathRequirement(75).levelRequirement(61),
            new IncubatorItemRandomReward(List.of(
                    ItemObject.FORM_OF_TYCHE_MOD_ITEM.getItem().create(),
                    ItemObject.EPIC_REAGENT_VERGE_MOD_ITEM.getItem().create(),
                    ItemObject.ABSOLUTE_DUST_MOD_ITEM.getItem().create(),
                    ItemObject.MIXED_REAGENT_VERGE_MOD_ITEM.getItem().create(),
                    ItemObject.ORB_OF_DISTORTION_MOD_ITEM.getItem().create(),
                    ItemObject.ORB_OF_HEPHAESTUS_MOD_ITEM.getItem().create(),
                    ItemObject.ESSENCE_OF_TYCHE_MOD_ITEM.getItem().create(),
                    ItemObject.SHARD_OF_ABYSS_MOD_ITEM.getItem().create(),
                    ItemObject.STAR_OF_CORRUPT_MOD_ITEM.getItem().create(),
                    ItemObject.CLEANSING_TEAR_MOD_ITEM.getItem().create()
                    ), new RandomIntValue(1, 2), "&61-2 &fслучайных базовых материалов")),
    INCUBATOR_RANDOM_STANDARD_MATERIAL_2(
            IncubatorRequirement.create().deathRequirement(75).levelRequirement(71),
            new IncubatorItemRandomReward(List.of(
                    ItemObject.FORM_OF_TYCHE_MOD_ITEM.getItem().create(),
                    ItemObject.EPIC_REAGENT_VERGE_MOD_ITEM.getItem().create(),
                    ItemObject.ABSOLUTE_DUST_MOD_ITEM.getItem().create(),
                    ItemObject.MIXED_REAGENT_VERGE_MOD_ITEM.getItem().create(),
                    ItemObject.ORB_OF_DISTORTION_MOD_ITEM.getItem().create(),
                    ItemObject.ORB_OF_HEPHAESTUS_MOD_ITEM.getItem().create(),
                    ItemObject.ESSENCE_OF_TYCHE_MOD_ITEM.getItem().create(),
                    ItemObject.SHARD_OF_ABYSS_MOD_ITEM.getItem().create(),
                    ItemObject.STAR_OF_CORRUPT_MOD_ITEM.getItem().create(),
                    ItemObject.CLEANSING_TEAR_MOD_ITEM.getItem().create()
                    ), new RandomIntValue(2, 3), "&62-3 &fслучайных базовых материалов")),
    INCUBATOR_RANDOM_STANDARD_MATERIAL_3(
            IncubatorRequirement.create().deathRequirement(75).levelRequirement(75),
            new IncubatorItemRandomReward(List.of(
                    ItemObject.FORM_OF_TYCHE_MOD_ITEM.getItem().create(),
                    ItemObject.EPIC_REAGENT_VERGE_MOD_ITEM.getItem().create(),
                    ItemObject.ABSOLUTE_DUST_MOD_ITEM.getItem().create(),
                    ItemObject.MIXED_REAGENT_VERGE_MOD_ITEM.getItem().create(),
                    ItemObject.ORB_OF_DISTORTION_MOD_ITEM.getItem().create(),
                    ItemObject.ORB_OF_HEPHAESTUS_MOD_ITEM.getItem().create(),
                    ItemObject.ESSENCE_OF_TYCHE_MOD_ITEM.getItem().create(),
                    ItemObject.SHARD_OF_ABYSS_MOD_ITEM.getItem().create(),
                    ItemObject.STAR_OF_CORRUPT_MOD_ITEM.getItem().create(),
                    ItemObject.CLEANSING_TEAR_MOD_ITEM.getItem().create()
            ), new RandomIntValue(4, 5), "&64-5 &fслучайных базовых материалов")),
    INCUBATOR_RANDOM_CATALYST(
            IncubatorRequirement.create().deathRequirement(75).levelRequirement(75),
            new IncubatorItemRandomReward(List.of(
                    ItemObject.CATALYST_RESISTANCE_MOD_ITEM.getItem().create(),
                    ItemObject.CATALYST_PHYS_CHAOS_MOD_ITEM.getItem().create(),
                    ItemObject.CATALYST_LIFE_MANA_MOD_ITEM.getItem().create(),
                    ItemObject.CATALYST_ELEM_DMG_MOD_ITEM.getItem().create(),
                    ItemObject.CATALYST_DEFENSE_MOD_ITEM.getItem().create(),
                    ItemObject.CATALYST_SPEED_MOD_ITEM.getItem().create(),
                    ItemObject.CATALYST_CRIT_MOD_ITEM.getItem().create()
            ), new RandomIntValue(1, 3), "&61-3 &fслучайных катализаторов качества")),
    INCUBATOR_RANDOM_EDGE_MASK(
            IncubatorRequirement.create().deathRequirement(75).levelRequirement(67),
            new IncubatorItemRandomReward(List.of(
                    ItemObject.MASK_ANGER.getItem().create(),
                    ItemObject.MASK_CONTEMPT.getItem().create(),
                    ItemObject.MASK_DREAD.getItem().create(),
                    ItemObject.MASK_ENVY.getItem().create(),
                    ItemObject.MASK_HATRED.getItem().create(),
                    ItemObject.MASK_LOATHING.getItem().create(),
                    ItemObject.MASK_ZEAL.getItem().create(),
                    ItemObject.MASK_STRENGTHENING.getItem().create(),
                    ItemObject.MASK_MISERY.getItem().create(),
                    ItemObject.MASK_SCORN.getItem().create(),
                    ItemObject.MASK_WRATH.getItem().create()
            ), new RandomIntValue(1, 3), "&61-3 &fслучайных слепка Грани")),

    INCUBATOR_STACKED_DECK(IncubatorRequirement.create().deathRequirement(75).levelRequirement(61),
            new IncubatorItemReward(ItemObject.STACKED_DECK.getItem().create(), new RandomIntValue(1, 5))),

    INCUBATOR_RANDOM_DOUBLE_DIRECTION_ARMOR(IncubatorRequirement.create().deathRequirement(150).levelRequirement(76),
            new IncubatorRandomGearReward(List.of(
                    ItemObject.FIRM_BODY.getItem().create(),
                    ItemObject.FIRM_HELMET.getItem().create(),
                    ItemObject.FIRM_BOOTS.getItem().create(),
                    ItemObject.FIRM_LEGGINGS.getItem().create(),
                    ItemObject.REKN_BODY.getItem().create(),
                    ItemObject.REKN_HELMET.getItem().create(),
                    ItemObject.REKN_BOOTS.getItem().create(),
                    ItemObject.REKN_LEGGINGS.getItem().create(),
                    ItemObject.SALT_BODY.getItem().create(),
                    ItemObject.SALT_HELMET.getItem().create(),
                    ItemObject.SALT_BOOTS.getItem().create(),
                    ItemObject.SALT_LEGGINGS.getItem().create(),
                    ItemObject.TARN_BODY.getItem().create(),
                    ItemObject.TARN_HELMET.getItem().create(),
                    ItemObject.TARN_BOOTS.getItem().create(),
                    ItemObject.TARN_LEGGINGS.getItem().create(),
                    ItemObject.KELN_BODY.getItem().create(),
                    ItemObject.KELN_HELMET.getItem().create(),
                    ItemObject.KELN_BOOTS.getItem().create(),
                    ItemObject.KELN_LEGGINGS.getItem().create()
            ), new StableIntValue(1), "&61 &fслучайная броня с двойным модификатором направления", player -> player.<User>getBungeePlayer().getLevel(),
                    imperiumItem -> imperiumItem.apply(ArmorItem.class).getName() + " с Поля Битвы")),
    INCUBATOR_RANDOM_DOUBLE_DIRECTION_WEAPON(IncubatorRequirement.create().deathRequirement(150).levelRequirement(76),
            new IncubatorRandomGearReward(List.of(
                    ItemObject.FIRM_SWORD.getItem().create(),
                    ItemObject.FIRM_DAGGER.getItem().create(),
                    ItemObject.FIRM_SPEAR.getItem().create(),
                    ItemObject.FIRM_AXE.getItem().create(),
                    ItemObject.REKN_SWORD.getItem().create(),
                    ItemObject.REKN_DAGGER.getItem().create(),
                    ItemObject.REKN_SPEAR.getItem().create(),
                    ItemObject.REKN_AXE.getItem().create(),
                    ItemObject.SALT_SWORD.getItem().create(),
                    ItemObject.SALT_DAGGER.getItem().create(),
                    ItemObject.SALT_SPEAR.getItem().create(),
                    ItemObject.SALT_AXE.getItem().create(),
                    ItemObject.TARN_SWORD.getItem().create(),
                    ItemObject.TARN_DAGGER.getItem().create(),
                    ItemObject.TARN_SPEAR.getItem().create(),
                    ItemObject.TARN_AXE.getItem().create(),
                    ItemObject.KELN_SWORD.getItem().create(),
                    ItemObject.KELN_DAGGER.getItem().create(),
                    ItemObject.KELN_SPEAR.getItem().create(),
                    ItemObject.KELN_AXE.getItem().create()
            ), new StableIntValue(1), "&61 &fслучайное оружие с двойным модификатором направления", player -> player.<User>getBungeePlayer().getLevel(),
                    imperiumItem -> imperiumItem.apply(WeaponItem.class).getName() + " с Поля Битвы"));

    private final IncubatorRequirement requirement;
    private final IncubatorReward reward;

    public Incubator createEmptyInstance() {
        return new Incubator(this, new IncubatorData());
    }

}
