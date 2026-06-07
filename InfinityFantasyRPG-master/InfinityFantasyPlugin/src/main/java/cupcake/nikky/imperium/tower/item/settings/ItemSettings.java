package cupcake.nikky.imperium.tower.item.settings;

import cupcake.nikky.imperium.tower.item.ImperiumItem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
@RequiredArgsConstructor
public abstract class ItemSettings {

    private final ImperiumItem item;

}
