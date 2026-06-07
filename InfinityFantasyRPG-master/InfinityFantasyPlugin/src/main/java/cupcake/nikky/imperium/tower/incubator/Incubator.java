package cupcake.nikky.imperium.tower.incubator;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.chat.ChatType;
import cupcake.nikky.imperium.tower.entity.monster.Monster;
import cupcake.nikky.imperium.tower.sound.AdvancedSounds;
import cupcake.nikky.imperium.tower.protocol.utility.NumberUtility;
import cupcake.nikky.imperium.tower.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class Incubator {

    private final UUID uuid = UUID.randomUUID();
    private final Incubators type;
    private final IncubatorData data;

    public void onKill(@NotNull final Player killer, @NotNull final Monster monster) {
        if (type.getRequirement().levelRequirement() <= monster.getLevel()) {
            if (data.increment() >= type.getRequirement().deathRequirement()) {
                type.getReward().reward(killer);
                this.onComplete(killer);
            }

            Fantasy.clientProvider().updateIncubators(killer);
        }
    }

    public void onComplete(@NotNull final Player player) {
        var user = player.<User>getBungeePlayer();
        user.findIncubatorSettings().invalidateIncubator(getUuid());
        Fantasy.bukkitProvider().sound(player, AdvancedSounds.RING);
        Fantasy.bukkitProvider().sendMessage(ChatType.SYSTEM, "Инкубируемый предмет выпал рядом с Вами!", player);
    }

    public String serialize() {
        return type.name() + ":" + data.getCurrent();
    }

    public static Incubator deserialize(@Nullable final String serialized) {
        if (serialized == null || serialized.isBlank()) return null;
        var args = serialized.split(":");
        var type = Incubators.valueOf(args[0]);
        var instance = type.createEmptyInstance();
        instance.getData().setCurrentDeaths(NumberUtility.parseInteger(args[1]));
        return instance;
    }

}
