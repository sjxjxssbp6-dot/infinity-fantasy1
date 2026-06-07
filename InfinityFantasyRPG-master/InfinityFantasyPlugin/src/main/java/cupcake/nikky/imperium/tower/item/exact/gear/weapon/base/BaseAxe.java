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

public class BaseAxe extends WeaponItem {

    @Getter
    private final ModifierBaseCreatorType baseCreatorType;

    public BaseAxe(@NotNull final WeaponType weaponType, @NotNull final ModifierBaseCreatorType baseCreatorType) {
        super(ItemRare.COMMON, weaponType, Material.WOOD_SWORD, (short) 1016, List.of("Притягивает монстров вокруг к основному противнику", "Наносит урон по множеству целей"));

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
    public ItemObject getObject() {
        return ItemObject.valueOf(getType().name());
    }

    @Override
    public short getDurability() {
        if (getGeneralSettings().itemLevel() > LevelStorage.getInstance().getThirdStageLevel()) return 1019;
        if (getGeneralSettings().itemLevel() > LevelStorage.getInstance().getSecondStageLevel()) return 1018;
        if (getGeneralSettings().itemLevel() > LevelStorage.getInstance().getFirstStageLevel())  return 1017;
        return 1016;
    }

    @Override
    public ImperiumItem create() {
        return new BaseAxe(getType(), getBaseCreatorType());
    }

    @Override
    public void updateAttributes() {
        super.updateAttributes();
        if (getModifierSettings().hasExaltedSpecificModifier()) {
            getGeneralSettings().customNBTKey("weapons");
            getGeneralSettings().customNBTValue("derex_axe_diamond");
        }
    }
}
