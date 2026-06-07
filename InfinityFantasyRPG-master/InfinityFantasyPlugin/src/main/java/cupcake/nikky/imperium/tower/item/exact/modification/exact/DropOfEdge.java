package cupcake.nikky.imperium.tower.item.exact.modification.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.item.ItemType;
import cupcake.nikky.imperium.tower.item.exact.modification.ModificationBase;
import cupcake.nikky.imperium.tower.item.exact.modification.ModificationCursorHandler;
import cupcake.nikky.imperium.tower.item.exact.modification.drop.ModificationDropApplier;
import cupcake.nikky.imperium.tower.item.settings.exact.cursor.CursorResultType;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.Material;

import java.util.Arrays;

@Getter
@Accessors(fluent = true)
public class DropOfEdge extends ModificationBase {

    public DropOfEdge() {
        super("Капля Грани", Arrays.asList(
                        "Создает полную копию снаряжения или аспекта",
                        "&cВ два раза понижает потенциал копии и оригинала",
                        "&cКопия не может быть скопирована повторно"),
                Material.SNOW_BALL, 1042, -1, Arrays.asList(ItemType.WEAPON, ItemType.ARMOR, ItemType.ASPECT), ModificationDropApplier.DROP_OF_EDGE);
    }

    @Override
    public ModificationCursorHandler handler() {
        return (container, targetItem, cursorItem) -> {
            if (container.cursorItemStack().getAmount() != 1) {
                Fantasy.clientProvider().notification(container.player(), SystemNotificationType.ERROR, "Превышено необходимое", "количество модифицирующих предметов");
                container.cursorResultType(CursorResultType.CANCEL);
                return;
            }

            if (targetItem.getGearSettings().copy()) {
                Fantasy.clientProvider().notification(container.player(), SystemNotificationType.ERROR, "Копию снаряжения нельзя", "копировать повторно");
                container.cursorResultType(CursorResultType.CANCEL);
                return;
            }

            if (container.targetItemStack().getAmount() > 1) {
                Fantasy.clientProvider().notification(container.player(), SystemNotificationType.ERROR, "Копировать можно только", "одно снаряжение за раз");
                container.cursorResultType(CursorResultType.CANCEL);
                return;
            }


            targetItem.getGearSettings().copy(true);
            targetItem.getGearSettings().potential(targetItem.getGearSettings().potential() / 2);
            container.cursorItemStack(targetItem.createNew());
            targetItem.getGearSettings().copy(false);
            container.cursorResultType(CursorResultType.REPLACE_CURSOR);
        };
    }

}
