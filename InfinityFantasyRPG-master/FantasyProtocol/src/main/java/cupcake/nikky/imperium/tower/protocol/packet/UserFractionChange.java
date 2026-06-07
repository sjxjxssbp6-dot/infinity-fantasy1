package cupcake.nikky.imperium.tower.protocol.packet;

import cupcake.nikky.imperium.tower.protocol.fraction.FractionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.cristalix.core.network.CorePackage;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserFractionChange extends CorePackage {

    private FractionType previous;
    private FractionType next;

}
