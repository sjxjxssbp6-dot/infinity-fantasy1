package cupcake.nikky.imperium.tower.protocol.packet;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.item.ItemType;
import cupcake.nikky.imperium.tower.protocol.ImperiumPacket;
import cupcake.nikky.imperium.tower.trade.npc.session.TradeNpcProvider;
import lombok.Getter;
import lombok.val;
import me.func.mod.conversation.ModTransfer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ImperiumTradeSellPacket extends ImperiumPacket<Player> {

    private final int index;
    @Getter
    private int sendFlag;

    /*
        flag: 0 - remove state, 1 - confirm to sell
     */
    public ImperiumTradeSellPacket(@NotNull Player client, final int index) {
        super(client);
        this.index = index;
    }

    @Override
    public void notice(int... flags) {
        for (int flag : flags) {
            if (flag == 0) {
                this.updateItem();
            }
        }
    }

    public void updateItem() {
        TradeNpcProvider.getInstance().findSession(getClient().getUniqueId()).ifPresent(tradeSession -> {
            int flag = 0;

            if (tradeSession.getItemsToSell().containsKey(index)) {
                tradeSession.getItemsToSell().remove(index);
            } else {
                val item = getClient().getInventory().getItem(index);
                if (item == null || item.getType() == Material.AIR) {
                    sendFlag = 1;
                    return;
                }

                val imperiumItem = Fantasy.itemProvider().find(item);
                if (imperiumItem == null || imperiumItem.getImperiumType() != ItemType.WEAPON && imperiumItem.getImperiumType() != ItemType.ARMOR &&
                        imperiumItem.getGeneralSettings().npcTradePrice() < 0) {
                    Fantasy.clientProvider().notification(getClient(), SystemNotificationType.ERROR, "Данный предмет нельзя продать");
                    return;
                }

                tradeSession.getItemsToSell().put(index, imperiumItem);
                flag = 1;
            }

            new ModTransfer()
                    .varInt(index)
                    .varInt(flag)
                    .varInt(tradeSession.calculateFullPrice()).send("imp:trade-sell", getClient());
            sendFlag = 2;
        });
    }

}
