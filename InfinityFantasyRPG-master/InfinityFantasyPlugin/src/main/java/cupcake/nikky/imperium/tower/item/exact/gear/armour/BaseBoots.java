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

public class BaseBoots extends ArmorItem {

    @Getter
    private final ModifierBaseCreatorType baseCreatorType;

    public BaseBoots(@NotNull final BaseType baseType, @NotNull final ModifierBaseCreatorType baseCreatorType) {
        super(baseType, GearType.BOOTS, baseType.getBootsTitle(), ItemRare.COMMON, Material.LEATHER_BOOTS, Collections.emptyList());
        this.baseCreatorType = baseCreatorType;
    }

    @Override
    public Material getMaterial() {
        if (getGeneralSettings().itemLevel() > LevelStorage.getInstance().getThirdStageLevel()) return Material.DIAMOND_BOOTS;
        if (getGeneralSettings().itemLevel() > LevelStorage.getInstance().getSecondStageLevel()) return Material.IRON_BOOTS;
        if (getGeneralSettings().itemLevel() > LevelStorage.getInstance().getFirstStageLevel()) return Material.CHAINMAIL_BOOTS;
        return super.getMaterial();
    }

    @Override
    public ItemObject getObject() {
        return ItemObject.valueOf(getBaseType().name() + "_BOOTS");
    }

    @Override
    public ImperiumItem create() {
        return new BaseBoots(getBaseType(), getBaseCreatorType());
    }

}
