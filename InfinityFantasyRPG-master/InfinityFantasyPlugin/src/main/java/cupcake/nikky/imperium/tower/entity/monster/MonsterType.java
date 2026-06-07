package cupcake.nikky.imperium.tower.entity.monster;

import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.entity.monster.conduct.ConductType;
import cupcake.nikky.imperium.tower.entity.monster.damage.cast.OutboundDamageCast;
import cupcake.nikky.imperium.tower.entity.monster.damage.cast.exact.SwapLocationInboundCast;
import cupcake.nikky.imperium.tower.entity.monster.damage.cast.exact.SwapSlotInboundCast;
import cupcake.nikky.imperium.tower.entity.monster.data.MonsterData;
import cupcake.nikky.imperium.tower.entity.monster.exact.*;
import cupcake.nikky.imperium.tower.entity.monster.exact.sharding.ProjectionOfTheYIBina;
import cupcake.nikky.imperium.tower.entity.monster.exact.sharding.ProjectionOfTheYIHochma;
import cupcake.nikky.imperium.tower.entity.monster.exact.unique.*;
import cupcake.nikky.imperium.tower.entity.monster.goal.GoalType;
import cupcake.nikky.imperium.tower.entity.monster.loot.LootStrategies;
import cupcake.nikky.imperium.tower.entity.monster.loot.other.ExpLoot;
import cupcake.nikky.imperium.tower.item.Icons;
import cupcake.nikky.imperium.tower.item.ItemObject;
import cupcake.nikky.imperium.tower.rng.exact.loot.LootWeightPool;
import cupcake.nikky.imperium.tower.utility.TriFunction;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.anfanik.steda.api.utility.MathUtility;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.core.datasync.EntityColorData;
import ru.cristalix.core.datasync.EntityScaleData;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

@Getter
@RequiredArgsConstructor
public enum MonsterType {

    GHOUL((location, monsterData1, type) -> FantasyMonsterBuilder.of()
            .addSpawnListener(monster -> ((Zombie) monster.getSpigot()).setBaby(false))
            .addOutboundDamageCast(monster -> new OutboundDamageCast(monster) {
                @Override
                public boolean canApply() {
                    return true;
                }

                @Override
                public void apply(LivingEntity entity) {
                    monster.changeHealth(1);
                }
            }).build(location, monsterData1, type), MonsterData.builder()
            .name("Нежить")
            .level(1)
            .entityType(EntityType.ZOMBIE)
            .speed(-20)
            .attacksDescription("&fПри нанесении урона восстанавливает 3 здоровья")
            .build()),
    INFECTED_WOLF((location, monsterData1, type) -> FantasyMonsterBuilder.of().build(location, monsterData1, type),
            MonsterData.builder()
                    .name("Зараженный волк")
                    .level(2)
                    .entityType(EntityType.WOLF)
                    .speed(20)
                    .build()),
    STILLBORN_SPIDER((location, monsterData1, type) -> FantasyMonsterBuilder.of().build(location, monsterData1, type), MonsterData.builder()
            .name("Мертворожденный паук")
            .applyAdditionalAttribute(ImperiumAttribute.CHANCE_TO_POISON, 100)
            .level(3)
            .entityType(EntityType.SPIDER)
            .speed(10)
            .attacksDescription("&fПри нанесении урона вызывает отравление\n&cОтравление&f: наносит периодический урон")
            .build()),
    CANRIAN_SPIDER((location, monsterData1, type) -> FantasyMonsterBuilder.of().build(location, monsterData1, type),
            MonsterData.builder()
                    .name("Канрийский паук")
                    .level(4)
                    .entityType(EntityType.SPIDER)
                    .speed(40)
                    .build()),
    WOLF_LEADER((location, monsterData1, type) -> FantasyMonsterBuilder.of().build(location, monsterData1, type), MonsterData.builder()
            .name("Вожак волков")
            .applyAdditionalAttribute(ImperiumAttribute.CHANCE_TO_CAUSE_BLEEDING, 100)
            .level(5)
            .entityType(EntityType.WOLF)
            .speed(30)
            .attacksDescription("&fПри нанесении урона\n&fвызывает сильное кровотечение\n&cСильное кровотечение&f: наносит периодический урон")
            .build()),
    FIRE_ELEMENTAL(FireSkeleton::new, MonsterData.builder()
            .name("Огненный скелет")
            .goalType(GoalType.ARROW)
            .level(6)
            .entityType(EntityType.SKELETON)
            .physicalDamageMultiplier(.5)
            .elementalAndChaosDamageMultiplier(.5)
            .speed(-200)
            .attacksDescription("&fПри нанесении урона с некоторым шансом вызывает ожог\n&cОжог&f: наносит периодический урон")
            .build()),
    CANRIAN_ENDERMITE(CanrianEndermite::new, MonsterData.builder()
            .name("Канрийский эндермит")
            .level(7)
            .scaleData(new EntityScaleData(2, 2, 2))
            .entityType(EntityType.ENDERMITE)
            .speed(-35)
            .attacksDescription("&fПри нанесении урона вызывает помутнение\n&cПомутнение&f: уменьшает скорость атаки")
            .build()),
    ANDOR(Andor::new, MonsterData.builder()
            .name("Андор")
            .level(8)
            .entityType(EntityType.WOLF)
            .speed(25)
            .attacksDescription("&fПри нанесении урона вызывает замедление. Складывается до 4х раз.\n&cЗамедление&f: уменьшает скорость передвижения")
            .build()),
    MOON_SPIDER((location, monsterData1, type) -> FantasyMonsterBuilder.of().build(location, monsterData1, type),
            MonsterData.builder()
                    .name("Лунный паук")
                    .level(10)
                    .entityType(EntityType.SPIDER)
                    .speed(50)
                    .build()),
    SHADOW_OCELOT(ShadowOcelot::new, MonsterData.builder()
            .name("Теневой оцелот")
            .level(9)
            .entityType(EntityType.OCELOT)
            .speed(30)
            .attacksDescription("&fПри нанесении урона вызывает ослепление")
            .build()),
    DREAM_WALKER_SPIDER(DreamWalkerSpider::new, MonsterData.builder()
            .name("Паук сноходец")
            .level(12)
            .entityType(EntityType.SPIDER)
            .defense(-10)
            .attacksDescription("&fВ некотором радиусе от себя\n&fнакладывает замедление\n&cЗамедление&f: уменьшает скорость передвижения")
            .build()),
    ALP(Alp::new, MonsterData.builder()
            .name("Альп")
            .applyAdditionalAttribute(ImperiumAttribute.CHANCE_TO_CAUSE_BLEEDING, 50)
            .level(11)
            .entityType(EntityType.ZOMBIE)
            .speed(30)
            .attacksDescription("&fПри нанесении урона с некоторым шансом вызывает кровотечение\n&cКровотечение&f: наносит периодический урон\n\n&f7.5% нанесенного урона поглощается в виде здоровья")
            .build()),
    SKARIGA(Skariga::new, MonsterData.builder()
            .name("Скаррига")
            .level(12)
            .lootWeightPool(LootWeightPool.newInstance(1)
                    .newWeightInstance(ItemObject.JUMP_ASPECT, 1, 1_000).apply()
                    .newWeightInstance(ItemObject.SKARIGA_SWORD, 1, 200).apply()
            )
            .goalType(GoalType.IMMOBILIZED)
            .scaleData(new EntityScaleData(1.5, 1.5, 1.5))
            .entityType(EntityType.SPIDER)
            .respawnTicks(20 * 60 * 5)
            .healthMultiplier(3)
            .fireResistance(40).lightningResistance(40).coldResistance(40).chaosResistance(25)
            .speed(0D)
            .unique(true)
            .showRespawnText(true)
            .attacksDescription("&fАтакует в радиусе 6 блоков.\n&fНа 50% здоровье накладывает страх.\n&cСтрах&f: 40% уменьшение скорости атаки")
            .build()),
    IRON_SKELETON(IronSkeleton::new, MonsterData.builder()
            .name("Железный скелет")
            .goalType(GoalType.ARROW)
            .level(13)
            .entityType(EntityType.SKELETON)
            .speed(25)
            .attacksDescription("&fС некоторым шансом при нанесении\n&fурона накладывает обездвиживание.")
            .build()),
    INFECTED_MLENIA_RESIDENT((location, monsterData1, type) -> FantasyMonsterBuilder.of()
            .addSpawnListener(monster -> ((Zombie) monster.getSpigot()).setBaby(false))
            .build(location, monsterData1, type), MonsterData.builder()
            .name("Житель Млении")
            .applyAdditionalAttribute(ImperiumAttribute.CHANCE_TO_POISON, 100)
            .conduct(ConductType.NEUTRAL)
            .level(14)
            .entityType(EntityType.ZOMBIE)
            .speed(-20)
            .attacksDescription("&fПри нанесении урона вызывает заражение\n&cЗаражение&f: наносит периодический урон")
            .build()),
    DISCIPLE_OF_LICH(LichDisciple::new, MonsterData.builder()
            .name("Последователь Лича")
            .level(16)
            .goalType(GoalType.MELEE)
            .entityType(EntityType.SKELETON)
            .speed(20)
            .physicalDamageMultiplier(.5)
            .elementalAndChaosDamageMultiplier(.5)
            .attacksDescription("&fПри нанесении урона с некоторым шансом вызывает ожог\n&cОжог&f: наносит периодический урон")
            .build()),
    CHAOS_SKELETON(ChaosSkeleton::new, MonsterData.builder()
            .name("Скелет хаоса")
            .level(15)
            .goalType(GoalType.ARROW)
            .entityType(EntityType.WITHER_SKELETON)
            .speed(10)
            .attacksDescription("&fПри нанесении урона с некоторым шансом\n&fвызывает инвертацию стихий\n&cИнвертация стихий&f: инвертирует все сопротивления")
            .build()),
    ELEMENTAL_SKELETON(ElementalSkeleton::new, MonsterData.builder()
            .name("Стихийный скелет")
            .level(17)
            .goalType(GoalType.ARROW)
            .entityType(EntityType.WITHER_SKELETON)
            .speed(30)
            .attacksDescription("&fПри нанесении урона с некоторым шансом\n&fослепляет и вызывает тошноту")
            .build()),
    LICH(Lich::new, MonsterData.builder()
            .name("Лич")
            .level(18)
            .goalType(GoalType.MELEE)
            .entityType(EntityType.WITHER_SKELETON)
            .speed(25)
            .build()),
    ABYSS_SKELETON((location, monsterData1, type) -> FantasyMonsterBuilder.of().build(location, monsterData1, type),
            MonsterData.builder()
                    .name("Скелет бездны")
                    .level(19)
                    .goalType(GoalType.ARROW)
                    .entityType(EntityType.SKELETON)
                    .speed(30)
                    .build()),
    FOLLOWER_OF_BINA(FollowerOfBina::new, MonsterData.builder()
            .name("Последователь Бина")
            .level(20)
            .entityType(EntityType.PIG_ZOMBIE)
            .speed(25)
            .attacksDescription("&fНа 50% здоровье повышает свою защиту на 25%")
            .build()),
    CHARMED_GOLEM(CharmedGolem::new, MonsterData.builder()
            .name("Заколдованный голем")
            .conduct(ConductType.NEUTRAL)
            .level(21)
            .entityType(EntityType.IRON_GOLEM)
            .scaleData(new EntityScaleData(.75, .75, .75))
            .speed(10)
            .attacksDescription("&fЗамедляет в радиусе 5 блоков.\n&fПри нанесении урона подбрасывает вверх.")
            .build()),
    ELEMENTAL_GOLEM((location, monsterData1, type) -> FantasyMonsterBuilder.of().build(location, monsterData1, type), MonsterData.builder()
            .name("Стихийный голем")
            .conduct(ConductType.NEUTRAL)
            .level(22)
            .entityType(EntityType.IRON_GOLEM)
            .speed(10)
            .build()),
    CORRUPTED_PIG((location, monsterData1, type) -> FantasyMonsterBuilder.of().build(location, monsterData1, type), MonsterData.builder()
            .name("Оскверненный паук")
            .level(23)
            .entityType(EntityType.SPIDER)
            .speed(25)
            .build()),
    DARK_ENDERMIT((location, monsterData1, type) -> FantasyMonsterBuilder.of().build(location, monsterData1, type), MonsterData.builder()
            .name("Темный эндермит")
            .level(24)
            .entityType(EntityType.ENDERMITE)
            .speed(40)
            .build()),
    DISTORTED_VEX((location, monsterData1, type) -> FantasyMonsterBuilder.of().addSpawnListener(monster -> ((ZombieVillager) monster.getSpigot()).setBaby(false)).build(location, monsterData1, type),
            MonsterData.builder()
            .name("Искаженный житель")
            .level(25)
            .entityType(EntityType.ZOMBIE_VILLAGER)
            .speed(-20)
            .build()),
    DECAYED_SHELL((location, monsterData1, type) -> FantasyMonsterBuilder.of().addSpawnListener(monster -> ((Husk) monster.getSpigot()).setBaby(true)).build(location, monsterData1, type),
            MonsterData.builder()
            .name("Сгнившая оболочка")
            .level(26)
            .entityType(EntityType.HUSK)
            .speed(-20)
            .build()),
    DISTORT_OF_MORTA(DistortOfMorta::new, MonsterData.builder()
            .name("Искажение Морты")
            .level(26)
            .respawnTicks(20 * 60 * 5)
            .chaosDamage(25).coldDamage(0)
            .healthMultiplier(3D)
            .entityType(EntityType.HUSK)
            .scaleData(new EntityScaleData(.75, .75, .75))
            .fireResistance(40).lightningResistance(40).coldResistance(40).chaosResistance(25)
            .unique(true)
            .showRespawnText(true)
            .lootWeightPool(LootWeightPool.newInstance(1)
                    .newWeightInstance(ItemObject.MORT_DAGGER, 1, 200).cancelSizing().apply()
            )
            .attacksDescription("""
                    &fНа 90%, 60% и 30% создает сферу вокруг себя, при ударе по которой на всех атакующих накладывается отравление.
                    &fПока активна сфера - становится неуязвимым. Сфера пропадает после 10 ударов.
                    """)
            .build()),
    DISTORTED_ILLAGER((location, monsterData1, type) -> FantasyMonsterBuilder.of().build(location, monsterData1, type),
            MonsterData.builder()
                    .name("Искаженный разбойник")
                    .level(27)
                    .entityType(EntityType.VINDICATOR)
                    .speed(20)
                    .build()),
    DISTORTED_SPIDER((location, monsterData1, type) -> FantasyMonsterBuilder.of().build(location, monsterData1, type),
            MonsterData.builder()
                    .name("Искаженный паук")
                    .level(28)
                    .entityType(EntityType.CAVE_SPIDER)
                    .speed(-10)
                    .build()),
    ETERNAL_GUARDIAN((location, monsterData1, type) -> FantasyMonsterBuilder.of(new ItemStack(Material.DIAMOND_SWORD), new ItemStack(Material.SHIELD),
            new ItemStack[]{new ItemStack(Material.IRON_BOOTS), null, new ItemStack(Material.IRON_CHESTPLATE), null})
            .addSpawnListener(monster -> ((Zombie) monster.getSpigot()).setBaby(false)).build(location, monsterData1, type),
            MonsterData.builder()
                    .name("Вечный стражник")
                    .level(29)
                    .entityType(EntityType.ZOMBIE)
                    .speed(20)
                    .build()),
    ETERNAL_BABY((location, monsterData1, type) -> FantasyMonsterBuilder.of(new ItemStack(Material.GOLD_SWORD), null,
            new ItemStack[]{null, null, new ItemStack(Material.IRON_CHESTPLATE), new ItemStack(Material.GOLD_HELMET)})
            .addSpawnListener(monster -> ((Zombie) monster.getSpigot()).setBaby(true))
            .build(location, monsterData1, type),
            MonsterData.builder()
                    .name("Вечное дитя")
                    .level(30)
                    .entityType(EntityType.ZOMBIE)
                    .speed(-10)
                    .build()),
    ASHEN_WOLF((location, monsterData1, type) -> FantasyMonsterBuilder.of().build(location, monsterData1, type),
            MonsterData.builder()
                    .name("Пепельный волк")
                    .level(31)
                    .entityType(EntityType.WOLF)
                    .speed(25)
                    .build()),
    MORK((location, monsterData1, type) -> FantasyMonsterBuilder.of().addSpawnListener(monster -> ((PigZombie) monster.getSpigot()).setBaby(false)).build(location, monsterData1, type),
            MonsterData.builder()
                    .name("Морк")
                    .level(32)
                    .entityType(EntityType.PIG_ZOMBIE)
                    .speed(35)
                    .build()),
    FREE_SOUL((location, monsterData1, type) -> FantasyMonsterBuilder.of(null, null,
            new ItemStack[]{null, new ItemStack(Material.CHAINMAIL_LEGGINGS), null, null})
            .addSpawnListener(monster -> ((Husk) monster.getSpigot()).setBaby(false))
            .build(location, monsterData1, type),
            MonsterData.builder()
                    .name("Вольная душа")
                    .level(33)
                    .entityType(EntityType.HUSK)
                    .speed(50)
                    .build()),
    FOLLOWER_OF_FATUM((location, monsterData1, type) -> FantasyMonsterBuilder.of(new ItemStack(Material.STONE_SWORD), null,
            new ItemStack[]{new ItemStack(Material.GOLD_BOOTS), null, null, null}).build(location, monsterData1, type),
            MonsterData.builder()
                    .name("Последователь фатума")
                    .level(34)
                    .entityType(EntityType.WITHER_SKELETON)
                    .speed(40)
                    .build()),
    SOUL_OF_BANDIT_WARRIOR((location, monsterData1, type) -> FantasyMonsterBuilder.of(new ItemStack(Material.IRON_SWORD), null,
            new ItemStack[]{new ItemStack(Material.IRON_BOOTS), null, new ItemStack(Material.IRON_CHESTPLATE), null}).build(location, monsterData1, type),
            MonsterData.builder()
                    .name("Душа разбойника-война")
                    .level(36)
                    .entityType(EntityType.SKELETON)
                    .speed(20)
                    .build()),
    SOUL_OF_BANDIT_ARCHER((location, monsterData1, type) -> FantasyMonsterBuilder.of(null, null,
            new ItemStack[]{new ItemStack(Material.IRON_BOOTS), null, new ItemStack(Material.IRON_CHESTPLATE), null}).build(location, monsterData1, type),
            MonsterData.builder()
                    .name("Душа разбойника-лучника")
                    .level(35)
                    .goalType(GoalType.ARROW)
                    .entityType(EntityType.SKELETON)
                    .speed(20)
                    .build()),
    EDGE_WATCHER((location, monsterData1, type) -> FantasyMonsterBuilder.of(new ItemStack(Material.STONE_SWORD), null,
            new ItemStack[]{null, null, null, new ItemStack(Material.GOLD_HELMET)}).build(location, monsterData1, type),
            MonsterData.builder()
                    .name("Созерцатель Грани")
                    .level(37)
                    .entityType(EntityType.WITHER_SKELETON)
                    .speed(-20)
                    .build()),
    ABSORBED_BY_EDGE((location, monsterData1, type) -> FantasyMonsterBuilder.of(null, null,
            new ItemStack[]{null, null, new ItemStack(Material.IRON_CHESTPLATE), new ItemStack(Material.IRON_HELMET)})
            .addSpawnListener(monster -> ((Zombie) monster.getSpigot()).setBaby(false))
            .build(location, monsterData1, type),
            MonsterData.builder()
                    .name("Поглощенный Гранью")
                    .level(38)
                    .entityType(EntityType.ZOMBIE)
                    .speed(-10)
                    .build()),
    FOLLOWER_OF_KETHER((location, monsterData1, type) -> FantasyMonsterBuilder.of().build(location, monsterData1, type),
            MonsterData.builder()
                    .name("Последователь Кетера")
                    .level(39)
                    .entityType(EntityType.CREEPER)
                    .scaleData(new EntityScaleData(.75, .75, .75))
                    .speed(10)
                    .build()),
    PRIEST_OF_CHESED((location, monsterData1, type) -> FantasyMonsterBuilder.of().setAuraRadius(12).addSpawnListener(monster -> ((Villager) monster.getSpigot()).setAdult()).build(location, monsterData1, type),
            MonsterData.builder()
                    .name("Жрец Хеседа")
                    .level(40)
                    .goalType(GoalType.IMMOBILIZED)
                    .entityType(EntityType.VILLAGER)
                    .speed(0D)
                    .attacksDescription("&fНаносит урон в радиусе 12 блоков по атакующим")
                    .build()),
    EDGE_OCELOT((location, monsterData1, type) -> FantasyMonsterBuilder.of().build(location, monsterData1, type),
            MonsterData.builder()
                    .name("Оцелот Грани")
                    .level(41)
                    .entityType(EntityType.OCELOT)
                    .speed(-25)
                    .build()),
    ETERNAL_CENTURION((location, monsterData1, type) -> FantasyMonsterBuilder.of(new ItemStack(Material.IRON_SWORD), null,
                    new ItemStack[]{new ItemStack(Material.DIAMOND_BOOTS), new ItemStack(Material.DIAMOND_LEGGINGS), new ItemStack(Material.DIAMOND_CHESTPLATE), new ItemStack(Material.DIAMOND_HELMET)})
            .addSpawnListener(monster -> ((Zombie) monster.getSpigot()).setBaby(false))
            .build(location, monsterData1, type),
            MonsterData.builder()
                    .name("Вечный сотник")
                    .level(42)
                    .entityType(EntityType.ZOMBIE)
                    .speed(10)
                    .build()),
    FOREST_GUARDIAN((location, monsterData1, type) -> FantasyMonsterBuilder.of().build(location, monsterData1, type),
            MonsterData.builder()
                    .name("Страж леса")
                    .level(43)
                    .entityType(EntityType.SKELETON)
                    .colorData(new EntityColorData(1, 56, 13))
                    .speed(25)
                    .build()),
    FOREST_VILLAGER((location, monsterData1, type) -> FantasyMonsterBuilder.of()
            .addSpawnListener(monster -> ((ZombieVillager) monster.getSpigot()).setBaby(true))
            .build(location, monsterData1, type),
            MonsterData.builder()
                    .name("Житель леса")
                    .level(44)
                    .conduct(ConductType.NEUTRAL)
                    .entityType(EntityType.ZOMBIE_VILLAGER)
                    .colorData(new EntityColorData(1, 56, 13))
                    .speed(-50)
                    .build()),
    FOREST_GOLEM(((location, monsterData1, type) -> FantasyMonsterBuilder.of()
            .build(location, monsterData1, type)), MonsterData.builder()
            .applyAdditionalAttribute(ImperiumAttribute.CHANCE_TO_CAUSE_BLEEDING, 40)
            .name("Голем леса")
            .level(44)
            .respawnTicks(20 * 60 * 2)
            .goalType(GoalType.MELEE)
            .scaleData(new EntityScaleData(1.25, 1.25, 1.25))
            .entityType(EntityType.IRON_GOLEM)
            .lootWeightPool(LootWeightPool.newInstance(1)
                    .newWeightInstance(ItemObject.FOREST_SPEAR, 1, 200).apply()
            )
            .healthMultiplier(3D)
            .fireResistance(40).lightningResistance(40).coldResistance(40).chaosResistance(25)
            .unique(true)
            .showRespawnText(true)
            .build()),
    EDGE_WARRIOR((location, monsterData1, type) -> FantasyMonsterBuilder.of(new ItemStack(Material.WOOD_SWORD), null,
                    new ItemStack[]{new ItemStack(Material.GOLD_BOOTS), new ItemStack(Material.GOLD_LEGGINGS), new ItemStack(Material.GOLD_CHESTPLATE), new ItemStack(Material.GOLD_HELMET)})
            .addSpawnListener(monster -> ((Zombie) monster.getSpigot()).setBaby(false))
            .build(location, monsterData1, type),
            MonsterData.builder()
                    .name("Воин Грани")
                    .level(45)
                    .entityType(EntityType.ZOMBIE)
                    .speed(50)
                    .build()),
    WANDERER_EDGE((location, monsterData1, type) -> FantasyMonsterBuilder.of(new ItemStack(Material.BOOK), null, null).build(location, monsterData1, type),
            MonsterData.builder()
                    .name("Странник Грани")
                    .level(46)
                    .entityType(EntityType.WITHER_SKELETON)
                    .speed(25)
                    .build()),
    MERNIGA((location, monsterData1, type) -> FantasyMonsterBuilder.of().setAuraRadius(12).build(location, monsterData1, type),
            MonsterData.builder()
                    .name("Мернига")
                    .level(48)
                    .goalType(GoalType.IMMOBILIZED)
                    .entityType(EntityType.WITCH)
                    .speed(0D)
                    .attacksDescription("&fНаносит урон в радиусе 12 блоков по атакующим")
                    .build()),
    SERVANT_OF_MERNIGA((location, monsterData1, type) -> FantasyMonsterBuilder.of().build(location, monsterData1, type),
            MonsterData.builder()
                    .name("Слуга Мерниги")
                    .level(47)
                    .entityType(EntityType.IRON_GOLEM)
                    .build()),
    DISTORTED_COW((location, monsterData1, type) -> FantasyMonsterBuilder.of()
            .addSpawnListener(monster -> {
                var cow = ((MushroomCow) monster.getSpigot());
                cow.setAdult();
                cow.setAgeLock(true);
            }).build(location, monsterData1, type),
            MonsterData.builder()
                    .name("Искаженная корова")
                    .level(49)
                    .entityType(EntityType.MUSHROOM_COW)
                    .build()),
    SHADOW_OF_CHESED((location, monsterData1, type) -> FantasyMonsterBuilder.of().build(location, monsterData1, type),
            MonsterData.builder()
                    .name("Тень Хеседа")
                    .level(50)
                    .entityType(EntityType.ENDERMAN)
                    .build()),
    WARRIOR_OF_CHESED((location, monsterData1, type) -> FantasyMonsterBuilder.of().build(location, monsterData1, type),
            MonsterData.builder()
                    .name("Хаосид")
                    .level(51)
                    .entityType(EntityType.SKELETON)
                    .build()),
    CHAOS_ENTITY((location, monsterData1, type) ->
            FantasyMonsterBuilder.of(null, null, new ItemStack[]{new ItemStack(Material.IRON_BOOTS), null, null, null}).build(location, monsterData1, type),
            MonsterData.builder()
                    .name("Хаосит")
                    .level(52)
                    .entityType(EntityType.WITHER_SKELETON)
                    .build()),
    LEADER_OF_CHAOS((location, monsterData1, type) ->
            FantasyMonsterBuilder.of(null, null, new ItemStack[]{new ItemStack(Material.IRON_BOOTS), null, null, new ItemStack(Material.IRON_HELMET)})
                    .addSpawnListener(monster -> ((Zombie) monster.getSpigot()).setBaby(true))
                    .build(location, monsterData1, type),
            MonsterData.builder()
                    .name("Проводник хаоса")
                    .level(54)
                    .entityType(EntityType.ZOMBIE)
                    .build()),
    DISTORTED_BY_CHAOS((location, monsterData1, type) ->
            FantasyMonsterBuilder.of(new ItemStack(Material.DIAMOND_AXE), null, new ItemStack[]{null, null, new ItemStack(Material.DIAMOND_CHESTPLATE), null})
                    .addSpawnListener(monster -> ((PigZombie) monster.getSpigot()).setBaby(false))
                    .build(location, monsterData1, type),
            MonsterData.builder()
                    .name("Искаженный хаосом")
                    .level(53)
                    .entityType(EntityType.PIG_ZOMBIE)
                    .build()),
    HAND_OF_CHESED((location, monsterData1, type) -> FantasyMonsterBuilder.of().build(location, monsterData1, type),
            MonsterData.builder()
                    .name("Длань Хеседа")
                    .level(55)
                    .entityType(EntityType.OCELOT)
                    .scaleData(new EntityScaleData(0.75, 0.75, 0.75))
                    .build()),
    GAZE_OF_CHESED((location, monsterData1, type) -> FantasyMonsterBuilder.of().build(location, monsterData1, type),
            MonsterData.builder()
                    .name("Взор Хеседа")
                    .level(56)
                    .entityType(EntityType.ENDERMITE)
                    .scaleData(new EntityScaleData(2, 2, 2))
                    .build()),
    EDGE_BEAR((location, monsterData1, type) -> FantasyMonsterBuilder.of().addSpawnListener(monster -> ((PolarBear) monster.getSpigot()).setAdult()).build(location, monsterData1, type),
            MonsterData.builder()
                    .name("Медведь Грани")
                    .level(57)
                    .entityType(EntityType.POLAR_BEAR)
                    .build()),
    MINE_EXILE((location, monsterData1, type) -> FantasyMonsterBuilder.of().build(location, monsterData1, type),
            MonsterData.builder()
                    .name("Изгнанник шахты")
                    .level(58)
                    .entityType(EntityType.IRON_GOLEM)
                    .scaleData(new EntityScaleData(.5, .5, .5))
                    .build()),
    MINER((location, monsterData1, type) -> FantasyMonsterBuilder.of(new ItemStack(Material.GOLD_PICKAXE), null, new ItemStack[] {
            new ItemStack(Material.GOLD_BOOTS), new ItemStack(Material.GOLD_LEGGINGS), new ItemStack(Material.GOLD_CHESTPLATE), new ItemStack(Material.GOLD_HELMET)
    }).build(location, monsterData1, type),
            MonsterData.builder()
                    .name("Золотой шахтер")
                    .level(59)
                    .entityType(EntityType.VINDICATOR)
                    .build()),
    ALIEN_MINER((location, monsterData1, type) -> FantasyMonsterBuilder.of(new ItemStack(Material.DIAMOND_PICKAXE), null, null).build(location, monsterData1, type),
            MonsterData.builder()
                    .name("Чуждый шахтер")
                    .level(60)
                    .entityType(EntityType.WITHER_SKELETON)
                    .build()),
    MISGUIDED_MINER((location, data, type) -> FantasyMonsterBuilder.of(null, null, new ItemStack[]{null, null, new ItemStack(Material.GOLD_CHESTPLATE), null})
            .addSpawnListener(monster -> ((ZombieVillager) monster.getSpigot()).setBaby(true))
            .build(location, data, type),
            MonsterData.builder()
                    .name("Заблудший шахтер")
                    .level(60)
                    .respawnTicks(-1).fireDamage(0).lightningDamage(0).chaosDamage(0).coldDamage(50)
                    .entityType(EntityType.ZOMBIE_VILLAGER)
                    .scaleData(new EntityScaleData(1.5, 1.5, 1.5))
                    .expLoot(new ExpLoot(0, 0))
                    .speed(150)
                    .healthMultiplier(1.5D)
                    .build()),
    DISTORTED_MINER(DistortedMiner::new, MonsterData.builder()
            .name("Искаженный шахтер")
            .level(60)
            .respawnTicks(20 * 60 * 5).coldDamage(0).chaosDamage(25)
            .goalType(GoalType.MELEE)
            .scaleData(new EntityScaleData(1.5, 1.5, 1.5))
            .entityType(EntityType.ZOMBIE_VILLAGER)
            .lootWeightPool(LootWeightPool.newInstance(1)
                    .newWeightInstance(ItemObject.MINER_HELMET, 1, 100).apply()
            )
            .speed(-200)
            .physicalDamageMultiplier(1.5)
            .elementalAndChaosDamageMultiplier(1.5)
            .healthMultiplier(5D)
            .fireResistance(40).lightningResistance(40).coldResistance(40).chaosResistance(25)
            .unique(true)
            .showRespawnText(true)
            .attacksDescription("""
                    &fПри нанесении урона ослепляет. По достижению 50% здоровья призывает 3 Заблудших шахтеров.
                    &fНеуязвим пока живы Заблудшие шахтеры. Каждые 5 секунд откидывает и наносит урон.
                    &fПри получении урона притягивает атакующего к себе.
                    """)
            .build()),



    FUGITIVE_SOUL((location, data, type) -> FantasyMonsterBuilder.of(Icons.STONE_AXE, null,
                    new ItemStack[]{new ItemStack(Material.CHAINMAIL_BOOTS), new ItemStack(Material.CHAINMAIL_LEGGINGS), new ItemStack(Material.CHAINMAIL_CHESTPLATE), new ItemStack(Material.CHAINMAIL_HELMET)})
            .addSpawnListener(monster -> ((ZombieVillager) monster.getSpigot()).setBaby(false))
            .build(location, data, type), MonsterData.builder()
            .name("Душа беглеца")
            .level(61)
            .goalType(GoalType.MELEE)
            .entityType(EntityType.ZOMBIE_VILLAGER)
            .build()),
    FUGITIVE_SHELL((location, data, type) -> FantasyMonsterBuilder.of(Icons.STONE_SPEAR, null,
                    new ItemStack[]{new ItemStack(Material.CHAINMAIL_BOOTS), new ItemStack(Material.CHAINMAIL_LEGGINGS), new ItemStack(Material.CHAINMAIL_CHESTPLATE), new ItemStack(Material.CHAINMAIL_HELMET)})
            .addSpawnListener(monster -> ((Zombie) monster.getSpigot()).setBaby(false))
            .build(location, data, type), MonsterData.builder()
            .name("Оболочка беглеца")
            .level(62)
            .goalType(GoalType.MELEE)
            .entityType(EntityType.ZOMBIE)
            .build()),
    JUNIOR_VIRAN((location, data, type) -> FantasyMonsterBuilder.of().build(location, data, type), MonsterData.builder()
            .name("Младший Виран")
            .level(63)
            .goalType(GoalType.MELEE)
            .entityType(EntityType.CAVE_SPIDER)
            .scaleData(new EntityScaleData(.8, .8, .8))
            .build()),
    SENIOR_VIRAN((location, data, type) -> FantasyMonsterBuilder.of().build(location, data, type), MonsterData.builder()
            .name("Старший Виран")
            .level(64)
            .goalType(GoalType.MELEE)
            .entityType(EntityType.CAVE_SPIDER)
            .scaleData(new EntityScaleData(1.5, 1.5, 1.5))
            .build()),
    STILLBIRTH_SPIDER((location, data, type) -> FantasyMonsterBuilder.of().build(location, data, type), MonsterData.builder()
            .name("Мертво-рождающий паук")
            .level(65)
            .goalType(GoalType.MELEE)
            .entityType(EntityType.SPIDER)
            .scaleData(new EntityScaleData(1.25, 1.25, 1.25))
            .build()),
    CANRIAN_FUGITIVE((location, data, type) -> FantasyMonsterBuilder.of().addSpawnListener(monster -> ((Villager) monster.getSpigot()).setAdult()).build(location, data, type), MonsterData.builder()
            .name("Канрийский беглец")
            .level(66)
            .goalType(GoalType.MELEE)
            .entityType(EntityType.VILLAGER)
            .build()),
    EDGE_WOLF((location, data, type) -> FantasyMonsterBuilder.of().build(location, data, type), MonsterData.builder()
            .name("Волк Грани")
            .level(67)
            .goalType(GoalType.MELEE)
            .entityType(EntityType.WOLF)
            .scaleData(new EntityScaleData(1.5, 1.5, 1.5))
            .build()),
    EDGE_ELEMENTALIST((location, data, type) -> FantasyMonsterBuilder
            .of(new ItemStack(Material.BOW), new ItemStack(Material.TORCH),
                    new ItemStack[]{new ItemStack(Material.GOLD_BOOTS), null, new ItemStack(Material.DIAMOND_CHESTPLATE), null})
            .build(location, data, type), MonsterData.builder()
            .name("Элементалист Грани")
            .level(68)
            .goalType(GoalType.ARROW)
            .entityType(EntityType.WITHER_SKELETON)
            .scaleData(new EntityScaleData(.75, .75, .75))
            .build()),
    CHAOS_INFUSED_ENEDERMITE((location, data, type) -> FantasyMonsterBuilder.of().build(location, data, type), MonsterData.builder()
            .name("Насыщенный хаосом эндермит")
            .level(69)
            .goalType(GoalType.MELEE)
            .entityType(EntityType.ENDERMITE)
            .scaleData(new EntityScaleData(2, 2, 2))
            .build()),
    NEWBORN_BY_CHAOS((location, data, type) -> FantasyMonsterBuilder.of().build(location, data, type), MonsterData.builder()
            .name("Новорожденный Хаосом")
            .level(70)
            .goalType(GoalType.MELEE)
            .entityType(EntityType.IRON_GOLEM)
            .scaleData(new EntityScaleData(.5, .5, .5))
            .build()),
    CHAOS_BLOODHOUND((location, data, type) -> FantasyMonsterBuilder.of().build(location, data, type), MonsterData.builder()
            .name("Ищейка Хаоса")
            .level(71)
            .goalType(GoalType.MELEE)
            .entityType(EntityType.OCELOT)
            .scaleData(new EntityScaleData(.75, .75, .75))
            .build()),
    EDGE_PILLAR((location, data, type) -> FantasyMonsterBuilder.of().build(location, data, type), MonsterData.builder()
            .name("Столп Грани")
            .level(72)
            .goalType(GoalType.MELEE)
            .entityType(EntityType.ENDERMAN)
            .scaleData(new EntityScaleData(1.2, 1.75, 1.2))
            .build()),
    AWAKENED_BEAR((location, data, type) -> FantasyMonsterBuilder.of().build(location, data, type), MonsterData.builder()
            .name("Пробужденный медведь")
            .level(73)
            .goalType(GoalType.MELEE)
            .entityType(EntityType.POLAR_BEAR)
            .scaleData(new EntityScaleData(.5, .5, .5))
            .build()),
    AWAKENED_SKELET((location, data, type) -> FantasyMonsterBuilder
            .of(Icons.AWAKENED_SPEAR, new ItemStack(Material.SHIELD), new ItemStack[]{new ItemStack(Material.CHAINMAIL_BOOTS), null, new ItemStack(Material.GOLD_CHESTPLATE), null})
            .build(location, data, type), MonsterData.builder()
            .name("Пробужденный скелет")
            .level(74)
            .goalType(GoalType.MELEE)
            .entityType(EntityType.SKELETON)
            .scaleData(new EntityScaleData(.5, .5, .5))
            .build()),
    CHAOS_MLENIA((location, data, type) -> FantasyMonsterBuilder
            .of(Icons.AWAKENED_DAGGER, Icons.AWAKENED_DAGGER, new ItemStack[]{new ItemStack(Material.DIAMOND_BOOTS), null, null, null})
            .addSpawnListener(monster -> ((Zombie) monster.getSpigot()).setBaby(false))
            .build(location, data, type), MonsterData.builder()
            .name("Хаосит Млении")
            .level(75)
            .goalType(GoalType.MELEE)
            .entityType(EntityType.ZOMBIE)
            .build()),
    PRIEST_OF_CHAOS((location, data, type) -> FantasyMonsterBuilder.of().build(location, data, type), MonsterData.builder()
            .name("Жрица Хаоса")
            .level(76)
            .goalType(GoalType.MELEE)
            .entityType(EntityType.WITCH)
            .build()),
    BATTLE_GROUND_WITCH((location, monsterData1, type) -> FantasyMonsterBuilder.of()
//            .addOutboundDamageCast(monster -> new OutboundDamageCast(monster) {
//                @Override
//                public boolean canApply() {
//                    return MathUtility.randomInRange(1, 100) <= 30;
//                }
//
//                @Override
//                public void apply(LivingEntity entity) {
//                    if (Fantasy.buffService().findById(entity.getUniqueId()).map(buffedEntity -> buffedEntity.find(BuffType.BLEEDING).isPresent()).orElse(false)) return;
//                    if (entity instanceof Player player) Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "На Вас было наложено &cкровотечение&f!");
//                    var bleeding = Fantasy.buffService().appendBuff(entity.getUniqueId(), BuffType.BLEEDING, getMonster().findAttribute(ImperiumAttribute.STRENGTH).findValue() * .7, 3);
//                    if (bleeding != null) ((Bleeding) bleeding).setInfluence(getMonster().getSpigot());
//                }
//            })
            .build(location, monsterData1, type),
            MonsterData.builder()
                    .name("Дух Поля Битвы")
                    .applyAdditionalAttribute(ImperiumAttribute.CHANCE_TO_CAUSE_BLEEDING, 30)
                    .level(61)
                    .goalType(GoalType.MELEE)
                    .entityType(EntityType.WITCH)
                    .attacksDescription("&fС некоторым шансом накладывает &cкровотечение")
                    .lootWeightPool(LootWeightPool.newInstance(1).newWeightInstance(ItemObject.DIVINATION_CARD_29, 1, 100).apply())
                    .build()
                    .applyLootStrategy(LootStrategies.INCUBATOR, LootStrategies.INCUBATOR_CHANCE_PREPROCESS)),
    BATTLE_GROUND_HUSK((location, monsterData1, type) -> FantasyMonsterBuilder.of()
//            .addOutboundDamageCast(monster -> new OutboundDamageCast(monster) {
//                @Override
//                public boolean canApply() {
//                    return MathUtility.randomInRange(1, 100) <= 50;
//                }
//
//                @Override
//                public void apply(LivingEntity entity) {
//                    if (entity instanceof Player player) Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "На Вас было наложено &cзамедление&f!");
//                    Fantasy.buffService().appendBuff(entity.getUniqueId(), BuffType.SLOWNESS, 40, 4);
//                }
//            })
            .addSpawnListener(monster -> {
                var spigot = monster.getSpigot();
                ((Husk) spigot).setBaby(false);
            })
            .build(location, monsterData1, type),
            MonsterData.builder()
                    .applyAdditionalAttribute(ImperiumAttribute.CHANCE_TO_FREEZE_ON_HIT, 50)
                    .name("Дух Поля Битвы")
                    .level(65)
                    .goalType(GoalType.MELEE)
                    .entityType(EntityType.HUSK)
                    .attacksDescription("&fС некоторым шансом накладывает &cзамедление")
                    .lootWeightPool(LootWeightPool.newInstance(1).newWeightInstance(ItemObject.DIVINATION_CARD_29, 1, 100).apply())
                    .build().applyLootStrategy(LootStrategies.INCUBATOR, LootStrategies.INCUBATOR_CHANCE_PREPROCESS)),
    BATTLE_GROUND_ENDERMAN((location, monsterData1, type) -> FantasyMonsterBuilder.of().build(location, monsterData1, type),
            MonsterData.builder()
                    .name("Дух Поля Битвы")
                    .level(71)
                    .scaleData(new EntityScaleData(1.5, 1.5, 1.5))
                    .goalType(GoalType.MELEE)
                    .entityType(EntityType.ENDERMAN)
                    .lootWeightPool(LootWeightPool.newInstance(1).newWeightInstance(ItemObject.DIVINATION_CARD_29, 1, 100).apply())
                    .build().applyLootStrategy(LootStrategies.INCUBATOR, LootStrategies.INCUBATOR_CHANCE_PREPROCESS)),
    BATTLE_GROUND_IRON_GOLEM((location, monsterData1, type) -> FantasyMonsterBuilder.of().build(location, monsterData1, type),
            MonsterData.builder()
                    .name("Дух Поля Битвы")
                    .level(76)
                    .goalType(GoalType.MELEE)
                    .entityType(EntityType.IRON_GOLEM)
                    .lootWeightPool(LootWeightPool.newInstance(1).newWeightInstance(ItemObject.DIVINATION_CARD_29, 1, 100).apply())
                    .build().applyLootStrategy(LootStrategies.INCUBATOR, LootStrategies.INCUBATOR_CHANCE_PREPROCESS)),
    BATTLE_GROUND_MAGMA_CUBE((location, monsterData1, type) -> FantasyMonsterBuilder.of()
            .setAuraRadius(12)
            .addOutboundDamageCast(monster -> new OutboundDamageCast(monster) {

                @Override
                public boolean canApply() {
                    return MathUtility.randomInRange(1, 100) <= 20;
                }

                @Override
                public void apply(LivingEntity entity) {
                    entity.setVelocity(new Vector(0, .75, 0));
                }
            })
            .build(location, monsterData1, type),
            MonsterData.builder()
                    .name("Наследие Поля Битвы")
                    .unique(true)
                    .level(76)
                    .goalType(GoalType.IMMOBILIZED)
                    .speed(0D)
                    .healthMultiplier(2D)
                    .entityType(EntityType.CREEPER)
                    .respawnTicks(20 * 60 * 3).fireResistance(50).lightningResistance(50).coldResistance(50).chaosResistance(30)
                    .attacksDescription("&fС некоторым шансом &cподкидывает &fпри атаке")
                    .lootWeightPool(LootWeightPool.newInstance(1)
                            .newWeightInstance(ItemObject.BATTLE_GROUND_LEGGINGS, 1, 100).apply()
                            .newWeightInstance(ItemObject.DIVINATION_CARD_29, 1, 200).apply()
                    )
                    .showRespawnText(true)
                    .build().applyLootStrategy(LootStrategies.INCUBATOR, LootStrategies.INCUBATOR_CHANCE_PREPROCESS)),

    SHELL_SYNERGY_MELLE((location, monsterData1, type) -> FantasyMonsterBuilder.of()
            .addInboundDamageCast(monster -> new SwapLocationInboundCast(monster, 10))
            .addInboundDamageCast(monster -> new SwapSlotInboundCast(monster, 10))
            .build(location, monsterData1, type), MonsterData.builder()
            .name("Оболочка синергии")
            .level(61)
            .goalType(GoalType.MELEE)
            .entityType(EntityType.ENDERMITE)
            .scaleData(new EntityScaleData(2, 2, 2))
            .healthMultiplier(2)
            .speed(200)
            .build().applyLootStrategy(LootStrategies.FRACTION_DROP)),
    SHELL_SYNERGY_BOW((location, monsterData1, type) -> FantasyMonsterBuilder.of(null, null, new ItemStack[]{
                new ItemStack(Material.IRON_BOOTS), null, null, new ItemStack(Material.IRON_HELMET)
            })
            .addInboundDamageCast(monster -> new SwapLocationInboundCast(monster, 10))
            .build(location, monsterData1, type), MonsterData.builder()
            .name("Оболочка синергии")
            .level(68)
            .goalType(GoalType.ARROW)
            .entityType(EntityType.WITHER_SKELETON)
            .scaleData(new EntityScaleData(.75, .75, .75))
            .healthMultiplier(2)
            .build().applyLootStrategy(LootStrategies.FRACTION_DROP)),
    WARRIOR_SYNERGY(WarriorSynergy::new, MonsterData.builder()
            .name("Воин синергии")
            .respawnTicks(20 * 60 * 5).fireResistance(50).lightningResistance(50).coldResistance(50).chaosResistance(30)
            .unique(true)
            .level(76)
            .goalType(GoalType.IMMOBILIZED)
            .speed(0)
            .entityType(EntityType.ENDERMAN)
            .scaleData(new EntityScaleData(1.2, 1.5, 1.2))
            .chaosDamage(30).fireDamage(6.6).lightningDamage(6.6).coldDamage(6.6)
            .healthMultiplier(10D).physicalDamageMultiplier(.5D).elementalAndChaosDamageMultiplier(.5)
            .showRespawnText(true)
            .lootWeightPool(LootWeightPool.newInstance(2).newWeightInstance(ItemObject.SYNERGY_BOOTS, 1, 100).apply())
            .attacksDescription("""
                    &fВокруг монстра появлется красная зона.
                    &fВне зоны наносится постоянный урон.
                    &fКрасная зона постепенно уменьшается.
                    &fВ некоторых местах появлются зеленые зоны,
                    &fзаходя в которые Вы расширяете красную.
                    """)
            .build().applyLootStrategy(LootStrategies.FRACTION_DROP)),

    MASK_ENTITY(MaskEntity::new, MonsterData.builder()
            .name("Проводник Грани")
            .unique(true)
            .invalidateWhenForgotTarget(true)
            .level(61)
            .goalType(GoalType.IMMOBILIZED)
            .speed(0D)
            .entityType(EntityType.CREEPER)
            .respawnTicks(-1).fireResistance(40).lightningResistance(40).coldResistance(40).chaosResistance(25)
            .build().applyLootStrategy(LootStrategies.MASK)),
    PROJECTION_OF_THE_YOUNG_INDIVIDUAL_BINA(ProjectionOfTheYIBina::new, MonsterData.builder()
            .name("Проекция Бина")
            .conduct(ConductType.NEUTRAL)
            .respawnTicks(-1)
            .lootWeightPool(LootWeightPool.newInstance(1)
                    .newWeightInstance(ItemObject.BINA_BOOTS, 1, 100).apply()
                    .newWeightInstance(ItemObject.DIVINATION_CARD_25, 1, 200).apply()
            )
            .level(61)
            .invalidateWhenForgotTarget(true)
            .entityType(EntityType.CAVE_SPIDER)
            .scaleData(new EntityScaleData(1.5, 1.5, 1.5))
            .colorData(new EntityColorData(0, 128, 219))
            .chaosDamage(30).fireDamage(6.6).lightningDamage(6.6).coldDamage(6.6)
            .speed(10)
            .fireResistance(50).lightningResistance(50).coldResistance(50).chaosResistance(30)
            .healthMultiplier(3D)
            .unique(true)
            .attacksDescription("""
                    &fНа 90% и 50% здоровья появляется несколько столбов.
                    &fКаждый столб притягивает к себе и наносит постепенный урон хаосом.
                    &fЧерез 10 секунд столбы пропадают.
                    На 10% и 40% монстр получает усиление своих атак.
                    """)
            .build()),
    PROJECTION_OF_THE_YOUNG_INDIVIDUAL_HOCHMA(ProjectionOfTheYIHochma::new, MonsterData.builder()
            .name("Проекция Хохма")
            .conduct(ConductType.NEUTRAL)
            .respawnTicks(-1)
            .lootWeightPool(LootWeightPool.newInstance(1)
                    .newWeightInstance(ItemObject.HOCHMA_BODY, 1, 100).apply()
                    .newWeightInstance(ItemObject.DIVINATION_CARD_26, 1, 200).apply()
            )
            .level(61)
            .invalidateWhenForgotTarget(true)
            .entityType(EntityType.WITHER_SKELETON)
            .scaleData(new EntityScaleData(.8, .8, .8))
            .colorData(new EntityColorData(122, 1, 1))
            .chaosDamage(40).fireDamage(3.3).lightningDamage(3.3).coldDamage(3.3)
            .speed(25)
            .fireResistance(50).lightningResistance(50).coldResistance(50).chaosResistance(30)
            .healthMultiplier(6)
            .unique(true)
            .attacksDescription("""
                    &fНа 80% монстр получает эффект &6Стабильность&f.
                    Также, начинают появляться зоны обновления &6стабильности &fХохмы.
                    Если &6стабильность &fупадет до нуля, то все игроки будут убиты.
                    Зона появляется каждые 3 секунды. Эффект &6стабильности &fдлится 5 секунд.
                    На 10% и 40% монстр получает усиление своих атак.
                    """)
            .build());

    private final TriFunction<Location, MonsterData, MonsterType, Monster> monsterSupplier;
    private final MonsterData monsterData;

    private final List<Location> spawnLocations = new ArrayList<>();

    MonsterType(@NotNull final BiFunction<Location, MonsterData, Monster> monsterBiFunction, @NotNull final MonsterData data) {
        this(((location, monsterData1, type) -> monsterBiFunction.apply(location, monsterData1)), data);
    }

}
