package cupcake.nikky.imperium.tower.trade.npc;

import cupcake.nikky.imperium.tower.protocol.trade.npc.TradeDuration;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@Setter
@AllArgsConstructor
public class PlayerTradeData {

    private final UUID uuid;
    private final List<TradeData> trades = new CopyOnWriteArrayList<>();

    public void addTradeData(@NotNull final TradeData tradeData) {
        this.trades.add(tradeData);
    }

    public TradeData findTrade(final int npcId, @NotNull final String tradeId) {
        return trades.stream().filter(tradeData -> tradeData.getNpcId() == npcId && tradeData.getTradeId().equals(tradeId)).findAny().orElse(null);
    }

    public List<TradeData> findTrades(@NotNull final TradeDuration tradeDuration) {
        return trades.stream().filter(tradeData -> tradeData.getTradeDuration() == tradeDuration).toList();
    }

    public void invalidateAllByNPCId(final int npcId) {
        this.trades.removeIf(tradeData -> tradeData.getNpcId() == npcId);
    }

    public void invalidateWeekDuration() {
        this.trades.removeIf(tradeData -> tradeData.getTradeDuration() == TradeDuration.WEEK);
    }

    public void invalidateDayDuration() {
        this.trades.removeIf(tradeData -> tradeData.getTradeDuration() == TradeDuration.DAY);
    }

    public void invalidateAll() {
        this.trades.clear();
    }

}
