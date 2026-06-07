package cupcake.nikky.imperium.tower.item.modifier.filter;

import cupcake.nikky.imperium.tower.item.ImperiumItem;

@FunctionalInterface
public interface ItemFilter {

    boolean isSuitedItem(ImperiumItem item);

}
