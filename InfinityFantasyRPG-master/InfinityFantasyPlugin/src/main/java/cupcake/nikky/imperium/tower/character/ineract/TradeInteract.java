package cupcake.nikky.imperium.tower.character.ineract;

import cupcake.nikky.imperium.tower.character.ImperiumCharacter;
import cupcake.nikky.imperium.tower.trade.npc.session.TradeNpcProvider;
import cupcake.nikky.imperium.tower.trade.npc.session.TradeType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TradeInteract extends DefaultInteract {

    private final TradeType tradeType;

    public TradeInteract(ImperiumCharacter character, TradeType tradeType) {
        super(character);
        this.tradeType = tradeType;
    }

    @Override
    public void interact(@NotNull final Player player) {
        TradeNpcProvider.getInstance().registerSession(player, getCharacter().getNpcId(), tradeType);
    }

}
