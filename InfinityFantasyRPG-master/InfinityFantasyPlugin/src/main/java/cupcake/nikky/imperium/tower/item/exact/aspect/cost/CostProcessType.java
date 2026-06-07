package cupcake.nikky.imperium.tower.item.exact.aspect.cost;

import cupcake.nikky.imperium.tower.attribute.AttributeMap;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.utility.TriFunction;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.anfanik.steda.api.utility.TextUtility;

import java.util.function.BiFunction;
import java.util.function.Function;

@Getter
@RequiredArgsConstructor
public enum CostProcessType {

    DEFAULT_HEALTH((user, attributeMap, value) -> {
        user.changeHealth(-value);
        return !user.isShouldDeath();
    }, value -> "&f" + value + " &c" + TextUtility.transformViaAmount(value, "здоровье", "здоровья", "здоровья")),

    DEFAULT_MANA((user, attributeMap, value) -> user.getManaController().changeMana(-value), value -> "&f" + value + " &b" + TextUtility.transformViaAmount(value, "мана", "маны", "маны")),

    MANA_RESERVE_PERCENT((user, attributeMap, value) -> {
        if (!user.getManaController().canReservePercent(value, attributeMap)) return false;

        attributeMap.find(ImperiumAttribute.MANA_RESERVED).applyAdditional(user.getManaController().transformReservePercent(value));
        return true;
    }, value -> "&f" + value + "% &f" + TextUtility.transformViaAmount(value, "резервация &bманы", "резервация &bманы", "резервация &bманы")),

    MANA_RESERVE_VALUE((user, attributeMap, value) -> {
        if (!user.getManaController().canReserveValue(value, attributeMap)) return false;

        attributeMap.find(ImperiumAttribute.MANA_RESERVED_VALUE).applyAdditional(user.getManaController().transformReserveValue(value));
        return true;
    }, value -> "&f" + value + " &f" + TextUtility.transformViaAmount(value, "резервация &bманы", "резервация &bманы", "резервация &bманы"));

    private final TriFunction<User, AttributeMap, Integer, Boolean> process;
    private final Function<Integer, String> descriptionProcess;

}
