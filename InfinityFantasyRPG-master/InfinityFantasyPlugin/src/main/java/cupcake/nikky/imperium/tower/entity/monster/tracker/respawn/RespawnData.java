package cupcake.nikky.imperium.tower.entity.monster.tracker.respawn;

import cupcake.nikky.imperium.tower.entity.monster.Monster;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.anfanik.steda.api.utility.TextUtility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.cristalix.wada.Wada;
import ru.cristalix.wada.module.worldtext.WorldText;
import ru.cristalix.wada.util.Color;

import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public class RespawnData {

    @Getter private final Monster monster;
    private final AtomicInteger leftTicks = new AtomicInteger();
    private WorldText worldText;

    public void removeText() {
        if (worldText != null) {
            Wada.get().getWorldTextModule().removeTextGlobal(this.worldText);
        }
    }

    public RespawnData withText(final boolean state) {
        if (!state) return this;

        this.worldText = Wada.get().getWorldTextModule().createWorldText(this.generateText(), monster.getSpawnLocation().clone().add(0, 1, 0), true, Color.BLACK.alpha(.62));
        Wada.get().getWorldTextModule().addTextGlobal(this.worldText);
        return this;
    }

    private String generateText() {
        var leftMinutes = leftTicks.get() / 20 / 60;

        return "%s%n&fДо возрождения%s %d %s".formatted(
                monster.getNameForPlayer(null),
                leftMinutes <= 0 ? " меньше" : "",
                leftMinutes <= 0 ? 1 : leftMinutes,
                leftMinutes <= 0 ? "минуты" : TextUtility.transformViaAmount(leftMinutes, "минута", "минуты", "минут")
        );
    }

    public void updateText() {
        if (worldText != null) worldText.setText(this.generateText());
    }

    public RespawnData withTicks(final int ticks) {
        leftTicks.set(ticks);
        return this;
    }

    public int getAndAdd(final int delta) {
        var old = this.leftTicks.getAndAdd(delta);
        var result = this.leftTicks.get();

        if (old / 20 / 60 != result / 20 / 60) this.updateText();

        return old;
    }

}
