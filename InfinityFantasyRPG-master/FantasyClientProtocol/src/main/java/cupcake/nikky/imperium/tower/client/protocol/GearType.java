package cupcake.nikky.imperium.tower.client.protocol;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum GearType {

    HELMET(1, 0, "Шлем"),
    BODY(2, 1, "Нагрудник"),
    LEGGINGS(3, 2, "Поножи"),
    BOOTS(4, 3, "Боты"),
    RING(5, 4, "Кольцо"),
    AMULET(6, 5, "Амулет");

    @Getter
    private final int id;
    @Getter
    private final int position;
    @Getter
    private final String name;

    public static GearType findById(final int id) {
        for (GearType value : values()) {
            if (value.getId() == id) return value;
        }

        return null;
    }

}
