package cupcake.nikky.imperium.tower.buff;

import com.google.common.reflect.TypeToken;
import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.entity.Attributable;
import cupcake.nikky.imperium.tower.protocol.utility.NumberUtility;
import lombok.val;
import org.bukkit.entity.LivingEntity;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.IntConsumer;

public interface Buff extends Attributable {

    String getTitle();

    List<String> getDescription();

    Buff setLevel(int level);

    Buff registerValueAttribute(ImperiumAttribute attribute, final double value);

    Buff registerPercentAttribute(ImperiumAttribute attribute, final double value);

    Buff registerMultiplierAttribute(ImperiumAttribute attribute, final double value);

    Buff addAttributeHandler(IntConsumer handler);

    List<IntConsumer> getAttributeHandlers();

    boolean shouldInvalidate();

    boolean shouldInvalidateAfterDeath();

    Buff setShouldInvalidateAfterDeath(final boolean shouldInvalidate);

    Buff setDurationOnlineOnly(boolean value);

    boolean isDurationOnlineOnly();

    boolean isJustUpdatedMultiplier();

    void setJustUpdatedMultiplier(boolean value);

    int getDurationSeconds();

    int getCurrentSeconds();

    Buff setCurrentSeconds(int ticks);

    int getMaxMultiplier();

    Buff setMaxMultiplier(int value);

    boolean isStackable();

    Buff setStackable(boolean value);

    int getMultiplier();

    Buff setMultiplier(int multiplier);

    BuffType getBuffType();

    Buff setType(BuffType buffType);

    void onReceive(LivingEntity entity);

    /*
        Executing every second
     */
    void onTick(LivingEntity entity);

    void onCompletion(LivingEntity entity);

    /*
        Serialize data of current buff
     */
    String serialize();

    /*
        Deserialize data and write into current object
     */
    static Buff deserialize(String serialized) {
        if (serialized == null)
            return null;

        val type = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> data = Fantasy.gson().fromJson(serialized, type);

        BuffType buffType;

        try {
            buffType = BuffType.valueOf(data.get("buffType"));
        } catch (IllegalArgumentException e) {
            return null;
        }

        val value = Double.parseDouble(data.getOrDefault("value", "0"));
        val duration = NumberUtility.parseInteger(data.getOrDefault("duration", "0"));
        val multiplier = NumberUtility.parseInteger(data.getOrDefault("multiplier", "1"));
        int currentSeconds = NumberUtility.parseInteger(data.get("currentTicks"));

        val savedTime = NumberUtility.parseLong(data.getOrDefault("savedTime", "0"));

        if (savedTime > 0) {
            val delta = Instant.now().toEpochMilli() - savedTime;
            currentSeconds += TimeUnit.MILLISECONDS.toSeconds(delta);
        }

        val buff = buffType.createNew(value, duration);

        buff.setMultiplier(multiplier);
        buff.setCurrentSeconds(currentSeconds);

        return buff;
    }

}
