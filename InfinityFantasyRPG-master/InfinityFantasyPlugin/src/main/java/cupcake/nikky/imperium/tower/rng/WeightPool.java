package cupcake.nikky.imperium.tower.rng;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import me.anfanik.steda.api.utility.MathUtility;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WeightPool<T extends WeightWrapper<?>> {

    @Getter @Setter
    private int initialSize;
    @Getter @Setter
    private int repeatTime;
    @Getter
    private boolean finalized;
    private final List<Weight<T>> storage = new ArrayList<>();

    public WeightPool(int repeatTime) {
        this.initialSize = 0;
        this.repeatTime = repeatTime;
    }

    public WeightPool(int initialSize, int repeatTime) {
        this.initialSize = initialSize;
        this.repeatTime = repeatTime;
    }

    @SafeVarargs
    public final WeightPool<T> apply(@NotNull final Weight<T>... data) {
        this.storage.addAll(Arrays.asList(data));
        return this;
    }

    public final WeightPool<T> apply(@NotNull final Collection<Weight<T>> data) {
        this.storage.addAll(data);
        return this;
    }

    public int findTotalSize() {
        return storage.stream().map(tWeight -> tWeight.weightWrapper().getWeight()).reduce(0, Integer::sum);
    }

    public List<Weight<T>> copyStorage() {
        return storage.stream().map(Weight::copy).collect(Collectors.toList());
    }

    public List<Weight<T>> generatePool() {
        this.finalizePool();
        return this.generatePool(this.copyStorage(), this.getRepeatTime());
    }

    public void finalizePool() {
        if (finalized) return;

        val size = storage.size();
        storage.stream()
                .filter(Objects::nonNull)
                .filter(tWeight -> tWeight.weightWrapper().getWeightApplier() != null)
                .forEach(tWeight -> tWeight.weightWrapper().setWeight(tWeight.weightWrapper().getWeightApplier().apply(size)));
        finalized = true;
    }

    public List<Weight<T>> generatePool(@NotNull final List<Weight<T>> storage, final int repeatTime) {
        this.finalizePool();
        val copyStorage = storage.stream().sorted(Comparator.comparingInt(w -> w.weightWrapper().getWeight())).collect(Collectors.toList());
        final List<Weight<T>> result = new ArrayList<>();

        int poolSize = this.getInitialSize() == 0 ? this.findTotalSize() : this.getInitialSize();
        if (poolSize <= 0) return result;

        for (int i = 0; i < repeatTime; i++) {
            val point = MathUtility.randomInRange(1, poolSize);
            val foundWeight = this.findWeightByPoint(copyStorage.stream().sorted(Comparator.comparingInt(w -> w.weightWrapper().getWeight())), point);

            if (foundWeight != null) {
                val wrapper = foundWeight.weightWrapper();

                int newWeight = this.onSuccess(foundWeight);

                if (!wrapper.isCancelPoolSizing())
                    poolSize = poolSize - (wrapper.getWeight() - newWeight);

                wrapper.setWeight(newWeight);
                result.add(foundWeight);
            }

            for (Weight<T> weightObject : copyStorage.stream().filter(weightObject -> !weightObject.equals(foundWeight)).collect(Collectors.toList())) {
                val newWeight = this.onFail(weightObject, foundWeight);
                if (this.getInitialSize() == 0 && !weightObject.weightWrapper().isCancelPoolSizing()) {
                    poolSize = poolSize - (weightObject.weightWrapper().getWeight() - newWeight);
                }

                weightObject.weightWrapper().setWeight(newWeight);
                if (newWeight == 0) copyStorage.remove(weightObject);
            }
        }

        return result;
    }

    private Weight<T> findWeightByPoint(@NotNull final Stream<Weight<T>> storage, final int point) {
        val currentWeight = new AtomicInteger(0);

        return storage.filter(weightObject -> {
            val weight = weightObject.weightWrapper().getWeight();
            return point <= currentWeight.addAndGet(weight);
        }).findFirst().orElse(null);
    }


    public int onFail(Weight<T> weight, Weight<T> winner) {
        return weight.onFail().apply(winner);
    }

    public int onSuccess(Weight<T> weight) {
        return weight.onSuccess().apply(weight);
    }

}
