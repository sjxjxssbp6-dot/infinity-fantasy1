package cupcake.nikky.imperium.tower.rng;

@FunctionalInterface
public interface WeightApplier {

    int apply(final int totalAmount);

}
