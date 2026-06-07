package cupcake.nikky.imperium.tower.attack.damage;

import org.jetbrains.annotations.NotNull;

public abstract class DamageProcess {

    public abstract void invoke(@NotNull final DamageContainer container);

}
