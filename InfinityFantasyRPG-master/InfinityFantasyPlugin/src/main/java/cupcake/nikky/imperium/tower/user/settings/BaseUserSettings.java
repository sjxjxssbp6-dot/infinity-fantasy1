package cupcake.nikky.imperium.tower.user.settings;

import cupcake.nikky.imperium.tower.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@Getter
@RequiredArgsConstructor
public abstract class BaseUserSettings implements UserSettings {

    private final User user;
    private Player player;

    @Override
    public Player getPlayer() {
        if (player == null)
            player = user.getSpigot();
        return player;
    }

}
