package cupcake.nikky.imperium.tower.trade.npc.session.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.item.ItemObject;
import cupcake.nikky.imperium.tower.item.exact.mask.MaskItem;
import cupcake.nikky.imperium.tower.protocol.trade.npc.TradeDuration;
import cupcake.nikky.imperium.tower.protocol.utility.NumberUtility;
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

import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
public class MaskTrade implements TradeGenerator {

    private final String title = "Материалы Грани";

    @Override
    public List<TradeContainer> generate(@NotNull User user) {
        final List<TradeContainer> result = new ArrayList<>();
        val playerTradeData = Fantasy.tradeNpcService().findByUuid(user.getUniqueId());
        if (playerTradeData == null) return result;

        val npcId = TradeNpcProvider.getInstance().findSession(user.getUniqueId()).map(TradeNpcSession::getNpcId).orElse(-1);
        if (npcId == -1) return result;

        for (ItemObject itemObject : ItemObject.values()) {
            if (itemObject.getItem().checkInstance(MaskItem.class)) {
                result.add(TradeContainer.of(NumberUtility.parseInteger(itemObject.getUniqueId()), itemObject.getItem().create(), 5_000));
            }
        }

        result.add(TradeContainer.of(NumberUtility.parseInteger(ItemObject.RESET_ORB.getUniqueId()), ItemObject.RESET_ORB.getItem().create(), 2_500));

        val whenReset = TimeUtility.tradeWeekReset();

        result.forEach(tradeContainer -> {
            val id = tradeContainer.getUniqueId();
            TradeData tradeData = playerTradeData.findTrade(npcId, tradeContainer.getUniqueId());

            if (tradeData == null) {
                tradeData = new TradeData(npcId, id, TradeDuration.WEEK, 0);
                playerTradeData.addTradeData(tradeData);
            }

            val finalTrade = tradeData;

            tradeContainer.setCurrentCount(tradeData.getCurrentCount());
            tradeContainer.setMaxCount(6);

            if (tradeContainer.getPrice() == 2_000)
                tradeContainer.setMaxCount(64);

            tradeContainer.setWhenReset(whenReset);
            tradeContainer.setOnUpdate(player -> {
                finalTrade.setCurrentCount(finalTrade.getCurrentCount() + 1);
                tradeContainer.setCurrentCount(finalTrade.getCurrentCount());
            });
        });

        return result;
    }

}
