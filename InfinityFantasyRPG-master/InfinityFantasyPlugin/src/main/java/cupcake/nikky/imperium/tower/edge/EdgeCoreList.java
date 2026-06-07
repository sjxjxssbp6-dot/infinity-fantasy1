package cupcake.nikky.imperium.tower.edge;

import cupcake.nikky.imperium.tower.buff.BuffType;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.rng.Weight;
import cupcake.nikky.imperium.tower.rng.WeightPool;
import cupcake.nikky.imperium.tower.rng.WeightWrapper;
import cupcake.nikky.imperium.tower.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.wada.util.Color;

import java.util.Collections;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum EdgeCoreList {

    GREEN(new EdgeCore(BuffType.GREEN_EDGE_CORE, 40, 15), Color.GREEN),
    RED(new EdgeCore(BuffType.RED_EDGE_CORE, 3, 15), Color.RED),
    PURPLE(new EdgeCore(BuffType.PURPLE_EDGE_CORE, 0.5, 15), Color.VIOLET),
    WHITE(new EdgeCore(BuffType.WHITE_EDGE_CORE, 50, 15), Color.WHITE),
    BLUE(new EdgeCore(BuffType.BLUE_EDGE_CORE, 100, 15), Color.BLUE),
    YELLOW(new EdgeCore(BuffType.YELLOW_EDGE_CORE, 50, 15), Color.YELLOW);

    private final EdgeCore edgeCore;
    private final Color edgeColorEffect;

    public EdgeCoreEffect createEffect(@NotNull final Location location, @NotNull final List<Player> players) {
        val instance = new EdgeCoreEffect(this.getEdgeCore(), getEdgeColorEffect());
        instance.createEffect(location, players);

        return instance;
    }

    public EdgeCoreEffect createEffect(@NotNull final Location location, @NotNull final Player player) {
        val instance = new EdgeCoreEffect(this.getEdgeCore(), getEdgeColorEffect());
        instance.createEffect(location, Collections.singletonList(player));

        return instance;
    }

    public static EdgeCoreEffect generate(@NotNull final Player player, @NotNull final LivingEntity target) {
        val pool = new WeightPool<WeightWrapper<EdgeCoreList>>(1);
        val user = player.<User>getBungeePlayer();

        val chanceToSpawnCore = user.findAttribute(ImperiumAttribute.INCREASE_EDGE_CORE_CHANCE).findValue() / 100D;

        int defaultSize = 0;
        val generalSize = 100;

        for (EdgeCoreList value : values()) {
            defaultSize += generalSize;
            pool.apply(new Weight<>(new WeightWrapper<>(value, (int) Math.round(generalSize * chanceToSpawnCore), false)));
        }

        val transitionChance = (int) Math.round(Math.min(user.getLevelController().getLevel(), 60) / 60D * 50);
        pool.setInitialSize(defaultSize * (50 + transitionChance));

        val list = pool.generatePool();
        val result = list.isEmpty() ? null : list.getFirst().weightWrapper().getWrapped();

        return result == null ? null : result.createEffect(target.getLocation().add(0, 1, 0), Collections.singletonList(player));
    }

}
