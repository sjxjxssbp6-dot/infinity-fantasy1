package cupcake.nikky.imperium.tower.trade.npc.session.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.client.protocol.trade.TradeCurrency;
import cupcake.nikky.imperium.tower.item.ItemObject;
import cupcake.nikky.imperium.tower.protocol.trade.npc.TradeDuration;
import cupcake.nikky.imperium.tower.trade.npc.TradeData;
import cupcake.nikky.imperium.tower.trade.npc.session.TradeContainer;
import cupcake.nikky.imperium.tower.trade.npc.session.TradeNpcProvider;
import cupcake.nikky.imperium.tower.trade.npc.session.TradeNpcSession;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.utility.TimeUtility;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Getter
@Accessors(fluent = true)
public class WayTrade implements TradeGenerator {

    private final String title = "Осколки Грани";

    @Override
    public List<TradeContainer> generate(@NotNull User user) {
        if (user.getLevel() <= 58) return Collections.emptyList();

        final List<TradeContainer> result = new ArrayList<>();

        val playerTradeData = Fantasy.tradeNpcService().findByUuid(user.getUniqueId());
        if (playerTradeData == null) return result;

        val npcId = TradeNpcProvider.getInstance().findSession(user.getUniqueId()).map(TradeNpcSession::getNpcId).orElse(-1);
        if (npcId == -1) return result;

        val limits = user.findSyncTradeSettings().findTradeWaysByLevel(user.getLevelController().getLevel());

        var index = 1;
        Map<String, ItemObject> converts = new HashMap<>();
        for (ItemObject itemObject : limits.keySet()) {
            converts.put(itemObject.getUniqueId(), itemObject);
            result.add(TradeContainer.of(index++, itemObject.getItem().create(), TradeCurrency.GRAN, 3000));
        }

        val whenReset = TimeUtility.tradeDayReset();

        result.forEach(tradeContainer -> {
            val limit = limits.getOrDefault(converts.get(tradeContainer.getUniqueId()), -1);

            if (limit != -1 && tradeContainer.getCurrency() == TradeCurrency.GRAN) {
                val id = tradeContainer.getUniqueId();
                TradeData tradeData = playerTradeData.findTrade(npcId, tradeContainer.getUniqueId());

                if (tradeData == null) {
                    tradeData = new TradeData(npcId, id, TradeDuration.DAY, 0);
                    playerTradeData.addTradeData(tradeData);
                }

                val finalTrade = tradeData;

                tradeContainer.setCurrentCount(tradeData.getCurrentCount());
                tradeContainer.setMaxCount(limit);
                tradeContainer.setWhenReset(whenReset);
                tradeContainer.setOnUpdate(player -> {
                    finalTrade.setCurrentCount(finalTrade.getCurrentCount() + 1);
                    tradeContainer.setCurrentCount(finalTrade.getCurrentCount());
                });
            }
        });

        result.sort(Comparator.comparingInt(data -> converts.get(data.getUniqueId()).getItem().getGeneralSettings().itemLevel()));
        return result;
    }

}
