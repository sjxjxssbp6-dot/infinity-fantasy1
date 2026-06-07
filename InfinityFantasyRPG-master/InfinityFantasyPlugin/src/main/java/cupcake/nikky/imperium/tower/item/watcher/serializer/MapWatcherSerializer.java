package cupcake.nikky.imperium.tower.item.watcher.serializer;

import com.google.gson.reflect.TypeToken;
import cupcake.nikky.imperium.tower.Fantasy;

import java.util.Map;

public final class MapWatcherSerializer<T, E> extends WatcherSerializer<Map<T, E>> {

    public MapWatcherSerializer() {
        super(map -> Fantasy.gson().toJson(map), string -> Fantasy.gson().fromJson(string, new TypeToken<Map<T, E>>(){}.getType()));
    }

}
