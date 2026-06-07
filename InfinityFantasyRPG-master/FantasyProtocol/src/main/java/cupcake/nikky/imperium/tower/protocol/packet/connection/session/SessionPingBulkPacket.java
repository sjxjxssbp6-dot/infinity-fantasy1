package cupcake.nikky.imperium.tower.protocol.packet.connection.session;

import lombok.*;
import ru.cristalix.core.network.CorePackage;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SessionPingBulkPacket extends CorePackage {

    private List<UUID> users;

}
