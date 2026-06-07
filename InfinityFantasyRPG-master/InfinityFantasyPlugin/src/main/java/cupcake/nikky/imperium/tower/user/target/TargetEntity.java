package cupcake.nikky.imperium.tower.user.target;

import cupcake.nikky.imperium.tower.entity.monster.Monster;
import cupcake.nikky.imperium.tower.entity.monster.target.TargetBarData;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
public class TargetEntity {

    private final AtomicReference<TargetBarData> barData = new AtomicReference<>(null);

    public boolean updateTarget(@Nullable final TargetBarData targetBarData) {
        var isNew = false;

        val target = this.barData.get();

        if (targetBarData == null && target != null) {
            isNew = true;
        } else if (targetBarData != null && target != null) {
            isNew = !target.getMonster().getMonsterUniqueId().equals(targetBarData.getMonster().getMonsterUniqueId());
        } else if (targetBarData != null) {
            isNew = true;
        }

        this.barData.set(targetBarData);

        return isNew;
    }

    public boolean isEmpty() {
        return getTargetData() == null;
    }

    public Monster getTargetMonster() {
        val targetData = getTargetData();
        return targetData == null ? null : targetData.getMonster();
    }

    public TargetBarData getTargetData() {
        return this.barData.get();
    }

}
