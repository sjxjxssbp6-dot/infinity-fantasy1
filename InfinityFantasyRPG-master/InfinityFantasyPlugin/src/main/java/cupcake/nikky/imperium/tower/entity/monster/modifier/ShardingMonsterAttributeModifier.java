package cupcake.nikky.imperium.tower.entity.monster.modifier;

import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.entity.monster.Monster;
import cupcake.nikky.imperium.tower.entity.monster.shard.ShardingMonster;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ShardingMonsterAttributeModifier extends AttributeModifier {

    public ShardingMonsterAttributeModifier() {
        super(null, null, null);
    }

    @Override
    public void applyModifier(@NotNull Monster monster) {
        if (!(monster instanceof ShardingMonster shardingMonster)) return;

        if (shardingMonster.getIncreaseHealth() <= 0D && shardingMonster.getIncreaseDamage() <= 0D) return;

        val healthInstance = monster.getAttributes().find(ImperiumAttribute.HEALTH);
        healthInstance.applyBaseValue(healthInstance.getBaseValue() * (shardingMonster.getIncreaseHealth() / 100D));

        Map.of(ImperiumAttribute.MIN_PHYSICAL_DAMAGE, shardingMonster.getIncreaseDamage(),
                ImperiumAttribute.MAX_PHYSICAL_DAMAGE, shardingMonster.getIncreaseDamage(),
                ImperiumAttribute.MIN_FIRE_DAMAGE, shardingMonster.getIncreaseDamage(),
                ImperiumAttribute.MAX_FIRE_DAMAGE, shardingMonster.getIncreaseDamage(),
                ImperiumAttribute.MIN_COLD_DAMAGE, shardingMonster.getIncreaseDamage(),
                ImperiumAttribute.MAX_COLD_DAMAGE, shardingMonster.getIncreaseDamage(),
                ImperiumAttribute.MIN_LIGHTNING_DAMAGE, shardingMonster.getIncreaseDamage(),
                ImperiumAttribute.MAX_LIGHTNING_DAMAGE, shardingMonster.getIncreaseDamage(),
                ImperiumAttribute.MIN_CHAOS_DAMAGE, shardingMonster.getIncreaseDamage(),
                ImperiumAttribute.MAX_CHAOS_DAMAGE, shardingMonster.getIncreaseDamage()
                ).forEach((imperiumAttribute, aDouble) -> {
                    val attributeInstance = monster.getAttributes().find(imperiumAttribute);
                    attributeInstance.applyBaseValue(attributeInstance.getBaseValue() * (aDouble / 100D));
                });
    }
}
