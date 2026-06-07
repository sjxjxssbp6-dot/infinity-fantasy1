package cupcake.nikky.imperium.tower.entity.monster.data;

import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.entity.monster.conduct.ConductType;
import cupcake.nikky.imperium.tower.entity.monster.goal.GoalType;
import cupcake.nikky.imperium.tower.entity.monster.loot.LootStrategies;
import cupcake.nikky.imperium.tower.entity.monster.loot.other.ExpLoot;
import cupcake.nikky.imperium.tower.entity.monster.modifier.ModifierCollection;
import cupcake.nikky.imperium.tower.rng.exact.loot.LootWeightPool;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.core.datasync.EntityColorData;
import ru.cristalix.core.datasync.EntityScaleData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Builder
@Getter
@Setter
public class MonsterData {

    @Builder.Default private boolean applied = false;

    private String name;
    private EntityType entityType;
    private Object2DoubleMap<ImperiumAttribute> additionalAttributes;
    @Builder.Default private ConductType conduct = ConductType.AGGRESSIVE;
    @Builder.Default private GoalType goalType = GoalType.MELEE;
    private int level;
    @Builder.Default private boolean showRespawnText = false;
    @Builder.Default private int respawnTicks = 20 * 12;
    @Builder.Default private int attackSpeed = 10;
    @Builder.Default private double distanceToAttack = -1;
    private ExpLoot expLoot;
    @Builder.Default private List<LootStrategies> lootStrategies = Arrays.asList(
            LootStrategies.GENERAL, LootStrategies.ASPECT,
            LootStrategies.EQUIP, LootStrategies.UNIQUE_ITEMS_CHANCE_PREPROCESS,
            LootStrategies.WAY_FRAGMENT_CHANCE_PREPROCESS);
    private LootWeightPool lootWeightPool;
    @Builder.Default private EntityScaleData scaleData = EntityScaleData.DEFAULT;
    @Builder.Default private EntityColorData colorData = EntityColorData.DEFAULT;
    @Builder.Default private String attacksDescription = "&aНе имеет особых атак";
    @Builder.Default private boolean unique = false;
    private ModifierCollection modifierCollection;
    @Builder.Default private boolean isPandoraEntity = false;

    @Builder.Default private boolean invalidateWhenForgotTarget = false;

    @Builder.Default private double healthMultiplier = 1D;
    @Builder.Default private double physicalDamageMultiplier = 1D;
    @Builder.Default private double elementalAndChaosDamageMultiplier = 1D;
    @Builder.Default private double speed = 100D;
    @Builder.Default private double defense = 0D;

    @Builder.Default private double physicalDamage = 50D;
    @Builder.Default private double fireDamage = 12.5D;
    @Builder.Default private double coldDamage = 12.5D;
    @Builder.Default private double lightningDamage = 12.5D;
    @Builder.Default private double chaosDamage = 12.5D;

    @Builder.Default private double fireResistance = 0D;
    @Builder.Default private double coldResistance = 0D;
    @Builder.Default private double lightningResistance = 0D;
    @Builder.Default private double chaosResistance = 0D;

    @Builder.Default private double fireResistancePenetration = 0D;
    @Builder.Default private double coldResistancePenetration = 0D;
    @Builder.Default private double lightningResistancePenetration = 0D;
    @Builder.Default private double chaosResistancePenetration = 0D;

    @Builder.Default private double defensePenetration = 0D;
    @Builder.Default private int expGoal = 100;

    public MonsterData applyLootStrategy(@NotNull final LootStrategies... lootStrategies) {
        if (!(this.lootStrategies instanceof ArrayList<LootStrategies>))
            this.lootStrategies = new ArrayList<>(this.lootStrategies);
        this.lootStrategies.addAll(Arrays.asList(lootStrategies));
        return this;
    }

    public MonsterData removeLootStrategy(@NotNull final LootStrategies... lootStrategies) {
        if (!(this.lootStrategies instanceof ArrayList<LootStrategies>))
            this.lootStrategies = new ArrayList<>(this.lootStrategies);
        this.lootStrategies.removeAll(Arrays.asList(lootStrategies));
        return this;
    }

    public MonsterData copy() {
        return MonsterData.builder()
                .lootStrategies(new ArrayList<>(lootStrategies))
                .showRespawnText(showRespawnText)
                .applied(applied)
                .goalType(goalType)
                .name(name)
                .attackSpeed(attackSpeed)
                .distanceToAttack(distanceToAttack)
                .colorData(colorData)
                .scaleData(scaleData)
                .entityType(entityType)
                .conduct(conduct)
                .level(level)
                .respawnTicks(respawnTicks)
                .expLoot(expLoot)
                .unique(unique)
                .lootWeightPool(LootWeightPool.newInstance(lootWeightPool))
                .physicalDamage(physicalDamage)
                .fireDamage(fireDamage)
                .coldDamage(coldDamage)
                .lightningDamage(lightningDamage)
                .chaosDamage(chaosDamage)
                .healthMultiplier(healthMultiplier)
                .physicalDamageMultiplier(physicalDamageMultiplier)
                .elementalAndChaosDamageMultiplier(elementalAndChaosDamageMultiplier)
                .speed(speed)
                .defense(defense)
                .fireResistance(fireResistance)
                .coldResistance(coldResistance)
                .lightningResistance(lightningResistance)
                .chaosResistance(chaosResistance)
                .defensePenetration(defensePenetration)
                .expGoal(expGoal)
                .invalidateWhenForgotTarget(invalidateWhenForgotTarget)
                .additionalAttributes(additionalAttributes)
                .modifierCollection(modifierCollection)
                .isPandoraEntity(isPandoraEntity)
                .build();
    }

    public static class MonsterDataBuilder {

        public MonsterDataBuilder applyAdditionalAttribute(@NotNull final ImperiumAttribute attribute, final double value) {
            if (this.additionalAttributes == null)
                this.additionalAttributes = new Object2DoubleOpenHashMap<>();
            this.additionalAttributes.put(attribute, value);
            return this;
        }

    }

}
