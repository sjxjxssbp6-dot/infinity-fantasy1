package cupcake.nikky.imperium.tower.battlepass.reward;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.aura.data.AuraObject;
import cupcake.nikky.imperium.tower.currency.CurrencyType;
import cupcake.nikky.imperium.tower.user.User;
import me.func.protocol.data.rare.DropRare;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

public class BattlePassAuraRewardExecutor extends BattlePassItemRewardExecutor {

    private final AuraObject auraObject;
    private final int duplicate;

    public BattlePassAuraRewardExecutor(@NotNull final ItemStack itemStack, @NotNull final AuraObject auraObject, final int duplicate) {
        super(itemStack, DropRare.INCREDIBLE);

        this.auraObject = auraObject;
        this.duplicate = duplicate;
    }

    @Override
    public void execute(@NotNull final Player player, @NotNull final RewardSettings rewardSettings) {
        var donateData = player.<User>getBungeePlayer().findDonateSettings().donateData();
        if (donateData.containsMetadata(auraObject.name())) {
            player.<User>getBungeePlayer().getCurrencyHandler().depositCurrency(CurrencyType.FIRM_PRIVATE, duplicate);
        } else donateData.applyMetadata(auraObject.name(), Instant.now().toEpochMilli());
        player.<User>getBungeePlayer().findBattlePassSettings().applyReward(rewardSettings.getActualRewardId());
    }

}
