package cupcake.nikky.imperium.tower.entity.monster.modifier;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.buff.BuffType;
import cupcake.nikky.imperium.tower.entity.monster.Monster;
import cupcake.nikky.imperium.tower.rng.Weight;
import cupcake.nikky.imperium.tower.rng.WeightPool;
import cupcake.nikky.imperium.tower.rng.WeightWrapper;
import cupcake.nikky.imperium.tower.zone.ZoneList;
import lombok.RequiredArgsConstructor;
import lombok.val;
import me.anfanik.steda.api.utility.MathUtility;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
public class ModifierEngine {

    private final Monster monster;

    private final AtomicReference<ModifierCollection> currentModifiers = new AtomicReference<>(ModifierCollection.NONE);
    private final AtomicInteger countAmplifiers = new AtomicInteger(0);

    public void applyModifiers() {
        getCurrentModifiers().getModifiers().forEach(modifier -> modifier.applyModifier(monster));
        val affix = getCurrentModifiers().getModifierAffix();
        if (affix.getMinAmount() == affix.getMaxAmount() && affix.getMinAmount() == 0) return;

        val randomized = MathUtility.randomInRange(affix.getMinAmount(), affix.getMaxAmount());
        this.setCountAmplifiers(randomized);

        val weightPool = new WeightPool<WeightWrapper<BuffType>>(randomized);
        affix.getAmplifiers().forEach(buffType -> weightPool.apply(new Weight<>(new WeightWrapper<>(buffType, 500, true)).onSuccess(weightWrapperWeight -> 0)));
        weightPool.generatePool().forEach(weightWrapperWeight -> Fantasy.buffService().appendBuff(monster.getMonsterUniqueId(), weightWrapperWeight.weightWrapper().getWrapped()));
    }

    public void generateModifiers() {
        if (monster.getMonsterData().getModifierCollection() != null) {
            this.setCurrentModifiers(monster.getMonsterData().getModifierCollection());
            return;
        }

        if (monster.getMonsterData().isUnique()) {
            this.setCurrentModifiers(ModifierCollection.UNIQUE);
            return;
        }

        if (ZoneList.REKT_MINE_ZONE.isIn(monster.getSpawnLocation())) {
            this.setCurrentModifiers(ModifierCollection.EPIC);
            return;
        }

        val monsters = Fantasy.monsterProvider().findMonstersByType(monster.getMonsterType());
        val size = (double) monsters.size();

        val none = monsters.stream().filter(monster1 -> monster1.getModifierEngine().getCurrentModifiers() == ModifierCollection.NONE).count();
        val rare = monsters.stream().filter(monster1 -> monster1.getModifierEngine().getCurrentModifiers() == ModifierCollection.RARE).count();
        val epic = size - none - rare;

        boolean canApplyRare = rare / size < .2D;
        boolean canApplyEpic = epic / size < .1D;

        if (!canApplyRare && !canApplyEpic) {
            this.setCurrentModifiers(ModifierCollection.NONE);
            return;
        }

        int noneWeight = 1250;
        int rareWeight = 150;
        int epicWeight = 75;

        val cutWeight = monster.getLevel() * 6;

        rareWeight += Math.min(cutWeight, 110);
        epicWeight += Math.max(cutWeight - 110, 0);

        val pool = new WeightPool<WeightWrapper<ModifierCollection>>(1)
                .apply(new Weight<>(new WeightWrapper<>(ModifierCollection.NONE, noneWeight, false)));

        if (canApplyRare) pool.apply(new Weight<>(new WeightWrapper<>(ModifierCollection.RARE, rareWeight, false)));
        if (canApplyEpic) pool.apply(new Weight<>(new WeightWrapper<>(ModifierCollection.EPIC, epicWeight, false)));

        val result = pool.generatePool();

        if (result.isEmpty()) this.setCurrentModifiers(ModifierCollection.NONE);
        else this.setCurrentModifiers(result.getFirst().weightWrapper().getWrapped());
    }

    public void reset() {
        this.setCurrentModifiers(ModifierCollection.NONE);
        this.setCountAmplifiers(0);
    }

    public int getCountAmplifiers() {
        return countAmplifiers.get();
    }

    private void setCountAmplifiers(final int countAmplifiers) {
        for (;;) {
            var old = getCountAmplifiers();
            if (this.countAmplifiers.compareAndSet(old, countAmplifiers)) break;
        }
    }

    public ModifierCollection getCurrentModifiers() {
        return currentModifiers.get();
    }

    private void setCurrentModifiers(@NotNull final ModifierCollection currentModifiers) {
        for (;;) {
            var old = getCurrentModifiers();
            if (this.currentModifiers.compareAndSet(old, currentModifiers)) break;
        }
    }

}
