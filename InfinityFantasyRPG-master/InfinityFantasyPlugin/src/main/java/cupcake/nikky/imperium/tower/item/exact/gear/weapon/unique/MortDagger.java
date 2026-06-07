package cupcake.nikky.imperium.tower.item.exact.gear.weapon.unique;

import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.ItemObject;
import cupcake.nikky.imperium.tower.item.ItemRare;
import cupcake.nikky.imperium.tower.item.exact.gear.weapon.WeaponType;
import cupcake.nikky.imperium.tower.item.exact.gear.weapon.base.BaseDagger;
import cupcake.nikky.imperium.tower.item.modifier.Modifier;
import cupcake.nikky.imperium.tower.item.modifier.ModifierPlace;
import cupcake.nikky.imperium.tower.item.modifier.ModifierProvider;
import cupcake.nikky.imperium.tower.item.modifier.ModifierType;
import cupcake.nikky.imperium.tower.item.modifier.base.ModifierBaseCreatorType;
import cupcake.nikky.imperium.tower.item.modifier.exact.ModifierItemListExtended;
import cupcake.nikky.imperium.tower.item.modifier.exact.ModifierUniqueItemList;

import java.util.Collections;
import java.util.Map;

public class MortDagger extends BaseDagger {

    private static final Map<Modifier, Integer> UNIQUE_MODIFIERS = Map.of(
            ModifierItemListExtended.GLOBAL_CRITICAL_CHANCE, 4,
            ModifierItemListExtended.GLOBAL_CRITICAL_DAMAGE, 4,
            ModifierUniqueItemList.CHANCE_TO_POISON_ON_HIT, 1,
            ModifierItemListExtended.ADD_FIRE_DAMAGE, 5,
            ModifierItemListExtended.ADD_COLD_DAMAGE, 5
    );

    public MortDagger() {
        super(WeaponType.SALT_DAGGER, ModifierBaseCreatorType.SALT_DAGGER);
        ModifierProvider.getInstance().randomizeItemRarity(this, 26, Collections.singletonList(ItemRare.UNIQUE));
        this.setName("Кинжал Морты");
        this.getModifierSettings().removeSpecificModifierByPlace(ModifierPlace.PREFIX);
        this.getModifierSettings().removeSpecificModifierByPlace(ModifierPlace.SUFFIX);

        getLootSettings().lootGeneratorSettings().rarity(false).extendedModifiers(false).exaltedModifiers(false);

        getGeneralSettings().npcTradePrice(2_000);
        getGeneralSettings().customNBTKey("weapons");
        getGeneralSettings().customNBTValue("mythril_dagger_stone");
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
        return ItemObject.MORT_DAGGER;
    }

    @Override
    public ImperiumItem create() {
        return new MortDagger();
    }

}
