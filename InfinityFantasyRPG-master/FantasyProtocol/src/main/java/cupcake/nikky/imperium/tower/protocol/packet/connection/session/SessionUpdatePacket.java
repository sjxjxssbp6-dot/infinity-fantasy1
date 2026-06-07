package cupcake.nikky.imperium.tower.protocol.packet.connection.session;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.cristalix.core.network.CorePackage;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class SessionUpdatePacket extends CorePackage {

    private UUID userUuid;
    private SessionState state;

}
