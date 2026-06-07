package cupcake.nikky.imperium.tower.board.exact;

import cupcake.nikky.imperium.tower.board.FantasyBoard;
import cupcake.nikky.imperium.tower.bootstrap.exact.enable.MapServiceProcess;
import cupcake.nikky.imperium.tower.protocol.packet.UserGeneralBoardPacket;
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
public class GeneralBoard implements FantasyBoard<UserGeneralBoardPacket> {

    private final Board board;

    @Accessors(fluent = true)
    private UUID firstPlace;
    @Accessors(fluent = true)
    private UUID secondPlace;
    @Accessors(fluent = true)
    private UUID thirdPlace;

    public GeneralBoard() {
        board = Board.newBuilder()
                .title("Общий топ")
                .addColumn("Игрок", 50)
                .addColumn("Количество очков", 40)
                .location(new Location(MapServiceProcess.GAME_WORLD, 2.6, 99, 63.5, -90, 0))
                .build();
    }

    @Override
    public boolean completedPlaces() {
        return this.firstPlace() != null && this.secondPlace() != null && this.thirdPlace() != null;
    }

    public void handlePacket(@NotNull final UserGeneralBoardPacket packet) {
        val data = packet.getData();

        board.clearContent();
        var finalData = data.entrySet().stream().sorted(Comparator.comparingDouble(entry -> -entry.getValue())).toList();

        var size = finalData.size();

        if (size > 0) firstPlace(finalData.getFirst().getKey());
        if (size > 1) secondPlace(finalData.get(1).getKey());
        if (size > 2) thirdPlace(finalData.get(2).getKey());

        finalData.forEach(entry -> board.addContent(packet.getNames().get(entry.getKey()), String.format("%.1f", entry.getValue())));

        board.updateContent();
    }



}
