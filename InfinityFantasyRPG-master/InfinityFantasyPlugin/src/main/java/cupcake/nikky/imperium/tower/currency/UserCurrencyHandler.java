package cupcake.nikky.imperium.tower.currency;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.donate.DonateData;
import cupcake.nikky.imperium.tower.user.User;
import lombok.val;
import org.jetbrains.annotations.NotNull;

public class UserCurrencyHandler {

    private final User user;

    public UserCurrencyHandler(@NotNull final User user) {
        this.user = user;
    }

    public int getCurrency(@NotNull final CurrencyType currencyType) {
        if (currencyType == CurrencyType.GRAN) {
            return user.getGrans();
        } else if (currencyType == CurrencyType.FIRM) {
            val donateData = user.findDonateSettings().donateData();
            return donateData.getCurrency();
        } else if (currencyType == CurrencyType.MINES) {
            return user.getMines();
        } else if (currencyType == CurrencyType.FIRM_PRIVATE) {
            val donateData = user.findDonateSettings().donateData();
            return donateData.getPrivateCurrency();
        } else if (currencyType == CurrencyType.FLOWER) {
            return user.getFlowers();
        }

        return 0;
    }

    public boolean withdrawDonateCurrencyWithPriority(final int amount) {
        val privateCurrency = getCurrency(CurrencyType.FIRM_PRIVATE);
        if (privateCurrency >= amount) {
            return withdrawCurrency(CurrencyType.FIRM_PRIVATE, amount);
        }

        val currency = getCurrency(CurrencyType.FIRM);

        if (currency + privateCurrency >= amount) {
            if (withdrawCurrency(CurrencyType.FIRM_PRIVATE, privateCurrency)) {
                if (withdrawCurrency(CurrencyType.FIRM, amount - privateCurrency)) {
                    return true;
                } else {
                    depositCurrency(CurrencyType.FIRM_PRIVATE, privateCurrency);
                }
            } else return false;
        }

        return false;
    }

    public boolean withdrawCurrency(@NotNull final CurrencyType currencyType, final int amount) {
        if (amount == 0) return true;

        if (currencyType == CurrencyType.GRAN) {
            if (user.getGrans() < amount) return false;

            user.changeGrans(-amount);
        } else if (currencyType == CurrencyType.FIRM) {
            val donateData = user.findDonateSettings().donateData();
            if (donateData.getCurrency() < amount) return false;

            donateData.changeCurrency(-amount);
        } else if (currencyType == CurrencyType.MINES) {
            if (user.getMines() < amount) return false;

            user.changeMines(-amount);
        } else if (currencyType == CurrencyType.FIRM_PRIVATE) {
            val donateData = user.findDonateSettings().donateData();
            if (donateData.getPrivateCurrency() < amount) return false;

            donateData.changePrivateCurrency(-amount);
        } else if (currencyType == CurrencyType.FLOWER) {
            if (user.getFlowers() < amount) return false;

            user.changeFlowers(-amount);
        }

        Fantasy.clientProvider().updateCurrency(user);

        return true;
    }

    public boolean depositCurrency(@NotNull final CurrencyType currencyType, final long amount) {
        if (amount == 0) return true;

        if (currencyType == CurrencyType.GRAN) {
            user.setGrans((int) Math.min(user.getGrans() + amount, Integer.MAX_VALUE));
        } else if (currencyType == CurrencyType.FIRM) {
            val donateData = user.findDonateSettings().donateData();
            donateData.changeCurrency((int) amount);
        } else if (currencyType == CurrencyType.MINES) {
            user.setMines((int) Math.min(user.getMines() + amount, Integer.MAX_VALUE));
        } else if (currencyType == CurrencyType.FIRM_PRIVATE) {
            val donateData = user.findDonateSettings().donateData();
            donateData.changePrivateCurrency((int) amount);
        } else if (currencyType == CurrencyType.FLOWER) {
            user.setFlowers((int) Math.min(user.getFlowers() + amount, Integer.MAX_VALUE));
        }

        Fantasy.clientProvider().updateCurrency(user);

        return true;
    }

}
