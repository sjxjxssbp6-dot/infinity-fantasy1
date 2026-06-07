package cupcake.nikky.imperium.tower.mount;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

@Getter
@Setter
@AllArgsConstructor
public class MountRequirement {

    private Predicate<Player> acceptRequirement;
    private String requirementDescription;

    public static MountRequirement of(@NotNull final Predicate<Player> acceptRequirement, @NotNull final String requirementDescription) {
        return new MountRequirement(acceptRequirement, requirementDescription);
    }

    public static MountRequirement of(@NotNull final Predicate<Player> acceptRequirement) {
        return new MountRequirement(acceptRequirement, null);
    }

}
