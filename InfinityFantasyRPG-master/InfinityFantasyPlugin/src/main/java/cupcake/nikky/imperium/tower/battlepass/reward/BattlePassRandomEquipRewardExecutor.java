package cupcake.nikky.imperium.tower.battlepass.reward;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.chat.ChatType;
import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.ItemBuildProcessor;
import cupcake.nikky.imperium.tower.item.ItemObject;
import cupcake.nikky.imperium.tower.item.ItemRare;
import cupcake.nikky.imperium.tower.item.modifier.ModifierProvider;
import cupcake.nikky.imperium.tower.item.modifier.ModifierType;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.utility.ImpInventoryUtility;
import cupcake.nikky.imperium.tower.utility.NbtUtility;
import cupcake.nikky.imperium.tower.utility.PotentialUtility;
import lombok.val;
import me.anfanik.steda.api.utility.ItemBuilder;
import me.anfanik.steda.api.utility.MathUtility;
import me.func.protocol.data.rare.DropRare;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BattlePassRandomEquipRewardExecutor extends BattlePassRewardExecutor {

    private final ItemStack icon;
    private final ItemBuildProcessor rewardItem;
    private final DropRare dropRare;

    public BattlePassRandomEquipRewardExecutor(@NotNull final ItemObject rewardItem, final int level, boolean exaltedModifier, int defaultDirectionModifiers) {
        this.icon = new ItemStack(ItemBuilder.fromItem(rewardItem.getItem().createNew())
                .setName("&aСлучайный %s &6%d &aуровня".formatted(rewardItem.getItem().getName(), level))
                .setLore("")
                .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .addItemFlag(ItemFlag.HIDE_PLACED_ON)
                .build());
        this.dropRare = DropRare.COMMON;
        this.rewardItem = ItemBuildProcessor.of(rewardItem.getItem()).apply(imperiumItem -> {

            ModifierProvider.getInstance().randomizeItemRarity(imperiumItem, level,
                    exaltedModifier ? Collections.singletonList(ItemRare.EXALTED) : Arrays.asList(ItemRare.COMMON, ItemRare.RARE, ItemRare.EPIC));
            ModifierProvider.getInstance().createRandomizedBaseModifiers(imperiumItem);
            ModifierProvider.getInstance().createRandomizedExtendedModifiers(imperiumItem);
            imperiumItem.getGearSettings().potential(PotentialUtility.processPotential(level));

            if (exaltedModifier) {
                var modifiers = imperiumItem.getModifierSettings().findSpecificByType(ModifierType.EXTENDED).stream()
                        .filter(specificModifier -> specificModifier.getModifier().getRange().sizeWithoutExalted() != specificModifier.getModifier().getRange().size())
                        .toList();
                val random = MathUtility.randomInRange(0, modifiers.size());
                var modifier = modifiers.get(random);
                imperiumItem.getModifierSettings().findSpecificById(modifier.getModifierId()).setKey(modifier.getModifier().getRange().findExaltedTier().generate());
            }

            if (defaultDirectionModifiers > 0) {
                for (int i = 0; i < defaultDirectionModifiers; i++)
                    ModifierProvider.getInstance().createRandomizedDirectionModifiers(imperiumItem, false);
            } else ModifierProvider.getInstance().createRandomizedDirectionModifiers(imperiumItem, true);
        });
    }

    public BattlePassRandomEquipRewardExecutor(@NotNull final ItemObject rewardItem, final int level) {
        this(rewardItem, level, false, 0);
    }

    @Override
    public void execute(@NotNull final Player player, @NotNull final RewardSettings rewardSettings) {
        if (ImpInventoryUtility.addItem(player, rewardItem.catchItem().createNew())) {
            Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&fВ Вашем инвентаре нет места. Предмет был выброшен рядом с Вами.");
        }

        player.<User>getBungeePlayer().findBattlePassSettings().applyReward(rewardSettings.getActualRewardId());
    }

    @Override
    public ItemStack getIcon(Player player, final int rewardId) {
        return NbtUtility.applyTagCustomize(new ItemStack(icon), nbtTag -> {
            nbtTag.setInt("rare", dropRare.ordinal());
            if (player != null) {
                var battlePassSettings = player.<User>getBungeePlayer().findBattlePassSettings();
                nbtTag.setBoolean("taken", battlePassSettings.isRewardApplied(rewardId));
            }
        });
    }

}
