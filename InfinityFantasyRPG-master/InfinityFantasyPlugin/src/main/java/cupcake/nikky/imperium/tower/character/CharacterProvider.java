package cupcake.nikky.imperium.tower.character;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CharacterProvider {

    @Getter
    private static final CharacterProvider instance = new CharacterProvider();

    private final Map<Integer, ImperiumCharacter> data = new ConcurrentHashMap<>(32, 0.75F, 4);

    public void registerCharacter(@NotNull final ImperiumCharacter character) {
        this.data.put(character.getEntityId(), character);
    }

    public void invalidateCharacter(final int entityId) {
        this.data.remove(entityId);
    }

    public ImperiumCharacter findById(final int entityId) {
        return this.data.get(entityId);
    }

    public List<ImperiumCharacter> findAll() {
        return new ArrayList<>(this.data.values());
    }

}
