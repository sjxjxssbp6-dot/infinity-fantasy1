package cupcake.nikky.imperium.tower.item.exact.modification.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.item.ItemRare;
import cupcake.nikky.imperium.tower.item.ItemType;
import cupcake.nikky.imperium.tower.item.exact.modification.ModificationBase;
import cupcake.nikky.imperium.tower.item.exact.modification.ModificationCursorHandler;
import cupcake.nikky.imperium.tower.item.exact.modification.drop.ModificationDropApplier;
import cupcake.nikky.imperium.tower.item.modifier.ModifierPlace;
import cupcake.nikky.imperium.tower.item.settings.exact.cursor.CursorResultType;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;
import org.bukkit.Material;

import java.util.Arrays;

@Getter
@Accessors(fluent = true)
public class FormOfTyche extends ModificationBase {

    private final ModificationCursorHandler handler = (container, targetItem, cursorItem) -> {
        val baseModifiers = targetItem.getModifierSettings().findSpecificByPlace(ModifierPlace.BASE);

        if (baseModifiers.isEmpty()) {
            Fantasy.clientProvider().notification(container.player(), SystemNotificationType.ERROR, "Данный предмет не имеет", "базовых модификаторов");
            container.cursorResultType(CursorResultType.CANCEL);
            return;
        }

        baseModifiers.forEach(specificModifier -> specificModifier.setKey(specificModifier.getModifier().getRange().findByNumericTier(targetItem.getGeneralSettings().itemLevel()).generate()));

        container.cursorResultType(CursorResultType.REMOVE_ONE_ITEM);
    };

    public FormOfTyche() {
        super("Образ Тихэ", Arrays.asList("Случайно изменяет числовые значения", "базовых модификаторов предмета"),
                Material.STICK, 1025, -1, Arrays.asList(ItemType.WEAPON, ItemType.ARMOR), ModificationDropApplier.FORM_OF_TYCHE);
    }

}
