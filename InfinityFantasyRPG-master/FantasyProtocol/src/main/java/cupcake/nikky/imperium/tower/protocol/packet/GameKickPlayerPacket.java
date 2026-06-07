package cupcake.nikky.imperium.tower.protocol.packet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.cristalix.core.network.CorePackage;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameKickPlayerPacket extends CorePackage {

    private UUID playerUuid;
    private String reason;

}
