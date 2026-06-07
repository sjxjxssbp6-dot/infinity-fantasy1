package cupcake.nikky.imperium.tower.entity.monster.location;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.entity.monster.MonsterType;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class MonsterLocationProvider {

    private final MonsterLocationRepository repository = new MonsterLocationRepository();

    public MonsterLocationStorage findStorage(@NotNull final MonsterType type) {
        return repository.findByType(type);
    }

    public void registerLocation(@NotNull final MonsterType type, @NotNull final Location location) {
        this.repository.registerNewLocation(type, location);
    }

    public void registerLocations(@NotNull final MonsterType type, @NotNull final Collection<Location> locations) {
        locations.forEach(location -> this.registerLocation(type, location));
    }

    public CompletableFuture<Void> asyncLoad() {
        return this.repository.loadAll();
    }

    public void asyncUpdateLocations() {
        this.repository.saveLocationsAsync();
    }

    public void loadSpawnLocations() {
        for (MonsterType value : MonsterType.values()) {
            value.getSpawnLocations().addAll(Fantasy.monsterProvider().locationProvider().findStorage(value).findAll());
        }
    }

}
