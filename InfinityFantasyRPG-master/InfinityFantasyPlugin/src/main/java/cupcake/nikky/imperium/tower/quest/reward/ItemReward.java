package cupcake.nikky.imperium.tower.quest.reward;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.chat.ChatType;
import cupcake.nikky.imperium.tower.item.ItemBuildProcessor;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.utility.ImpInventoryUtility;
import cupcake.nikky.imperium.tower.utility.NbtUtility;
import lombok.Getter;
import lombok.val;
import me.anfanik.steda.api.utility.TextUtility;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ItemReward implements QuestReward {

    @Getter private final ItemBuildProcessor buildProcessor;
    @Getter private final int amount;

    public ItemReward(@NotNull final ItemBuildProcessor buildProcessor, final int amount) {
        this.buildProcessor = buildProcessor;
        this.amount = amount;
    }

    public ItemReward(@NotNull final String uniqueItemId, final int amount) {
        this.buildProcessor = ItemBuildProcessor.of(uniqueItemId);
        this.amount = amount;
    }

    @Override
    public void onReward(@NotNull final Player player) {
        val item = buildProcessor.catchItem();
        val user = player.<User>getBungeePlayer();

        if (ImpInventoryUtility.canMoveToInventory(player.getInventory(), item, amount)) {
            player.getInventory().addItem(NbtUtility.clearSessionTags(item.createNew(getAmount())));

            Fantasy.clientProvider().cursorNotify(user, TextUtility.colorize(item.getRare().getColor() + item.getImperiumType().getTypeColor() + item.getName() + " &fX " + getAmount()), "");
        } else {
            val stack = item.createNew(getAmount(), user == null ? null : user.getSpigot());
            Fantasy.runLater(() -> Fantasy.bukkitProvider().spawnItem(player.getLocation(), player.getUniqueId(), stack));
            Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&cВаш инвентарь полон, награда выпадет рядом с Вами.");
        }
    }

    @Override
    public boolean canGive(@NotNull final Player player) {
        val result = ImpInventoryUtility.canMoveToInventory(player.getInventory(), buildProcessor.catchItem(), amount);
        if (!result) Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&cВаш инвентарь полон, Вы не можете получить награду.");
        return result;
    }
}
