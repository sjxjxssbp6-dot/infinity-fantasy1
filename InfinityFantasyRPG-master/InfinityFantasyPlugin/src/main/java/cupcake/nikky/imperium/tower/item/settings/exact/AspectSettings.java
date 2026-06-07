package cupcake.nikky.imperium.tower.item.settings.exact;

import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectBase;
import cupcake.nikky.imperium.tower.item.settings.ItemSettings;
import cupcake.nikky.imperium.tower.item.watcher.ItemWatcher;

public class AspectSettings extends ItemSettings {

    private AspectBase aspect;
    private int aspectCorrupt;
    private int exaltedValue;
    private boolean copy;

    public AspectSettings(ImperiumItem item) {
        super(item);
    }

    public void aspect(AspectBase aspect) {
        item().getItemWatcherRegistry().watch(ItemWatcher.ASPECT);
        this.aspect = aspect;
    }

    public void exaltedValue(final int exaltedValue) {
        item().getItemWatcherRegistry().watch(ItemWatcher.ASPECT_EXALTED_VALUE);
        this.exaltedValue = exaltedValue;
    }

    public int exaltedValue() {
        item().getItemWatcherRegistry().watch(ItemWatcher.ASPECT_EXALTED_VALUE);
        return exaltedValue;
    }

    public void aspectCorrupt(final int aspectCorrupt) {
        item().getItemWatcherRegistry().watch(ItemWatcher.ASPECT_CORRUPT);
        this.aspectCorrupt = aspectCorrupt;
    }

    public AspectBase aspect() {
        item().getItemWatcherRegistry().watch(ItemWatcher.ASPECT);
        return aspect;
    }

    public int aspectLevel() {
        return aspectCorrupt() + exaltedValue();
    }

    public int aspectCorrupt() {
        item().getItemWatcherRegistry().watch(ItemWatcher.ASPECT_CORRUPT);
        return aspectCorrupt;
    }

    public void aspectCopy(final boolean aspectCopy) {
        item().getItemWatcherRegistry().watch(ItemWatcher.ASPECT_COPY_STATEMENT);
        this.copy = aspectCopy;
    }

    public boolean aspectCopy() {
        item().getItemWatcherRegistry().watch(ItemWatcher.ASPECT_COPY_STATEMENT);
        return copy;
    }
}
