package cupcake.nikky.imperium.tower.item.exact.modification.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.item.ItemType;
import cupcake.nikky.imperium.tower.item.exact.modification.ModificationBase;
import cupcake.nikky.imperium.tower.item.exact.modification.ModificationCursorHandler;
import cupcake.nikky.imperium.tower.item.exact.modification.drop.ModificationDropApplier;
import cupcake.nikky.imperium.tower.item.modifier.ModifierPlace;
import cupcake.nikky.imperium.tower.item.modifier.ModifierProvider;
import cupcake.nikky.imperium.tower.item.modifier.ModifierType;
import cupcake.nikky.imperium.tower.item.settings.exact.cursor.CursorResultType;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.Material;

import java.util.Arrays;

@Getter
@Accessors(fluent = true)
public class OrbOfPandora extends ModificationBase {

    private final ModificationCursorHandler handler = (container, targetItem, cursorItem) -> {
        if (container.targetItemStack().getAmount() > 1) {
            Fantasy.clientProvider().notification(container.player(), SystemNotificationType.ERROR, "Изменять можно только", "один предмет за раз");
            container.cursorResultType(CursorResultType.CANCEL);
            return;
        }

        if (targetItem.getModifierSettings().findSpecificByPlace(ModifierPlace.DIRECTION).size() > 1) {
            Fantasy.clientProvider().notification(container.player(), SystemNotificationType.ERROR, "Нельзя использовать на снаряжении с двумя", "и более модификаторами направления");
            container.cursorResultType(CursorResultType.CANCEL);
            return;
        }

        if (targetItem.getModifierSettings().findPossibleByPlace(ModifierPlace.DIRECTION).isEmpty()) {
            Fantasy.clientProvider().notification(container.player(), SystemNotificationType.ERROR, "У данного снаряжения отсутствуют", "возможные модификаторы направления");
            container.cursorResultType(CursorResultType.CANCEL);
            return;
        }

        ModifierProvider.getInstance().clearModifiers(targetItem, ModifierType.DIRECTION);
        ModifierProvider.getInstance().createRandomizedDirectionModifiers(targetItem, false);

        container.cursorResultType(CursorResultType.REMOVE_ONE_ITEM);
    };

    public OrbOfPandora() {
        super("Сфера Пандоры",
                Arrays.asList(
                        "Добавляет случайный модификатор направления",
                        "Заменяет существующий модификатор направления на случайный",
                        "Нельзя использовать на снаряжении с двумя",
                        "и более модификаторами направления"
                ),
                Material.STICK, 1024, -1, Arrays.asList(ItemType.WEAPON, ItemType.ARMOR), ModificationDropApplier.ORB_OF_PANDORA);
    }

}
