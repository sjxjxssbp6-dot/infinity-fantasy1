package cupcake.nikky.imperium.tower.user.settings.exact;

import com.google.common.reflect.TypeToken;
import com.mongodb.client.model.Updates;
import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.achievement.Achievement;
import cupcake.nikky.imperium.tower.achievement.AchievementType;
import cupcake.nikky.imperium.tower.achievement.Achievements;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.user.settings.SavableUserSettings;
import lombok.val;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class AchievementSettings extends SavableUserSettings {

    private final List<Achievement> achievements = new CopyOnWriteArrayList<>();
    private static final Type tokenType = new TypeToken<Map<String, String>>(){}.getType();

    public AchievementSettings(User user) {
        super(user);
    }

    public List<Achievement> getAchievements() {
        return new ArrayList<>(achievements);
    }

    public void addAchievement(@NotNull final Achievement achievement) {
        this.achievements.add(achievement);
    }

    public void invalidate(@NotNull final Achievements achievements) {
        this.achievements.removeIf(achievement -> achievement.getType() == achievements);
    }

    public void invalidateAll() {
        this.achievements.clear();
    }

    public Achievement findById(@NotNull final String uniqueId) {
        val current = getAchievements().stream().filter(achievement -> achievement.getUniqueId().equals(uniqueId)).findFirst();
        return current.orElseGet(() -> this.applyAchievement(uniqueId));
    }

    public Achievement findByEnum(@NotNull final Achievements achievements) {
        return this.findById(achievements.getUniqueId());
    }

    public Collection<Achievement> findByType(@NotNull final AchievementType type) {
        return getAchievements().stream()
                .filter(achievement -> {
                    val achievements = Achievements.findById(achievement.getUniqueId());
                    if (achievements == null) return false;

                    return achievements.getAchievementType() == type;
                }).collect(Collectors.toList());
    }

    public boolean containAchievement(@NotNull final String uniqueId) {
        return getAchievements().stream().anyMatch(achievement -> achievement.getUniqueId().equals(uniqueId));
    }

    public Achievement applyAchievement(@NotNull final String uniqueId) {
        if (this.containAchievement(uniqueId)) return this.findById(uniqueId);

        val result = Achievements.createInstance(Achievements.findById(uniqueId));

        if (result == null) return null;

        this.addAchievement(result);

        if (result.getType().getChecker().isWithReward()) result.setupGoal("reward", 1);

        return result;
    }

    @Override
    public List<Bson> createUpdates() {
        final Map<String, String> playerMap = new HashMap<>();
        this.achievements.forEach(achievement -> playerMap.put(achievement.getUniqueId(), achievement.serialize()));
        return Collections.singletonList(Updates.set("achievementData", Fantasy.gson().toJson(playerMap, tokenType)));
    }

    @Override
    public void readDocument(Document document) {
        if (document.isEmpty()) return;

        val achievementData = document.get("achievementData", "");

        if (achievementData == null || achievementData.trim().isEmpty()) return;

        final Map<String, String> playerMap = Fantasy.gson().fromJson(achievementData, tokenType);

        playerMap.forEach((key, serializedAchievement) -> {
            val achievement = this.applyAchievement(key);
            if (achievement != null)
                achievement.deserialize(serializedAchievement);
        });
    }

}