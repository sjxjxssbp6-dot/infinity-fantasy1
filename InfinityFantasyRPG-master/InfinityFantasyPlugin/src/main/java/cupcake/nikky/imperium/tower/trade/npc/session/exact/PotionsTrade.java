package cupcake.nikky.imperium.tower.trade.npc.session.exact;

import cupcake.nikky.imperium.tower.item.ItemObject;
import cupcake.nikky.imperium.tower.item.ItemType;
import cupcake.nikky.imperium.tower.item.exact.potion.PotionItem;
import cupcake.nikky.imperium.tower.protocol.utility.NumberUtility;
import cupcake.nikky.imperium.tower.trade.npc.session.TradeContainer;
import cupcake.nikky.imperium.tower.user.User;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Getter
@Accessors(fluent = true)
public class PotionsTrade implements TradeGenerator {

    private final String title = "Усиления";

    @Override
    public List<TradeContainer> generate(@NotNull User user) {
        val userLevel = user.getLevel();
        if (userLevel >= 4) {
            final List<TradeContainer> selection = new ArrayList<>();

            for (ItemObject value : ItemObject.values()) {
                if (value.getItem().getImperiumType() != ItemType.POTION) continue;
                val grades = new int[] {4, 19, 44};
                for (int grade : grades) {
                    if (userLevel >= grade) {
                        PotionItem item = value.getItem().apply(PotionItem.class);
                        if (!item.isExistPotion(grade)) continue;

                        item = item.create().apply(PotionItem.class);
                        item.getGeneralSettings().itemLevel(grade);

                        val startPrice = grade == 4 ? 40 : 60;
                        selection.add(TradeContainer.of(NumberUtility.parseInteger(value.getUniqueId()) + grade * 10, item, startPrice * grade));
                    }
                }
            }

            selection.sort(Comparator.comparingInt(tradeContainer -> NumberUtility.parseInteger(tradeContainer.getUniqueId())));

            return selection;
        } else return Collections.emptyList();
    }

}
