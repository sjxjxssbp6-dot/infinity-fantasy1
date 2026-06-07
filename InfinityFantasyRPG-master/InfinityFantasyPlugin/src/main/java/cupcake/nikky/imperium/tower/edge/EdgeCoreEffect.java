package cupcake.nikky.imperium.tower.edge;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.sound.AdvancedSounds;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.wada.Wada;
import ru.cristalix.wada.module.sphere.ISphere;
import ru.cristalix.wada.util.Color;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class EdgeCoreEffect {

    private final Color color;
    private final AtomicInteger duration;
    @Getter private final EdgeCore edgeCore;
    private ISphere sphere;

    public EdgeCoreEffect(@NotNull final EdgeCore edgeCore, @NotNull final Color color) {
        this.edgeCore = edgeCore;
        this.duration = new AtomicInteger(edgeCore.getDuration());
        this.color = color;
    }

    public int decreaseDuration() {
        return duration.decrementAndGet();
    }

    public void invalidate() {
        Optional.ofNullable(sphere).ifPresent(currentSphere -> Wada.get().getSphereManager().removeSphere(currentSphere));
    }

    public void createEffect(@NotNull final Location location, @NotNull final List<Player> attackers) {
        this.sphere = Wada.get().getSphereManager().createSphere(.75F, location, color, (sphere, player) -> {
            Wada.get().getSphereManager().removeSphere(sphere);
            this.getEdgeCore().create(player);
            Fantasy.bukkitProvider().sound(player, AdvancedSounds.RING);
        });
        sphere.send(attackers);
    }

}
