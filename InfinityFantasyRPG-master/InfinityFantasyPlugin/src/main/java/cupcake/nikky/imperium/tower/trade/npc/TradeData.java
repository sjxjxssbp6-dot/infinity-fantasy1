package cupcake.nikky.imperium.tower.trade.npc;

import cupcake.nikky.imperium.tower.protocol.trade.npc.TradeDuration;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public final class TradeData {

    private int npcId;
    private String tradeId;
    private TradeDuration tradeDuration;
    private int currentCount;

}
