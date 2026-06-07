package cupcake.nikky.imperium.tower.item.exact.gear.weapon.unique;

import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.ItemObject;
import cupcake.nikky.imperium.tower.item.ItemRare;
import cupcake.nikky.imperium.tower.item.exact.gear.weapon.WeaponType;
import cupcake.nikky.imperium.tower.item.exact.gear.weapon.base.BaseSpear;
import cupcake.nikky.imperium.tower.item.modifier.Modifier;
import cupcake.nikky.imperium.tower.item.modifier.ModifierPlace;
import cupcake.nikky.imperium.tower.item.modifier.ModifierProvider;
import cupcake.nikky.imperium.tower.item.modifier.ModifierType;
import cupcake.nikky.imperium.tower.item.modifier.base.ModifierBaseCreatorType;
import cupcake.nikky.imperium.tower.item.modifier.exact.ModifierItemListExtended;
import cupcake.nikky.imperium.tower.item.modifier.exact.ModifierUniqueItemList;

import java.util.Collections;
import java.util.Map;

public class ForestSpear extends BaseSpear {

    private static final Map<Modifier, Integer> UNIQUE_MODIFIERS = Map.of(
            ModifierItemListExtended.GLOBAL_CRITICAL_CHANCE, 6,
            ModifierUniqueItemList.CHANCE_TO_CAUSE_BLEEDING_ON_HIT, 1,
            ModifierItemListExtended.DAMAGE_LEECH_AS_LIFE, 2,
            ModifierItemListExtended.ACCURACY, 4,
            ModifierItemListExtended.ADD_FIRE_DAMAGE, 7,
            ModifierItemListExtended.ATTACK_SPEED, 6
    );

    public ForestSpear() {
        super(WeaponType.TARN_SPEAR, ModifierBaseCreatorType.TARN_SPEAR);
        ModifierProvider.getInstance().randomizeItemRarity(this, 44, Collections.singletonList(ItemRare.UNIQUE));
        this.setName("Копье Леса");
        this.getModifierSettings().removeSpecificModifierByPlace(ModifierPlace.PREFIX);
        this.getModifierSettings().removeSpecificModifierByPlace(ModifierPlace.SUFFIX);

        getLootSettings().lootGeneratorSettings().rarity(false).extendedModifiers(false).exaltedModifiers(false);

        getGeneralSettings().npcTradePrice(3_000);
        getGeneralSettings().customNBTKey("weapons");
        getGeneralSettings().customNBTValue("spear_2_iron");
    }

    @Override
    public void updateAttributes() {
        if (this.getModifierSettings().findSpecificExtendedModifiersSize() == 0) {
            UNIQUE_MODIFIERS.forEach((modifier, tier) -> this.getModifierSettings().registerSpecificModifier(modifier.process().type(ModifierType.EXTENDED).apply(tier)));
        }

        super.updateAttributes();
    }

    @Override
    public ItemObject getObject() {
        return ItemObject.FOREST_SPEAR;
    }

    @Override
    public ImperiumItem create() {
        return new ForestSpear();
    }

}
