package cupcake.nikky.imperium.tower.user.settings.exact;

import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.user.settings.BaseUserSettings;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

@Getter
public class SyncHideSettings extends BaseUserSettings {

    private boolean hideAll = true;
    private long lastChange;

    public SyncHideSettings(@NotNull final User user) {
        super(user);
        this.lastChange = Instant.now().toEpochMilli() - 10_000;
    }

    public void onConnectPlayer(@NotNull final Player player) {
        if (!hideAll && !player.getUniqueId().equals(getPlayer().getUniqueId())) {
            getPlayer().dd_showPlayer(player);
        }
    }

    public boolean setHideAll(final boolean hideAll) {
        if (lastChange + 2_000 > Instant.now().toEpochMilli()) return false;

        this.hideAll = hideAll;
        this.lastChange = Instant.now().toEpochMilli();

        if (hideAll) {
            Bukkit.getOnlinePlayers().stream()
                    .filter(targetPlayer -> !targetPlayer.getUniqueId().equals(getPlayer().getUniqueId()))
                    .forEach(targetPlayer -> getPlayer().dd_hidePlayer(targetPlayer));
        } else {
            Bukkit.getOnlinePlayers().stream()
                    .filter(targetPlayer -> !targetPlayer.getUniqueId().equals(getPlayer().getUniqueId()))
                    .forEach(targetPlayer -> getPlayer().dd_showPlayer(targetPlayer));
        }

        return true;
    }

}
