package cupcake.nikky.imperium.tower.protocol.packet.connection.session;

import cupcake.nikky.imperium.tower.protocol.ban.BanData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.cristalix.core.network.CorePackage;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class SessionConnectionPacket extends CorePackage {

    private UUID userUuid;
    private boolean available;
    private BanData banData;

    public SessionConnectionPacket(UUID uuid) {
        this.userUuid = uuid;
        this.available = false;
        this.banData = null;
    }

}
