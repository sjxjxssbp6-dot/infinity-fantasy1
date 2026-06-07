package cupcake.nikky.imperium.tower.quest.reward;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.client.protocol.image.FantasyImage;
import cupcake.nikky.imperium.tower.currency.CurrencyType;
import cupcake.nikky.imperium.tower.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.anfanik.steda.api.utility.TextUtility;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class GranReward implements QuestReward {

    @Getter
    private final int amount;

    @Override
    public void onReward(@NotNull final Player player) {
        var user = player.<User>getBungeePlayer();
        user.changeCurrency(CurrencyType.GRAN, amount);
        Fantasy.clientProvider().cursorNotify(user,
                TextUtility.colorize("&2+&e" + amount),
                FantasyImage.GRAN.toTexture()
        );
    }

    @Override
    public boolean canGive(@NotNull final Player player) {
        return player.<User>getBungeePlayer() != null;
    }

}
