package cupcake.nikky.imperium.tower.trade.npc.session;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.item.ImperiumItem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TradeNpcProvider {

    @Getter
    private static final TradeNpcProvider instance = new TradeNpcProvider();

    private final Map<UUID, TradeNpcSession> data = new ConcurrentHashMap<>(32, 0.75F, 2);

    public void registerSession(@NotNull final Player player, final int npcId, TradeType tradeType) {
        val present = this.findSession(player.getUniqueId()).orElse(null);
        if (present != null) return;

        Fantasy.userService().findById(player.getUniqueId()).ifPresent(user -> {
            val session = new TradeNpcSession(player, npcId, new ArrayList<>(), tradeType);
            this.data.put(player.getUniqueId(), session);

            tradeType.getTradeGenerator().generate(user).forEach(session::addContainer);

            session.open();

            if (session.isClosed())
                this.unregister(player.getUniqueId());
        });
    }

    public int findPriceForItem(@NotNull final ImperiumItem item, final int amount) {
        return item.getGeneralSettings().calculatePrice() * amount;
    }

    public Optional<TradeNpcSession> findSession(@NotNull final UUID uuid) {
        return Optional.ofNullable(this.data.get(uuid));
    }

    public void unregister(@NotNull final UUID uuid) {
        this.data.remove(uuid);
    }

}
