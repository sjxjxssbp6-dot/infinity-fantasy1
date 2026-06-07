package cupcake.nikky.imperium.tower.item.exact.modification.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.item.ItemRare;
import cupcake.nikky.imperium.tower.item.ItemType;
import cupcake.nikky.imperium.tower.item.exact.modification.ModificationBase;
import cupcake.nikky.imperium.tower.item.exact.modification.ModificationCursorHandler;
import cupcake.nikky.imperium.tower.item.exact.modification.drop.ModificationDropApplier;
import cupcake.nikky.imperium.tower.item.modifier.ModifierType;
import cupcake.nikky.imperium.tower.item.settings.exact.cursor.CursorResultType;
import cupcake.nikky.imperium.tower.level.LevelStorage;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;
import me.anfanik.steda.api.utility.MathUtility;
import org.bukkit.Material;

import java.util.Arrays;

@Getter
@Accessors(fluent = true)
public class ExaltedOrb extends ModificationBase {

    private final ModificationCursorHandler handler = (container, targetItem, cursorItem) -> {
        if (targetItem.getImperiumType() == ItemType.ASPECT) {
            if (targetItem.getGearSettings().corrupted() != 20) {
                Fantasy.clientProvider().notification(container.player(), SystemNotificationType.ERROR, "Этап осквернения данного аспекта", "не позволяет быть возвышенным");
                container.cursorResultType(CursorResultType.CANCEL);
                return;
            }

            if (targetItem.getAspectSettings().exaltedValue() != 0) {
                Fantasy.clientProvider().notification(container.player(), SystemNotificationType.ERROR, "Аспект уже возвышен");
                container.cursorResultType(CursorResultType.CANCEL);
                return;
            }

            targetItem.getAspectSettings().exaltedValue(MathUtility.randomInRange(0, 1) == 0 ? -1 : 1);
            targetItem.setRare(ItemRare.EXALTED);
            container.cursorResultType(CursorResultType.REMOVE_ONE_ITEM);
            Fantasy.clientProvider().notification(container.player(), SystemNotificationType.FINE, "Вы успешно возвысили аспект!");
            return;
        }

        if (targetItem.getGeneralSettings().itemLevel() <= 60) {
            Fantasy.clientProvider().notification(container.player(), SystemNotificationType.ERROR, "Для возвышения необходимо", "снаряжение 60+ уровня");
            container.cursorResultType(CursorResultType.CANCEL);
            return;
        }

        if (targetItem.getModifierSettings().hasExaltedSpecificModifier()) {
            Fantasy.clientProvider().notification(container.player(), SystemNotificationType.ERROR, "Снаряжение уже имеет", "возвышенный модификатор");
            container.cursorResultType(CursorResultType.CANCEL);
            return;
        }

        var modifiers = targetItem.getModifierSettings().findSpecificByType(ModifierType.EXTENDED).stream()
                .filter(specificModifier -> specificModifier.getModifier().getRange().sizeWithoutExalted() != specificModifier.getModifier().getRange().size())
                .toList();

        if (modifiers.isEmpty()) {
            Fantasy.clientProvider().notification(container.player(), SystemNotificationType.ERROR, "Снаряжение не имеет модификаторов", "которые можно возвысить");
            container.cursorResultType(CursorResultType.CANCEL);
            return;
        }

        if (modifiers.size() < 4) {
            Fantasy.clientProvider().notification(container.player(), SystemNotificationType.ERROR, "Снаряжение имеет меньше 4 модификаторов");
            container.cursorResultType(CursorResultType.CANCEL);
            return;
        }

        val random = MathUtility.randomInRange(0, modifiers.size() - 1);
        var modifier = modifiers.get(random);
        if (targetItem.getRare().lowestThan(ItemRare.UNIQUE)) targetItem.setRare(ItemRare.EXALTED);
        targetItem.getModifierSettings().findSpecificById(modifier.getModifierId()).setKey(modifier.getModifier().getRange().findExaltedTier().generate());
        Fantasy.clientProvider().notification(container.player(), SystemNotificationType.FINE, "Вы успешно возвысили снаряжение!");
        container.cursorResultType(CursorResultType.REMOVE_ONE_ITEM);
    };

    public ExaltedOrb() {
        super("Сфера возвышения",
                Arrays.asList(
                        "Возвышает случайный расширенный модификатор снаряжения,",
                        "уровень которого больше %d".formatted(60),
                        "Не может возвысить искаженный или созданный модификатор",
                        "Снаряжение может иметь только один возвышенный модификатор",
                        "Снаряжение должно иметь минимум 4 расширенных модификатора",
                        "Возвышенный модификатор получает нулевой уровень",
                        " ",
                        "Возвышает аспект 20 уровня, добавляя или отнимая 1 уровень случайным образом",
                        "Аспект можно возвысить только один раз"),
                Material.STICK, 1060, -1, Arrays.asList(ItemType.WEAPON, ItemType.ARMOR, ItemType.ASPECT), ModificationDropApplier.EXALTED_ORB);
    }

}
