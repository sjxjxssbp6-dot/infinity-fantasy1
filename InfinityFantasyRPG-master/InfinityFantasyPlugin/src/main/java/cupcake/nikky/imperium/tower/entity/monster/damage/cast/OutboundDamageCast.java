package cupcake.nikky.imperium.tower.entity.monster.damage.cast;

import cupcake.nikky.imperium.tower.entity.monster.Monster;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.LivingEntity;

@RequiredArgsConstructor
public abstract class OutboundDamageCast {

    @Getter
    private final Monster monster;

    @Getter
    @Setter
    private double currentHealthPercent;

    public abstract boolean canApply();

    public abstract void apply(LivingEntity entity);

}
