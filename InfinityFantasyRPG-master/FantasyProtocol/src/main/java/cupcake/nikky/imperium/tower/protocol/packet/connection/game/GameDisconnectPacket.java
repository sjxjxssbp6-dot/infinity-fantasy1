package cupcake.nikky.imperium.tower.protocol.packet.connection.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.cristalix.core.network.CorePackage;

@Getter
@Setter
@AllArgsConstructor
public class GameDisconnectPacket extends CorePackage {

    private boolean reload;

}
