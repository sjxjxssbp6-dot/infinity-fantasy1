package cupcake.nikky.imperium.tower.board.exact;

import cupcake.nikky.imperium.tower.board.FantasyBoard;
import cupcake.nikky.imperium.tower.bootstrap.exact.enable.MapServiceProcess;
import cupcake.nikky.imperium.tower.protocol.packet.time.UserTimeBoardPacket;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.val;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.wada.module.board.Board;

import java.util.Comparator;
import java.util.UUID;

@Getter
@Setter
public class HochmaBoard implements FantasyBoard<UserTimeBoardPacket> {

    private final Board board;

    @Accessors(fluent = true)
    private UUID firstPlace;
    @Accessors(fluent = true)
    private UUID secondPlace;
    @Accessors(fluent = true)
    private UUID thirdPlace;

    public HochmaBoard() {
        board = Board.newBuilder()
                .title("Топ по времени убийства: Хохма")
                .addColumn("Игрок", 50)
                .addColumn("Количество очков", 40)
                .location(new Location(MapServiceProcess.GAME_WORLD, 6.3, 99, 49.4, -90, 0))
                .build();
    }

    @Override
    public boolean completedPlaces() {
        return this.firstPlace() != null && this.secondPlace() != null && this.thirdPlace() != null;
    }

    public void handlePacket(@NotNull final UserTimeBoardPacket packet) {
        val keys = packet.getData().keySet();
        val data = packet.getData();

        board.clearContent();
        var finalData = keys.stream().map(data::get)
                .filter(timeData -> timeData.getLevel() > 1)
                .sorted(Comparator.comparingDouble(timeData -> -timeData.getScore())).toList();

        var size = finalData.size();

        if (size > 0) firstPlace(finalData.getFirst().getUuid());
        if (size > 1) secondPlace(finalData.get(1).getUuid());
        if (size > 2) thirdPlace(finalData.get(2).getUuid());

        finalData.forEach(timeData -> board.addContent(packet.getNames().get(timeData.getUuid()), "%.1f".formatted(timeData.getScore())));

        board.updateContent();
    }

}
