package cupcake.nikky.imperium.tower.item.exact.modification.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.item.ItemRare;
import cupcake.nikky.imperium.tower.item.ItemType;
import cupcake.nikky.imperium.tower.item.exact.modification.ModificationBase;
import cupcake.nikky.imperium.tower.item.exact.modification.ModificationCursorHandler;
import cupcake.nikky.imperium.tower.item.exact.modification.drop.ModificationDropApplier;
import cupcake.nikky.imperium.tower.item.modifier.ModifierProvider;
import cupcake.nikky.imperium.tower.item.modifier.ModifierType;
import cupcake.nikky.imperium.tower.item.settings.exact.cursor.CursorResultType;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.Material;

import java.util.Arrays;

@Getter
@Accessors(fluent = true)
public class OrbOfHephaestus extends ModificationBase {

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

        if (targetItem.getRare() == ItemRare.COMMON) {
            Fantasy.clientProvider().notification(container.player(), SystemNotificationType.ERROR, "Редкость данного предмета", "не позволяет иметь модификаторов");
            container.cursorResultType(CursorResultType.CANCEL);
            return;
        }

        ModifierProvider.getInstance().clearModifiers(targetItem, ModifierType.EXTENDED, ModifierType.CRAFTED);
        ModifierProvider.getInstance().createRandomizedExtendedModifiers(targetItem);
        if (targetItem.getRare() == ItemRare.EXALTED && !targetItem.getModifierSettings().hasExaltedSpecificModifier())
            targetItem.setRare(ItemRare.EPIC);

        container.cursorResultType(CursorResultType.REMOVE_ONE_ITEM);
    };

    public OrbOfHephaestus() {
        super("Сущность Гефеста", Arrays.asList("Перековывает %sвозвышенный &7или ниже".formatted(ItemRare.EXALTED.getColor()), "предмет с новыми случайными модификаторами"),
                Material.STICK, 1006, -1, Arrays.asList(ItemType.WEAPON, ItemType.ARMOR, ItemType.TOOL, ItemType.EDGE_FRAGMENT), ModificationDropApplier.ORB_OF_HEPHAESTUS);
    }

}
