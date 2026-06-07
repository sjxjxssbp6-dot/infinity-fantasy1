package cupcake.nikky.imperium.tower.indicator;

import lombok.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.wada.util.ModTransfer;

import java.util.Collection;

@Getter
@RequiredArgsConstructor
public class DamageIndicator {

    private final int entityId;
    private final DamageIndicatorSettings damageIndicatorSettings;

    @Setter(value = AccessLevel.PRIVATE)
    private String text;

    public void spawn(@NotNull final Player... players) {
        if (text == null || text.trim().isEmpty())
            text = damageIndicatorSettings.createText();

        val transfer = new ModTransfer().writeByte(1).writeLong(entityId).writeString(text);
        for (Player player : players) {
            transfer.send("w2:entity_damage", player);
        }
    }

    public void spawn(@NotNull final Collection<? extends Player> players) {
        if (text == null || text.trim().isEmpty())
            text = damageIndicatorSettings.createText();

        val transfer = new ModTransfer().writeByte(1).writeLong(entityId).writeString(text);
        for (Player player : players) {
            transfer.send("w2:entity_damage", player);
        }
    }

}
