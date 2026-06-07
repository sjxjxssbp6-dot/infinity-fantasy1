package cupcake.nikky.imperium.tower.trade.npc.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.client.model.Updates;
import cupcake.nikky.imperium.tower.protocol.database.DatabaseService;
import cupcake.nikky.imperium.tower.trade.npc.PlayerTradeData;
import cupcake.nikky.imperium.tower.trade.npc.TradeData;
import cupcake.nikky.imperium.tower.protocol.trade.npc.TradeDuration;
import cupcake.nikky.imperium.tower.trade.npc.repository.TradeNpcRepository;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;
import org.bson.conversions.Bson;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public final class TradeNpcService {

    private final TradeNpcRepository repository = new TradeNpcRepository();

    @Getter @Accessors(fluent = true) private Gson gson;
    @Getter @Accessors(fluent = true) private DatabaseService databaseService;

    public TradeNpcService initialize(@NotNull final DatabaseService databaseService, @NotNull final Gson gson) {
        this.gson = gson;
        this.databaseService = databaseService;
        return this;
    }

    public PlayerTradeData findByUuid(@NotNull final UUID uuid) {
        return repository.findData(uuid);
    }

    public Collection<PlayerTradeData> findAll() {
        return repository.findAll();
    }

    public CompletableFuture<PlayerTradeData> load(@NotNull final UUID uuid) {
        return databaseService.findUser(uuid).thenApplyAsync(document -> {
            val playerTradeData = new PlayerTradeData(uuid);
            if (document.isEmpty()) return playerTradeData;

            for (TradeDuration value : TradeDuration.values()) {
                val data = document.get(value.getFieldName(), "");
                if (!data.isEmpty()) {
                    List<TradeData> list = gson.fromJson(data, new TypeToken<List<TradeData>>(){}.getType());
                    list.forEach(playerTradeData::addTradeData);
                }
            }

            return playerTradeData;
        }).thenApplyAsync(playerTradeData -> {
            this.repository.registerNew(uuid, playerTradeData);
            return playerTradeData;
        });
    }

    public List<Bson> update(@NotNull final UUID uuid) {
        val tradeData = this.findByUuid(uuid);
        if (tradeData == null) return Collections.emptyList();

        final List<Bson> result = new ArrayList<>();

        for (TradeDuration value : TradeDuration.values()) {
            result.add(Updates.set(value.getFieldName(), gson.toJson(tradeData.findTrades(value))));
        }

        return result;
    }

    public List<Bson> invalidate(@NotNull final UUID uuid) {
        val updates = this.update(uuid);
        this.repository.invalidate(uuid);
        return updates;
    }

}
