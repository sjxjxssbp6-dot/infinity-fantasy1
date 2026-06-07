package cupcake.nikky.imperium.tower.item.exact.modification.rule;

import cupcake.nikky.imperium.tower.item.ImperiumItem;
import lombok.val;
import org.jetbrains.annotations.NotNull;

public class ShardStarRule implements StarRule {

    @Override
    public int corruptLimit() {
        return 300;
    }

    @Override
    public double corruptChance(final int currentCorrupt) {
        return 100D;
    }

    @Override
    public boolean canCorrupt(final int currentCorrupt) {
        return currentCorrupt < this.corruptLimit();
    }

    @Override
    public CorruptResult applyCorrupt(@NotNull final ImperiumItem item) {
        val currentCorrupt = item.getGearSettings().corrupted();
        val nextCorrupt = currentCorrupt + 10;

        if (!this.canCorrupt(currentCorrupt) || nextCorrupt > corruptLimit()) return CorruptResult.CORRUPT_LIMIT;

        item.getGearSettings().corrupted(nextCorrupt);

        return CorruptResult.SUCCESSFUL;
    }

}
