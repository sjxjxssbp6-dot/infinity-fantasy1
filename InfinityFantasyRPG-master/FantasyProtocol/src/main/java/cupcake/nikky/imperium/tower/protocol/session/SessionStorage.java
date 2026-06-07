package cupcake.nikky.imperium.tower.protocol.session;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SessionStorage {

    @Getter
    private static final SessionStorage instance = new SessionStorage();

    private final Map<UUID, ConnectionSession> data = new ConcurrentHashMap<>();

    public Collection<ConnectionSession> findAll() {
        return new ArrayList<>(data.values());
    }

    public void registerSession(@NotNull final ConnectionSession session) {
        this.data.put(session.getUuid(), session);
    }

    public Optional<ConnectionSession> findSession(@NotNull final UUID uuid) {
        return Optional.ofNullable(this.data.get(uuid));
    }

    public void invalidate(@NotNull final UUID uuid) {
        this.data.remove(uuid);
    }

}
