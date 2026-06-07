package cupcake.nikky.imperium.tower.item.exact.modification.rule;

import cupcake.nikky.imperium.tower.item.ImperiumItem;
import lombok.val;
import me.anfanik.steda.api.utility.MathUtility;
import org.jetbrains.annotations.NotNull;

public class EquipStarRule implements StarRule {

    @Override
    public int corruptLimit() {
        return 10;
    }

    @Override
    public double corruptChance(final int currentCorrupt) {
        return switch (currentCorrupt) {
            case 0 -> 100;
            case 1, 2, 3 -> 75;
            case 4, 5, 6, 7 -> 50;
            default -> 40;
        };
    }

    @Override
    public boolean canCorrupt(final int currentCorrupt) {
        return currentCorrupt < this.corruptLimit();
    }

    @Override
    public CorruptResult applyCorrupt(@NotNull final ImperiumItem item) {
        val currentCorrupt = item.getGearSettings().corrupted();
        if (!this.canCorrupt(currentCorrupt)) return CorruptResult.CORRUPT_LIMIT;

        val chance = this.corruptChance(currentCorrupt);
        item.getGearSettings().potential(item.getGearSettings().potential() - 1);

        if (MathUtility.randomInRange(1, 100) > chance && currentCorrupt > 0) {
            item.getGearSettings().corrupted(currentCorrupt - 1);
            return CorruptResult.FAIL;
        }


        item.getGearSettings().corrupted(currentCorrupt + 1);
        return CorruptResult.SUCCESSFUL;
    }

}
