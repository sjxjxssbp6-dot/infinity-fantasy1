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
import cupcake.nikky.imperium.tower.rng.Weight;
import cupcake.nikky.imperium.tower.rng.WeightPool;
import cupcake.nikky.imperium.tower.rng.WeightWrapper;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;
import me.anfanik.steda.api.utility.MathUtility;
import org.bukkit.Material;

import java.util.Arrays;

@Getter
@Accessors(fluent = true)
public class EssenceOfTyche extends ModificationBase {

    private final ModificationCursorHandler handler = (container, targetItem, cursorItem) -> {
        if (container.targetItemStack().getAmount() > 1) {
            Fantasy.clientProvider().notification(container.player(), SystemNotificationType.ERROR, "Изменять можно только", "один предмет за раз");
            container.cursorResultType(CursorResultType.CANCEL);
            return;
        }

        val modifiers = targetItem.getModifierSettings().findSpecificByType(ModifierType.EXTENDED, ModifierType.CRAFTED);

        if (modifiers.isEmpty()) {
            Fantasy.clientProvider().notification(container.player(), SystemNotificationType.ERROR, "Данный предмет не имеет", "расширенных модификаторов");
            container.cursorResultType(CursorResultType.CANCEL);
            return;
        }

        val weightPool = new WeightPool<WeightWrapper<SpecificModifier>>(modifiers.size());

        modifiers.forEach(specificModifier -> weightPool.apply(new Weight<>(new WeightWrapper<>(specificModifier, 100, false)).onSuccess(winner -> 0)));
        weightPool.generatePool().forEach(weight -> {
            val specificModifier = weight.weightWrapper().getWrapped();
            specificModifier.setKey(specificModifier.getModifier().getRange().findByNumericTier(specificModifier.findTier()).generate());
        });

        container.cursorResultType(CursorResultType.REMOVE_ONE_ITEM);
    };

    public EssenceOfTyche() {
        super("Сущность Тихэ", Arrays.asList("Случайно изменяет числовые значения", "расширенных модификаторов предмета"),
                Material.STICK, 1007, -1, Arrays.asList(ItemType.WEAPON, ItemType.ARMOR, ItemType.TOOL, ItemType.EDGE_FRAGMENT), ModificationDropApplier.ESSENCE_OF_TYCHE);
    }

}
