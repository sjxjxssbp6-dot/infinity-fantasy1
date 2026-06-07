package cupcake.nikky.imperium.tower.entity.monster;

import cupcake.nikky.imperium.tower.entity.monster.damage.cast.InboundDamageCast;
import cupcake.nikky.imperium.tower.entity.monster.damage.cast.OutboundDamageCast;
import cupcake.nikky.imperium.tower.entity.monster.data.MonsterData;
import cupcake.nikky.imperium.tower.entity.monster.goal.GoalType;
import lombok.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@Getter
@Setter
@RequiredArgsConstructor(staticName = "of")
public class FantasyMonsterBuilder {

    private final List<Consumer<Monster>> onSpawnListeners = new ArrayList<>();
    private final List<Function<Monster, OutboundDamageCast>> outboundDamageCasts = new ArrayList<>();
    private final List<Function<Monster, InboundDamageCast>> inboundDamageCasts = new ArrayList<>();

    private ItemStack itemInHand;
    private ItemStack itemInOffhand;
    private ItemStack[] armor;

    private double auraRadius;

    public FantasyMonsterBuilder(ItemStack itemInHand, ItemStack itemInOffhand, ItemStack[] armor) {
        this.itemInHand = itemInHand;
        this.itemInOffhand = itemInOffhand;
        this.armor = armor;
    }

    public static FantasyMonsterBuilder of(ItemStack itemInHand, ItemStack itemInOffhand, ItemStack[] armor) {
        return new FantasyMonsterBuilder(itemInHand, itemInOffhand, armor);
    }

    public FantasyMonsterBuilder addSpawnListener(@NotNull final Consumer<Monster> handler) {
        this.onSpawnListeners.add(handler);
        return this;
    }

    public FantasyMonsterBuilder addOutboundDamageCast(@NotNull final Function<Monster, OutboundDamageCast> outboundDamageCast) {
        this.outboundDamageCasts.add(outboundDamageCast);
        return this;
    }

    public FantasyMonsterBuilder addInboundDamageCast(@NotNull final Function<Monster, InboundDamageCast> inboundDamageCast) {
        this.inboundDamageCasts.add(inboundDamageCast);
        return this;
    }

    public FantasyMonsterBuilder setAuraRadius(final double radius) {
        auraRadius = radius;
        return this;
    }

    public FantasyMonster build(@NotNull final Location location, @NotNull final MonsterData data, @NotNull final MonsterType type) {
        val monster = new FantasyMonster(location, data, type, armor, data.getGoalType() == GoalType.ARROW ? new ItemStack(Material.BOW) : itemInHand, itemInOffhand);
        monster.getOnSpawnListeners().addAll(onSpawnListeners);
        monster.setAuraRadius(Math.max(auraRadius, 0));
        outboundDamageCasts.forEach(outboundDamageCast -> monster.appendCast(outboundDamageCast.apply(monster)));
        inboundDamageCasts.forEach(inboundDamageCast -> monster.appendCast(inboundDamageCast.apply(monster)));
        return monster;
    }

}
