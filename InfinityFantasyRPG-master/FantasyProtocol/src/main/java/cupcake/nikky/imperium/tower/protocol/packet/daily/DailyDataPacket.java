package cupcake.nikky.imperium.tower.protocol.packet.daily;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.cristalix.core.network.CorePackage;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DailyDataPacket extends CorePackage {

    private boolean nextDay;
    private boolean nextWeek;

}
