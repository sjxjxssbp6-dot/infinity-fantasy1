package cupcake.nikky.imperium.tower.data.buff;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class BuffStorage {

    @Getter
    private static final BuffStorage instance = new BuffStorage();
    @Getter
    private static final BuffStorage monsterInstance = new BuffStorage();

    private final List<BuffData> buffs = new ArrayList<>();

    public void updateDuration() {
        buffs.forEach(data -> data.setDuration(Math.max(data.getDuration() - 1, 0)));
    }

    public void registerBuff(@NotNull final BuffData data) {
        this.buffs.add(data);
    }

    public void unregisterBuff(final int id) {
        buffs.removeIf(data -> data.getId() == id);
    }

    public int size() {
        return getBuffs().size();
    }

    public void clear() {
        buffs.clear();
    }

    public List<BuffData> getBuffs() {
        buffs.removeIf(buffData -> buffData.getDuration() == 0);
        return new ArrayList<>(buffs);
    }

    public BuffData findById(final int id) {
        for (BuffData buff : buffs) {
            if (buff.getId() == id) return buff;
        }

        return null;
    }
}
