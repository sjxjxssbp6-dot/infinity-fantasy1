package cupcake.nikky.imperium.tower.edge;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.buff.BuffType;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Getter
@RequiredArgsConstructor
public class EdgeCore {

    private final BuffType type;
    private final double value;
    private final int duration;

    public void create(@NotNull final Player player) {
        val user = player.<User>getBungeePlayer();
        if (user == null) return;

        val edgeIncreaseDuration = 1D + user.findAttribute(ImperiumAttribute.INCREASE_EDGE_CORE_DURATION).findValue() / 100D;
        val edgeIncreaseAmplifier = 1D + user.findAttribute(ImperiumAttribute.INCREASE_EDGE_CORE_AMPLIFIER).findValue() / 100D;

        Fantasy.buffService().appendBuff(player.getUniqueId(), type, value * edgeIncreaseAmplifier, (int) Math.round(duration * edgeIncreaseDuration));
    }

}
