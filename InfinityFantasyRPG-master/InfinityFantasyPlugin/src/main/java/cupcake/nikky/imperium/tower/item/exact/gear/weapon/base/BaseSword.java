package cupcake.nikky.imperium.tower.item.exact.gear.weapon.base;

import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.ItemObject;
import cupcake.nikky.imperium.tower.item.ItemRare;
import cupcake.nikky.imperium.tower.item.exact.gear.weapon.WeaponItem;
import cupcake.nikky.imperium.tower.item.exact.gear.weapon.WeaponType;
import cupcake.nikky.imperium.tower.item.modifier.base.ModifierBaseCreatorType;
import cupcake.nikky.imperium.tower.item.modifier.base.ModifierBaseRegistry;
import cupcake.nikky.imperium.tower.item.modifier.exact.ModifierItemListDirection;
import cupcake.nikky.imperium.tower.level.LevelStorage;
import lombok.Getter;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BaseSword extends WeaponItem {

    @Getter
    private final ModifierBaseCreatorType baseCreatorType;

    public BaseSword(@NotNull final WeaponType weaponType, @NotNull final ModifierBaseCreatorType baseCreatorType) {
        super(ItemRare.COMMON, weaponType, Material.WOOD_SWORD, (short) 0, List.of("Наносит урон по множеству целей"));

        this.baseCreatorType = baseCreatorType;

        getModifierSettings().registerPossibleModifier(
                ModifierItemListDirection.DAMAGE_LEECH_AS_LIFE,
                ModifierItemListDirection.GLOBAL_CRITICAL_CHANCE,
                ModifierItemListDirection.GLOBAL_CRITICAL_DAMAGE,
                ModifierItemListDirection.FLAT_CRITICAL_CHANCE,
                ModifierItemListDirection.INCREASE_DROP_QUANTITY_OF_ITEMS
        );

        this.getModifierSettings().registerPossibleModifier(ModifierBaseRegistry.createBaseModifiers(baseCreatorType));
        this.getGeneralSettings().requiredLevel(baseCreatorType.getRequiredUserLevel());
    }

    @Override
    public Material getMaterial() {
        if (getGeneralSettings().itemLevel() > LevelStorage.getInstance().getThirdStageLevel()) return Material.DIAMOND_SWORD;
        if (getGeneralSettings().itemLevel() > LevelStorage.getInstance().getSecondStageLevel()) return Material.IRON_SWORD;
        if (getGeneralSettings().itemLevel() > LevelStorage.getInstance().getFirstStageLevel()) return Material.STONE_SWORD;
        return Material.WOOD_SWORD;
    }

    @Override
    public void updateAttributes() {
        super.updateAttributes();
        if (getModifierSettings().hasExaltedSpecificModifier()) {
            getGeneralSettings().customNBTKey("weapons");
            getGeneralSettings().customNBTValue("felix_sword_diamond");
        }
    }

    @Override
    public ItemObject getObject() {
        return ItemObject.valueOf(getType().name());
    }

    @Override
    public ImperiumItem create() {
        return new BaseSword(getType(), getBaseCreatorType());
    }

}