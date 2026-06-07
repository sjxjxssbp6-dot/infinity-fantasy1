package cupcake.nikky.imperium.tower.entity.monster.tracker.respawn;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.entity.monster.Monster;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Accessors(fluent = true)
@RequiredArgsConstructor
public class MonsterRespawnTracker {

    @Getter private final int interval;

    @Getter private final Map<UUID, RespawnData> data = new ConcurrentHashMap<>(64, 0.75F, 2);
    @Getter private List<RespawnData> respawnDone;

    public void register(@NotNull final Monster monster) {
        if (data.containsKey(monster.getMonsterUniqueId())) return;
        this.data.put(monster.getMonsterUniqueId(), new RespawnData(monster).withTicks(monster.getRespawnTicks()).withText(monster.getMonsterData().isShowRespawnText()));
    }

    public void register(@NotNull final Monster monster, final int ticks) {
        if (data.containsKey(monster.getMonsterUniqueId())) return;
        this.data.put(monster.getMonsterUniqueId(), new RespawnData(monster).withTicks(ticks).withText(monster.getMonsterData().isShowRespawnText()));
    }

    public boolean isTracked(@NotNull final UUID monsterUuid) {
        return this.data.containsKey(monsterUuid);
    }

    public void unregister(@NotNull final Monster monster) {
        var old = this.data.remove(monster.getMonsterUniqueId());
        if (old != null) old.removeText();
    }

    public void update() {
        if (this.respawnDone != null && !this.respawnDone.isEmpty()) {
            Fantasy.runLater(() -> {
                val data = new ArrayList<>(respawnDone);

                data.forEach(respawnData -> {
                    this.respawnDone.remove(respawnData);

                    val monster = respawnData.getMonster();
                    monster.naturallySpawn(monster.isCanRespawn());
                });
            });
        }

        final List<RespawnData> currentRespawnDone = new ArrayList<>();

        this.data.forEach((uuid, respawnData) -> {
            if (respawnData.getAndAdd(-interval()) <= 0) {
                this.unregister(respawnData.getMonster());
                currentRespawnDone.add(respawnData);
            }
        });

        if (!currentRespawnDone.isEmpty()) {
            Fantasy.runLater(() -> {
                if (this.respawnDone == null || this.respawnDone.isEmpty())
                    this.respawnDone = currentRespawnDone;
                else this.respawnDone.addAll(currentRespawnDone);
            });
        }
    }

}
