package cupcake.nikky.imperium.tower.protocol.packet.time;

import cupcake.nikky.imperium.tower.protocol.board.PlayerTimeData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.cristalix.core.network.CorePackage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserTimeBoardPacket extends CorePackage {

    private Map<UUID, String> names = new HashMap<>();
    private Map<UUID, PlayerTimeData> data = new HashMap<>();

}
