package cupcake.nikky.imperium.tower.item.watcher.serializer;

import com.google.gson.reflect.TypeToken;
import cupcake.nikky.imperium.tower.item.modifier.ModifierTag;
import cupcake.nikky.imperium.tower.item.modifier.SpecificModifier;
import cupcake.nikky.imperium.tower.protocol.utility.NumberUtility;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Getter
@RequiredArgsConstructor
public sealed class WatcherSerializer<T> permits ListWatcherSerializer, MapWatcherSerializer {

    private final Function<T, String> serializer;
    private final Function<String, T> deserializer;

    public static final WatcherSerializer<Integer> INTEGER_WATCHER_SERIALIZER = new WatcherSerializer<>(String::valueOf, NumberUtility::parseInteger);
    public static final WatcherSerializer<String> STRING_WATCHER_SERIALIZER = new WatcherSerializer<>(string -> string, string -> string);
    public static final WatcherSerializer<Long> LONG_WATCHER_SERIALIZER = new WatcherSerializer<>(String::valueOf, NumberUtility::parseLong);
    public static final WatcherSerializer<Boolean> BOOLEAN_WATCHER_SERIALIZER = new WatcherSerializer<>(String::valueOf, Boolean::parseBoolean);
    public static final WatcherSerializer<Double> DOUBLE_WATCHER_SERIALIZER = new WatcherSerializer<>(String::valueOf, Double::parseDouble);

    public static WatcherSerializer<List<SpecificModifier>> listSpecificModifierWatcherSerializer() {
        return new ListWatcherSerializer<>(new TypeToken<List<SpecificModifier>>(){});
    }

    public static WatcherSerializer<List<ModifierTag>> listModifierTagWatcherSerializer() {
        return new ListWatcherSerializer<>(new TypeToken<List<ModifierTag>>(){});
    }

    public static <E, P> WatcherSerializer<Map<E, P>> mapWatcherSerializer() {
        return new MapWatcherSerializer<>();
    }

}
