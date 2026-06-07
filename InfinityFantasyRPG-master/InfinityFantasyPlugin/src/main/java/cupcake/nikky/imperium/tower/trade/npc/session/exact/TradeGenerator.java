package cupcake.nikky.imperium.tower.trade.npc.session.exact;

import cupcake.nikky.imperium.tower.trade.npc.session.TradeContainer;
import cupcake.nikky.imperium.tower.user.User;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface TradeGenerator {

    String title();

    List<TradeContainer> generate(@NotNull final User user);

}
