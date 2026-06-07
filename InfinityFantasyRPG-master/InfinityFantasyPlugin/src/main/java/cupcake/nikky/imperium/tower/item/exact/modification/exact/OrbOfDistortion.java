package cupcake.nikky.imperium.tower.item.exact.modification.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.item.ItemRare;
import cupcake.nikky.imperium.tower.item.ItemType;
import cupcake.nikky.imperium.tower.item.exact.modification.ModificationBase;
import cupcake.nikky.imperium.tower.item.exact.modification.ModificationCursorHandler;
import cupcake.nikky.imperium.tower.item.exact.modification.drop.ModificationDropApplier;
import cupcake.nikky.imperium.tower.item.modifier.ModifierCreatorSettings;
import cupcake.nikky.imperium.tower.item.modifier.ModifierPlace;
import cupcake.nikky.imperium.tower.item.modifier.ModifierProvider;
import cupcake.nikky.imperium.tower.item.settings.exact.cursor.CursorResultType;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;
import me.anfanik.steda.api.utility.MathUtility;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Accessors(fluent = true)
public class OrbOfDistortion extends ModificationBase {

    private final ModificationCursorHandler handler = (container, targetItem, cursorItem) -> {
        if (!targetItem.getRare().lowestThan(ItemRare.UNIQUE)) {
            Fantasy.clientProvider().notification(container.player(), SystemNotificationType.ERROR, "Необходима редкость предмета", "ниже, чем " + ItemRare.UNIQUE.getColoredName());
            container.cursorResultType(CursorResultType.CANCEL);
            return;
        }

        if (container.targetItemStack().getAmount() > 1) {
            Fantasy.clientProvider().notification(container.player(), SystemNotificationType.ERROR, "Изменять можно только", "один предмет за раз");
            container.cursorResultType(CursorResultType.CANCEL);
            return;
        }

        List<Integer[]> options = new ArrayList<>();

        val prefixAvailable = targetItem.getRare().getModifierSize() - targetItem.getModifierSettings().findSpecificByPlace(ModifierPlace.PREFIX).size();
        val suffixAvailable = targetItem.getRare().getModifierSize() - targetItem.getModifierSettings().findSpecificByPlace(ModifierPlace.SUFFIX).size();

        if (prefixAvailable <= 0 && suffixAvailable <= 0) {
            Fantasy.clientProvider().notification(container.player(), SystemNotificationType.ERROR, "Предмет не может иметь", "больше модификаторов");
            container.cursorResultType(CursorResultType.CANCEL);
            return;
        }

        if (prefixAvailable > 0) options.add(new Integer[]{1, 0});
        if (suffixAvailable > 0) options.add(new Integer[]{0, 1});

        val option = options.get(MathUtility.randomInRange(0, options.size() - 1));

        ModifierProvider.getInstance().createExtendedModifiers(targetItem,
                new ModifierCreatorSettings()
                        .registerPlace(ModifierPlace.PREFIX, option[0])
                        .registerPlace(ModifierPlace.SUFFIX, option[1]));

        container.cursorResultType(CursorResultType.REMOVE_ONE_ITEM);
    };

    public OrbOfDistortion() {
        super("Сфера искажения", Arrays.asList("Добавляет случайный модификатор %sвозвышенному".formatted(ItemRare.EXALTED.getColor()), "или ниже предмету"),
                Material.STICK, 1023, -1, Arrays.asList(ItemType.WEAPON, ItemType.ARMOR, ItemType.TOOL, ItemType.EDGE_FRAGMENT), ModificationDropApplier.ORB_OF_DISTORTION);
    }

}
