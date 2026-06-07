package cupcake.nikky.imperium.tower.item.settings.exact;

import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.modifier.ModifierTag;
import cupcake.nikky.imperium.tower.item.settings.ItemSettings;
import cupcake.nikky.imperium.tower.item.watcher.ItemWatcher;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GearSettings extends ItemSettings {

    private int corrupted;
    private boolean copy;
    private int potential;

    private int quality;
    private final List<ModifierTag> qualityTags = new ArrayList<>();

    public GearSettings(ImperiumItem item) {
        super(item);
    }

    public int potential() {
        item().getItemWatcherRegistry().watch(ItemWatcher.GEAR_POTENTIAL);
        return potential;
    }

    public void potential(final int potential) {
        item().getItemWatcherRegistry().watch(ItemWatcher.GEAR_POTENTIAL);
        this.potential = potential;
    }

    public void resetQualityTags() {
        item().getItemWatcherRegistry().watch(ItemWatcher.GEAR_TAG_QUALITY);
        this.qualityTags.clear();
    }

    public void addQualityTags(@NotNull final List<ModifierTag> tags) {
        item().getItemWatcherRegistry().watch(ItemWatcher.GEAR_TAG_QUALITY);
        this.qualityTags.addAll(tags);
    }

    public List<ModifierTag> qualityTags() {
        item().getItemWatcherRegistry().watch(ItemWatcher.GEAR_TAG_QUALITY);
        return new ArrayList<>(qualityTags);
    }

    public int quality() {
        item().getItemWatcherRegistry().watch(ItemWatcher.GEAR_QUALITY);
        return quality;
    }

    public void quality(final int quality) {
        item().getItemWatcherRegistry().watch(ItemWatcher.GEAR_QUALITY);
        this.quality = quality;
    }

    public void corrupted(int corrupted) {
        item().getItemWatcherRegistry().watch(ItemWatcher.CORRUPTED);
        this.corrupted = corrupted;
    }

    public void copy(boolean copy) {
        item().getItemWatcherRegistry().watch(ItemWatcher.COPY_STATEMENT);
        this.copy = copy;
    }

    public int corrupted() {
        item().getItemWatcherRegistry().watch(ItemWatcher.CORRUPTED);
        return corrupted;
    }

    public boolean copy() {
        item().getItemWatcherRegistry().watch(ItemWatcher.COPY_STATEMENT);
        return copy;
    }
}
