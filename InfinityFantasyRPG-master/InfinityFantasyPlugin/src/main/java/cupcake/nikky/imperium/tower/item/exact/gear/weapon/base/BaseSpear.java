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

public class BaseSpear extends WeaponItem {

    @Getter
    private final ModifierBaseCreatorType baseCreatorType;

    public BaseSpear(@NotNull final WeaponType weaponType, @NotNull final ModifierBaseCreatorType baseCreatorType) {
        super(ItemRare.COMMON, weaponType, Material.WOOD_SWORD, (short) 1011, List.of("Сильно отталкивает", "Наносит урон по множеству целей"));

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
    public short getDurability() {
        if (getGeneralSettings().itemLevel() > LevelStorage.getInstance().getThirdStageLevel()) return 1014;
        if (getGeneralSettings().itemLevel() > LevelStorage.getInstance().getSecondStageLevel()) return 1013;
        if (getGeneralSettings().itemLevel() > LevelStorage.getInstance().getFirstStageLevel())  return 1012;
        return 1011;
    }

    @Override
    public void updateAttributes() {
        super.updateAttributes();
        if (getModifierSettings().hasExaltedSpecificModifier()) {
            getGeneralSettings().customNBTKey("weapons");
            getGeneralSettings().customNBTValue("alebarde1_diamond");
        }
    }

    @Override
    public ItemObject getObject() {
        return ItemObject.valueOf(getType().name());
    }

    @Override
    public ImperiumItem create() {
        return new BaseSpear(getType(), getBaseCreatorType());
    }

}

