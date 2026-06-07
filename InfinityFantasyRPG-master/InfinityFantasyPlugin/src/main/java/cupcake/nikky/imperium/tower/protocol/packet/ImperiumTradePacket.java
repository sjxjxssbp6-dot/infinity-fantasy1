package cupcake.nikky.imperium.tower.protocol.packet;

import cupcake.nikky.imperium.tower.protocol.ImperiumPacket;
import cupcake.nikky.imperium.tower.trade.npc.session.TradeNpcSession;
import cupcake.nikky.imperium.tower.user.User;
import lombok.Getter;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.wada.util.ModTransfer;

public class ImperiumTradePacket extends ImperiumPacket<User> {

    private final TradeNpcSession tradeNpcSession;
    @Getter private boolean send = false;

    public ImperiumTradePacket(@NotNull final User client, @NotNull final TradeNpcSession tradeNpcSession) {
        super(client);
        this.tradeNpcSession = tradeNpcSession;
    }

    @Override
    public void notice(int... flags) {
        for (int flag : flags) {
            if (flag == 0) {
                this.registerTrade();
            } else if (flag == 1) {
                this.updateSellContext();
            }
        }
    }

    public void updateSellContext() {
        val transfer = new ModTransfer();

        val sections = tradeNpcSession.getContainers();
        transfer.writeInt(sections.size());
        sections.forEach(tradeContainer -> transfer.writeInt(tradeContainer.getId()).writeInt(tradeContainer.getCurrentCount()));

        transfer.send("imp:trade-upd", getClient().getSpigot());
    }

    private void registerTrade() {
        val transfer = new ModTransfer().writeString(tradeNpcSession.getTradeType().getTradeGenerator().title());
        val player = getClient().getSpigot();

        val sections = tradeNpcSession.getContainers();

        if (sections.size() == 0) {
            send = false;
            return;
        }

        transfer.writeInt(sections.size());
        sections.forEach(tradeContainer -> tradeContainer.write(transfer));
        transfer.send("imp:trade-create", player);

        send = true;
    }

}
