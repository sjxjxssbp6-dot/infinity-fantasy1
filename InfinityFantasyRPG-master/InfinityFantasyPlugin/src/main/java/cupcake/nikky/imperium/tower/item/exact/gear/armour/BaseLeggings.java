package cupcake.nikky.imperium.tower.item.exact.gear.armour;

import cupcake.nikky.imperium.tower.client.protocol.GearType;
import cupcake.nikky.imperium.tower.item.*;
import cupcake.nikky.imperium.tower.item.modifier.base.ModifierBaseCreator;
import cupcake.nikky.imperium.tower.item.modifier.base.ModifierBaseCreatorType;
import cupcake.nikky.imperium.tower.level.LevelStorage;
import lombok.Getter;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class BaseLeggings extends ArmorItem {

    @Getter
    private final ModifierBaseCreatorType baseCreatorType;

    public BaseLeggings(@NotNull final BaseType baseType, @NotNull final ModifierBaseCreatorType baseCreatorType) {
        super(baseType, GearType.LEGGINGS, baseType.getLeggingsTitle(), ItemRare.COMMON, Material.LEATHER_LEGGINGS, Collections.emptyList());
        this.baseCreatorType = baseCreatorType;
    }

    @Override
    public Material getMaterial() {
        if (getGeneralSettings().itemLevel() > LevelStorage.getInstance().getThirdStageLevel()) return Material.DIAMOND_LEGGINGS;
        if (getGeneralSettings().itemLevel() > LevelStorage.getInstance().getSecondStageLevel()) return Material.IRON_LEGGINGS;
        if (getGeneralSettings().itemLevel() > LevelStorage.getInstance().getFirstStageLevel()) return Material.CHAINMAIL_LEGGINGS;
        return super.getMaterial();
    }

    @Override
    public ItemObject getObject() {
        return ItemObject.valueOf(getBaseType().name() + "_LEGGINGS");
    }

    @Override
    public ImperiumItem create() {
        return new BaseLeggings(getBaseType(), getBaseCreatorType());
    }

}
