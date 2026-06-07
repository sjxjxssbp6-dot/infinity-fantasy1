package cupcake.nikky.imperium.tower.quest.session;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class QuestSessionProvider {

    @Getter
    private static final QuestSessionProvider instance = new QuestSessionProvider();

    private final Map<UUID, QuestSession> data = new ConcurrentHashMap<>(32, 0.75F, 2);

    public QuestSession createSession(@NotNull final Player player, @NotNull final List<Integer> quests) {
        val session = new QuestSession(player, quests);
        this.data.put(player.getUniqueId(), session);
        return session;
    }

    public Optional<QuestSession> findSession(@NotNull final UUID uuid) {
        return Optional.ofNullable(this.data.get(uuid));
    }

    public void invalidateSession(@NotNull final UUID uuid) {
        this.data.remove(uuid);
    }

}
