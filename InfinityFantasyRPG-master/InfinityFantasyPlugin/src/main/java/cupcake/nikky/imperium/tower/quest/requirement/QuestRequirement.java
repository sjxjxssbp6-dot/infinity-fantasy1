package cupcake.nikky.imperium.tower.quest.requirement;

import com.google.common.reflect.TypeToken;
import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.sound.AdvancedSounds;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class QuestRequirement {

    @Getter(value = AccessLevel.PROTECTED)
    private final Map<String, Integer> data = new HashMap<>();

    private final Type typeToken = new TypeToken<Map<String, Integer>>(){}.getType();
    @Getter @Setter
    private boolean done;

    protected void sendNotification(Player player) {
        Fantasy.bukkitProvider().sound(player, AdvancedSounds.TAP);
    }

    protected abstract void checkDone();

    public abstract List<String> goalDescription();

    public abstract QuestRequirement clone();

    public String serialize() {
        return Fantasy.gson().toJson(data, typeToken);
    }

    public void deserialize(@Nullable final String serialized) {
        if (serialized == null) return;
        this.data.putAll(Fantasy.gson().fromJson(serialized, typeToken));
        this.checkDone();
    }

}
