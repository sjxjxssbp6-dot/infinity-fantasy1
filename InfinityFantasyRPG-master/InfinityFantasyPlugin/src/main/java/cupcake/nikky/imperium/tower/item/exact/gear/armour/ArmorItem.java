package cupcake.nikky.imperium.tower.item.exact.gear.armour;

import cupcake.nikky.imperium.tower.client.protocol.GearType;
import cupcake.nikky.imperium.tower.item.*;
import cupcake.nikky.imperium.tower.item.modifier.base.ModifierBaseCreatorType;
import cupcake.nikky.imperium.tower.item.modifier.base.ModifierBaseRegistry;
import cupcake.nikky.imperium.tower.item.modifier.exact.ModifierItemListDirection;
import cupcake.nikky.imperium.tower.item.watcher.ItemWatcher;
import cupcake.nikky.imperium.tower.level.LevelStorage;
import lombok.Getter;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class ArmorItem extends DefaultImperiumItem {

    @Getter
    private final BaseType baseType;
    @Getter
    private final GearType gearType;

    public ArmorItem(@NotNull final BaseType baseType, @NotNull final GearType gearType, @NotNull final String name, @NotNull final ItemRare itemRare,
                     @NotNull final Material material, @NotNull final List<String> description) {
        super(itemRare, ItemType.ARMOR, material, (short) 0, name, description);

        this.baseType = baseType;
        this.gearType = gearType;

        getGeneralSettings().npcTradePrice(-1);
        getLootSettings().lootGeneratorSettings().maxItemLevel(LevelStorage.getInstance().getArmorLevel())
                .potential(true).rarity(true).baseModifiers(true).directionModifiers(true).extendedModifiers(true).exaltedModifiers(true);
        getDescriptionSettings().showItemLevel(true).showItemType(true).itemLevelPattern("&fУровень снаряжения: %s%d");

        this.getItemWatcherRegistry().registerWatcher(ItemWatcher.LEVEL);
        this.getItemWatcherRegistry().registerWatcher(ItemWatcher.DISPLAY_NAME);
        this.getItemWatcherRegistry().registerWatcher(ItemWatcher.DROPPABLE);
        this.getItemWatcherRegistry().registerWatcher(ItemWatcher.MODIFIERS);
        this.getItemWatcherRegistry().registerWatcher(ItemWatcher.ASPECT);
        this.getItemWatcherRegistry().registerWatcher(ItemWatcher.COPY_STATEMENT);
        this.getItemWatcherRegistry().registerWatcher(ItemWatcher.ASPECT_CORRUPT);
        this.getItemWatcherRegistry().registerWatcher(ItemWatcher.CORRUPTED);
        this.getItemWatcherRegistry().registerWatcher(ItemWatcher.RARITY);
        this.getItemWatcherRegistry().registerWatcher(ItemWatcher.REQUIRED_LEVEL);
        this.getItemWatcherRegistry().registerWatcher(ItemWatcher.ASPECT_EXALTED_VALUE);
        this.getItemWatcherRegistry().registerWatcher(ItemWatcher.ASPECT_COPY_STATEMENT);
        this.getItemWatcherRegistry().registerWatcher(ItemWatcher.GEAR_QUALITY);
        this.getItemWatcherRegistry().registerWatcher(ItemWatcher.GEAR_TAG_QUALITY);
        this.getItemWatcherRegistry().registerWatcher(ItemWatcher.GEAR_POTENTIAL);

        if (this.getModifierSettings().findPossibleModifiersSize() > 0) return;

        var baseCreatorType =
                this.checkInstance(BaseHelmet.class) ? baseType.getHelmetBaseCreatorType() :
                this.checkInstance(BaseBody.class) ? baseType.getBodyBaseCreatorType() :
                this.checkInstance(BaseLeggings.class) ? baseType.getLeggingsBaseCreatorType() : baseType.getBootsBaseCreatorType();
        getModifierSettings().registerPossibleModifier(ModifierBaseRegistry.createBaseModifiers(baseCreatorType));
        this.getGeneralSettings().requiredLevel(baseCreatorType.getRequiredUserLevel());
        getModifierSettings().registerPossibleModifier(ModifierBaseRegistry.findExtendedModifiers(this));

        getModifierSettings().registerPossibleModifier(
                ModifierItemListDirection.DEFENSE,
                ModifierItemListDirection.REGENERATION_IN_FIGHT,
                ModifierItemListDirection.ADDITIONAL_MAX_HEALTH,
                ModifierItemListDirection.LIGHTNING_RESISTANCE,
                ModifierItemListDirection.COLD_RESISTANCE,
                ModifierItemListDirection.FIRE_RESISTANCE,
                ModifierItemListDirection.CHAOS_RESISTANCE,
                ModifierItemListDirection.ELEMENTAL_DAMAGE,
                ModifierItemListDirection.ALL_ELEMENTAL_RESISTANCE,
                ModifierItemListDirection.INCREASED_MANA_RESERVE_EFFICIENCY,
                ModifierItemListDirection.ADDITIONAL_MAX_MANA,
                ModifierItemListDirection.INCREASED_MAXIMUM_MANA,
                ModifierItemListDirection.ADDITIONAL_MANA_REGENERATION,
                ModifierItemListDirection.INCREASE_DROP_QUANTITY_OF_ITEMS
        );
    }

}
