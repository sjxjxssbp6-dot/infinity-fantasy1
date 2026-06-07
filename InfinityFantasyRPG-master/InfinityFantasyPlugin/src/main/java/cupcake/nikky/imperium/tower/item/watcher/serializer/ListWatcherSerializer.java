package cupcake.nikky.imperium.tower.item.watcher.serializer;

import com.google.gson.reflect.TypeToken;
import cupcake.nikky.imperium.tower.Fantasy;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class ListWatcherSerializer<T> extends WatcherSerializer<List<T>> {

    public ListWatcherSerializer(@NotNull final TypeToken<List<T>> typeToken) {
        super(list -> Fantasy.gson().toJson(list), string -> Fantasy.gson().fromJson(string, typeToken));
    }

}
