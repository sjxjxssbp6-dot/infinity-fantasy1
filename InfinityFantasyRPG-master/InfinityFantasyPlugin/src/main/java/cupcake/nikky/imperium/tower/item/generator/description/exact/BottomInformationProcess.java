package cupcake.nikky.imperium.tower.item.generator.description.exact;

import cupcake.nikky.imperium.tower.item.exact.gear.tool.ToolItem;
import cupcake.nikky.imperium.tower.item.generator.description.DescriptionContainer;
import cupcake.nikky.imperium.tower.item.generator.description.DescriptionProcess;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.Collectors;

public class BottomInformationProcess extends DescriptionProcess {

    @Override
    public void process(@NotNull DescriptionContainer container) {
        val item =  container.getItem();

        val aspectSettings = item.getAspectSettings();
        val aspect = aspectSettings.aspect();
        val level = aspectSettings.aspectLevel();

        if (aspect != null) {
            if (!container.isLastLineEmpty()) container.applyEmptyLine();
            val exaltedNaming = aspectSettings.exaltedValue() == 0 ? "" : "Возвышенный ";
            container.applyLine("&6" + exaltedNaming + aspect.title().replaceFirst(" ", " &a") + (level > 0 ? " &c+" + level : ""));
            if (aspectSettings.aspectCopy()) container.applyAdvancedLine("&bКопия");
            container.applyAdvancedLines(aspect.description().apply(level).stream().map(s -> "&7" + s).collect(Collectors.toList()));
            container.applyLine("&fСтоимость: " + aspect.costData().getCostProcessType().getDescriptionProcess().apply(aspect.costData().getCostValue().apply(level)));
        }

        val itemDescription = container.getItem().getAdditionalDescription();

        if(itemDescription != null && !itemDescription.isEmpty()) {
            if (!container.isLastLineEmpty()) container.applyEmptyLine();
            container.applyLines(itemDescription.stream().map(s -> s.startsWith("&") ? s : "&7" + s).collect(Collectors.toList()));
        }

        if (item.checkInstance(ToolItem.class) && item.getToolSettings().canUpgrade()) {
            if (!container.isLastLineEmpty()) container.applyEmptyLine();
            container.applyLine("&fОпыт инструмента: &3" + item.getToolSettings().exp() + "&f/&3" + item.getToolSettings().requirementExp().get());
        }

        String[] redLines = new String[2];
        int i = 0;

        if (!item.getDropSettings().droppable())
            redLines[i] = "&cНельзя выкинуть";

//        if (!item.getTradeSettings().isTradable())
//            redLines[i] = "&cНельзя продать";

        val resultRedLine = Arrays.stream(redLines).filter(s -> s != null && !s.trim().equals("")).collect(Collectors.joining("&f, "));
        if (!resultRedLine.trim().isEmpty()) {
            if (!container.isLastLineEmpty()) container.applyEmptyLine();

            container.applyLine(resultRedLine);
        }
    }

}
