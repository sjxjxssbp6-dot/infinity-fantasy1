package cupcake.nikky.imperium.tower.protocol.packet;

import cupcake.nikky.imperium.tower.protocol.fraction.FractionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.core.network.CorePackage;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class GameGlobalMessagePacket extends CorePackage {

    private UUID playerUuid;
    private String realmNameSource;
    private String message;
    private FractionType fractionType;
    private int playerBaseLevel;
    private boolean deathless;
    private boolean isSubscribed;
    private String userDescription;
    private String serializedItem;

    public GameGlobalMessagePacket(@NotNull final String message) {
        this.message = message;
    }

    public boolean isPlayer() {
        return playerUuid != null;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(playerUuid);
    }

}
