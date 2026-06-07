package cupcake.nikky.imperium.tower.data;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class InventoryOverlayTextStorage {

    @Getter
    private static final InventoryOverlayTextStorage instance = new InventoryOverlayTextStorage();

    @Getter @Setter
    @Accessors(fluent = true)
    private String[] text = new String[0];

    @Getter @Setter
    @Accessors(fluent = true)
    private String seasonUrl;

}
