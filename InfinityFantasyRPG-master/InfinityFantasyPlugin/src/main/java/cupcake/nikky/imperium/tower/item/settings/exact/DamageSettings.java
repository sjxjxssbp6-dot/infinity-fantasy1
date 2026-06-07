package cupcake.nikky.imperium.tower.item.settings.exact;

import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.settings.ItemSettings;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DamageSettings extends ItemSettings {

    private boolean isDamageable;
    private int attacks = -1;
    private int maxAttackSpeed = -1;

    public DamageSettings(ImperiumItem item) {
        super(item);
    }

}
