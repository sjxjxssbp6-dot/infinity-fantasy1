package cupcake.nikky.imperium.tower.pandora;

import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.entity.monster.Monster;
import cupcake.nikky.imperium.tower.entity.monster.MonsterType;
import cupcake.nikky.imperium.tower.user.User;
import lombok.Getter;
import lombok.Setter;
import me.anfanik.steda.api.utility.MathUtility;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
public class PandoraData {

    private final UUID uuid = UUID.randomUUID();
    private Monster monster;
    private Location pandoraLocation;
    private int currentUpgrade = -1;
    private double attempts;
    private double amplitude;
    private double increase;

    public PandoraData(@NotNull final User user, @NotNull final Location location, @NotNull final Monster monster) {
        this.attempts = user.findAttribute(ImperiumAttribute.PANDORA_BASE_ATTEMPTS).findValue();
        this.monster = monster;
        this.pandoraLocation = location;
        this.amplitude = MathUtility.randomInRange(90, 110) / 100D;
    }

    public void increaseAttemptsAmount(final double increase) {
        this.increase += increase;
    }

    public List<String> currentUpgrades() {
        return List.of("%.0f%% увеличение\nпредметов".formatted(getIncrease()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PandoraData that = (PandoraData) o;
        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
