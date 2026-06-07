package cupcake.nikky.imperium.tower.box.reward;

import cupcake.nikky.imperium.tower.rng.Weight;
import cupcake.nikky.imperium.tower.rng.exact.box.CaseWrapper;
import cupcake.nikky.imperium.tower.rng.exact.box.WeightCaseItemWrapper;
import cupcake.nikky.imperium.tower.utility.NbtUtility;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class CaseItemReward implements CaseReward {

    private final Weight<CaseWrapper<?>> weight;

    @Override
    public void reward(Player player) {
        var wrapper = weight.weightWrapper();

        if (wrapper instanceof WeightCaseItemWrapper weightCaseItemWrapper) {
            var item = weightCaseItemWrapper.getWrapped().getItem().create();
            item.getDropSettings().droppable(false);
            player.getInventory().addItem(NbtUtility.clearSessionTags(item.createNew(weightCaseItemWrapper.getAmount())));
        }
    }

}
