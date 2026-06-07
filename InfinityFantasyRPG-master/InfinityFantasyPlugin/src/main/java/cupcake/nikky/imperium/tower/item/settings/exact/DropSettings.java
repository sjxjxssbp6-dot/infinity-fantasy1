package cupcake.nikky.imperium.tower.item.settings.exact;

import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.settings.ItemSettings;
import cupcake.nikky.imperium.tower.item.watcher.ItemWatcher;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.entity.Player;

import java.util.function.Consumer;
import java.util.function.Predicate;

@Setter @Getter
@Accessors(fluent = true)
public class DropSettings extends ItemSettings {

    private Predicate<Player> canDrop = player -> true;
    private Consumer<Player> onDrop = player -> {};

    private boolean droppable = true;

    public DropSettings(ImperiumItem item) {
        super(item);
    }

    public void droppable(boolean droppable) {
        item().getItemWatcherRegistry().watch(ItemWatcher.DROPPABLE);
        this.droppable = droppable;
    }

    public boolean droppable() {
        item().getItemWatcherRegistry().watch(ItemWatcher.DROPPABLE);
        return droppable;
    }
}
