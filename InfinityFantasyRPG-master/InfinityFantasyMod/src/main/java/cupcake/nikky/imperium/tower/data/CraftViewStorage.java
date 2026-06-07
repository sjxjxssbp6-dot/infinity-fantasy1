package cupcake.nikky.imperium.tower.data;

import dev.xdark.clientapi.item.ItemStack;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CraftViewStorage {

    @Getter
    private static final CraftViewStorage instance = new CraftViewStorage();

    @Getter @Setter @Accessors(fluent = true)
    private ItemStack craftStack;
    @Getter @Setter @Accessors(fluent = true)
    private int state;

    public void clear() {
        this.state = 0;
        this.craftStack = null;
    }

}
