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

import java.util.Arrays;
import java.util.Collections;

@Getter
@Accessors(fluent = true)
public class MixedReagentVerge extends ModificationBase {

    private final ModificationCursorHandler handler = (container, targetItem, cursorItem) -> {
        if (targetItem.getRare() != ItemRare.RARE) {
            Fantasy.clientProvider().notification(container.player(), SystemNotificationType.ERROR, "Необходимая редкость предмета: " + ItemRare.RARE.getColoredName());
            container.cursorResultType(CursorResultType.CANCEL);
            return;
        }

        if (container.targetItemStack().getAmount() > 1) {
            Fantasy.clientProvider().notification(container.player(), SystemNotificationType.ERROR, "Изменять можно только", "один предмет за раз");
            container.cursorResultType(CursorResultType.CANCEL);
            return;
        }

        int[][] options = {{0, 1}, {1, 0}, {1, 1}, {2, 1}, {1, 2}, {2, 2}};
        val randomized = MathUtility.randomInRange(0, 2);

        val option = options[randomized];

        targetItem.setRare(ItemRare.EPIC);

        ModifierProvider.getInstance().createExtendedModifiers(
                targetItem,
                new ModifierCreatorSettings()
                        .registerPlace(ModifierPlace.PREFIX, option[0])
                        .registerPlace(ModifierPlace.SUFFIX, option[1]));

        container.cursorResultType(CursorResultType.REMOVE_ONE_ITEM);
    };

    public MixedReagentVerge() {
        super("Смешанный реагент Грани", Collections.singletonList("Улучшает %sредкий &7предмет до %sэпического".formatted(ItemRare.RARE.getColor(), ItemRare.EPIC.getColor())),
                Material.STICK, 1034, -1, Arrays.asList(ItemType.WEAPON, ItemType.ARMOR, ItemType.EDGE_FRAGMENT), ModificationDropApplier.MIXED_REAGENT_VERGE);
    }

}
