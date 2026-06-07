package cupcake.nikky.imperium.tower.entity.monster.data;

import cupcake.nikky.imperium.tower.attribute.AttributeMap;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.entity.monster.loot.LootStrategies;
import cupcake.nikky.imperium.tower.entity.monster.loot.LootStrategyBuilder;
import cupcake.nikky.imperium.tower.entity.monster.loot.other.ExpLoot;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;

public class MonsterDataHandler {

    private final MonsterData monsterData;

    public MonsterDataHandler(@NotNull final MonsterData monsterData) {
        this.monsterData = monsterData;
    }

    public AttributeMap applyMap() {
        val attributeMap = new AttributeMap(new EnumMap<>(ImperiumAttribute.class));

        val level = monsterData.getLevel();

        var health = 0D;

        if (level < 25) {
            health = 22 + 4 * Math.max(0, level - 1) + Math.max(0, level - 2);
        } else if (level < 77) {
            health = Math.pow(1.085, level - 1) * 22;
        } else if (level <= 100) {
            health = Math.pow(1.0841 - ((level - 76) / 10000) * 2, level - 1) * 22;
        }

        var baseDamage = Math.pow(1.065, level - 1) * 4.99;
        var defense = 0;

        if (level > 20 && level < 60) {
            if ((level - 20) % 3 == 0) {
                defense = 5 + (level - 20) / 3;
            } else {
                defense = 5 + (level - 20 - (level - 20) % 3) / 3;
            }
        } else if (level >= 60) {
            defense = 20 + (level - 59) * 2;
        }

        if (level > 80) {
            baseDamage = Math.pow(1.065 - ((level - 80) / 10000) * 2, level - 1) * 4.99;
        }

        attributeMap.register(ImperiumAttribute.HEALTH, health * monsterData.getHealthMultiplier());
        attributeMap.register(ImperiumAttribute.MIN_PHYSICAL_DAMAGE, baseDamage * monsterData.getPhysicalDamageMultiplier() * monsterData.getPhysicalDamage() / 100D * .8);
        attributeMap.register(ImperiumAttribute.MAX_PHYSICAL_DAMAGE, baseDamage * monsterData.getPhysicalDamageMultiplier() * monsterData.getPhysicalDamage() / 100D);
        attributeMap.register(ImperiumAttribute.MIN_FIRE_DAMAGE, baseDamage * monsterData.getElementalAndChaosDamageMultiplier() * monsterData.getFireDamage() / 100D * .8);
        attributeMap.register(ImperiumAttribute.MAX_FIRE_DAMAGE, baseDamage * monsterData.getElementalAndChaosDamageMultiplier() * monsterData.getFireDamage() / 100D);
        attributeMap.register(ImperiumAttribute.MIN_COLD_DAMAGE, baseDamage * monsterData.getElementalAndChaosDamageMultiplier() * monsterData.getColdDamage() / 100D * .8);
        attributeMap.register(ImperiumAttribute.MAX_COLD_DAMAGE, baseDamage * monsterData.getElementalAndChaosDamageMultiplier() * monsterData.getColdDamage() / 100D);
        attributeMap.register(ImperiumAttribute.MIN_LIGHTNING_DAMAGE, baseDamage * monsterData.getElementalAndChaosDamageMultiplier() * monsterData.getLightningDamage() / 100D * .8);
        attributeMap.register(ImperiumAttribute.MAX_LIGHTNING_DAMAGE, baseDamage * monsterData.getElementalAndChaosDamageMultiplier() * monsterData.getLightningDamage() / 100D);
        attributeMap.register(ImperiumAttribute.MIN_CHAOS_DAMAGE, baseDamage * monsterData.getElementalAndChaosDamageMultiplier() * monsterData.getChaosDamage() / 100D * .8);
        attributeMap.register(ImperiumAttribute.MAX_CHAOS_DAMAGE, baseDamage * monsterData.getElementalAndChaosDamageMultiplier() * monsterData.getChaosDamage() / 100D);

        if (monsterData.getAdditionalAttributes() != null)
            monsterData.getAdditionalAttributes().forEach(attributeMap::register);

        val defaultFireResistance = monsterData.getFireResistance();
        val defaultColdResistance = monsterData.getColdResistance();
        val defaultLightningResistance = monsterData.getLightningResistance();
        val defaultChaosResistance = monsterData.getChaosResistance();

        val maximum = 75;

        attributeMap.register(ImperiumAttribute.FIRE_RESISTANCE_MAXIMUM, maximum);
        attributeMap.register(ImperiumAttribute.COLD_RESISTANCE_MAXIMUM, maximum);
        attributeMap.register(ImperiumAttribute.LIGHTNING_RESISTANCE_MAXIMUM, maximum);
        attributeMap.register(ImperiumAttribute.CHAOS_RESISTANCE_MAXIMUM, maximum);

        attributeMap.register(ImperiumAttribute.MOVEMENT_SPEED, monsterData.getSpeed());
        attributeMap.register(ImperiumAttribute.DEFENSE, defense);
        attributeMap.register(ImperiumAttribute.DEFENSE_PENETRATION, monsterData.getDefensePenetration());

        attributeMap.register(ImperiumAttribute.FIRE_RESISTANCE, defaultFireResistance);
        attributeMap.register(ImperiumAttribute.COLD_RESISTANCE, defaultColdResistance);
        attributeMap.register(ImperiumAttribute.LIGHTNING_RESISTANCE, defaultLightningResistance);
        attributeMap.register(ImperiumAttribute.CHAOS_RESISTANCE, defaultChaosResistance);

        var quantityMultiplier = 1D;
        attributeMap.register(ImperiumAttribute.MONSTER_QUANTITY_OF_ITEMS, quantityMultiplier);
        attributeMap.register(ImperiumAttribute.MONSTER_CHANCE_TO_DROP_RELICT_ITEMS, quantityMultiplier);
        attributeMap.register(ImperiumAttribute.DAMAGE_OVER_TIME, 100);
        attributeMap.register(ImperiumAttribute.SPELL_DAMAGE, 100);
        attributeMap.register(ImperiumAttribute.ATTACK_SPEED, 100);

        return attributeMap;
    }

    public void applyLoot() {
        if (monsterData.isApplied()) return;

        if (monsterData.getExpLoot() == null) {
            monsterData.setExpLoot(new ExpLoot(100 * monsterData.getLevel() * .85, 100 * monsterData.getLevel() * 1.15));
        }

        val lootStrategy = LootStrategyBuilder.create()
                .of(monsterData.getLootStrategies().stream().map(LootStrategies::builder).toArray(LootStrategyBuilder[]::new))
                .build(monsterData);

        monsterData.setLootWeightPool(lootStrategy.invoke());
        monsterData.setApplied(true);
    }

}
