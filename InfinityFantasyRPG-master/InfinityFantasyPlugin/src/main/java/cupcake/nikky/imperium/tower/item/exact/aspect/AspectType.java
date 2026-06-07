package cupcake.nikky.imperium.tower.item.exact.aspect;

import cupcake.nikky.imperium.tower.item.ItemType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum AspectType {

    WEAPON("Оружие", Collections.singletonList(ItemType.WEAPON)),
    ARMOR("Броня", Collections.singletonList(ItemType.ARMOR)),
    ALL("Везде", Arrays.asList(ItemType.WEAPON, ItemType.ARMOR));

    private final String typeTitle;
    private final List<ItemType> itemTypes;

}
