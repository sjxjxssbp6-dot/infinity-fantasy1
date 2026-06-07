package cupcake.nikky.imperium.tower.donate.shop.sample;

import cupcake.nikky.imperium.tower.donate.DonateData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Consumer;

public interface DonateSample<T> {

    Map<T, Integer> findPrices();

    Consumer<Player> createProcess(@NotNull final DonateData donateData, @NotNull final T t, final int price);

}
