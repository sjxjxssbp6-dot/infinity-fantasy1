package cupcake.nikky.imperium.tower.item.exact.modification.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.item.ItemRare;
import cupcake.nikky.imperium.tower.item.ItemType;
import cupcake.nikky.imperium.tower.item.exact.modification.ModificationBase;
import cupcake.nikky.imperium.tower.item.exact.modification.ModificationCursorHandler;
import cupcake.nikky.imperium.tower.item.exact.modification.drop.ModificationDropApplier;
import cupcake.nikky.imperium.tower.item.modifier.ModifierTag;
import cupcake.nikky.imperium.tower.item.settings.exact.cursor.CursorResultType;
import joptsimple.internal.Strings;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Catalyst extends ModificationBase {

    private final List<ModifierTag> tags;

    public Catalyst(@NotNull final List<ModifierTag> tags, @NotNull final String title, final int data) {
        super(title, List.of(
                "Добавляет качество снаряжению",
                "",
                "&fУлучшает модификаторы с тегами: &6%s".formatted(Strings.join(tags.stream().map(ModifierTag::getName).toList(), ", ")),
                "&fМаксимальное качество: &620%",
                "&cСнаряжение может иметь только один тип качества"
                ),
                Material.STICK, (short) data, -1, false, List.of(ItemType.WEAPON, ItemType.ARMOR), ModificationDropApplier.CATALYST);

        this.tags = tags;
    }

    @Override
    public ModificationCursorHandler handler() {
        return (container, targetItem, cursorItem) -> {
            if (!targetItem.getGearSettings().qualityTags().equals(tags)) {
                targetItem.getGearSettings().quality(0);
                targetItem.getGearSettings().resetQualityTags();
            }

            if (targetItem.getGearSettings().quality() == 20) {
                Fantasy.clientProvider().notification(container.player(), SystemNotificationType.ERROR, "У данного предмета уже", "максимальное качество");
                container.cursorResultType(CursorResultType.CANCEL);
                return;
            }

            targetItem.getGearSettings().quality(targetItem.getGearSettings().quality() + 1);
            targetItem.getGearSettings().resetQualityTags();
            targetItem.getGearSettings().addQualityTags(tags);

            container.cursorResultType(CursorResultType.REMOVE_ONE_ITEM);
        };
    }

}
