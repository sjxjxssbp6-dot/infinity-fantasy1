package cupcake.nikky.imperium.tower.item.exact.gear.armour.unique;

import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.ItemObject;
import cupcake.nikky.imperium.tower.item.ItemRare;
import cupcake.nikky.imperium.tower.item.exact.gear.armour.BaseLeggings;
import cupcake.nikky.imperium.tower.item.exact.gear.armour.BaseType;
import cupcake.nikky.imperium.tower.item.modifier.Modifier;
import cupcake.nikky.imperium.tower.item.modifier.ModifierPlace;
import cupcake.nikky.imperium.tower.item.modifier.ModifierProvider;
import cupcake.nikky.imperium.tower.item.modifier.ModifierType;
import cupcake.nikky.imperium.tower.item.modifier.base.ModifierBaseCreatorType;
import cupcake.nikky.imperium.tower.item.modifier.exact.ModifierItemListExtended;

import java.util.Collections;
import java.util.Map;

public class LeggingsBattleGround extends BaseLeggings {

    private static final Map<Modifier, Integer> UNIQUE_MODIFIERS = Map.of(
            ModifierItemListExtended.LIGHTNING_RESISTANCE, 7,
            ModifierItemListExtended.REGENERATION_IN_FIGHT, 7,
            ModifierItemListExtended.ADDITIONAL_MANA_REGENERATION, 5,
            ModifierItemListExtended.ADDITIONAL_MAX_MANA, 11,
            ModifierItemListExtended.INCREASE_ELEMENTAL_DAMAGE, 7,
            ModifierItemListExtended.INCREASE_DAMAGE, 7
    );

    public LeggingsBattleGround() {
        super(BaseType.KELN, ModifierBaseCreatorType.KELN_LEGGINGS);
        ModifierProvider.getInstance().randomizeItemRarity(this, 75, Collections.singletonList(ItemRare.UNIQUE));
        this.setName("Наследие Поля Битвы");
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
        return ItemObject.BATTLE_GROUND_LEGGINGS;
    }

    @Override
    public ImperiumItem create() {
        return new LeggingsBattleGround();
    }

}
