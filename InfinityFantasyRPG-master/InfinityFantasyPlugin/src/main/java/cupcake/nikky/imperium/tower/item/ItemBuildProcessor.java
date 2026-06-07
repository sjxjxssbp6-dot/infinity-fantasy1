package cupcake.nikky.imperium.tower.item;

import cupcake.nikky.imperium.tower.Fantasy;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@RequiredArgsConstructor(staticName = "of")
public class ItemBuildProcessor {

    private final ImperiumItem item;
    private final List<Consumer<ImperiumItem>> handlers = new ArrayList<>();

    public static ItemBuildProcessor of(@NotNull final String id) {
        return new ItemBuildProcessor(Fantasy.itemProvider().find(id));
    }

    public ItemBuildProcessor apply(@NotNull final Consumer<ImperiumItem> handler) {
        handlers.add(handler);
        return this;
    }

    public ImperiumItem catchItem() {
        val copy = item.create();
        handlers.forEach(handler -> handler.accept(copy));
        return copy;
    }

}
