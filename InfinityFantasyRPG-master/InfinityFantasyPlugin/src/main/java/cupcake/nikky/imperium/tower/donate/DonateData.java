package cupcake.nikky.imperium.tower.donate;

import com.google.common.reflect.TypeToken;
import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.protocol.utility.NumberUtility;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

@AllArgsConstructor
@RequiredArgsConstructor
public class DonateData {

    @Getter private final UUID userUuid;
    @Getter private final List<Consumer<DonateData>> subHandlers;

    private final Map<String, Object> donateMetadata = new ConcurrentHashMap<>(32, 0.75F, 2);

    private AtomicInteger specialCurrency = new AtomicInteger();
    private AtomicInteger privateSpecialCurrency = new AtomicInteger();
    private AtomicLong subscriptionTime = new AtomicLong(); // when subscription ends (0 if expired)
    private AtomicLong lootFilterTime = new AtomicLong(); // when loot filter ends (0 of expired)

    private AtomicInteger purchasedStoragePages = new AtomicInteger();

    public int getPurchasedStoragePages() {
        return purchasedStoragePages.get();
    }

    public void setPurchasedStoragePages(int purchasedStoragePages) {
        this.purchasedStoragePages.set(purchasedStoragePages);
    }

    public int getTotalCurrency() {
        return getCurrency() + getPrivateCurrency();
    }

    public void setPrivateCurrency(final int currency) {
        this.privateSpecialCurrency.set(currency);
    }

    public void changePrivateCurrency(final int delta) {
        for (;;) {
            val old = this.privateSpecialCurrency.get();
            if (this.privateSpecialCurrency.compareAndSet(old, old + delta)) break;
        }
    }

    public int getPrivateCurrency() {
        return privateSpecialCurrency.get();
    }

    public void setCurrency(final int currency) {
        this.specialCurrency.set(currency);
    }

    public void changeCurrency(final int delta) {
        for (;;) {
            val old = this.specialCurrency.get();
            if (this.specialCurrency.compareAndSet(old, old + delta)) break;
        }
    }

    public int getCurrency() {
        return specialCurrency.get();
    }

    public long getSubscriptionTime() {
        return subscriptionTime.get();
    }

    public long getLootFilterTime() {
        return lootFilterTime.get();
    }

    public void updateSubscription(@NotNull final SubscriptionDurationType type) {
        this.updateSubscription(type.getDuration());
    }

    public void updateLootFilter(@NotNull final SubscriptionDurationType type) {
        this.updateLootFilter(type.getDuration());
    }

    public void updateLootFilter(final long time) {
        val state = isDonateLootFilter();

        if (!state) lootFilterTime.set(Instant.now().toEpochMilli());

        for (;;) {
            val old = this.lootFilterTime.get();
            if (this.lootFilterTime.compareAndSet(old, old + time)) break;
        }
    }

    public void updateSubscription(final long time) {
        val state = isSubscribed();
        if (!state) subscriptionTime.set(Instant.now().toEpochMilli());

        for (;;) {
            val old = this.subscriptionTime.get();
            if (this.subscriptionTime.compareAndSet(old, old + time)) break;
        }

        subHandlers.forEach(handler -> handler.accept(this));
    }

    public long getSubscriptionTimeLong() {
        return Math.max(getSubscriptionTime() - Instant.now().toEpochMilli(), 0);
    }

    public long getLootFilterTimeLong() {
        return Math.max(getLootFilterTime() - Instant.now().toEpochMilli(), 0);
    }

    public void invalidateSubscription() {
        subscriptionTime.set(0L);

        subHandlers.forEach(handler -> handler.accept(this));
    }

    public void invalidateLootFilter() {
        lootFilterTime.set(0);
    }

    public boolean isSubscribed() {
        var subscriptionTime = getSubscriptionTime();
        var result = subscriptionTime > Instant.now().toEpochMilli();

        if (!result && subscriptionTime != 0L) {
            this.invalidateSubscription();
            subHandlers.forEach(handler -> handler.accept(this));
        }

        return result;
    }

    public boolean isDonateLootFilter() {
        return getLootFilterTime() > Instant.now().toEpochMilli();
    }

    public boolean containsMetadata(@NotNull final String key) {
        return donateMetadata.containsKey(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T findMetadata(@NotNull final String key) {
        return (T) donateMetadata.get(key);
    }

    public <T> T findMetadata(@NotNull final String key, @NotNull final Class<? extends T> clazz) {
        val found = donateMetadata.get(key);
        if (found == null) return null;
        return clazz.cast(found);
    }

    public <T> void applyMetadata(@NotNull final String key, @NotNull final T value) {
        donateMetadata.put(key, value);
    }

    public void invalidateMetadata(@NotNull final String key) {
        donateMetadata.remove(key);
    }

    public List<String> findAllMetadata() {
        return new ArrayList<>(donateMetadata.keySet());
    }

    public String serialize() {
        val token = new TypeToken<Map<String, String>>(){}.getType();
        final Map<String, String> data = new HashMap<>();

        data.put("specialCurrency", String.valueOf(specialCurrency));
        data.put("privateSpecialCurrency", String.valueOf(privateSpecialCurrency));
        data.put("subscriptionTime", String.valueOf(subscriptionTime));
        data.put("lootFilterTime", String.valueOf(lootFilterTime));
        data.put("purchasedStoragePages", String.valueOf(purchasedStoragePages));
        data.put("metadata", Fantasy.gson().toJson(donateMetadata));

        return Fantasy.gson().toJson(data, token);
    }

    public static DonateData deserialize(@NotNull final UUID uuid, @Nullable final String serialized, @NotNull final List<Consumer<DonateData>> subHandlers) {
        val instance = new DonateData(uuid, subHandlers);

        if (serialized == null || serialized.trim().isEmpty()) return instance;

        val token = new TypeToken<Map<String, String>>(){}.getType();
        val metadataToken = new TypeToken<Map<String, Object>>(){}.getType();

        final Map<String, String> data = Fantasy.gson().fromJson(serialized, token);
        final Map<String, Object> metadata = Fantasy.gson().fromJson(data.get("metadata"), metadataToken);

        instance.specialCurrency.set(NumberUtility.parseInteger(data.getOrDefault("specialCurrency", "0")));
        instance.privateSpecialCurrency.set(NumberUtility.parseInteger(data.getOrDefault("privateSpecialCurrency", "0")));
        instance.subscriptionTime.set(NumberUtility.parseLong(data.getOrDefault("subscriptionTime", "0")));
        instance.lootFilterTime.set(NumberUtility.parseLong(data.getOrDefault("lootFilterTime", "0")));
        instance.purchasedStoragePages.set(NumberUtility.parseInteger(data.getOrDefault("purchasedStoragePages", "0")));
        if (metadata != null) instance.donateMetadata.putAll(metadata);

        return instance;
    }

}
