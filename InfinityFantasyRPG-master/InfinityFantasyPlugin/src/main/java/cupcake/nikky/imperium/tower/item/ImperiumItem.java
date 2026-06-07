package cupcake.nikky.imperium.tower.item;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.attribute.AttributeMap;
import cupcake.nikky.imperium.tower.item.generator.description.DescriptionContainer;
import cupcake.nikky.imperium.tower.item.settings.ItemSettings;
import cupcake.nikky.imperium.tower.item.settings.exact.cursor.CursorSettings;
import cupcake.nikky.imperium.tower.item.settings.exact.*;
import cupcake.nikky.imperium.tower.item.settings.exact.loot.LootSettings;
import cupcake.nikky.imperium.tower.item.watcher.ItemWatcherRegistry;
import lombok.val;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ImperiumItem {

    default <T> T apply(@NotNull final Class<? extends T> clazz) {
        return clazz.cast(this);
    }

    default boolean checkInstance(@NotNull final Class<? extends ImperiumItem> clazz) {
        return clazz.isInstance(this);
    }

    String getUniqueId();

    List<String> getAdditionalDescription();

    Material getMaterial();

    ItemObject getObject();

    boolean isUnique();

    default ModifierSettings getModifierSettings() {
        return this.findSettings(ModifierSettings.class);
    }

    default LootSettings getLootSettings() {
        return this.findSettings(LootSettings.class);
    }

    default GeneralSettings getGeneralSettings() {
        return this.findSettings(GeneralSettings.class);
    }

    default DamageSettings getDamageSettings() {
        return this.findSettings(DamageSettings.class);
    }

    default ToolSettings getToolSettings() {
        return this.findSettings(ToolSettings.class);
    }

    default InteractSettings getInteractSettings() {
        return this.findSettings(InteractSettings.class);
    }

    default DropSettings getDropSettings() {
        return this.findSettings(DropSettings.class);
    }

    default GearSettings getGearSettings() {
        return this.findSettings(GearSettings.class);
    }

    default CursorSettings getCursorSettings() {
        return this.findSettings(CursorSettings.class);
    }

    default AspectSettings getAspectSettings() {
        return this.findSettings(AspectSettings.class);
    }

    default DescriptionSettings getDescriptionSettings() {
        return this.findSettings(DescriptionSettings.class);
    }

    ItemWatcherRegistry getItemWatcherRegistry();

    <T extends ItemSettings> T findSettings(Class<? extends T> clazz);

    String getName();

    void setName(String name);

    short getDurability();

    void setDurability(short durability);

    default void setDurability(int durability) {
        this.setDurability((short) durability);
    }

    ItemRare getRare();

    void setRare(ItemRare itemRare);

    ItemType getImperiumType();

    AttributeMap getAttributes();

    List<String> generateDescription();

    DescriptionContainer processDescriptionContainer();

    ImperiumItem originalStack(@NotNull final ItemStack itemStack);

    ItemStack createNew();

    ItemStack createNew(int amount);

    ItemStack createNew(int amount, Player player);

    ItemStack createNew(Player player);

    ImperiumItem create();

    void updateAttributes();

    ItemStack update(final Player player);

    default String serialize() {
        return "%s~%s".formatted(getUniqueId(), getItemWatcherRegistry().serializeWatchers());
    }

    static ImperiumItem deserialize(@Nullable final String serialized) {
        if (serialized == null || serialized.isBlank()) return null;

        val split = serialized.split("~");
        val item = Fantasy.itemProvider().find(split[0]);
        if (item == null) return null;

        item.getItemWatcherRegistry().deserializeWatchers(split[1]);

        return item;
    }

}
