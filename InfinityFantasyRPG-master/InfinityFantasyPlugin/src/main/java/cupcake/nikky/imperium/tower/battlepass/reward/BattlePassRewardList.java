package cupcake.nikky.imperium.tower.battlepass.reward;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class BattlePassRewardList {

    private final int page;
    private final boolean advanced;
    private final BattlePassRewardExecutor[] rewardExecutor = new BattlePassRewardExecutor[10];
    private int lastIndex;

    public BattlePassRewardExecutor getRewardExecutor(final int index) {
        return rewardExecutor[index];
    }

    public BattlePassRewardList applyNext(@NotNull final BattlePassRewardExecutor rewardExecutor) {
        this.rewardExecutor[lastIndex++] = rewardExecutor;
        return this;
    }

    public List<ItemStack> toIcons() {
        return Arrays.stream(rewardExecutor).map(rewardExecutor1 -> rewardExecutor1.getIcon(null, 0)).toList();
    }

    public List<ItemStack> toIcons(@NotNull final Player player) {
        final List<ItemStack> result = new ArrayList<>();
        for (int i = 0; i < rewardExecutor.length; i++) {
            var currentRewardExecutor = rewardExecutor[i];
            var id = i + (page - 1) * 10;
            result.add(currentRewardExecutor.getIcon(player, id * (advanced ? 100 : 1)));
        }
        return result;
    }

}
