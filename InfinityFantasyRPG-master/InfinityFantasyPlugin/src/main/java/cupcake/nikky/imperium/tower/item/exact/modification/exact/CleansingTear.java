package cupcake.nikky.imperium.tower.item.exact.modification.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.item.ItemRare;
import cupcake.nikky.imperium.tower.item.ItemType;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectItem;
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
public class CleansingTear extends ModificationBase {

    public CleansingTear() {
        super("Очищающая слеза", Arrays.asList("Достает аспект из снаряжения"),
                Material.GHAST_TEAR, 0, -1, Arrays.asList(ItemType.WEAPON, ItemType.ARMOR), ModificationDropApplier.CLEANSING_TEAR);
    }

    @Override
    public ModificationCursorHandler handler() {
        return (container, targetItem, cursorItem) -> {
            if (container.cursorItemStack().getAmount() > 1) {
                Fantasy.clientProvider().notification(container.player(), SystemNotificationType.ERROR, "Применять можно только", "один материал за раз");
                container.cursorResultType(CursorResultType.CANCEL);
                return;
            }

            var aspect = targetItem.getAspectSettings().aspect();

            if (aspect == null) {
                Fantasy.clientProvider().notification(container.player(), SystemNotificationType.ERROR, "У данного снаряжения нет аспекта");
                container.cursorResultType(CursorResultType.CANCEL);
                return;
            }

            var corrupt = targetItem.getAspectSettings().aspectCorrupt();
            var copy = targetItem.getAspectSettings().aspectCopy();
            var exaltedValue = targetItem.getAspectSettings().exaltedValue();
            var aspectItem = new AspectItem(aspect);

            aspectItem.getGeneralSettings().itemLevel(aspectItem.getGeneralSettings().itemLevel() + corrupt);
            aspectItem.getGearSettings().corrupted(corrupt);
            aspectItem.getGearSettings().copy(copy);
            aspectItem.getAspectSettings().exaltedValue(exaltedValue);
            container.cursorItemStack(aspectItem.createNew());

            targetItem.getAspectSettings().aspect(null);
            targetItem.getAspectSettings().aspectCorrupt(0);
            targetItem.getAspectSettings().aspectCopy(false);
            targetItem.getAspectSettings().exaltedValue(0);

            container.cursorResultType(CursorResultType.REPLACE_CURSOR);
        };
    }

}
