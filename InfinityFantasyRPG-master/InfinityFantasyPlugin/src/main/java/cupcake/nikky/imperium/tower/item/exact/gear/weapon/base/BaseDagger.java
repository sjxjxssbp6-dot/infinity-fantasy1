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

public class BaseDagger extends WeaponItem {

    @Getter
    private final ModifierBaseCreatorType baseCreatorType;

    public BaseDagger(@NotNull final WeaponType weaponType, @NotNull final ModifierBaseCreatorType baseCreatorType) {
        super(ItemRare.COMMON, weaponType, Material.WOOD_SWORD, (short) 1001, List.of("Наносит урон только по одиночной цели"));

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
        if (getGeneralSettings().itemLevel() > LevelStorage.getInstance().getThirdStageLevel()) return 1004;
        if (getGeneralSettings().itemLevel() > LevelStorage.getInstance().getSecondStageLevel()) return 1003;
        if (getGeneralSettings().itemLevel() > LevelStorage.getInstance().getFirstStageLevel())  return 1002;
        return 1001;
    }

    @Override
    public void updateAttributes() {
        super.updateAttributes();
        if (getModifierSettings().hasExaltedSpecificModifier()) {
            getGeneralSettings().customNBTKey("weapons");
            getGeneralSettings().customNBTValue("mythril_dagger_diamond");
        }
    }

    @Override
    public ItemObject getObject() {
        return ItemObject.valueOf(getType().name());
    }

    @Override
    public ImperiumItem create() {
        return new BaseDagger(getType(), getBaseCreatorType());
    }

}
