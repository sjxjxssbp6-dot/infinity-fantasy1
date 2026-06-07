package cupcake.nikky.imperium.service.board.user;

import java.util.UUID;

public interface TopBoardService<T> {

    void clearAll();

    void applyName(UUID uuid, String name);

    void applyData(UUID uuid, T data);

    void sendPacket();

}
