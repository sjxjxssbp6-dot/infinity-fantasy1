package cupcake.nikky.imperium.tower.currency;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.anfanik.steda.api.utility.TextUtility;

import java.util.function.Function;

@Getter
@RequiredArgsConstructor
public enum CurrencyType {

    GRAN(1, amount -> TextUtility.transformViaAmount(amount, "грана", "граны", "гран")),
    FIRM(2, amount -> TextUtility.transformViaAmount(amount, "фирм-монета", "фирм-монеты", "фирм-монет")),
    FIRM_PRIVATE(4, amount -> TextUtility.transformViaAmount(amount, "личная фирм-монета", "личные фирм-монеты", "личных фирм-монет")),
    FLOWER(5, amount -> TextUtility.transformViaAmount(amount, "цветок", "цветка", "цветков")),
    MINES(3, amount -> TextUtility.transformViaAmount(amount, "мирна", "мирны", "мирн"));

    private final int clientId;
    private final Function<Integer, String> textTransformer;

}
