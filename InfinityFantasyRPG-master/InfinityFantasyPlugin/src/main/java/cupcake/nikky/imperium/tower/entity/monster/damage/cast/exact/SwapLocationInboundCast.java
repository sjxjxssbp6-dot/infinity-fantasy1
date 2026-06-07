package cupcake.nikky.imperium.tower.entity.monster.damage.cast.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.entity.monster.Monster;
import cupcake.nikky.imperium.tower.entity.monster.damage.cast.InboundDamageCast;
import me.anfanik.steda.api.utility.MathUtility;

public class SwapLocationInboundCast extends InboundDamageCast {

    private final double chance;

    public SwapLocationInboundCast(Monster monster, final double chance) {
        super(monster);
        this.chance = chance;
    }

    @Override
    public boolean canApply() {
        return MathUtility.randomInRange(0, 100) <= chance;
    }

    @Override
    public void apply() {
        var attackers = getMonster().getAttackers();
        if (attackers.isEmpty()) return;

        var spigot = getMonster().getSpigot();
        if (spigot == null) return;

        var location = spigot.getLocation();
        var attacker = attackers.get(MathUtility.randomInRange(0, attackers.size() - 1));

        Fantasy.runLater(() -> {
            spigot.teleport(attacker.getLocation());
            attacker.teleport(location);
        });
    }
}
