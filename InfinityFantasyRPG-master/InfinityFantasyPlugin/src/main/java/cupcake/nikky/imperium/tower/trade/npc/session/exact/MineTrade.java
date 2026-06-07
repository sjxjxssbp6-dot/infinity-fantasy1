package cupcake.nikky.imperium.tower.trade.npc.session.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.client.protocol.trade.TradeCurrency;
import cupcake.nikky.imperium.tower.item.ImperiumItem;
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
import me.anfanik.steda.api.utility.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Getter
@Accessors(fluent = true)
public class MineTrade implements TradeGenerator {

    private final String title = "Стандартные материалы";

    @Override
    public List<TradeContainer> generate(@NotNull User user) {
        final List<TradeContainer> result = new ArrayList<>();

        val playerTradeData = Fantasy.tradeNpcService().findByUuid(user.getUniqueId());
        if (playerTradeData == null) return result;

        val npcId = TradeNpcProvider.getInstance().findSession(user.getUniqueId()).map(TradeNpcSession::getNpcId).orElse(-1);
        if (npcId == -1) return result;

        Map<String, Integer> limits = Map.of(
                ItemObject.STAR_OF_CORRUPT_MOD_ITEM.getUniqueId(), 5,
                ItemObject.SHARD_OF_ABYSS_MOD_ITEM.getUniqueId(), 2,
                ItemObject.ESSENCE_OF_TYCHE_MOD_ITEM.getUniqueId(), 3,
                ItemObject.FRAGMENT_BATTLE_1.getUniqueId(), 1,
                ItemObject.FRAGMENT_BATTLE_3.getUniqueId(), 1,
                ItemObject.FRAGMENT_BATTLE_6.getUniqueId(), 1
        );

        Map<Integer, Pair<ImperiumItem, Integer>> trades = new HashMap<>();

        trades.put(1, new Pair<>(ItemObject.EPIC_REAGENT_VERGE_MOD_ITEM.getItem().create(), 50));
        trades.put(2, new Pair<>(ItemObject.MIXED_REAGENT_VERGE_MOD_ITEM.getItem().create(), 100));
        trades.put(3, new Pair<>(ItemObject.ORB_OF_DISTORTION_MOD_ITEM.getItem().create(), 250));
        trades.put(4, new Pair<>(ItemObject.ABSOLUTE_DUST_MOD_ITEM.getItem().create(), 250));
        trades.put(5, new Pair<>(ItemObject.FORM_OF_TYCHE_MOD_ITEM.getItem().create(), 500));
        trades.put(6, new Pair<>(ItemObject.ESSENCE_OF_TYCHE_MOD_ITEM.getItem().create(), 500));
        trades.put(7, new Pair<>(ItemObject.SHARD_OF_ABYSS_MOD_ITEM.getItem().create(), 750));
        trades.put(8, new Pair<>(ItemObject.STAR_OF_CORRUPT_MOD_ITEM.getItem().create(), 250));
        trades.put(11, new Pair<>(ItemObject.RESET_ORB.getItem().create(), 50));
        trades.put(12, new Pair<>(ItemObject.FRAGMENT_BATTLE_1.getItem().create(), 750));
        trades.put(13, new Pair<>(ItemObject.FRAGMENT_BATTLE_3.getItem().create(), 1000));
        trades.put(14, new Pair<>(ItemObject.FRAGMENT_BATTLE_6.getItem().create(), 1400));
        trades.put(32, new Pair<>(ItemObject.WAY_FRAGMENT_1.getItem().create(), 200));

//                result.add(TradeContainer.of(15, ItemObject.EVENT_HEALTH_POTION.getItem().create(), TradeCurrency.FLOWER, 3));
//                result.add(TradeContainer.of(16, ItemObject.EVENT_ATTACK_POTION.getItem().create(), TradeCurrency.FLOWER, 3));
//                result.add(TradeContainer.of(17, ItemObject.EVENT_EXPERIENCE_POTION.getItem().create(), TradeCurrency.FLOWER, 3));
//                result.add(TradeContainer.of(18, ItemObject.EVENT_ELEM_RES_POTION.getItem().create(), TradeCurrency.FLOWER, 3));
//                result.add(TradeContainer.of(19, ItemObject.EVENT_CHAOS_RES_POTION.getItem().create(), TradeCurrency.FLOWER, 3));
//                result.add(TradeContainer.of(20, ItemObject.FRAGMENT_BATTLE_1.getItem().create(), TradeCurrency.FLOWER, 2));
//                result.add(TradeContainer.of(21, ItemObject.FRAGMENT_BATTLE_3.getItem().create(), TradeCurrency.FLOWER, 2));
//                result.add(TradeContainer.of(22, ItemObject.FRAGMENT_BATTLE_6.getItem().create(), TradeCurrency.FLOWER, 2));
//                result.add(TradeContainer.of(23, ItemObject.SHARD_OF_ABYSS_MOD_ITEM.getItem().create(), TradeCurrency.FLOWER, 3));
//                result.add(TradeContainer.of(24, ItemObject.WAY_FRAGMENT_2.getItem().create(), TradeCurrency.FLOWER, 1));

        val whenReset = TimeUtility.tradeWeekReset();

        trades.forEach((id, paired) -> {
            val imperiumItem = paired.getFirst();
            val price = paired.getSecond();

            val limit = limits.getOrDefault(imperiumItem.getUniqueId(), -1);

            if (limit != -1) {
                val dataId = imperiumItem.getUniqueId();
                TradeData tradeData = playerTradeData.findTrade(npcId, imperiumItem.getUniqueId());

                if (tradeData == null) {
                    tradeData = new TradeData(npcId, dataId, TradeDuration.WEEK, 0);
                    playerTradeData.addTradeData(tradeData);
                }

                val finalTrade = tradeData;

                imperiumItem.getDropSettings().droppable(false);
                val tradeContainer = TradeContainer.of(id, imperiumItem.getUniqueId(), imperiumItem.createNew(), TradeCurrency.MINES, price, player -> imperiumItem);
                tradeContainer.setCurrentCount(tradeData.getCurrentCount());
                tradeContainer.setMaxCount(limit);
                tradeContainer.setWhenReset(whenReset);
                tradeContainer.setOnUpdate(player -> {
                    finalTrade.setCurrentCount(finalTrade.getCurrentCount() + 1);
                    tradeContainer.setCurrentCount(finalTrade.getCurrentCount());
                });
                result.add(tradeContainer);
            } else {
                result.add(TradeContainer.of(id, imperiumItem, TradeCurrency.MINES, price));
            }
        });

        result.sort(Comparator.comparingInt(TradeContainer::getPrice));
        return result;
    }

}
