package cupcake.nikky.imperium.tower.data.prompt.cursor;

import cupcake.nikky.imperium.tower.client.protocol.cursor.CursorPromptData;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CursorPromptStorage {

    @Getter
    private static final CursorPromptStorage instance = new CursorPromptStorage();

    private final List<CursorPromptData> data = new ArrayList<>();

    public List<CursorPromptData> findAll() {
        return new ArrayList<>(data);
    }

    public Optional<CursorPromptData> findAny() {
        return !this.data.isEmpty() ? Optional.ofNullable(this.data.get(0)) : Optional.empty();
    }

    public void addAll(@NotNull final Collection<CursorPromptData> data) {
        this.data.addAll(data);
    }

    public void invalidate(@NotNull final UUID uuid) {
        this.data.removeIf(data2 -> data2.getUuid().equals(uuid));
    }

    public boolean isEmpty() {
        return this.data.isEmpty();
    }

    public void clear() {
        this.data.clear();
    }

}
