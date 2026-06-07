package cupcake.nikky.imperium.tower.quest.reward;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.tree.PointType;
import cupcake.nikky.imperium.tower.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.anfanik.steda.api.utility.TextUtility;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class TreePointReward implements QuestReward {

    @Getter
    private final PointType pointType;

    @Override
    public void onReward(@NotNull final Player player) {
        var user = player.<User>getBungeePlayer();
        Fantasy.clientProvider().cursorNotify(user,
                TextUtility.colorize("&2+&e" + 1),
                String.valueOf(pointType.getIcon())
        );
    }

    @Override
    public boolean canGive(@NotNull final Player player) {
        return player.<User>getBungeePlayer() != null;
    }

}
