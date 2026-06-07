package cupcake.nikky.imperium.tower.zone;

import cupcake.nikky.imperium.tower.bootstrap.exact.enable.MapServiceProcess;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record Zone(float startX, float endX, float startY, float endY, float startZ, float endZ, boolean pvp, int dayTime, boolean timeless, Map<String, Location> spawns) {

    public Zone(float startX, float endX, float startY, float endY, float startZ, float endZ, boolean pvp, int dayTime, boolean timeless) {
        this(startX, endX, startY, endY, startZ, endZ, pvp, dayTime, timeless, new HashMap<>());
    }

    public Zone addSpawnLocation(@NotNull final String key, final float x, final float y, final float z) {
        return this.addSpawnLocation(key, x, y, z, 0F, 0F);
    }

    public Zone addSpawnLocation(@NotNull final String key, final float x, final float y, final float z, final float yaw, final float pitch) {
        this.spawns().put(key, new Location(MapServiceProcess.GAME_WORLD, x, y, z, yaw, pitch));
        return this;
    }

    public Location spawnLocation(@NotNull final String key) {
        var result = spawns().get(key);
        return result == null ? null : result.clone();
    }

    public Location nearSpawnLocation(@NotNull final Location location) {
        return allSpawnLocations().stream().min(Comparator.comparingDouble(targetLocation -> targetLocation.distanceSquared(location))).orElse(null);
    }

    public List<Location> allSpawnLocations() {
        return spawns().values().stream().peek(Location::clone).toList();
    }

    public boolean isIn(@NotNull final Location location) {
        return startX() <= location.getX() && endX() >= location.getX() &&
                startY() <= location.getY() && endY() >= location.getY() &&
                startZ() <= location.getZ() && endZ() >= location.getZ();
    }

    public boolean isIn(@NotNull final Entity entity) {
        return this.isIn(entity.getLocation());
    }

    public boolean anySpawnIn(@NotNull final Location location) {
        return spawns().values().stream().anyMatch(location1 -> location1.distanceSquared(location) <= 1.5 * 1.5);
    }

    public float xSize() {
        return endX - startX;
    }

    public float ySize() {
        return endY - startY;
    }

    public float zSize() {
        return endZ - startZ;
    }

    public float centerX() {
        return (startX + endX) / 2F;
    }

    public float centerY() {
        return (startY + endY) / 2F;
    }

    public float centerZ() {
        return (startZ + endZ) / 2F;
    }

}
