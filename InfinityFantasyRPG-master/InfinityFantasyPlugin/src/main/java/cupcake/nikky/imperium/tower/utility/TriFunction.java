package cupcake.nikky.imperium.tower.utility;

@FunctionalInterface
public interface TriFunction<T, U, K, R> {

    R apply(T t, U u, K k);

}
