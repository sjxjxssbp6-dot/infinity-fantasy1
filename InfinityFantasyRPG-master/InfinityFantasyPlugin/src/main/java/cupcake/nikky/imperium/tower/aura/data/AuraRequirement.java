package cupcake.nikky.imperium.tower.aura.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

@Getter
@Setter
@AllArgsConstructor
public class AuraRequirement {

    private Predicate<Player> acceptRequirement;
    private String requirementDescription;

    public static AuraRequirement of(@NotNull final Predicate<Player> acceptRequirement, @NotNull final String requirementDescription) {
        return new AuraRequirement(acceptRequirement, requirementDescription);
    }

    public static AuraRequirement of(@NotNull final Predicate<Player> acceptRequirement) {
        return new AuraRequirement(acceptRequirement, "&6Выбить из кейса с аурами");
    }

}
