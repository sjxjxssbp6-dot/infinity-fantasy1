package cupcake.nikky.imperium.tower.battlepass.quest;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public class BattlePassQuest {

    private final int id;
    private final int rewardExp;
    @Setter(value = AccessLevel.PACKAGE)
    private BattlePassLinkedQuest chain;
    private final BiFunction<Player, Integer, String> description;
    private final Predicate<Player> isDone;

    public String getDescription(@NotNull final Player player) {
        return description.apply(player, rewardExp);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BattlePassQuest quest = (BattlePassQuest) o;
        return id == quest.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
