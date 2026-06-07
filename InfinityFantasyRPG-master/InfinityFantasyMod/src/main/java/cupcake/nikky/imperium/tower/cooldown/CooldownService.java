package cupcake.nikky.imperium.tower.cooldown;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CooldownService {

    @Getter
    private static final CooldownService instance = new CooldownService();

    private final Map<String, Long> data = new HashMap<>();

    public boolean getAndExtendCooldown(@NotNull final String key, final double seconds) {
        if (this.data.getOrDefault(key, 0L) + (seconds * 1000) < Instant.now().toEpochMilli()) {
            this.data.put(key, Instant.now().toEpochMilli());
            return true;
        }

        return false;
    }

    public boolean getCooldown(@NotNull final String key, final double seconds) {
        return this.data.getOrDefault(key, 0L) + (seconds * 1000) < Instant.now().toEpochMilli();
    }

    public void invalidateCooldowns() {
        this.data.clear();
    }

}
