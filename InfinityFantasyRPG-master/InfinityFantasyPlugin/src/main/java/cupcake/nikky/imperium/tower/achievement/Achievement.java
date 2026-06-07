package cupcake.nikky.imperium.tower.achievement;

import com.google.common.reflect.TypeToken;
import cupcake.nikky.imperium.tower.Fantasy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class Achievement {

    @Getter private final String uniqueId;
    @Getter private final Achievements type;

    private final Map<String, Integer> achieves = new ConcurrentHashMap<>(32, 0.75F, 2);

    private static final Type tokenType = new TypeToken<Map<String, Integer>>(){}.getType();

    public void setupGoal(@NotNull final String key, final int goal) {
        this.achieves.put(key, -goal);
    }

    public Integer findAchieve(@NotNull final String key) {
        return this.achieves.get(key);
    }

    public boolean isDoneGoal(@NotNull final String key) {
        return this.findAchieve(key) == 0;
    }

    public AchievementProcess addProgress(@NotNull final String key) {
        return this.addProgress(key, 1);
    }

    public AchievementProcess addProgress(@NotNull final String key, final int progress) {
        val currentValue = this.findAchieve(key);

        if (currentValue == null || currentValue == 0)
            return new AchievementProcess(this, currentValue == null, false, true, false);

        this.achieves.put(key, currentValue + progress);

        return new AchievementProcess(this, false, true, false, currentValue + progress >= 0 && currentValue < 0);
    }

    public boolean isAchieveDone() {
        return this.achieves.values().stream().allMatch(integer -> integer == 0);
    }

    public String serialize() {
        return Fantasy.gson().toJson(achieves, tokenType);
    }

    public Achievement deserialize(@Nullable final String serialized) {
        if (serialized == null || serialized.trim().equals("")) return this;

        Map<String, Integer> deserialized = Fantasy.gson().fromJson(serialized, tokenType);

        this.achieves.putAll(deserialized);

        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T apply(Class<? extends T> clazz) {
        return (T) this;
    }

    public <T> boolean canApply(Class<? extends T> clazz) {
        return this.getClass().isInstance(clazz);
    }

}
