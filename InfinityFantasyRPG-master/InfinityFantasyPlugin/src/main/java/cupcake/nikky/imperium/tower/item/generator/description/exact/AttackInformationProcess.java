package cupcake.nikky.imperium.tower.item.generator.description.exact;

import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.item.generator.description.DescriptionContainer;
import cupcake.nikky.imperium.tower.item.generator.description.DescriptionProcess;
import cupcake.nikky.imperium.tower.item.modifier.exact.ModifierItemListExtended;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AttackInformationProcess extends DescriptionProcess {

    @Override
    public void process(@NotNull DescriptionContainer container) {
        val item =  container.getItem();

        if (item.getDamageSettings().isDamageable()) {
            if (!container.isLastLineEmpty()) container.applyEmptyLine();
            var corruptValue = item.getGearSettings().corrupted() > 0 ? " &f+ &c%d%%".formatted(item.getGearSettings().corrupted() * 4) : "";

            var specificModifier = item.getModifierSettings().findSpecificById(ModifierItemListExtended.ADD_PHYSICAL_DAMAGE.getId());
            var tier = specificModifier == null ? null : specificModifier.getModifier().getRange().findByNumericTier(specificModifier.findTier());
            var minValue = tier == null ? 0 : specificModifier.getKey() - tier.amplitude();
            var maxValue = tier == null ? 0 : specificModifier.getKey() + tier.amplitude();

            container.applyBaseLine(String.format("&fФизический урон: &3%.0f-%.0f",
                    item.getAttributes().find(ImperiumAttribute.MIN_PHYSICAL_DAMAGE).getValue(),
                    item.getAttributes().find(ImperiumAttribute.MAX_PHYSICAL_DAMAGE).getValue()));
            container.applyAdvancedLine(String.format("&fФизический урон: &3%.0f-%.0f%s",
                    item.getAttributes().find(ImperiumAttribute.MIN_PHYSICAL_DAMAGE).getBaseValue() - minValue,
                    item.getAttributes().find(ImperiumAttribute.MAX_PHYSICAL_DAMAGE).getBaseValue() - maxValue, corruptValue));

            List<String> base = new ArrayList<>();
            List<String> advanced = new ArrayList<>();

            if (item.getAttributes().find(ImperiumAttribute.MIN_FIRE_DAMAGE).getValue() > 0 ||
                    item.getAttributes().find(ImperiumAttribute.MIN_COLD_DAMAGE).getValue() > 0 ||
                    item.getAttributes().find(ImperiumAttribute.MIN_LIGHTNING_DAMAGE).getValue() > 0) {

                base.add(item.getAttributes().find(ImperiumAttribute.MIN_FIRE_DAMAGE).getValue() > 0 ? "&c%.0f-%.0f".formatted(
                        item.getAttributes().find(ImperiumAttribute.MIN_FIRE_DAMAGE).getValue(),
                        item.getAttributes().find(ImperiumAttribute.MAX_FIRE_DAMAGE).getValue()
                ) : "");
                advanced.add(item.getAttributes().find(ImperiumAttribute.MIN_FIRE_DAMAGE).getValue() > 0 ? "&c%.0f-%.0f%s".formatted(
                        item.getAttributes().find(ImperiumAttribute.MIN_FIRE_DAMAGE).getBaseValue(),
                        item.getAttributes().find(ImperiumAttribute.MAX_FIRE_DAMAGE).getBaseValue(), corruptValue
                ) : "");

                base.add(item.getAttributes().find(ImperiumAttribute.MIN_COLD_DAMAGE).getValue() > 0 ? "&b%.0f-%.0f".formatted(
                        item.getAttributes().find(ImperiumAttribute.MIN_COLD_DAMAGE).getValue(),
                        item.getAttributes().find(ImperiumAttribute.MAX_COLD_DAMAGE).getValue()
                ) : "");
                advanced.add(item.getAttributes().find(ImperiumAttribute.MIN_COLD_DAMAGE).getValue() > 0 ? "&b%.0f-%.0f%s".formatted(
                        item.getAttributes().find(ImperiumAttribute.MIN_COLD_DAMAGE).getBaseValue(),
                        item.getAttributes().find(ImperiumAttribute.MAX_COLD_DAMAGE).getBaseValue(), corruptValue
                ) : "");

                base.add(item.getAttributes().find(ImperiumAttribute.MIN_LIGHTNING_DAMAGE).getValue() > 0 ? "&e%.0f-%.0f".formatted(
                        item.getAttributes().find(ImperiumAttribute.MIN_LIGHTNING_DAMAGE).getValue(),
                        item.getAttributes().find(ImperiumAttribute.MAX_LIGHTNING_DAMAGE).getValue()
                ) : "");
                advanced.add(item.getAttributes().find(ImperiumAttribute.MIN_LIGHTNING_DAMAGE).getValue() > 0 ? "&e%.0f-%.0f%s".formatted(
                        item.getAttributes().find(ImperiumAttribute.MIN_LIGHTNING_DAMAGE).getBaseValue(),
                        item.getAttributes().find(ImperiumAttribute.MAX_LIGHTNING_DAMAGE).getBaseValue(), corruptValue
                ) : "");

                container.applyBaseLine(String.format("&fЭлементальный урон: %s", base.stream().filter(string -> !string.isBlank()).collect(Collectors.joining("&f, "))));
                container.applyAdvancedLine(String.format("&fЭлементальный урон: %s", advanced.stream().filter(string -> !string.isBlank()).collect(Collectors.joining("&f, "))));
            }

            if (item.getAttributes().find(ImperiumAttribute.MIN_CHAOS_DAMAGE).getValue() > 0) {
                container.applyBaseLine(String.format("&fУрон хаосом&f: &5%.0f-%.0f",
                        item.getAttributes().find(ImperiumAttribute.MIN_CHAOS_DAMAGE).getValue(),
                        item.getAttributes().find(ImperiumAttribute.MAX_CHAOS_DAMAGE).getValue()));
                container.applyAdvancedLine(String.format("&fУрон хаосом&f: &5%.0f-%.0f%s",
                        item.getAttributes().find(ImperiumAttribute.MIN_CHAOS_DAMAGE).getBaseValue(),
                        item.getAttributes().find(ImperiumAttribute.MAX_CHAOS_DAMAGE).getBaseValue(), corruptValue));
            }
        }

        val attackSpeed = item.getDamageSettings().getAttacks();
        if (attackSpeed != -1) {
            val attacks = 1D / (attackSpeed / 1000D);
            container.applyLine("&fСкорость атаки: &3%.1f %s/с".formatted(attacks, "ударов"));
        }
    }

}
