package cupcake.nikky.imperium.tower.uuid;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@RequiredArgsConstructor(staticName = "create")
public final class UUIDTranslator {

    private final UUID uuid;
    private final String server;

    public static UUIDTranslator from(@NotNull final String translated) {
        val uuidAndServer = translated.split(":");
        if (uuidAndServer.length != 2) return null;

        return new UUIDTranslator(UUID.fromString(uuidAndServer[0]), uuidAndServer[1]);
    }

    @Override
    public String toString() {
        return String.format("%s:%s", uuid, server);
    }

}
