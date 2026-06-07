package cupcake.nikky.imperium.tower.item.generator.description.exact;

import cupcake.nikky.imperium.tower.item.ItemRare;
import cupcake.nikky.imperium.tower.item.ItemType;
import cupcake.nikky.imperium.tower.item.exact.modification.rule.StarRule;
import cupcake.nikky.imperium.tower.item.generator.description.DescriptionContainer;
import cupcake.nikky.imperium.tower.item.generator.description.DescriptionProcess;
import cupcake.nikky.imperium.tower.item.modifier.ModifierTag;
import cupcake.nikky.imperium.tower.item.settings.exact.FragmentSettings;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TopInformationProcess extends DescriptionProcess {

    @Override
    public void process(@NotNull DescriptionContainer container) {
        val item =  container.getItem();

        final List<String> topLine = new ArrayList<>();
        if (item.getDescriptionSettings().showItemType())
            topLine.add("&f" + item.getImperiumType().getTypeColor() + item.getImperiumType().getTitle());

        if (item.getRare() != ItemRare.NONE)
            topLine.add(item.getRare().getColoredName());

        if (item.getGearSettings().copy())
            topLine.add("&bКопия");

        val currentCorrupt = item.getGearSettings().corrupted();

        if (currentCorrupt > 0) {
            val rule = StarRule.rule(item);
            val chance = rule.corruptChance(currentCorrupt);
            val isLimit = rule.corruptLimit() == currentCorrupt;

            topLine.add("&cОскверненное" + (isLimit ? "" : " &8(шанс " + chance + "%)"));
        }

        var currentLine = topLine.stream().filter(line -> !line.isBlank()).collect(Collectors.joining("&f, "));
        if (!currentLine.isBlank()) container.applyAdvancedLine(currentLine);

        if (item.getDescriptionSettings().showItemLevel()) {
            val exaltedValue = item.getAspectSettings().exaltedValue();
            val itemLevel = item.getGeneralSettings().itemLevel() + exaltedValue;

            if (item.getImperiumType() == ItemType.ASPECT && itemLevel > 0) {
                val corrupted = item.getGearSettings().corrupted();
                val hasExaltedValue = exaltedValue != 0;

                if (corrupted != 0) {
                    container.applyBaseLine(item.getDescriptionSettings().itemLevelPattern().formatted("&3", itemLevel));
                    if (hasExaltedValue) {
                        container.applyAdvancedLine(item.getDescriptionSettings().itemLevelPattern().formatted("&c", corrupted) + (exaltedValue > 0 ? " &f+" : " &f-") + " &b1");
                    } else {
                        container.applyAdvancedLine(item.getDescriptionSettings().itemLevelPattern().formatted("&c", corrupted));
                    }
                } else {
                    container.applyLine(item.getDescriptionSettings().itemLevelPattern().formatted("&3", itemLevel));
                }
            } else if (itemLevel > 0) {
                if (item.getImperiumType() == ItemType.WEAPON || item.getImperiumType() == ItemType.ARMOR)
                    container.applyAdvancedLine(item.getDescriptionSettings().itemLevelPattern().formatted("&3", itemLevel));
                else container.applyLine(item.getDescriptionSettings().itemLevelPattern().formatted("&3", itemLevel));
            }
        }

        val required = item.getGeneralSettings().requiredLevel();

        if (required > 0 && !item.getGeneralSettings().disableRequiredLevel())
            container.applyLine("&fНеобходимый уровень: &3" + required);

        var gearSettings = item.getGearSettings();

        if (item.getImperiumType() == ItemType.ASPECT || item.getImperiumType() == ItemType.WEAPON || item.getImperiumType() == ItemType.ARMOR) {
            container.applyLine("&fПотенциал: &3%d".formatted(gearSettings.potential()));
        }

        if (gearSettings.quality() > 0) {
            container.applyLine("&fКачество: &3%d%% &7(&7%s&7)".formatted(gearSettings.quality(),
                    String.join("&7, &7", gearSettings.qualityTags().stream().map(ModifierTag::getName).toArray(String[]::new))));
        }

        if (!container.isLastLineEmpty()) container.applyEmptyLine();

        val fragmentSettings = item.findSettings(FragmentSettings.class);
        if (fragmentSettings.getFragmentItemQuantity() > 0)
            container.applyLine("&3+%d%% &6количество выпадаемых предметов".formatted(fragmentSettings.getFragmentItemQuantity()));
        if (fragmentSettings.getFragmentExperienceIncrease() > 0)
            container.applyLine("&3%d%% &6увеличение получаемого опыта".formatted(fragmentSettings.getFragmentExperienceIncrease()));
        if (item.getImperiumType() == ItemType.EDGE_FRAGMENT && item.getGearSettings().corrupted() > 0) {
            if (!container.isLastLineEmpty()) container.applyEmptyLine();
            container.applyLine("&3%d%% увеличение получаемого урона".formatted(item.getGearSettings().corrupted()));
        }
        if (item.getImperiumType() == ItemType.SHARD_BREAKTHROUGH && item.getGearSettings().corrupted() > 0) {
            if (!container.isLastLineEmpty()) container.applyEmptyLine();
            container.applyLine("&3%d%% увеличение здоровья существа".formatted(item.getGearSettings().corrupted()));
            container.applyLine("&3%.1f%% увеличение урона существа".formatted(item.getGearSettings().corrupted() / 2D));
        }

        if (!container.isLastLineEmpty()) container.applyEmptyLine();
    }

}
