package cupcake.nikky.imperium.tower.protocol.trade.npc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TradeDuration {

    DAY("trade_day"),
    WEEK("trade_week");

    private final String fieldName;

}
