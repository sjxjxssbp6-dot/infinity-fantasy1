package cupcake.nikky.imperium.tower.mount;

import lombok.Builder;
import lombok.Getter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;

@Getter
@Builder
public class MountSettings {

    private String title;
    private EntityType type;
    private BiConsumer<Entity, Player> entitySpawnHandler;
    private ItemStack icon;
    private String texture;
    @Builder.Default private double speed = 100;
    @Builder.Default private MountRequirement mountRequirement = MountRequirement.of(player -> true);

}
