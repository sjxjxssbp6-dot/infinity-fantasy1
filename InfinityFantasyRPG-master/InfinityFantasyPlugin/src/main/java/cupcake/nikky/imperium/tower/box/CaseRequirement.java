package cupcake.nikky.imperium.tower.box;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.function.BiFunction;

@Getter
@RequiredArgsConstructor
public class CaseRequirement {

    private final BiFunction<Player, Integer, Boolean> handler;
    private final BiFunction<Player, Integer, String[]> textError;

}
