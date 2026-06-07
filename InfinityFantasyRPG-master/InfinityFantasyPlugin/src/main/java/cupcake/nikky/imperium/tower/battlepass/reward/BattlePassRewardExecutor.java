package cupcake.nikky.imperium.tower.battlepass.reward;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@Getter
@RequiredArgsConstructor
public abstract class BattlePassRewardExecutor {

    public abstract void execute(@NotNull final Player player, @NotNull final RewardSettings rewardSettings);

    public abstract ItemStack getIcon(Player player, final int rewardId);

}
