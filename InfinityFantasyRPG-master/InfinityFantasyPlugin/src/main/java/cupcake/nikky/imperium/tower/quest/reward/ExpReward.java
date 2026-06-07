package cupcake.nikky.imperium.tower.quest.reward;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.user.controller.ExpType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.anfanik.steda.api.utility.TextUtility;
import me.func.protocol.data.emoji.Emoji;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class ExpReward implements QuestReward {

    @Getter
    private final int amount;

    @Override
    public void onReward(@NotNull final Player player) {
        var user = player.<User>getBungeePlayer();
        user.getLevelController().changeExp(getAmount(), ExpType.OTHER);
        Fantasy.clientProvider().cursorNotify(user,
                TextUtility.colorize("&2+&c" + getAmount()),
                Emoji.EXP
        );
    }

    @Override
    public boolean canGive(@NotNull final Player player) {
        return player.<User>getBungeePlayer() != null;
    }

}
