package cupcake.nikky.imperium.tower.storage;

import com.google.common.reflect.TypeToken;
import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.inventory.StorageSerialization;
import cupcake.nikky.imperium.tower.protocol.utility.NumberUtility;
import lombok.val;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;

public class StoragePage {

    private static final Type tokenData = new TypeToken<Map<String, String>>(){}.getType();

    private final AtomicReference<ItemStack[]> userStorageItems = new AtomicReference<>();
    private final AtomicInteger blockedSlots = new AtomicInteger();

    private static final int INIT_BLOCKED_SLOTS = 36;

    public StoragePage(@NotNull final ItemStack[] pageItems) {
        this.setUserStorageItems(pageItems);
        this.setBlockedSlots(this.getInitBlockedSlots());
    }

    public StoragePage(@NotNull final ItemStack[] pageItems, final int blockedSlots) {
        this.setUserStorageItems(pageItems);
        this.setBlockedSlots(blockedSlots);
    }

    public void update(@NotNull final ItemStack itemStack, final int index) {
        for (;;) {
            val old = this.userStorageItems.get();
            val copy = this.getUserStorageItems();

            copy[index] = itemStack;
            if (this.userStorageItems.compareAndSet(old, copy)) break;
        }
    }

    public void update(@NotNull final StoragePage storagePage) {
        this.setUserStorageItems(storagePage.userStorageItems.get());
        this.setBlockedSlots(storagePage.getBlockedSlots());
    }

    public void validate(@NotNull final UnaryOperator<ItemStack[]> validateHandler) {
        for (;;) {
            var old = userStorageItems.get();
            if (userStorageItems.compareAndSet(old, validateHandler.apply(old))) break;
        }
    }

    public ItemStack[] getUserStorageItems() {
        return Arrays.copyOf(userStorageItems.get(), userStorageItems.get().length);
    }

    private void setUserStorageItems(@NotNull final ItemStack[] items) {
        for (;;) {
            var old = userStorageItems.get();
            if (userStorageItems.compareAndSet(old, items)) break;
        }
    }

    public int getBlockedSlots() {
        return blockedSlots.get();
    }

    public int getInitBlockedSlots() {
        return INIT_BLOCKED_SLOTS;
    }

    public void setBlockedSlots(final int value) {
        for (;;) {
            var old = getBlockedSlots();
            if (blockedSlots.compareAndSet(old, value)) break;
        }
    }

    public String serialize() {
        final Map<String, String> data = new HashMap<>();
        data.put("items", new StorageSerialization(getUserStorageItems()).serialize());
        data.put("blockedSlots", String.valueOf(getBlockedSlots()));
        return Fantasy.gson().toJson(data, tokenData);
    }

    public static StoragePage deserialize(@Nullable final String serialized) {
        val storagePage = new StoragePage(new ItemStack[54]);
        if (serialized == null || serialized.isEmpty()) return storagePage;

        final Map<String, String> data = Fantasy.gson().fromJson(serialized, tokenData);
        storagePage.setUserStorageItems(new StorageSerialization(null).deserialize(data.get("items")));
        storagePage.setBlockedSlots(NumberUtility.parseInteger(data.getOrDefault("blockedSlots", String.valueOf(storagePage.getInitBlockedSlots()))));

        return storagePage;
    }

}
