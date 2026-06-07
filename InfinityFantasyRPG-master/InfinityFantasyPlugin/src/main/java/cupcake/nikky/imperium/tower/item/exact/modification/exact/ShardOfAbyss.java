package cupcake.nikky.imperium.tower.item.exact.modification.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.item.ItemRare;
import cupcake.nikky.imperium.tower.item.ItemType;
import cupcake.nikky.imperium.tower.item.exact.modification.ModificationBase;
import cupcake.nikky.imperium.tower.item.exact.modification.ModificationCursorHandler;
import cupcake.nikky.imperium.tower.item.exact.modification.drop.ModificationDropApplier;
import cupcake.nikky.imperium.tower.item.modifier.ModifierPlace;
import cupcake.nikky.imperium.tower.item.modifier.ModifierType;
import cupcake.nikky.imperium.tower.item.settings.exact.cursor.CursorResultType;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;
import me.anfanik.steda.api.utility.MathUtility;
import org.bukkit.Material;

import java.util.Arrays;

@Getter
@Accessors(fluent = true)
public class ShardOfAbyss extends ModificationBase {

    private final ModificationCursorHandler handler = (container, targetItem, cursorItem) -> {
        if (!targetItem.getRare().lowestThan(ItemRare.UNIQUE)) {
            Fantasy.clientProvider().notification(container.player(), SystemNotificationType.ERROR, "Необходима редкость предмета", "ниже, чем " + ItemRare.UNIQUE.getColoredName());
            container.cursorResultType(CursorResultType.CANCEL);
            return;
        }

        if (!targetItem.getModifierSettings().findSpecificByType(ModifierType.DISTORTED).isEmpty()) {
            Fantasy.clientProvider().notification(container.player(), SystemNotificationType.ERROR, "Данный предмет уже имеет", "искаженный модификатор");
            container.cursorResultType(CursorResultType.CANCEL);
            return;
        }

        val modifiers = targetItem.getModifierSettings().findSpecificByPlace(ModifierPlace.PREFIX, ModifierPlace.SUFFIX);

        if (modifiers.size() < 4) {
            Fantasy.clientProvider().notification(container.player(), SystemNotificationType.ERROR, "Данный предмет имеет", "менее 4х модификаторов");
            container.cursorResultType(CursorResultType.CANCEL);
            return;
        }

        val option = MathUtility.randomInRange(0, modifiers.size() - 1);
        val modifier = modifiers.get(option);
        modifier.setModifierType(ModifierType.DISTORTED);

        container.cursorResultType(CursorResultType.REMOVE_ONE_ITEM);
    };

    public ShardOfAbyss() {
        super("Осколок Бездны", Arrays.asList(
                        "Искажает случайный модификатор на %sвозвышенном или ниже &7предмете".formatted(ItemRare.EXALTED.getColor()),
                        "Не применяется, если на предмете присутствует",
                        "менее 4х модификаторов",
                        "Предмет может иметь только один искаженный модификатор",
                        "Искаженный модификатор невозможно удалить с предмета"),
                Material.STICK, 1021, -1, Arrays.asList(ItemType.WEAPON, ItemType.ARMOR), ModificationDropApplier.SHARD_OF_ABYSS);
    }

}
