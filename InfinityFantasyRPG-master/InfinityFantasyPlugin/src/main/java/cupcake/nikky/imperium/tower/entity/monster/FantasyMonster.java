package cupcake.nikky.imperium.tower.entity.monster;

import cupcake.nikky.imperium.tower.entity.monster.data.MonsterDataHandler;
import cupcake.nikky.imperium.tower.entity.monster.data.MonsterData;
import cupcake.nikky.imperium.tower.entity.monster.goal.GoalType;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FantasyMonster extends DefaultMonster {

    @Getter
    private final List<Consumer<Monster>> onSpawnListeners = new ArrayList<>();

    public FantasyMonster(@NotNull final Location spawnLocation, @NotNull final MonsterData monsterData, @NotNull final MonsterType monsterType,
                          @Nullable final ItemStack[] armor, @Nullable final ItemStack itemInMainHand, @Nullable final ItemStack itemInOffHand) {
        super(monsterData.getName(), monsterData.getEntityType(), monsterType, monsterData.getConduct(), monsterData.getGoalType().getGoalUpdater(), monsterData.getLevel(),
                monsterData.getRespawnTicks(), spawnLocation, monsterData, armor, itemInMainHand, itemInOffHand);

        new MonsterDataHandler(monsterData).applyLoot();

        this.setDropExpLoot(monsterData.getExpLoot());
    }

    public FantasyMonster(@NotNull final Location spawnLocation, @NotNull final MonsterData monsterData, @NotNull final MonsterType monsterType) {
        this(spawnLocation, monsterData, monsterType, null, monsterData.getGoalType() == GoalType.ARROW ? new ItemStack(Material.BOW) : null, null);
    }

    @Override
    public void onSpawn() {
        super.onSpawn();

        onSpawnListeners.forEach(handler -> handler.accept(this));
    }
}
