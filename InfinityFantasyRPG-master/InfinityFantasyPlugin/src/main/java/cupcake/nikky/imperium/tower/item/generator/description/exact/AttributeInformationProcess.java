package cupcake.nikky.imperium.tower.item.generator.description.exact;

import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.item.generator.description.DescriptionContainer;
import cupcake.nikky.imperium.tower.item.generator.description.DescriptionProcess;
import cupcake.nikky.imperium.tower.item.modifier.ModifierPlace;
import cupcake.nikky.imperium.tower.item.modifier.ModifierType;
import lombok.val;
import me.anfanik.steda.api.utility.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public class AttributeInformationProcess extends DescriptionProcess {

    @Override
    public void process(@NotNull DescriptionContainer container) {
        val item =  container.getItem();

        item.getAttributes().getAll().stream()
                .filter(pair -> pair.getFirst().getPosition() >= 0)
                .map(pair -> new Pair<>(pair.getFirst(), pair.getSecond()))
                .sorted(Comparator.comparingInt(value -> value.getFirst().getPosition()))
                .filter(attributeAttributeInstancePair -> {
                    var value = attributeAttributeInstancePair.getSecond().getBaseValue();
                    return value < 0D || value >= 1D;
                })
                .forEach(pair -> {
                    val attribute = pair.getFirst();
                    val instance = pair.getSecond();

                    var baseValue = instance.getBaseValue();
                    var corruptValue = 0D;

                    var isWithPercentSymbol = attribute == ImperiumAttribute.CRITICAL_RATE
                            || attribute == ImperiumAttribute.CRITICAL_DAMAGE
                            || attribute == ImperiumAttribute.DEFENSE
                            || attribute == ImperiumAttribute.DEFENSE_PENETRATION
                            || attribute == ImperiumAttribute.MOVEMENT_SPEED
                            || attribute == ImperiumAttribute.ACCURACY
                            || attribute == ImperiumAttribute.FIRE_RESISTANCE
                            || attribute == ImperiumAttribute.COLD_RESISTANCE
                            || attribute == ImperiumAttribute.LIGHTNING_RESISTANCE
                            || attribute == ImperiumAttribute.CHAOS_RESISTANCE;
                    var isWithPoint = attribute == ImperiumAttribute.CRITICAL_RATE || attribute == ImperiumAttribute.ACCURACY;

                    String advancedLine = attribute.generateWithValue(baseValue, false) + (isWithPercentSymbol ? "%%" : "");

                    val corrupted = item.getGearSettings().corrupted();
                    if (corrupted > 0) {
                        corruptValue = Math.abs(baseValue) * ((corrupted * 4D) / 100D);
                        advancedLine += "%s &f+ &c" + (isWithPoint ? "%.1f".formatted(corruptValue) : Math.round(corruptValue));
                    } else advancedLine += "%s";

                    val baseModifier = item.getModifierSettings().findSpecificById(attribute.name().toLowerCase() + "_attribute");

                    if (baseModifier != null) {
                        val range = baseModifier.getModifier().getRange().findByNumericTier(item.getGeneralSettings().itemLevel());

                        if (range != null) {
                            container.applyAdvancedLine(String.format(advancedLine, String.format(" &8(%.0f-%.0f)", range.min(), range.max())));
                        }
                    }

                    container.applyBaseLine(attribute.generateWithValue(baseValue + corruptValue, false) + (isWithPercentSymbol ? "%" : ""));
                });

        val direction = item.getModifierSettings().findSpecificByPlace(ModifierPlace.DIRECTION);

        if (!direction.isEmpty()) {
            if (!container.isLastLineEmpty()) container.applyEmptyLine();

            direction.forEach(specificModifier -> {
                val modifierTierNumber = specificModifier.findTier();
                val tier = specificModifier.getModifier().getRange().sizeWithoutExalted() + 1 - modifierTierNumber;

                val advancedPart = "&8[Н" + tier + "] ";

                val modifierPart = specificModifier.getModifier().getModifierDescription().generateDescription("&3", "&a",
                        specificModifier.getKey(), specificModifier.getModifier().getRange().findByNumericTier(modifierTierNumber).amplitude());
                val modifierAdvancedPart = specificModifier.getModifier().getModifierDescription().generateAdvancedDescription("&3", "&a", specificModifier);

                container.applyAdvancedLine(advancedPart + modifierAdvancedPart);
                container.applyBaseLine(modifierPart);
            });
        }

        val extended = item.getModifierSettings().findSpecificByPlace(ModifierPlace.PREFIX, ModifierPlace.SUFFIX).stream()
                .sorted(Comparator.comparingInt(s ->
                        s.getModifier().getPlace().getPosition() + (s.getModifierType() == ModifierType.DISTORTED ? -4 : s.isExalted() ? -3 : s.getModifierType() == ModifierType.CRAFTED ? 2 : 0)))
                .toList();

        if (!extended.isEmpty()) {
            if (!container.isLastLineEmpty()) container.applyEmptyLine();

            var quality = item.getGearSettings().quality();

            extended.forEach(specificModifier -> {
                val place = specificModifier.getModifier().getPlace();
                var color = "&6";
                if (specificModifier.isExalted())
                    color = "&b";
                if (specificModifier.getModifierType() == ModifierType.DISTORTED)
                    color = "&d";
                if (specificModifier.getModifierType() == ModifierType.CRAFTED)
                    color = "&e";

                val modifierTierNumber = specificModifier.findTier();

                val hasSpecializedTags = item.getGearSettings().qualityTags().stream().anyMatch(modifierTag -> specificModifier.getModifier().getTags().contains(modifierTag));

                val advancedPart = "&8[%s%d] ".formatted(place == ModifierPlace.PREFIX ? "П" : "С", specificModifier.getModifier().getRange().sizeWithoutExalted() + 1 - modifierTierNumber);

                val modifierPart = specificModifier.getModifier().getModifierDescription().generateDescription("&3", color,
                        specificModifier.getKey() * (1D + (hasSpecializedTags ? (quality / 100D) : 0D)),
                        specificModifier.getModifier().getRange().findByNumericTier(modifierTierNumber).amplitude());
                val modifierAdvancedPart = specificModifier.getModifier().getModifierDescription().generateAdvancedDescription("&3", color, specificModifier);

                container.applyAdvancedLine(advancedPart + modifierAdvancedPart + (quality > 0 && hasSpecializedTags ? " &3+%d%%".formatted(quality) : ""));
                container.applyBaseLine(modifierPart);
            });
        }
    }

}
