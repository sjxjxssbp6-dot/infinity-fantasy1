package cupcake.nikky.imperium.tower.trade.npc.session;

import cupcake.nikky.imperium.tower.trade.npc.session.exact.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TradeType {

    MINE_TRADE(new MineTrade()),
    WAY_TRADE(new WayTrade()),
    EQUIP_TRADE(new EquipTrade()),
    POTIONS_TRADE(new PotionsTrade()),
    MASK_TRADE(new MaskTrade());

    private final TradeGenerator tradeGenerator;

}
