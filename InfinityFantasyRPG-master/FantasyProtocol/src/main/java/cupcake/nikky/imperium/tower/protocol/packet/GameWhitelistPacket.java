package cupcake.nikky.imperium.tower.protocol.packet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.cristalix.core.network.CorePackage;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameWhitelistPacket extends CorePackage {

    private boolean statement;

}
