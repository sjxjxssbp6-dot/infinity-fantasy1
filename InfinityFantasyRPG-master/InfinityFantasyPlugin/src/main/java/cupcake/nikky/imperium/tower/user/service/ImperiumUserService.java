package cupcake.nikky.imperium.tower.user.service;

import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.user.repository.ImperiumUserRepository;
import org.bson.conversions.Bson;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public final class ImperiumUserService {

    private final ImperiumUserRepository repository = new ImperiumUserRepository();

    public Optional<User> findById(@NotNull final UUID uuid) {
        return Optional.ofNullable(this.repository.findById(uuid));
    }

    public Collection<User> findAll() {
        return this.repository.findAll();
    }

    public CompletableFuture<User> load(@NotNull final UUID uuid) {
        return this.repository.load(uuid);
    }

    public List<Bson> invalidate(@NotNull final Player player) {
        return this.repository.invalidate(player);
    }

    public List<Bson> update(@NotNull final Player player) {
        return this.repository.save(player);
    }

}
