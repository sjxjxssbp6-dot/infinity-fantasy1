package cupcake.nikky.imperium.tower.item.exact.modification.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.item.ItemRare;
import cupcake.nikky.imperium.tower.item.ItemType;
import cupcake.nikky.imperium.tower.item.exact.modification.ModificationBase;
import cupcake.nikky.imperium.tower.item.exact.modification.ModificationCursorHandler;
import cupcake.nikky.imperium.tower.item.exact.modification.drop.ModificationDropApplier;
import cupcake.nikky.imperium.tower.item.exact.modification.rule.CorruptResult;
import cupcake.nikky.imperium.tower.item.exact.modification.rule.StarRule;
import cupcake.nikky.imperium.tower.item.settings.exact.cursor.CursorResultType;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;
import org.bukkit.Material;

import java.util.Arrays;

@Getter
@Accessors(fluent = true)
public class StarOfCorrupt extends ModificationBase {

    private final ModificationCursorHandler handler = (container, targetItem, cursorItem) -> {
        if (container.targetItemStack().getAmount() > 1) {
            Fantasy.clientProvider().notification(container.player(), SystemNotificationType.ERROR, "Осквернять можно только", "один предмет за раз");
            container.cursorResultType(CursorResultType.CANCEL);
            return;
        }

        if ((targetItem.getImperiumType() == ItemType.WEAPON || targetItem.getImperiumType() == ItemType.ARMOR || targetItem.getImperiumType() == ItemType.ASPECT) && targetItem.getGearSettings().potential() <= 0) {
            Fantasy.clientProvider().notification(container.player(), SystemNotificationType.ERROR, "У предмета отсутствует потенциал");
            container.cursorResultType(CursorResultType.CANCEL);
            return;
        }

        val rule = StarRule.rule(targetItem);
        val result = rule.applyCorrupt(targetItem);
        Fantasy.clientProvider().notification(container.player(), result.notificationType(), result.message());

        if (result == CorruptResult.CORRUPT_LIMIT) {
            container.cursorResultType(CursorResultType.CANCEL);
        } else {
            container.cursorResultType(CursorResultType.REMOVE_ONE_ITEM);
        }
    };

    public StarOfCorrupt() {
        super("Звезда осквернения",
                Arrays.asList(
                        "Оскверняет снаряжение, аспект,",
                        "осколок Грани или осколок прорыва.",
                        "Осколок прорыва можно осквернить до +300",
                        "Осколок Грани можно осквернить до +50",
                        "Снаряжение можно осквернить до +10",
                        "Аспект можно осквернить до +5",
                        "Не может осквернить снаряжение или аспект",
                        "При отсутствии потенциала",
                        "&cПонижает потенциал на единицу"
                ),
                Material.NETHER_STAR, 0, -1, Arrays.asList(ItemType.WEAPON, ItemType.ARMOR, ItemType.ASPECT, ItemType.EDGE_FRAGMENT, ItemType.SHARD_BREAKTHROUGH), ModificationDropApplier.STAR_OF_CORRUPT);
    }

}
