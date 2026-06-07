package cupcake.nikky.imperium.tower.item.modifier;

import cupcake.nikky.imperium.tower.item.ImperiumItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SpecificModifier {

    private ModifierType modifierType;
    private transient Modifier modifier;
    private String modifierId;
    private double key;

    public void applyProcess(@NotNull final ImperiumItem item) {
        modifier.getProcess().preGenerate(item, this, key);
    }

    public int findTier() {
        return getModifier().getRange().getTiers().stream()
                .filter(modifierTier -> {
                    var currentKey = Math.abs(key);
                    return Math.abs(modifierTier.min()) <= currentKey && Math.abs(modifierTier.max()) >= currentKey;
                })
                .findFirst()
                .map(ModifierTier::tier)
                .orElse(-1);
    }

    public boolean isExalted() {
        var exaltedTier = modifier.getRange().findExaltedTier();
        return exaltedTier != null && this.findTier() == exaltedTier.tier();
    }

}
