package cupcake.nikky.imperium.tower.entity.monster.damage.cast.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.entity.monster.Monster;
import cupcake.nikky.imperium.tower.entity.monster.damage.cast.InboundDamageCast;
import me.anfanik.steda.api.utility.MathUtility;

public class SwapSlotInboundCast extends InboundDamageCast {

    private final double chance;

    public SwapSlotInboundCast(Monster monster, final double chance) {
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

        var attacker = attackers.get(MathUtility.randomInRange(0, attackers.size() - 1));
        var slot = MathUtility.randomInRange(0, 35);

        Fantasy.runLater(() -> {
            var item = attacker.getInventory().getItemInMainHand();
            attacker.getInventory().setItemInMainHand(attacker.getInventory().getItem(slot));
            attacker.getInventory().setItem(slot, item);
        });
    }

}
