package cupcake.nikky.imperium.tower.utility;

import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.ItemType;
import cupcake.nikky.imperium.tower.item.exact.aspect.event.AspectEvent;
import cupcake.nikky.imperium.tower.item.exact.aspect.event.AspectHandEvent;
import cupcake.nikky.imperium.tower.user.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import me.anfanik.steda.api.utility.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AspectUtility {

    public static List<ImperiumItem> findItemsWithAspect(@NotNull final User user, final boolean checkHand) {
        val inventory = user.getFantasyInventory();

        ImperiumItem heldItem = inventory.getHeldItem();

        if (checkHand && heldItem != null && heldItem.getImperiumType() != ItemType.WEAPON) {
            heldItem = null;
        }

        return Stream.of(inventory.getHelmet(), inventory.getBody(), inventory.getLeggings(), inventory.getBoots(), heldItem)
                .filter(Objects::nonNull)
                .filter(item -> item.getAspectSettings().aspect() != null)
                .collect(Collectors.toList());
    }

    public static <T extends AspectEvent> List<Pair<ImperiumItem, Consumer<T>>> findAspectEvents(@NotNull final User user, @NotNull final Class<T> clazz) {
        final Set<String> markedEvents = new HashSet<>();
        return AspectUtility.findItemsWithAspect(user, clazz == AspectHandEvent.class).stream()
                .map(item -> {
                    val event = item.getAspectSettings().aspect().findEvent(clazz);
                    if (event == null) return null;

                    return new Pair<>(item, event);
                }).filter(Objects::nonNull)
                .filter(paired -> markedEvents.add(paired.getFirst().getAspectSettings().aspect().title()))
                .collect(Collectors.toList());
    }

}
