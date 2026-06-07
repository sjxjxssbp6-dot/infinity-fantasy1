package cupcake.nikky.imperium.tower.user.settings.exact;

import com.google.gson.reflect.TypeToken;
import com.mongodb.client.model.Updates;
import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.tree.builder.element.TreeElement;
import cupcake.nikky.imperium.tower.tree.PointType;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.user.settings.SavableUserSettings;
import lombok.val;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class TreeSettings extends SavableUserSettings {

    private final Set<Integer> data = new CopyOnWriteArraySet<>();
    private final AtomicInteger resetPoints = new AtomicInteger();

    public TreeSettings(User user) {
        super(user);
    }

    public void addUpgrade(final int id) {
        this.data.add(id);
        Fantasy.clientProvider().sendTreeIndicator(getUser());
    }

    public void removeUpgrade(final int id) {
        this.data.remove(id);
        Fantasy.clientProvider().sendTreeIndicator(getUser());
    }

    public void invalidateUpgrades() {
        this.data.clear();
        Fantasy.clientProvider().sendTreeIndicator(getUser());
    }

    public List<Integer> findUpgradeIds() {
        return new ArrayList<>(data);
    }

    public int getResetPoints() {
        return resetPoints.get();
    }

    public void changeResetPoints(final int delta) {
        for (;;) {
            val oldResetPoints = this.resetPoints.get();
            if (this.resetPoints.compareAndSet(oldResetPoints, oldResetPoints + delta)) break;
        }
    }

    public void setResetPoints(final int resetPoints) {
        for (;;) {
            var old = this.resetPoints.get();
            if (this.resetPoints.compareAndSet(old, resetPoints)) break;
        }
    }

    public int findUpgradesSize() {
        return data.size();
    }

    public List<TreeElement> findUpgrades() {
        return data.stream().map(id -> Fantasy.treeProvider().getData().get((int) id)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public int findFreePoints(@NotNull final PointType type) {
        val usedPoints = (int) findUpgrades().stream().filter(treeElement -> treeElement != null && treeElement.getPointType() == type).count();
        return type.getCountAllPoints().apply(getPlayer(), type) - usedPoints;
    }

    public String serialize() {
        return Fantasy.gson().toJson(
                Map.of(
                        "upgrades", Fantasy.gson().toJson(findUpgradeIds()),
                        "resetPoints", getResetPoints()
                )
        );
    }

    public void deserialize(@Nullable final String serialized) {
        if (serialized == null || serialized.isEmpty()) return;

        final Map<String, String> data = Fantasy.gson().fromJson(serialized, new TypeToken<Map<String, String>>(){}.getType());
        final List<Integer> upgrades = Fantasy.gson().fromJson(data.get("upgrades"), new TypeToken<List<Integer>>(){}.getType());

        if (upgrades != null)
            this.data.addAll(upgrades);

        final int resetPoints = Fantasy.gson().fromJson(data.get("resetPoints"), Integer.class);
        this.changeResetPoints(resetPoints);
    }

    @Override
    public List<Bson> createUpdates() {
        return Collections.singletonList(Updates.set("treeData", this.serialize()));
    }

    @Override
    public void readDocument(Document document) {
        this.deserialize(document.get("treeData", ""));
    }

}
