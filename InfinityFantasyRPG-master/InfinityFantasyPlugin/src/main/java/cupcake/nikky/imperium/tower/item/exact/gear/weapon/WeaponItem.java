package cupcake.nikky.imperium.tower.item.exact.gear.weapon;

import cupcake.nikky.imperium.tower.item.DefaultImperiumItem;
import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.ItemRare;
import cupcake.nikky.imperium.tower.item.ItemType;
import cupcake.nikky.imperium.tower.item.modifier.base.ModifierBaseRegistry;
import cupcake.nikky.imperium.tower.item.watcher.ItemWatcher;
import cupcake.nikky.imperium.tower.level.LevelStorage;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class WeaponItem extends DefaultImperiumItem {

    @Getter
    private final WeaponType type;

    protected WeaponItem(ItemRare rare, WeaponType type, Material material, short durability, List<String> additionalDescription) {
        super(rare, ItemType.WEAPON, material, durability, type.getTitle(), additionalDescription);

        this.type = type;

        getDamageSettings().setAttacks(type.getAttackSpeed());
        getDamageSettings().setDamageable(true);
        getLootSettings().lootGeneratorSettings().maxItemLevel(LevelStorage.getInstance().getArmorLevel())
                .potential(true).rarity(true).baseModifiers(true).directionModifiers(true).extendedModifiers(true).exaltedModifiers(true)
                .corruptionSettings()
                .minCorrupt(data -> 0)
                .maxCorrupt(data -> Math.max(data.getLevel() - LevelStorage.getInstance().getStartShardEdgeLevel() - 7, 0) / 3);
        getDescriptionSettings().showItemLevel(true).showItemType(true).itemLevelPattern("&fУровень снаряжения: %s%d");
        getGeneralSettings().npcTradePrice(-1);

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
        getModifierSettings().registerPossibleModifier(ModifierBaseRegistry.findExtendedModifiers(this));
    }

    @Override
    public ImperiumItem originalStack(@NotNull ItemStack itemStack) {
        super.originalStack(itemStack);
        if (this.getRare() == ItemRare.UNIQUE) return this;
        if (getGeneralSettings().itemLevel() != 1) getGeneralSettings().npcTradePrice(-1);
        else getGeneralSettings().npcTradePrice(0);

        return this;
    }

}
