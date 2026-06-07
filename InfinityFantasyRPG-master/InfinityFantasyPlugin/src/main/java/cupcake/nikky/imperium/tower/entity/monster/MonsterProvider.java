package cupcake.nikky.imperium.tower.entity.monster;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.entity.monster.data.MonsterData;
import cupcake.nikky.imperium.tower.entity.monster.location.MonsterLocationProvider;
import cupcake.nikky.imperium.tower.entity.monster.tracker.respawn.MonsterRespawnTracker;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

@Accessors(fluent = true)
public final class MonsterProvider {

    /* key - uuid of living entity; value - monster wrapper */
    private final Map<UUID, Monster> monsters = new ConcurrentHashMap<>(128, 1F, 2);
    private final Map<MonsterType, List<Monster>> monsterTypeListMap = new EnumMap<>(MonsterType.class);

    @Getter private final MonsterLocationProvider locationProvider = new MonsterLocationProvider();
    @Getter private final MonsterRespawnTracker respawnTracker = new MonsterRespawnTracker(10);

    private final AtomicBoolean loadedTypes = new AtomicBoolean(false);

    public boolean isTypesLoaded() {
        return loadedTypes.get();
    }

    public void loadTypes() {
        if (loadedTypes.get()) throw new RuntimeException("Attempt to load monster types again");

        int size = 0;

        for (MonsterType value : MonsterType.values()) {
            val spawnLocations = value.getSpawnLocations();
            spawnLocations.forEach(location -> {
                val instance = this.createInstance(value, location);
                instance.setCanRespawn(true);
                respawnTracker().register(instance, 20);

                var list = monsterTypeListMap.computeIfAbsent(value, k -> new ArrayList<>());
                list.add(instance);
            });
            size += spawnLocations.size();
        }

        loadedTypes.set(true);

        Bukkit.getLogger().info("Loaded monsters: " + size);
    }

    public List<Monster> findMonstersByType(@NotNull final MonsterType type) {
        val result = this.monsterTypeListMap.get(type);
        return result == null ? Collections.emptyList() : result;
    }

    public Monster createInstance(@NotNull final MonsterType type, @NotNull final Location location) {
        return this.createInstance(type, type.getMonsterData(), location);
    }

    public Monster createInstance(@NotNull final MonsterType type, @NotNull final MonsterData customMonsterData, @NotNull final Location location) {
        val monster = type.getMonsterSupplier().apply(location, customMonsterData, type);
        Fantasy.monsterProvider().register(monster);
        return monster;
    }

    public Optional<Monster> findById(final int id) {
        return monsters.values().stream().filter(Monster::isExist).filter(monster -> monster.getEntityId() == id).findFirst();
    }

    public Optional<Monster> findByUUID(@NotNull final UUID uuid) {
        return Optional.ofNullable(this.monsters.get(uuid));
    }

    public List<Monster> findAll() {
        return new ArrayList<>(monsters.values());
    }

    public void forEach(@NotNull final BiConsumer<UUID, Monster> action) {
        this.monsters.forEach(action);
    }

    public void invalidate(@NotNull final UUID uuid) {
        this.monsters.remove(uuid);
    }

    public void register(@NotNull final Monster monster) {
        this.monsters.put(monster.getMonsterUniqueId(), monster);
    }

}
