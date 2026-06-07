package cupcake.nikky.imperium.tower.item.watcher;

import com.google.gson.reflect.TypeToken;
import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.item.ImperiumItem;
import lombok.RequiredArgsConstructor;
import me.anfanik.steda.api.utility.ItemBuilder;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public final class ItemWatcherRegistry {

    private final ImperiumItem item;

    private final Map<ItemWatcher<?>, Boolean> watchers = new HashMap<>();
    private final Map<String, ItemWatcher<?>> watchersById = new HashMap<>();

    private NBTTagCompound originalTagCompound;

    public void registerWatcher(@NotNull final ItemWatcher<?> watcher) {
        this.watchers.put(watcher, false);
        this.watchersById.put(watcher.key(), watcher);
    }

    public void registerOriginalStack(@NotNull final ItemStack itemStack) {
        this.originalTagCompound = CraftItemStack.asNMSCopy(itemStack).getOrCreateTag();
    }

    public boolean isWatched(@NotNull final ItemWatcher<?> watcher) {
        return watchers.getOrDefault(watcher, true);
    }

    public void watch(@NotNull final ItemWatcher<?> watcher) {
        if (this.watchers.getOrDefault(watcher, true) || originalTagCompound == null) return;

        this.watchers.put(watcher, true);

        var serializedData = originalTagCompound.getString(watcher.key());
        if (serializedData == null || serializedData.isBlank()) return;

        watcher.watch(item, serializedData);
    }

    public void unwatchAll(@NotNull final ItemBuilder<?>.NbtBuilder nbtBuilder) {
        this.watchers.keySet().forEach(watcher -> nbtBuilder.setTag(watcher.key(), watcher.unwatch(item)));
    }

    public String serializeWatchers() {
        if (watchers.isEmpty()) return "";
        var map = new HashMap<String, String>();
        this.watchers.keySet().forEach(watcher -> map.put(watcher.key(), watcher.unwatch(item)));
        return Fantasy.gson().toJson(map);
    }

    public void deserializeWatchers(@Nullable String serializedWatchers) {
        if (serializedWatchers == null || serializedWatchers.isBlank()) return;
        final Map<String, String> map = Fantasy.gson().fromJson(serializedWatchers, new TypeToken<Map<String, String>>(){}.getType());
        if (originalTagCompound == null) originalTagCompound = new NBTTagCompound();

        map.forEach((key, serializedData) -> originalTagCompound.setString(key, serializedData));
    }

}
