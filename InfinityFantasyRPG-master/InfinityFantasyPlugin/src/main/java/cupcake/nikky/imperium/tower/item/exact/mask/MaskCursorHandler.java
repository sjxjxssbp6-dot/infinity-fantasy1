package cupcake.nikky.imperium.tower.item.exact.mask;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.ItemRare;
import cupcake.nikky.imperium.tower.item.modifier.*;
import cupcake.nikky.imperium.tower.item.modifier.base.ModifierBaseRegistry;
import cupcake.nikky.imperium.tower.item.settings.exact.cursor.CursorContainer;
import cupcake.nikky.imperium.tower.item.settings.exact.cursor.CursorResultType;
import lombok.val;
import me.anfanik.steda.api.utility.MathUtility;
import org.jetbrains.annotations.NotNull;

public class MaskCursorHandler {

    public void onHandle(CursorContainer container, MaskItem maskItem, ImperiumItem targetItem) {
        if (targetItem.getRare() != ItemRare.EPIC) {
            Fantasy.clientProvider().notification(container.player(), SystemNotificationType.ERROR, "Необходимая редкость предмета: " + ItemRare.EPIC.getColoredName());
            container.cursorResultType(CursorResultType.CANCEL);
            return;
        }

        val targetModifier = maskItem.getMaskType().getLinkedModifiers().get(targetItem.getImperiumType());

        if (targetModifier == null) {
            Fantasy.clientProvider().notification(container.player(), SystemNotificationType.ERROR, "Данный слепок невозможно применить", "на подобном предмете");
            container.cursorResultType(CursorResultType.CANCEL);
            return;
        }

        val targetTier = this.findHighestAvailableTier(targetModifier, targetItem.getGeneralSettings().itemLevel());

        if (targetTier == 0) {
            Fantasy.clientProvider().notification(container.player(), SystemNotificationType.ERROR, "Уровень предмета слишком низок", "для данного слепка");
            container.cursorResultType(CursorResultType.CANCEL);
            return;
        }

        val duplicatedModifier = targetItem.getModifierSettings().findSpecificById(targetModifier.getId());

        if (duplicatedModifier != null && duplicatedModifier.getModifierType() == ModifierType.DISTORTED) {
            Fantasy.clientProvider().notification(container.player(), SystemNotificationType.ERROR, "У данного снаряжения присутствует", "Подобный модификатор");
            container.cursorResultType(CursorResultType.CANCEL);
            return;
        }

        ModifierProvider.getInstance().clearModifiers(targetItem, ModifierType.EXTENDED, ModifierType.CRAFTED);

        val applied = ModifierBaseRegistry.findById(targetModifier.getId());
        targetItem.getModifierSettings().registerSpecificModifier(applied.process().type(ModifierType.EXTENDED).apply(targetTier));

        int prefix = targetItem.getRare().getModifierSize() - (int) targetItem.getModifierSettings().findSpecificByType(ModifierType.EXTENDED, ModifierType.DISTORTED, ModifierType.CRAFTED)
                .stream().filter(specificModifier -> specificModifier.getModifier().getPlace() == ModifierPlace.PREFIX).count();
        int suffix = targetItem.getRare().getModifierSize() - (int) targetItem.getModifierSettings().findSpecificByType(ModifierType.EXTENDED, ModifierType.DISTORTED, ModifierType.CRAFTED)
                .stream().filter(specificModifier -> specificModifier.getModifier().getPlace() == ModifierPlace.SUFFIX).count();

        ModifierProvider.getInstance().createExtendedModifiers(targetItem, new ModifierCreatorSettings()
                .registerPlace(ModifierPlace.PREFIX, MathUtility.randomInRange(1, prefix))
                .registerPlace(ModifierPlace.SUFFIX, MathUtility.randomInRange(1, suffix)));

        container.cursorResultType(CursorResultType.REMOVE_ONE_ITEM);
    }

    private int findHighestAvailableTier(@NotNull final Modifier modifier, final int baseItemLevel) {
        int result = 0;

        for (ModifierTier modifierTier : modifier.getRange().getTiers()) {
            if (!modifierTier.exalted() && modifierTier.requireBaseLevel() <= baseItemLevel) result = Math.max(result, modifierTier.tier());
        }

        return result;
    }

}
