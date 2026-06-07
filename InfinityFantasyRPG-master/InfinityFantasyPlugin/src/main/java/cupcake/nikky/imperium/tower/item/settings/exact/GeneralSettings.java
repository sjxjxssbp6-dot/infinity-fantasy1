package cupcake.nikky.imperium.tower.item.settings.exact;

import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.settings.ItemSettings;
import cupcake.nikky.imperium.tower.item.watcher.ItemWatcher;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.val;

@Setter
@Accessors(fluent = true, chain = true)
public class GeneralSettings extends ItemSettings {

    private int itemLevel;
    private int requiredLevel;
    @Getter private boolean disableRequiredLevel = false;
    @Getter private int color = -1;
    @Getter private int npcTradePrice = -2;

    @Getter @Setter private String customNBTKey;
    @Getter @Setter private String customNBTValue;

    public GeneralSettings(ImperiumItem item) {
        super(item);
    }

    public int calculatePrice() {
        val npcTradePrice = this.npcTradePrice();
        val rarePriceMultiplier = switch (item().getRare().getPosition()) {
            case 2 -> 1.25;
            case 3 -> 1.75;
            case 5 -> 3;
            default -> 1;
        };
        return npcTradePrice == -1 ? (int) Math.round(item().getGeneralSettings().itemLevel() * 3 * rarePriceMultiplier) : npcTradePrice;
    }

    public GeneralSettings itemLevel(int itemLevel) {
        item().getItemWatcherRegistry().watch(ItemWatcher.LEVEL);
        this.itemLevel = itemLevel;
        return this;
    }

    public GeneralSettings requiredLevel(int requiredLevel) {
        item().getItemWatcherRegistry().watch(ItemWatcher.REQUIRED_LEVEL);
        this.requiredLevel = requiredLevel;
        return this;
    }

    public int itemLevel() {
        item().getItemWatcherRegistry().watch(ItemWatcher.LEVEL);
        return itemLevel;
    }

    public int requiredLevel() {
        item().getItemWatcherRegistry().watch(ItemWatcher.REQUIRED_LEVEL);
        return requiredLevel;
    }
}
