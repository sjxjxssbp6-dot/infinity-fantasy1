package cupcake.nikky.imperium.tower.item.settings.exact;

import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.settings.ItemSettings;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FragmentSettings extends ItemSettings {

    private int fragmentItemQuantity;
    private int fragmentExperienceIncrease;

    public FragmentSettings(ImperiumItem item) {
        super(item);
    }
}
