package cupcake.nikky.imperium.tower.character.ineract;

import cupcake.nikky.imperium.tower.character.ImperiumCharacter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public abstract class DefaultInteract {

    @Getter
    private final ImperiumCharacter character;

    public abstract void interact(@NotNull final Player player);

}
