package cupcake.nikky.imperium.tower.item.settings.exact.cursor;

import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.settings.ItemSettings;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CursorSettings extends ItemSettings {

    private final List<CursorHandler> selfHandlers = new ArrayList<>();

    public CursorSettings(ImperiumItem item) {
        super(item);

        selfHandlers.add(new FantasyCursorHandler());
    }

    public void registerSelfHandler(@NotNull final CursorHandler cursorHandler) {
        this.selfHandlers.addFirst(cursorHandler);
    }

    public void clearHandlers() {
        this.selfHandlers.clear();
    }

    public List<CursorHandler> findSelfHandlers() {
        return new ArrayList<>(selfHandlers);
    }

}
