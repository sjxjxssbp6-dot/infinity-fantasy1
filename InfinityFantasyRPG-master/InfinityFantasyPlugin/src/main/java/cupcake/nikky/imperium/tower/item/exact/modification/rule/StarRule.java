package cupcake.nikky.imperium.tower.item.exact.modification.rule;

import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectItem;
import cupcake.nikky.imperium.tower.item.exact.fragment.FragmentItem;
import cupcake.nikky.imperium.tower.item.exact.shard.ShardItem;
import org.jetbrains.annotations.NotNull;

public interface StarRule {

    StarRule EQUIP_STAR_RULE = new EquipStarRule();
    StarRule ASPECT_STAR_RULE = new AspectStarRule();
    StarRule FRAGMENT_STAR_RULE = new FragmentStarRule();
    StarRule SHARD_STAR_RULE = new ShardStarRule();

    static StarRule rule(@NotNull final ImperiumItem item) {
        return item.checkInstance(AspectItem.class) ? ASPECT_STAR_RULE :
                item.checkInstance(FragmentItem.class) ? FRAGMENT_STAR_RULE : item.checkInstance(ShardItem.class) ? SHARD_STAR_RULE : EQUIP_STAR_RULE;
    }

    int corruptLimit();

    double corruptChance(int currentCorrupt);

    boolean canCorrupt(int currentCorrupt);

    CorruptResult applyCorrupt(@NotNull ImperiumItem item);

}
