package cupcake.nikky.imperium.tower.protocol.ban;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import cupcake.nikky.imperium.tower.protocol.database.Collections;
import cupcake.nikky.imperium.tower.protocol.database.DatabaseService;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public final class BanService {

    private final BanRepository repository = new BanRepository();
    private final DatabaseService databaseService;

    public BanData findData(@NotNull final UUID uuid) {
        return this.repository.find(uuid);
    }

    public CompletableFuture<BanData> findOrLoad(@NotNull final UUID uuid) {
        var currentData = this.repository.find(uuid);
        if (currentData == null) {
            return databaseService.asyncFind(Collections.BANNED_USERS, Filters.eq("_id", uuid.toString())).thenApply(documents -> {
                var found = Optional.ofNullable(documents.first()).orElse(new Document());
                var data = new BanData(uuid, -1, "Unknown");
                if (!found.isEmpty()) {
                    data = new BanData(uuid, found.getLong("endTime"), found.getString("reason"));
                }

                this.registerData(data);
                return data;
            }).whenComplete((unused, throwable) -> {
                if (throwable != null) throwable.printStackTrace();
            });
        } else return CompletableFuture.completedFuture(currentData);
    }

    public CompletableFuture<Void> writeData(@NotNull final BanData banData) {
        this.repository.register(banData);
        return databaseService.asyncCollection(Collections.BANNED_USERS).thenAccept(collection -> collection.updateOne(
                Filters.eq("_id", banData.getUuid().toString()),
                List.of(Updates.set("endTime", banData.getEndTime()), Updates.set("reason", banData.getReason())),
                new UpdateOptions().upsert(true)
        )).whenComplete((unused, throwable) -> {
            if (throwable != null) throwable.printStackTrace();
        });
    }

    public void registerData(@NotNull final BanData banData) {
        this.repository.register(banData);
    }

    public void invalidateCache(@NotNull final UUID uuid) {
        this.repository.invalidate(uuid);
    }

    public void invalidateData(@NotNull final UUID uuid) {
        this.invalidateCache(uuid);
        this.invalidateDataWithoutCache(uuid);
    }

    public void invalidateDataWithoutCache(@NotNull final UUID uuid) {
        databaseService.asyncCollection(Collections.BANNED_USERS).thenAccept(collection -> collection.deleteOne(Filters.eq("_id", uuid.toString())))
                .whenComplete((unused, throwable) -> {
                    if (throwable != null) throwable.printStackTrace();
                });
    }

}
