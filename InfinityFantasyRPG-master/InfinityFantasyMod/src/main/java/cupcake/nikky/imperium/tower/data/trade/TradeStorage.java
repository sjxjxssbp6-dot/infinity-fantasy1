package cupcake.nikky.imperium.tower.data.trade;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TradeStorage {

    @Getter
    private static final TradeStorage instance = new TradeStorage();

    @Getter @Setter
    private String title = "";

    private final List<TradeContainer> trades = new ArrayList<>();

    public void clearAll() {
        this.setTitle("");
        trades.clear();
    }

    public void addTrade(@NotNull final TradeContainer tradeContainer) {
        this.trades.add(tradeContainer);
    }

    public TradeContainer findTrade(final int id) {
        return trades.stream().filter(tradeContainer -> tradeContainer.id() == id).findAny().orElse(null);
    }

    public List<TradeContainer> getTrades() {
        return new ArrayList<>(trades);
    }
}
