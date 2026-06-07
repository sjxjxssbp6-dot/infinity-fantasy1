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

public class BaseBody extends ArmorItem {

    @Getter
    private final ModifierBaseCreatorType baseCreatorType;

    public BaseBody(@NotNull final BaseType baseType, @NotNull final ModifierBaseCreatorType baseCreatorType) {
        super(baseType, GearType.BODY, baseType.getBodyTitle(), ItemRare.COMMON, Material.LEATHER_CHESTPLATE, Collections.emptyList());
        this.baseCreatorType = baseCreatorType;
    }

    @Override
    protected void preInitialize() {
        super.preInitialize();
    }

    @Override
    public Material getMaterial() {
        if (getGeneralSettings().itemLevel() > LevelStorage.getInstance().getThirdStageLevel()) return Material.DIAMOND_CHESTPLATE;
        if (getGeneralSettings().itemLevel() > LevelStorage.getInstance().getSecondStageLevel()) return Material.IRON_CHESTPLATE;
        if (getGeneralSettings().itemLevel() > LevelStorage.getInstance().getFirstStageLevel()) return Material.CHAINMAIL_CHESTPLATE;
        return super.getMaterial();
    }

    @Override
    public ItemObject getObject() {
        return ItemObject.valueOf(getBaseType().name() + "_BODY");
    }

    @Override
    public ImperiumItem create() {
        return new BaseBody(getBaseType(), getBaseCreatorType());
    }

}
