package cupcake.nikky.imperium.tower.item.exact.modification.rule;

import cupcake.nikky.imperium.tower.item.ImperiumItem;
import lombok.val;
import me.anfanik.steda.api.utility.MathUtility;
import org.jetbrains.annotations.NotNull;

public class AspectStarRule implements StarRule {

    @Override
    public int corruptLimit() {
        return 20;
    }

    @Override
    public double corruptChance(final int currentCorrupt) {
        return switch (currentCorrupt) {
            case 10, 11, 12, 13, 14 -> 75;
            case 15, 16, 17, 18, 19 -> 50;
            default -> 100;
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
        val itemLevel = item.getGeneralSettings().itemLevel();
        item.getGearSettings().potential(item.getGearSettings().potential() - 1);

        if (MathUtility.randomInRange(1, 100) > chance) {
            if (currentCorrupt > 0) {
                item.getGeneralSettings().itemLevel(itemLevel - 1);
                item.getGearSettings().corrupted(currentCorrupt - 1);

                return CorruptResult.FAIL;
            }
        }

        item.getGeneralSettings().itemLevel(itemLevel + 1);
        item.getGearSettings().corrupted(currentCorrupt + 1);

        return CorruptResult.SUCCESSFUL;
    }

}
