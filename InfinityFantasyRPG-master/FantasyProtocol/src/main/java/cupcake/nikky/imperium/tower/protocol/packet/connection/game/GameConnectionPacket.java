package cupcake.nikky.imperium.tower.protocol.packet.connection.game;

import cupcake.nikky.imperium.tower.protocol.game.GameState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.cristalix.core.network.CorePackage;

@Getter
@Setter
@AllArgsConstructor
public class GameConnectionPacket extends CorePackage {

    private GameState gameState;

}
