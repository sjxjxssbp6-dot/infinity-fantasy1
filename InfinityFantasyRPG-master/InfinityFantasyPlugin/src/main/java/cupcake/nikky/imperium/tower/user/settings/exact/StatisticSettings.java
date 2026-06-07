package cupcake.nikky.imperium.tower.user.settings.exact;

import com.google.gson.reflect.TypeToken;
import com.mongodb.client.model.Updates;
import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.protocol.utility.NumberUtility;
import cupcake.nikky.imperium.tower.statistic.StatisticType;
import cupcake.nikky.imperium.tower.statistic.Statistics;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.user.settings.SavableUserSettings;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StatisticSettings extends SavableUserSettings {

    private static final Type token = new TypeToken<List<String>>(){}.getType();
    private final Map<Integer, StatisticType<?>> data = new ConcurrentHashMap<>();

    public StatisticSettings(User user) {
        super(user);
    }

    public void invalidateAll() {
        this.data.clear();
    }

    public void invalidate(@NotNull final StatisticType<?> statisticType) {
        this.data.remove(statisticType.index());
    }

    @SuppressWarnings("unchecked")
    public <T extends StatisticType<?>> T findStatistic(@NotNull final T t) {
        return (T) data.computeIfAbsent(t.index(), index -> t.copy());
    }

    public StatisticType<?> registerStatistic(final int index, @NotNull final String data) {
        var original = Statistics.get(index);
        var type = original.copy().convert(data);
        this.data.put(original.index(), type);
        return type;
    }

    @Override
    public List<Bson> createUpdates() {
        return List.of(Updates.set(
                "statistic",
                Fantasy.gson().toJson(data.values().stream().filter(statisticType -> !statisticType.isEmpty()).map(StatisticType::serialize).toList())
        ));
    }

    @Override
    public void readDocument(Document document) {
        final List<String> statistic = Fantasy.gson().fromJson(document.get("statistic", ""), token);
        if (statistic == null || statistic.isEmpty()) return;

        statistic.forEach(serialized -> {
            var split = serialized.split(":");
            this.registerStatistic(NumberUtility.parseInteger(split[0]), split[1]);
        });
    }

}
