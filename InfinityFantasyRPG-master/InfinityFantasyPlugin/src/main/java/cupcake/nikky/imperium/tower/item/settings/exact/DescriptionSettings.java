package cupcake.nikky.imperium.tower.item.settings.exact;

import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.settings.ItemSettings;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true, chain = true)
public class DescriptionSettings extends ItemSettings {

    private boolean showItemLevel;
    private String itemLevelPattern = "&fУровень: %s%d";
    private boolean showItemType;

    public DescriptionSettings(ImperiumItem item) {
        super(item);
    }


}
