package cupcake.nikky.imperium.tower.user.settings.exact;

import com.google.gson.reflect.TypeToken;
import com.mongodb.client.model.Updates;
import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.incubator.Incubator;
import cupcake.nikky.imperium.tower.incubator.Incubators;
import cupcake.nikky.imperium.tower.donate.DonateData;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.user.settings.SavableUserSettings;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class IncubatorSettings extends SavableUserSettings {

    private final List<Incubator> currentIncubators = new CopyOnWriteArrayList<>();
    private static final Type gsonType = new TypeToken<List<String>>(){}.getType();

    public IncubatorSettings(User user) {
        super(user);
    }

    public boolean canApplyIncubator(@NotNull final DonateData donateData) {
        return donateData.isSubscribed() ? currentIncubators.size() < 4 : currentIncubators.size() < 3;
    }

    public void applyIncubator(@NotNull final Incubators type) {
        this.currentIncubators.add(type.createEmptyInstance());
    }

    public void invalidateIncubator(@NotNull final UUID incubatorUuid) {
        this.currentIncubators.removeIf(incubator -> incubator.getUuid().equals(incubatorUuid));
    }

    public List<Incubator> findAllIncubators() {
        return new ArrayList<>(currentIncubators);
    }

    @Override
    public List<Bson> createUpdates() {
        return List.of(Updates.set("incubators", Fantasy.gson().toJson(currentIncubators.stream().map(Incubator::serialize).toList(), gsonType)));
    }

    @Override
    public void readDocument(Document document) {
        if (document.isEmpty()) return;

        var data = document.get("incubators");
        if (data == null) return;

        final List<String> serializedIncubators = Fantasy.gson().fromJson((String) data, gsonType);
        currentIncubators.addAll(serializedIncubators.stream().map(Incubator::deserialize).toList());
    }

}