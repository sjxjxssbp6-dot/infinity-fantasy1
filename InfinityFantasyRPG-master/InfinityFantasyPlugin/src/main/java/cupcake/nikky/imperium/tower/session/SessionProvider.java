package cupcake.nikky.imperium.tower.session;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.user.User;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionProvider {

    private final Map<Integer, UUID> sessionEntityData = new ConcurrentHashMap<>(32, 0.75F, 2);

    public Optional<Session> findSessionByUuid(@NotNull final UUID uuid) {
        for (User user : Fantasy.userService().findAll()) {
            if (user.getSession() != null && user.getSession().getSessionUuid().equals(uuid)) {
                return Optional.of(user.getSession());
            }
        }

        return Optional.empty();
    }

    private void addSessionData(@NotNull final Integer entityId, @NotNull final UUID sessionUuid) {
        this.sessionEntityData.put(entityId, sessionUuid);
    }

    private UUID findById(@NotNull final Integer entityId) {
        return this.sessionEntityData.get(entityId);
    }

    private void removeSessionData(@NotNull final Integer entityId) {
        this.sessionEntityData.remove(entityId);
    }

    private Map<Integer, UUID> findAll() {
        return new HashMap<>(sessionEntityData);
    }

    public void joinEntityToSession(@NotNull final User user, final Entity entity) {
        this.addSessionData(entity.getEntityId(), user.getSession().getSessionUuid());
    }

    public void removeEntityFromSession(@NotNull final User sessionOwner, final Entity entity) {
        this.removeSessionData(entity.getEntityId());
    }

    public void joinUserToSession(@NotNull final User ownSession, @NotNull final User whoJoin) {
        ownSession.getSession().add(whoJoin.getUniqueId());
        whoJoin.setSession(ownSession.getSession());
    }

    public void createUserSession(@NotNull final User user) {
        user.setSession(new Session());
        user.getSession().add(user.getUniqueId());
    }

    public void removeFromSession(@NotNull final User user) {
        if (user.getSession().isSoloSession()) return;

        user.getSession().remove(user.getUniqueId());
        this.createUserSession(user);
    }

}
