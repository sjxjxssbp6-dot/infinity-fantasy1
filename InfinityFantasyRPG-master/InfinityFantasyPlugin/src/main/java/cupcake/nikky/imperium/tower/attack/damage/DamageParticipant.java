package cupcake.nikky.imperium.tower.attack.damage;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.attribute.AttributeMap;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.entity.monster.Monster;
import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.user.User;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;

@Getter
public class DamageParticipant {

    private final int level;
    private final AttributeMap attributes;
    private final LivingEntity livingEntity;
    @Setter private ItemStack itemInHand;
    @Setter private ImperiumItem imperiumItemInHand;

    public DamageParticipant() {
        this.level = -1;
        this.attributes = new AttributeMap(new EnumMap<>(ImperiumAttribute.class));
        this.livingEntity = null;
    }

    public DamageParticipant(@NotNull final User user) {
        val player = user.getSpigot();

        this.level = user.getLevel();
        this.attributes = user.getAttributes();
        this.livingEntity = player;
        this.itemInHand = player.getInventory().getItem(player.getInventory().getHeldItemSlot());
        this.imperiumItemInHand = Fantasy.itemProvider().find(itemInHand);
    }

    public DamageParticipant(@NotNull final Monster monster) {
        this.level = monster.getLevel();
        this.attributes = monster.getAttributes();
        this.livingEntity = monster.getSpigot();
        this.itemInHand = null;
        this.imperiumItemInHand = null;
    }

    public ParticipantCallback entityTypeCallback() {
        return new ParticipantCallback(getLivingEntity());
    }

}
