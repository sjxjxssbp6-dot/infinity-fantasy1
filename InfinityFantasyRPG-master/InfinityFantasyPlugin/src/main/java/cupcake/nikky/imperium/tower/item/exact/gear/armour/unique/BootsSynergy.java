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

public class BootsSynergy extends BaseBoots {

    private static final Map<Modifier, Integer> UNIQUE_MODIFIERS = Map.of(
            ModifierItemListExtended.CHAOS_RESISTANCE, 6,
            ModifierUniqueItemList.CAN_RECEIVE_ONLY_PHYSICAL_DAMAGE_BY_PLAYER, 1,
            ModifierUniqueItemList.CRITICAL_CHANCE_ON_PLAYER, 1,
            ModifierItemListExtended.MOVEMENT_SPEED, 6,
            ModifierItemListExtended.DEFENSE, 7,
            ModifierItemListExtended.INCREASE_DAMAGE, 7
    );

    public BootsSynergy() {
        super(BaseType.KELN, ModifierBaseCreatorType.KELN_BOOTS);
        ModifierProvider.getInstance().randomizeItemRarity(this, 61, Collections.singletonList(ItemRare.UNIQUE));
        this.setName("Боты \"Синергия\"");
        this.getModifierSettings().removeSpecificModifierByPlace(ModifierPlace.PREFIX);
        this.getModifierSettings().removeSpecificModifierByPlace(ModifierPlace.SUFFIX);

        getLootSettings().lootGeneratorSettings().rarity(false).extendedModifiers(false).exaltedModifiers(false);

        getGeneralSettings().npcTradePrice(5_000);
        getGeneralSettings().customNBTKey("armors");
        getGeneralSettings().customNBTValue("crystal");
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
        return ItemObject.SYNERGY_BOOTS;
    }

    @Override
    public ImperiumItem create() {
        return new BootsSynergy();
    }
}
