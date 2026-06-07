package cupcake.nikky.imperium.tower.protocol.top;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import cupcake.nikky.imperium.tower.protocol.board.PlayerTimeData;
import cupcake.nikky.imperium.tower.protocol.board.TimeType;
import cupcake.nikky.imperium.tower.protocol.database.Collections;
import cupcake.nikky.imperium.tower.protocol.database.DatabaseService;
import cupcake.nikky.imperium.tower.protocol.game.GameServer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserTimeSearcher {

    @Getter
    private static final UserTimeSearcher instance = new UserTimeSearcher();
    private DatabaseService service;

    public void initialize(DatabaseService service) {
        this.service = service;
    }

    public CompletableFuture<Map<UUID, PlayerTimeData>> find(@NotNull final GameServer gameServer, @NotNull final TimeType timeType) {
        if (service == null) return CompletableFuture.completedFuture(null);
        return service.asyncCollection(Collections.USERS).thenApplyAsync(documentMongoCollection -> {
            final Map<UUID, PlayerTimeData> result = new HashMap<>();
            val filter = gameServer.isTest() ? Filters.empty() : Filters.ne("_id", "c989d6ef-afed-11ea-acca-1cb72caa35fd");

            documentMongoCollection.find()
                    .filter(filter)
                    .sort(Sorts.orderBy(Sorts.descending(timeType.getFieldName() + "_score")))
                    .limit(125)
                    .projection(Projections.fields(Projections.include(
                            timeType.getFieldName() + "_time",
                            timeType.getFieldName() + "_corrupt",
                            timeType.getFieldName() + "_level",
                            timeType.getFieldName() + "_score"))
                    )
                    .forEach(document -> {
                        val uuid = UUID.fromString(document.get("_id", UUID.randomUUID().toString()));
                        val time = document.get(timeType.getFieldName() + "_time", 0);
                        val level = document.get(timeType.getFieldName() +  "_level", 0);
                        val corrupt = document.get(timeType.getFieldName() +  "_corrupt", 0);
                        val score = document.get(timeType.getFieldName() +  "_score", 0D);

                        result.put(uuid, new PlayerTimeData(uuid, timeType, time, level, corrupt, score));
                    });

            return result;
        });
    }

}
