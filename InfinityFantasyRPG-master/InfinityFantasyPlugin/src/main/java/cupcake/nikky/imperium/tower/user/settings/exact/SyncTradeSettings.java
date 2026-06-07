package cupcake.nikky.imperium.tower.user.settings.exact;

import cupcake.nikky.imperium.tower.item.ItemObject;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.user.settings.BaseUserSettings;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.Map;

public class SyncTradeSettings extends BaseUserSettings {

    private static final Int2ObjectMap<Map<ItemObject, Integer>> wayItems = new Int2ObjectOpenHashMap<>();

    public SyncTradeSettings(User user) {
        super(user);

        if (wayItems.isEmpty()) {
            wayItems.put(60, Map.of(ItemObject.WAY_FRAGMENT_1, 1));
            wayItems.put(61, Map.of(ItemObject.WAY_FRAGMENT_1, 1));
            wayItems.put(62, Map.of(ItemObject.WAY_FRAGMENT_1, 2));
            wayItems.put(63, Map.of(ItemObject.WAY_FRAGMENT_1, 2, ItemObject.WAY_FRAGMENT_2, 1));
            wayItems.put(64, Map.of(ItemObject.WAY_FRAGMENT_1, 2, ItemObject.WAY_FRAGMENT_2, 2));
            wayItems.put(65, Map.of(ItemObject.WAY_FRAGMENT_1, 2, ItemObject.WAY_FRAGMENT_2, 2, ItemObject.WAY_FRAGMENT_3, 1));
            wayItems.put(66, Map.of(ItemObject.WAY_FRAGMENT_1, 2, ItemObject.WAY_FRAGMENT_2, 2, ItemObject.WAY_FRAGMENT_3, 2));
            wayItems.put(67, Map.of(ItemObject.WAY_FRAGMENT_1, 2, ItemObject.WAY_FRAGMENT_2, 2, ItemObject.WAY_FRAGMENT_3, 2, ItemObject.WAY_FRAGMENT_4, 1));
            wayItems.put(68, Map.of(ItemObject.WAY_FRAGMENT_1, 2, ItemObject.WAY_FRAGMENT_2, 2, ItemObject.WAY_FRAGMENT_3, 2, ItemObject.WAY_FRAGMENT_4, 2));

            wayItems.put(69, Map.of(
                    ItemObject.WAY_FRAGMENT_1, 2, ItemObject.WAY_FRAGMENT_2, 2, ItemObject.WAY_FRAGMENT_3, 2, ItemObject.WAY_FRAGMENT_4, 2,
                    ItemObject.WAY_FRAGMENT_5, 1)
            );
            wayItems.put(70, Map.of(
                    ItemObject.WAY_FRAGMENT_1, 2, ItemObject.WAY_FRAGMENT_2, 2, ItemObject.WAY_FRAGMENT_3, 2, ItemObject.WAY_FRAGMENT_4, 2,
                    ItemObject.WAY_FRAGMENT_5, 2)
            );

            wayItems.put(71, Map.of(
                    ItemObject.WAY_FRAGMENT_1, 2, ItemObject.WAY_FRAGMENT_2, 2, ItemObject.WAY_FRAGMENT_3, 2, ItemObject.WAY_FRAGMENT_4, 2,
                    ItemObject.WAY_FRAGMENT_5, 2, ItemObject.WAY_FRAGMENT_6, 1)
            );
            wayItems.put(72, Map.of(
                    ItemObject.WAY_FRAGMENT_1, 2, ItemObject.WAY_FRAGMENT_2, 2, ItemObject.WAY_FRAGMENT_3, 2, ItemObject.WAY_FRAGMENT_4, 2,
                    ItemObject.WAY_FRAGMENT_5, 2, ItemObject.WAY_FRAGMENT_6, 2)
            );

            wayItems.put(73, Map.of(
                    ItemObject.WAY_FRAGMENT_1, 2, ItemObject.WAY_FRAGMENT_2, 2, ItemObject.WAY_FRAGMENT_3, 2, ItemObject.WAY_FRAGMENT_4, 2,
                    ItemObject.WAY_FRAGMENT_5, 2, ItemObject.WAY_FRAGMENT_6, 2, ItemObject.WAY_FRAGMENT_7, 1)
            );
            wayItems.put(74, Map.of(
                    ItemObject.WAY_FRAGMENT_1, 2, ItemObject.WAY_FRAGMENT_2, 2, ItemObject.WAY_FRAGMENT_3, 2, ItemObject.WAY_FRAGMENT_4, 2,
                    ItemObject.WAY_FRAGMENT_5, 2, ItemObject.WAY_FRAGMENT_6, 2, ItemObject.WAY_FRAGMENT_7, 2)
            );

            wayItems.put(75, Map.of(
                    ItemObject.WAY_FRAGMENT_1, 2, ItemObject.WAY_FRAGMENT_2, 2, ItemObject.WAY_FRAGMENT_3, 2, ItemObject.WAY_FRAGMENT_4, 2,
                    ItemObject.WAY_FRAGMENT_5, 2, ItemObject.WAY_FRAGMENT_6, 2, ItemObject.WAY_FRAGMENT_7, 2, ItemObject.WAY_FRAGMENT_8, 1)
            );
            wayItems.put(76, Map.of(
                    ItemObject.WAY_FRAGMENT_1, 2, ItemObject.WAY_FRAGMENT_2, 2, ItemObject.WAY_FRAGMENT_3, 2, ItemObject.WAY_FRAGMENT_4, 2,
                    ItemObject.WAY_FRAGMENT_5, 2, ItemObject.WAY_FRAGMENT_6, 2, ItemObject.WAY_FRAGMENT_7, 2, ItemObject.WAY_FRAGMENT_8, 2)
            );
        }
    }

    public Map<ItemObject, Integer> findTradeWaysByLevel(final int level) {
        if (level < 60) return wayItems.get(60);
        else if (level > 76) return wayItems.get(76);
        return wayItems.get(level);
    }

}
