package cupcake.nikky.imperium.tower.item.settings.exact;

import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.modifier.ModifierCreatorSettings;
import cupcake.nikky.imperium.tower.item.modifier.ModifierPlace;
import cupcake.nikky.imperium.tower.item.modifier.ModifierProvider;
import cupcake.nikky.imperium.tower.item.settings.ItemSettings;
import cupcake.nikky.imperium.tower.item.watcher.ItemWatcher;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.val;

import java.util.function.Supplier;

public class ToolSettings extends ItemSettings {

    private int exp = -1;
    @Getter @Setter
    @Accessors(fluent = true, chain = true)
    private Supplier<Integer> requirementExp;

    public ToolSettings(ImperiumItem item) {
        super(item);
    }

    public int exp() {
        item().getItemWatcherRegistry().watch(ItemWatcher.TOOL_EXP);
        return exp;
    }

    public void exp(int exp) {
        item().getItemWatcherRegistry().watch(ItemWatcher.TOOL_EXP);
        this.exp = exp;
    }

    public boolean canUpgrade() {
        return item().getGeneralSettings().itemLevel() != 6;
    }

    /* return true if tool is leveling up */
    public boolean acceptExp(final int exp) {
        item().getItemWatcherRegistry().watch(ItemWatcher.TOOL_EXP);
        this.exp(this.exp() + exp);

        if (this.exp() >= this.requirementExp.get()) {
            val delta = this.exp() - this.requirementExp.get();
            this.exp(delta);
            item().getGeneralSettings().itemLevel(item().getGeneralSettings().itemLevel() + 1);

            if (item().getGeneralSettings().itemLevel() == 2) {
                ModifierProvider.getInstance().createExtendedModifiers(item(), new ModifierCreatorSettings()
                                .registerPlace(ModifierPlace.PREFIX, item().getRare().getModifierSize())
                                .registerPlace(ModifierPlace.SUFFIX, item().getRare().getModifierSize()));
            } else if (item().getGeneralSettings().itemLevel() == 5) {
                ModifierProvider.getInstance().createRandomizedDirectionModifiers(item(), false);
            } else if (item().getGeneralSettings().itemLevel() == 6) {
                item().getModifierSettings().findSpecificByPlace(ModifierPlace.DIRECTION).forEach(specificModifier -> {
                    item().getModifierSettings().removeSpecificModifier(specificModifier.getModifierId());
                    ModifierProvider.getInstance().createDirectionModifier(item(), specificModifier.getModifier(), specificModifier.findTier() + 1);
                });
            }

            return true;
        }

        return false;
    }

}
