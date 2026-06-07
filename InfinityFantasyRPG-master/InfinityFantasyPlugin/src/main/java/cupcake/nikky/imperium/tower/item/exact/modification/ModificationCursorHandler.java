package cupcake.nikky.imperium.tower.item.exact.modification;

import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.settings.exact.cursor.CursorContainer;

public interface ModificationCursorHandler {

    void onHandle(CursorContainer container, ImperiumItem targetItem, ModificationItem cursorItem);

}
