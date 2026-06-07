package cupcake.nikky.imperium.tower.item.exact.gear.armour.unique;

import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.ItemObject;
import cupcake.nikky.imperium.tower.item.ItemRare;
import cupcake.nikky.imperium.tower.item.exact.gear.armour.BaseBoots;
import cupcake.nikky.imperium.tower.item.exact.gear.armour.BaseType;
import cupcake.nikky.imperium.tower.item.modifier.Modifier;
import cupcake.nikky.imperium.tower.item.modifier.ModifierPlace;
import cupcake.nikky.imperium.tower.item.modifier.ModifierProvider;
import cupcake.nikky.imperium.tower.item.modifier.ModifierType;
import cupcake.nikky.imperium.tower.item.modifier.base.ModifierBaseCreatorType;
import cupcake.nikky.imperium.tower.item.modifier.exact.ModifierItemListExtended;
import cupcake.nikky.imperium.tower.item.modifier.exact.ModifierUniqueItemList;

import java.util.Collections;
import java.util.Map;

public class BootsBina extends BaseBoots {

    private static final Map<Modifier, Integer> UNIQUE_MODIFIERS = Map.of(
            ModifierItemListExtended.FIRE_RESISTANCE, 7,
            ModifierUniqueItemList.CHANCE_TO_GAIN_FRENZY_CHARGE_ON_HIT, 1,
            ModifierItemListExtended.INCREASE_MAX_HEALTH, 3,
            ModifierItemListExtended.MAXIMUM_FIRE_RESISTANCE, 2,
            ModifierItemListExtended.MOVEMENT_SPEED, 6
    );

    public BootsBina() {
        super(BaseType.TARN, ModifierBaseCreatorType.TARN_BOOTS);
        ModifierProvider.getInstance().randomizeItemRarity(this, 76, Collections.singletonList(ItemRare.UNIQUE));
        this.setName("Проекционные боты Бины");
        this.getModifierSettings().removeSpecificModifierByPlace(ModifierPlace.PREFIX);
        this.getModifierSettings().removeSpecificModifierByPlace(ModifierPlace.SUFFIX);

        getLootSettings().lootGeneratorSettings().rarity(false).extendedModifiers(false).exaltedModifiers(false);

        getGeneralSettings().npcTradePrice(5_000);
        getGeneralSettings().customNBTKey("armors");
        getGeneralSettings().customNBTValue("snow");
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
        return ItemObject.BINA_BOOTS;
    }

    @Override
    public ImperiumItem create() {
        return new BootsBina();
    }

}
