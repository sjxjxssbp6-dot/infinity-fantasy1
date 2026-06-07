package cupcake.nikky.imperium.tower.quest.reward;

import org.bukkit.entity.Player;

public interface QuestReward {

    void onReward(Player player);

    boolean canGive(Player player);

}
