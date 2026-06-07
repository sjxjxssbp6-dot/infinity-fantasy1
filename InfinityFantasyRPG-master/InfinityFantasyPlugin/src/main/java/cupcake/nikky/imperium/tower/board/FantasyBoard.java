package cupcake.nikky.imperium.tower.board;

import ru.cristalix.core.network.CorePackage;
import ru.cristalix.wada.module.board.Board;

import java.util.UUID;

public interface FantasyBoard<T extends CorePackage> {

    Board getBoard();

    void handlePacket(T packet);

    boolean completedPlaces();

    UUID firstPlace();

    UUID secondPlace();

    UUID thirdPlace();

}
