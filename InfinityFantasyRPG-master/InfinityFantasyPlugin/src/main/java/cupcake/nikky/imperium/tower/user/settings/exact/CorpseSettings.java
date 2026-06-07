package cupcake.nikky.imperium.tower.user.settings.exact;

import com.google.common.reflect.TypeToken;
import com.mongodb.client.model.Updates;
import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.corpse.CorpsePoints;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.user.settings.SavableUserSettings;
import me.anfanik.steda.api.utility.LocationUtility;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CorpseSettings extends SavableUserSettings {

    private static final Type token = new TypeToken<List<String>>(){}.getType();
    private final List<Location> detected = new CopyOnWriteArrayList<>();

    public CorpseSettings(User user) {
        super(user);
    }

    public void registerDetectedCorpse(@NotNull final Location location) {
        this.detected.add(location);
    }

    public void unregisterCorpse(@NotNull final Location location) {
        this.detected.remove(location);
    }

    public boolean isCorpseDetected(@NotNull final Location location) {
        return this.detected.contains(location);
    }

    public List<Location> findAll() {
        return new ArrayList<>(detected);
    }

    private String serialize() {
        return Fantasy.gson().toJson(this.findAll().stream().map(location -> LocationUtility.serialize(location, true)).toList(), token);
    }

    private void deserialize(@Nullable final String serialized) {
        if (serialized == null || serialized.isEmpty()) return;

        final List<String> detectedCorpses = Fantasy.gson().fromJson(serialized, token);
        if (detectedCorpses == null || detectedCorpses.isEmpty()) return;

        detectedCorpses.stream()
                .map(serializedLocation -> LocationUtility.deserialize(serializedLocation, CorpsePoints.getInstance().getCorpseWorld()))
                .forEach(this::registerDetectedCorpse);
    }

    @Override
    public List<Bson> createUpdates() {
        return Collections.singletonList(Updates.set("corpseData", this.serialize()));
    }

    @Override
    public void readDocument(Document document) {
        this.deserialize(document.get("corpseData", ""));
    }
}