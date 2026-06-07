package cupcake.nikky.imperium.tower.item.exact.gear.weapon.unique;

import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.ItemObject;
import cupcake.nikky.imperium.tower.item.ItemRare;
import cupcake.nikky.imperium.tower.item.exact.gear.weapon.WeaponType;
import cupcake.nikky.imperium.tower.item.exact.gear.weapon.base.BaseSword;
import cupcake.nikky.imperium.tower.item.modifier.Modifier;
import cupcake.nikky.imperium.tower.item.modifier.ModifierPlace;
import cupcake.nikky.imperium.tower.item.modifier.ModifierProvider;
import cupcake.nikky.imperium.tower.item.modifier.ModifierType;
import cupcake.nikky.imperium.tower.item.modifier.base.ModifierBaseCreatorType;
import cupcake.nikky.imperium.tower.item.modifier.exact.ModifierItemListExtended;

import java.util.Collections;
import java.util.Map;

public class SkarigaSword extends BaseSword {

    private static final Map<Modifier, Integer> UNIQUE_MODIFIERS = Map.of(
            ModifierItemListExtended.GLOBAL_CRITICAL_CHANCE, 3,
            ModifierItemListExtended.GLOBAL_CRITICAL_DAMAGE, 3,
            ModifierItemListExtended.DAMAGE_LEECH_AS_LIFE, 1,
            ModifierItemListExtended.ADD_FIRE_DAMAGE, 3,
            ModifierItemListExtended.ATTACK_SPEED, 3
    );

    public SkarigaSword() {
        super(WeaponType.REKN_SWORD, ModifierBaseCreatorType.REKN_SWORD);
        ModifierProvider.getInstance().randomizeItemRarity(this, 12, Collections.singletonList(ItemRare.UNIQUE));
        this.setName("Клинок Скариги");
        this.getModifierSettings().removeSpecificModifierByPlace(ModifierPlace.PREFIX);
        this.getModifierSettings().removeSpecificModifierByPlace(ModifierPlace.SUFFIX);

        getLootSettings().lootGeneratorSettings().rarity(false).extendedModifiers(false).exaltedModifiers(false);

        getGeneralSettings().npcTradePrice(1_000);
        getGeneralSettings().customNBTKey("weapons");
        getGeneralSettings().customNBTValue("devil_sword_stone");
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
        return ItemObject.SKARIGA_SWORD;
    }

    @Override
    public ImperiumItem create() {
        return new SkarigaSword();
    }
}
