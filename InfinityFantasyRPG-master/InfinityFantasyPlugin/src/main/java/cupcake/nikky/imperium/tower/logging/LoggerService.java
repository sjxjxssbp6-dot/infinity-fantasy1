package cupcake.nikky.imperium.tower.logging;

import org.bukkit.entity.Player;

public interface LoggerService {

    void log(Player player, String name, String state, String data);

    void stopLogger();

    void onDisable();

}
