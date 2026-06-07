package cupcake.nikky.imperium.tower.session;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

public class Session {

    @Getter private final UUID sessionUuid = UUID.randomUUID();

    private final Set<UUID> players = new CopyOnWriteArraySet<>();

    void add(@NotNull final UUID uuid) {
        this.players.add(uuid);

    }

    void remove(@NotNull final UUID uuid) {
        this.players.remove(uuid);
    }

    public boolean contains(@NotNull final UUID uuid) {
        return this.players.contains(uuid);
    }

    public List<Player> findAll() {
        return this.players.stream().map(Bukkit::getPlayer).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public boolean isSoloSession() {
        return this.players.size() == 1;
    }

}
