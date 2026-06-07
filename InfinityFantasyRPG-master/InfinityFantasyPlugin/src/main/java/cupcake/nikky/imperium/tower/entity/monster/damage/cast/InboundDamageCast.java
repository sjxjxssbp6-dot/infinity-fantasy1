package cupcake.nikky.imperium.tower.entity.monster.damage.cast;

import cupcake.nikky.imperium.tower.entity.monster.Monster;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public abstract class InboundDamageCast {

    @Getter
    private final Monster monster;

    @Getter
    @Setter
    private double currentHealthPercent;

    public abstract boolean canApply();

    public abstract void apply();

}
