package cupcake.nikky.imperium.tower.storage;

import com.google.common.reflect.TypeToken;
import cupcake.nikky.imperium.tower.Fantasy;
import lombok.AllArgsConstructor;
import lombok.val;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.UnaryOperator;

@AllArgsConstructor
public class StorageData {

    private final Map<Integer, StoragePage> pages = new ConcurrentHashMap<>(32, 0.75F, 2);
    private static final Type tokenData = new TypeToken<Map<Integer, String>>(){}.getType();

    private final AtomicInteger lastOpenedPage = new AtomicInteger(0);

    public void updateAll(@NotNull final StorageData storageData) {
        storageData.findAllPages().forEach(this::registerPage);
    }

    public void validateAll(@NotNull final UnaryOperator<ItemStack[]> validateHandler) {
        new HashMap<>(pages).forEach((page, storagePage) -> storagePage.validate(validateHandler));
    }

    public void registerPage(final int page, @NotNull final StoragePage storagePage) {
        this.pages.put(page, storagePage);
    }

    public Map<Integer, StoragePage> findAllPages() {
        return new HashMap<>(pages);
    }

    public int getLastOpenedPage() {
        return lastOpenedPage.get();
    }

    public void setLastOpenedPage(final int page) {
        for (;;) {
            var old = lastOpenedPage.get();
            if (lastOpenedPage.compareAndSet(old, page)) break;
        }
    }

    public String serialize() {
        final Map<Integer, String> data = new HashMap<>();

        new HashMap<>(pages).forEach((page, storageData) -> data.put(page, storageData.serialize()));
        return Fantasy.gson().toJson(data, tokenData);
    }

    public static StorageData deserialize(@Nullable final String serialized) {
        val storageData = new StorageData();
        if (serialized == null || serialized.isEmpty()) {
            storageData.registerPage(0, new StoragePage(new ItemStack[54]));
            storageData.registerPage(1, new StoragePage(new ItemStack[54]));
            return storageData;
        }

        final Map<Integer, String> data = Fantasy.gson().fromJson(serialized, tokenData);

        data.forEach((page, storagePageSerialized) -> {
            val storagePage = StoragePage.deserialize(storagePageSerialized);
            storageData.registerPage(page, storagePage);
        });

        return storageData;
    }

}
