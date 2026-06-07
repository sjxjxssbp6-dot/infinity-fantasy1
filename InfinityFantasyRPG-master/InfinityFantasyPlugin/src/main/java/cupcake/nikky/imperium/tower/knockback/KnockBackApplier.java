package cupcake.nikky.imperium.tower.knockback;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class KnockBackApplier {

    private final double power;
    @Setter private double multiplier = 1D;

    public void apply(@NotNull final Vector direction, @NotNull final LivingEntity target) {
        target.setVelocity(direction.multiply(-power * multiplier));
    }

}
