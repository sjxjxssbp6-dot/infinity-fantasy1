package cupcake.nikky.imperium.tower.protocol.packet;

import cupcake.nikky.imperium.tower.protocol.fraction.FractionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.cristalix.core.network.CorePackage;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FractionTopPacket extends CorePackage {

    private FractionType type;

}
