package cupcake.nikky.imperium.tower.item.exact.modification.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.item.ItemRare;
import cupcake.nikky.imperium.tower.item.ItemType;
import cupcake.nikky.imperium.tower.item.exact.modification.ModificationBase;
import cupcake.nikky.imperium.tower.item.exact.modification.ModificationCursorHandler;
import cupcake.nikky.imperium.tower.item.exact.modification.drop.ModificationDropApplier;
import cupcake.nikky.imperium.tower.item.modifier.ModifierType;
import cupcake.nikky.imperium.tower.item.modifier.SpecificModifier;
import cupcake.nikky.imperium.tower.item.settings.exact.cursor.CursorResultType;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;
import me.anfanik.steda.api.utility.MathUtility;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@Accessors(fluent = true)
public class AbsoluteDust extends ModificationBase {

    private final ModificationCursorHandler handler = (container, targetItem, cursorItem) -> {
        if (!targetItem.getRare().lowestThan(ItemRare.UNIQUE)) {
            Fantasy.clientProvider().notification(container.player(), SystemNotificationType.ERROR, "Необходима редкость предмета ниже, чем " + ItemRare.UNIQUE.getColoredName());
            container.cursorResultType(CursorResultType.CANCEL);
            return;
        }

        if (container.targetItemStack().getAmount() > 1) {
            Fantasy.clientProvider().notification(container.player(), SystemNotificationType.ERROR, "Изменять можно только", "один предмет за раз");
            container.cursorResultType(CursorResultType.CANCEL);
            return;
        }

        int modifierSize = targetItem.getModifierSettings().findSpecificByType(ModifierType.EXTENDED, ModifierType.CRAFTED).size();

        if (modifierSize == 0) {
            Fantasy.clientProvider().notification(container.player(), SystemNotificationType.ERROR, "Предмет не имеет модификаторов,", "которые можно удалить");
            container.cursorResultType(CursorResultType.CANCEL);
            return;
        }

        final List<SpecificModifier> options = targetItem.getModifierSettings().findSpecificByType(ModifierType.EXTENDED, ModifierType.CRAFTED);
        Collections.shuffle(options);

        val option = options.get(MathUtility.randomInRange(0, options.size() - 1));
        targetItem.getModifierSettings().removeSpecificModifier(option.getModifierId());

        if (targetItem.getRare() == ItemRare.EXALTED && !targetItem.getModifierSettings().hasExaltedSpecificModifier())
            targetItem.setRare(ItemRare.EPIC);

        container.cursorResultType(CursorResultType.REMOVE_ONE_ITEM);
    };

    public AbsoluteDust() {
        super("Абсолютная сфера", Arrays.asList("Удаляет случайный модификатор с %sвозвышенного".formatted(ItemRare.EXALTED.getColor()), "или ниже предмета"),
                Material.STICK, 1036, 4063179, Arrays.asList(ItemType.WEAPON, ItemType.ARMOR, ItemType.TOOL, ItemType.EDGE_FRAGMENT),
                ModificationDropApplier.ABSOLUTE_DUST);
    }

}
