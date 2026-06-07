package cupcake.nikky.imperium.tower.battlepass.reward;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.chat.ChatType;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.utility.ImpInventoryUtility;
import cupcake.nikky.imperium.tower.utility.NbtUtility;
import me.func.protocol.data.rare.DropRare;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BattlePassItemRewardExecutor extends BattlePassRewardExecutor {

    private final ItemStack itemStackReward;
    private final DropRare dropRare;

    public BattlePassItemRewardExecutor(@NotNull final ItemStack itemStack, @NotNull final DropRare dropRare) {
        this.itemStackReward = new ItemStack(itemStack);
        this.dropRare = dropRare;
    }

    public BattlePassItemRewardExecutor(@NotNull final ItemStack itemStack) {
        this.itemStackReward = new ItemStack(itemStack);
        this.dropRare = DropRare.COMMON;
    }

    @Override
    public void execute(@NotNull final Player player, @NotNull final RewardSettings rewardSettings) {
        if (ImpInventoryUtility.addItem(player, itemStackReward)) {
            Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&fВ Вашем инвентаре нет места. Предмет был выброшен рядом с Вами.");
        }

        player.<User>getBungeePlayer().findBattlePassSettings().applyReward(rewardSettings.getActualRewardId());
    }

    @Override
    public ItemStack getIcon(Player player, final int rewardId) {
        return NbtUtility.applyTagCustomize(new ItemStack(itemStackReward), nbtTag -> {
            nbtTag.setInt("rare", dropRare.ordinal());
            if (player != null) {
                var battlePassSettings = player.<User>getBungeePlayer().findBattlePassSettings();
                nbtTag.setBoolean("taken", battlePassSettings.isRewardApplied(rewardId));
            }
        });
    }

}
