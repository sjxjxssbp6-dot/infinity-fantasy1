package cupcake.nikky.imperium.tower.battlepass.reward;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.protocol.PacketRegistry;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.utility.PermissionUtility;
import me.func.protocol.data.rare.DropRare;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pw.lach.p13n.network.tower.GiveModelToUserPackage;

import java.util.UUID;

public class BattlePassPersonalizationRewardExecutor extends BattlePassItemRewardExecutor {

    private final UUID personalizationUUID;

    public BattlePassPersonalizationRewardExecutor(@NotNull final ItemStack itemStack, @NotNull final UUID personalizationUUID) {
        super(itemStack, DropRare.INCREDIBLE);
        this.personalizationUUID = personalizationUUID;
    }

    @Override
    public void execute(@NotNull Player player, @NotNull final RewardSettings rewardSettings) {
        if (Fantasy.gameServer().isTest() && !PermissionUtility.isOperator(player) && !PermissionUtility.isNiceTester(player)) return;

        player.<User>getBungeePlayer().findBattlePassSettings().applyReward(rewardSettings.getActualRewardId());
        PacketRegistry.writeAndAwaitResponse(new GiveModelToUserPackage(player.getUniqueId(), personalizationUUID)).whenComplete((giveModelToUserPackage, throwable) -> {
            if (throwable != null) {
                Bukkit.getLogger().warning("Ошибка при выдаче персонализации: " + throwable.getMessage());
                Bukkit.getLogger().warning("Игрок: " + player.getName() + ", uuid: " + player.getUniqueId());
                player.<User>getBungeePlayer().findBattlePassSettings().invalidateReward(rewardSettings.getActualRewardId());
            }
        });
    }
}
