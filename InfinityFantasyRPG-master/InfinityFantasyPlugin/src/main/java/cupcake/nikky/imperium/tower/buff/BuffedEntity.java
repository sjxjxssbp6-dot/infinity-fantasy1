package cupcake.nikky.imperium.tower.buff;

import com.google.common.reflect.TypeToken;
import cupcake.nikky.imperium.tower.Fantasy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class BuffedEntity {

    @Getter private final UUID uuid;
    private final Map<BuffType, Buff> data = new ConcurrentHashMap<>(32, 0.75F, 2);

    public boolean registerType(@NotNull final BuffType buffType) {
        return this.registerType(buffType, 1D, 0);
    }

    public boolean registerType(@NotNull final BuffType buffType, final double value, final int duration) {
        val foundData = this.find(buffType);

        if (foundData.isPresent()) {
            val data = foundData.get();
            if (data.isStackable() && data.getMultiplier() < data.getMaxMultiplier()) {
                data.setMultiplier(data.getMultiplier() + 1);
                data.setJustUpdatedMultiplier(true);
            }
            data.setCurrentSeconds(0);
            this.data.put(buffType, data);
            return false;
        } else {
            val buff = buffType.createNew(value, duration);
            this.data.put(buffType, buff);
            return true;
        }
    }

    public Optional<Buff> find(@NotNull final BuffType buffType) {
        return Optional.ofNullable(this.data.get(buffType));
    }

    public void invalidateType(@NotNull final BuffType buffType) {
        this.data.remove(buffType);
    }

    public void processDeath() {
        new ArrayList<>(data.values()).stream().filter(Buff::shouldInvalidateAfterDeath)
                .forEach(buff -> Fantasy.buffService().invalidateBuff(this, buff.getBuffType()));
    }

    public List<Buff> getAll() {
        return new ArrayList<>(data.values());
    }

    public String serialize() {
        val data = getAll();

        val token = new TypeToken<List<String>>(){}.getType();
        final List<String> list = new ArrayList<>();

        data.stream()
                .filter(buff -> buff.getBuffType() != null)
                .filter(buff -> buff.getBuffType() != BuffType.FRAGMENT_DEBUFF_INFO)
                .forEach(buff -> list.add(buff.serialize()));

        return Fantasy.gson().toJson(list, token);
    }

    public static BuffedEntity deserialize(@NotNull final UUID uuid, @Nullable final String serialized) {
        val player = new BuffedEntity(uuid);
        if (serialized == null || serialized.trim().isEmpty()) return player;

        val token = new TypeToken<List<String>>(){}.getType();
        final List<String> list = Fantasy.gson().fromJson(serialized, token);

        list.forEach(serializedBuff -> {
            val buff = Buff.deserialize(serializedBuff);
            if (buff == null) return;

            player.data.put(buff.getBuffType(), buff);
        });

        return player;
    }

}
