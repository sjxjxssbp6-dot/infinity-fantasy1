package cupcake.nikky.imperium.tower.trade.npc.session;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.chat.ChatType;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.protocol.packet.ImperiumTradePacket;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class TradeNpcSession {

    @Getter
    private final Player player;
    @Getter
    private final int npcId;
    private final List<TradeContainer> containers;
    @Getter
    private final TradeType tradeType;

    @Getter @Setter
    private boolean closed = true;
    @Getter
    private final Map<Integer, ImperiumItem> itemsToSell = new HashMap<>();

    public int calculateFullPrice() {
        int result = 0;

        for (Integer integer : itemsToSell.keySet()) {
            val value = itemsToSell.get(integer);
            val amount = player.getInventory().getItem(integer).getAmount();
            result += TradeNpcProvider.getInstance().findPriceForItem(value, amount);
        }

        return result;
    }

    public void addContainer(@NotNull final TradeContainer container) {
        this.containers.add(container);
    }

    public List<TradeContainer> getContainers() {
        return new ArrayList<>(containers);
    }

    public TradeContainer findById(final int id) {
        for (TradeContainer container : containers) {
            if (container.getId() == id) return container;
        }

        return null;
    }

    public void open() {
        if (!isClosed()) return;

        val user = Fantasy.userService().findById(player.getUniqueId()).orElse(null);
        if (user == null) {
            Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&cПроизошла ошибка, попробуйте позже");
            Bukkit.getLogger().warning("Attempt to open trade menu while user = null");
            return;
        }

        val packet = new ImperiumTradePacket(user, this);
        packet.notice(0);
        if (!packet.isSend()) {
            Fantasy.clientProvider().notification(player, SystemNotificationType.WARN, "Данный торговец ничего не может", "Вам предложить");
        } else this.setClosed(false);
    }

}
