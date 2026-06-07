package cupcake.nikky.imperium.tower.beacon;

import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.user.settings.exact.QuestSettings;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;
import java.util.function.ToIntFunction;

@Getter
@Setter
@Accessors(fluent = true, chain = true)
@NoArgsConstructor(staticName = "create")
public class BeaconSettings {

    private String title;
    private String requirement = "";
    private Predicate<Player> canTeleport;
    private ToIntFunction<BeaconObjects> priceFunction = beaconObjects -> 25 * beaconObjects.getId();

    private Predicate<QuestSettings> handlerQuest = questedPlayer -> true;
    private Predicate<User> handlerUser = user -> true;

    public BeaconSettings canTeleportQuestedPlayer(@NotNull final Predicate<QuestSettings> handler) {
        this.canTeleport = player -> handler.test(player.<User>getBungeePlayer().findQuestSettings());
        this.handlerQuest = handler;
        return this;
    }

    public BeaconSettings canTeleportUser(@NotNull final Predicate<User> handler) {
        this.canTeleport = player -> handler.test(player.getBungeePlayer());
        this.handlerUser = handler;
        return this;
    }

}
