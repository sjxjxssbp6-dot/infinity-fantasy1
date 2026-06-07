package cupcake.nikky.imperium.tower.entity.monster.location;

import com.google.common.reflect.TypeToken;
import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.bootstrap.exact.enable.MapServiceProcess;
import lombok.val;
import me.anfanik.steda.api.utility.LocationUtility;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class MonsterLocationStorage {

    private static final Type listType = new TypeToken<List<String>>(){}.getType();

    private final List<Location> data = new CopyOnWriteArrayList<>();

    public void registerNew(@NotNull final Location location) {
        this.data.add(location);
    }

    public void registerNew(@NotNull final Collection<Location> location) {
        this.data.addAll(location);
    }

    public void remove(@NotNull final Location location) {
        this.data.remove(location);
    }

    public List<Location> findAll() {
        return new ArrayList<>(data);
    }

    public String serialize() {
        return Fantasy.gson().toJson(this.findAll().stream().map(location -> LocationUtility.serialize(location, true)).collect(Collectors.toList()), listType);
    }

    public static MonsterLocationStorage deserialize(@Nullable final String serializedList) {
        List<Location> deserialized = Optional.ofNullable(Fantasy.gson().<List<String>>fromJson(serializedList, listType)).orElse(new ArrayList<>())
                .stream().map(line -> LocationUtility.deserialize(line, MapServiceProcess.GAME_WORLD))
                .collect(Collectors.toList());

        val result = new MonsterLocationStorage();
        result.registerNew(deserialized);

        return result;
    }

}
