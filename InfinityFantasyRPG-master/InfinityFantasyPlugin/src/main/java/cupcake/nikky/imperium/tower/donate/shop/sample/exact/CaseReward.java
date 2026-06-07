package cupcake.nikky.imperium.tower.donate.shop.sample.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.rng.WeightWrapper;
import cupcake.nikky.imperium.tower.rng.exact.loot.WeightItemWrapper;
import cupcake.nikky.imperium.tower.utility.NbtUtility;
import lombok.val;
import me.anfanik.steda.api.utility.TextUtility;
import me.func.mod.Anime;
import me.func.mod.conversation.data.LootDrop;
import org.bukkit.entity.Player;

public abstract class CaseReward<T extends WeightWrapper<?>> {

    public abstract void onReward(Player player, T reward);

    public abstract void visualize(Player player, CaseType caseType, T reward);

    public static class CaseItemReward extends CaseReward<WeightItemWrapper> {

        @Override
        public void onReward(Player player, WeightItemWrapper reward) {
            val item = reward.getWrapped().getItem().create();
            item.getDropSettings().droppable(false);

            val itemStack = item.createNew(reward.getAmount());
            player.getInventory().addItem(NbtUtility.clearSessionTags(itemStack));
        }

        @Override
        public void visualize(Player player, CaseType caseType, WeightItemWrapper reward) {
            val item = reward.getWrapped().getItem();
            val itemStack = reward.getWrapped().getItem().createNew();

            Anime.close(player);
            Anime.openLootBox(player, new LootDrop(itemStack,
                    TextUtility.colorize(item.getRare().getColor() + item.getName() + " &fX &e" + reward.getAmount()), item.getRare().getColoredName()));
            Fantasy.clientProvider().cursorNotify(player.getBungeePlayer(), TextUtility.colorize(itemStack.getItemMeta().getDisplayName() + " &fX &e" + reward.getAmount()), "");
            Fantasy.clientProvider().notification(player, "&aВы успешно приобрели: ", "&6" + caseType.getTitle());
        }
    }

}
