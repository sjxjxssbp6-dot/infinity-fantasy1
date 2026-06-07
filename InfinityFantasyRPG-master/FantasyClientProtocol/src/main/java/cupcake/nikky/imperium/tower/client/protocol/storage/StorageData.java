package cupcake.nikky.imperium.tower.client.protocol.storage;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class StorageData {

    @Getter
    private final Map<Integer, PageState> pages = new HashMap<>();

    public StorageData apply(final int page, @NotNull final PageState state) {
        this.pages.put(page, state);
        return this;
    }

}
