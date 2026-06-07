package cupcake.nikky.imperium.tower.utility;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.modifier.ModifierType;
import cupcake.nikky.imperium.tower.item.modifier.SpecificModifier;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class InventoryValidation {

    private final ItemStack[] content;

    public ItemStack[] validate() {
        val copy = new ItemStack[content.length];

        for (int i = 0; i < content.length; i++) {
            val itemStack = content[i];
            val imperiumItem = Fantasy.itemProvider().find(itemStack);

            if(imperiumItem == null) copy[i] = null;
            else {
                copy[i] = NbtUtility.clearSessionTags(this.checkDuplicates(imperiumItem).createNew(itemStack.getAmount()));
            }
        }

        return copy;
    }

    private ImperiumItem checkDuplicates(@NotNull final ImperiumItem imperiumItem) {
        for (SpecificModifier specificModifier : imperiumItem.getModifierSettings().findAllSpecificModifiers()) {
            if (imperiumItem.getModifierSettings().findSpecificCount(specificModifier) > 1) {
                imperiumItem.getModifierSettings().removeSpecificModifier(specificModifier.getModifierId());
            }
        }

        if (imperiumItem.getModifierSettings().findSpecificByType(ModifierType.EXTENDED, ModifierType.CRAFTED, ModifierType.DISTORTED).size() > 6) {
            imperiumItem.getModifierSettings().removeSpecificModifierByType(ModifierType.CRAFTED);
        }

        return imperiumItem;
    }

}
