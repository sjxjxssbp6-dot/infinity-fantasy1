package cupcake.nikky.imperium.tower.user;

import cupcake.nikky.imperium.tower.currency.CurrencyType;
import cupcake.nikky.imperium.tower.currency.UserCurrencyHandler;
import org.jetbrains.annotations.NotNull;

public interface CurrencyUser {

    int getMines();

    void changeMines(int delta);

    void setMines(int value);

    int getFlowers();

    void changeFlowers(int delta);

    void setFlowers(int value);

    int getGrans();

    void changeGrans(int delta);

    void setGrans(int value);

    UserCurrencyHandler getCurrencyHandler();

    default int getCurrency(@NotNull final CurrencyType currencyType) {
        return getCurrencyHandler().getCurrency(currencyType);
    }

    default boolean changeCurrency(@NotNull final CurrencyType currencyType, final int amount) {
        return getCurrencyHandler().depositCurrency(currencyType, amount);
    }

}
