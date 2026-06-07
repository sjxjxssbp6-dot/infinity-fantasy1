package cupcake.nikky.imperium.tower.protocol.top;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import cupcake.nikky.imperium.tower.protocol.database.Collections;
import cupcake.nikky.imperium.tower.protocol.database.DatabaseService;
import cupcake.nikky.imperium.tower.protocol.game.GameServer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserGeneralScoreSearcher {

    @Getter
    private static final UserGeneralScoreSearcher instance = new UserGeneralScoreSearcher();
    private DatabaseService service;

    public void initialize(DatabaseService service) {
        this.service = service;
    }

    public CompletableFuture<Map<UUID, Double>> findGeneralTop(@NotNull final GameServer gameServer) {
        if (service == null) return CompletableFuture.completedFuture(null);
        return service.asyncCollection(Collections.USERS).thenApplyAsync(documentMongoCollection -> {
            final List<Document> data = new ArrayList<>();
            Bson filter = Filters.exists("globalScore");

            if (!gameServer.isTest()) {
                filter = Filters.and(filter, Filters.gt("globalScore", 0D), Filters.ne("_id", "c989d6ef-afed-11ea-acca-1cb72caa35fd"));
            } else {
                filter = Filters.and(filter, Filters.gt("globalScore", 0D));
            }

            documentMongoCollection.find(filter)
                    .sort(Sorts.descending("globalScore"))
                    .projection(Projections.fields(Projections.include("_id", "globalScore")))
                    .limit(125)
                    .into(data);

            final Map<UUID, Double> result = new HashMap<>();
            data.forEach(document -> {
                val uuid = UUID.fromString(document.get("_id", UUID.randomUUID().toString()));
                Object score = document.getOrDefault("globalScore", 0D);
                val finalScore = score instanceof Integer ? ((Integer) score).doubleValue() : (Double) score;

                result.put(uuid, finalScore);
            });

            return result;
        }).whenComplete((result, throwable) -> {
            if (throwable != null)
                throwable.printStackTrace();
        });
    }

}
