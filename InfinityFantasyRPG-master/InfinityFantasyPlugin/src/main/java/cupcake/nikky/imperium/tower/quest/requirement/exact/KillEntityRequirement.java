package cupcake.nikky.imperium.tower.quest.requirement.exact;

import cupcake.nikky.imperium.tower.entity.monster.Monster;
import cupcake.nikky.imperium.tower.entity.monster.MonsterType;
import cupcake.nikky.imperium.tower.quest.requirement.QuestRequirement;
import lombok.val;
import me.anfanik.steda.api.utility.Pair;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class KillEntityRequirement extends QuestRequirement {

    private final Map<MonsterType, Integer> goals = new EnumMap<>(MonsterType.class);
    private final Map<MonsterType, Integer> levels = new EnumMap<>(MonsterType.class);

    @Override
    public KillEntityRequirement clone() {
        val goal = new KillEntityRequirement();
        goal.goals.putAll(goals);
        goal.levels.putAll(levels);
        goal.getData().putAll(getData());
        goal.setDone(goal.isDone());
        return goal;
    }

    @SafeVarargs
    public final KillEntityRequirement appendGoal(@NotNull final Pair<MonsterType, Integer>... pairs) {
        for (Pair<MonsterType, Integer> pair : pairs) {
            this.goals.put(pair.getFirst(), pair.getSecond());
            this.levels.put(pair.getFirst(), -1);
        }

        return this;
    }

    public final KillEntityRequirement appendGoal(@NotNull final MonsterType type, final int amount, final int minLevel) {
        this.goals.put(type, amount);
        this.levels.put(type, minLevel);

        return this;
    }

    @Override
    public List<String> goalDescription() {
        final List<String> result = new ArrayList<>();

        goals.keySet().forEach(type -> {
            var amount = getData().getOrDefault(type.name(), 0);

            val isDone = amount >= goals.get(type);
            val minLevel = levels.getOrDefault(type, -1);
            val levelLine = minLevel == -1 ? "" : " &7(&6" + minLevel + "+ &7уровня)";

            result.add("&7Убить " + type.getMonsterData().getName() + levelLine +
                    "&7: " + (isDone ? "&2" : "&6") + amount + "&7/" + (isDone ? "&2" : "&6") + goals.get(type));
        });

        return result;
    }

    public void onKill(Player killer, Monster monster, boolean notification) {
        if (this.isDone()) return;

        if (goals.containsKey(monster.getMonsterType())) {
            val minLevel = levels.get(monster.getMonsterType());
            if (monster.getLevel() < minLevel) return;

            val need = goals.get(monster.getMonsterType());

            val key = monster.getMonsterType().name();
            val current = getData().getOrDefault(key, 0);

            if (Objects.equals(current, need)) return;

            getData().put(key, Math.min(current + 1, need));

            if (notification && Objects.equals(getData().get(key), need)) {
                sendNotification(killer);
            }

            this.checkDone();
        }
    }

    @Override
    protected void checkDone() {
        boolean tempDone = true;

        for (MonsterType type : goals.keySet()) {
            if (goals.get(type) > getData().getOrDefault(type.name(), 0)) {
                tempDone = false;
                break;
            }
        }

        setDone(tempDone);
    }

}
