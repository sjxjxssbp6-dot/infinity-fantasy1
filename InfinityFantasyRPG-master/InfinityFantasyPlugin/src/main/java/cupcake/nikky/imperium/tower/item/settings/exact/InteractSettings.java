package cupcake.nikky.imperium.tower.item.settings.exact;

import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.settings.ItemSettings;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Setter @Getter
@Accessors(fluent = true)
public class InteractSettings extends ItemSettings {

    private Consumer<Player> onRightClick = player -> {};
    private BiConsumer<Player, LivingEntity> onRightClickAtEntity = (player, entity) -> {};

    public InteractSettings(ImperiumItem item) {
        super(item);
    }
}
