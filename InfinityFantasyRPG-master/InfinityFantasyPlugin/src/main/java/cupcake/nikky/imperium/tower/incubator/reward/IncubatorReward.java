package cupcake.nikky.imperium.tower.incubator.reward;

import cupcake.nikky.imperium.tower.incubator.Incubators;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IncubatorReward {

    String rewardName();

    ItemStack icon(Incubators type);

    void reward(Player player);

}
