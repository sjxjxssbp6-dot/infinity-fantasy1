package cupcake.nikky.imperium.tower.calendar;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Consumer;

@Getter
@RequiredArgsConstructor
public class CalendarReward {

    private final int index;
    private final ItemStack texture;
    private final List<String> description;
    private final Consumer<Player> handler;

}
