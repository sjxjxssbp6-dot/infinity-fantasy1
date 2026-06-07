package cupcake.nikky.imperium.tower.protocol.packet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.cristalix.core.network.CorePackage;
import ru.cristalix.core.realm.RealmId;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserTransferAccessibilityPacket extends CorePackage {

    private RealmId targetRealm;
    private boolean access;

}
