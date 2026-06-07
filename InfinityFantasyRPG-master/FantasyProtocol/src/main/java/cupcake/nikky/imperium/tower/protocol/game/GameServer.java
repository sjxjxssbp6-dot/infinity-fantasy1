package cupcake.nikky.imperium.tower.protocol.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.core.realm.RealmId;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class GameServer {

    private final String realmType;
    private final int realmId;
    private GameState state;
    private long lastUpdate;

    public GameServer(@NotNull final RealmId realmId, @NotNull final GameState state) {
        this.realmType = realmId.getTypeName();
        this.realmId = realmId.getId();
        this.state = state;
        this.lastUpdate = Instant.now().toEpochMilli();
    }

    public RealmId toRealm() {
        return RealmId.of(realmType, realmId);
    }

    public boolean isTest() {
        return realmId < 0 || realmType.equalsIgnoreCase("test");
    }

    public String nameSpace() {
        return isTest() ? "TEST" : "PROD";
    }

}
