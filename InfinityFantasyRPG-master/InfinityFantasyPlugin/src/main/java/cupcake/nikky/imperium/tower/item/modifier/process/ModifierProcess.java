package cupcake.nikky.imperium.tower.item.modifier.process;

import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.modifier.SpecificModifier;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface ModifierProcess {

    Collection<ImperiumAttribute> getAttributes();

    void preGenerate(@NotNull final ImperiumItem item, SpecificModifier specificModifier, final double key);

    default double quality(ImperiumItem item, SpecificModifier specificModifier) {
        var tags = item.getGearSettings().qualityTags();
        return (item.getGearSettings().quality() > 0 && specificModifier.getModifier().getTags().stream().anyMatch(tags::contains)) ? item.getGearSettings().quality() : 0D;
    }

}
