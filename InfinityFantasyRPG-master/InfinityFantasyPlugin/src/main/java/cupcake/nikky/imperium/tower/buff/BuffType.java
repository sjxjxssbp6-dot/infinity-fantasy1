package cupcake.nikky.imperium.tower.buff;

import cupcake.nikky.imperium.tower.attribute.AttributeInstance;
import cupcake.nikky.imperium.tower.buff.exact.*;
import cupcake.nikky.imperium.tower.buff.exact.amplifier.*;
import cupcake.nikky.imperium.tower.buff.exact.aspect.RegenerationDistortionDebuff;
import cupcake.nikky.imperium.tower.buff.exact.charge.EnduranceChargeBuff;
import cupcake.nikky.imperium.tower.buff.exact.charge.FrenzyChargeBuff;
import cupcake.nikky.imperium.tower.buff.exact.charge.PowerChargeBuff;
import cupcake.nikky.imperium.tower.buff.exact.critical.chance.IncreaseCriticalChanceBuff;
import cupcake.nikky.imperium.tower.buff.exact.critical.damage.IncreaseCriticalDamage;
import cupcake.nikky.imperium.tower.buff.exact.SlownessDebuff;
import cupcake.nikky.imperium.tower.buff.exact.ElementalResistanceInvert;
import cupcake.nikky.imperium.tower.buff.exact.Ignite;
import cupcake.nikky.imperium.tower.buff.exact.AttackSpeedDebuff;
import cupcake.nikky.imperium.tower.buff.exact.NecrosisDebuff;
import cupcake.nikky.imperium.tower.buff.exact.monster.yihochma.HochmaStabilityBuff;
import cupcake.nikky.imperium.tower.buff.exact.potion.PotionHeal;
import cupcake.nikky.imperium.tower.buff.exact.potion.PotionMana;
import cupcake.nikky.imperium.tower.buff.exact.potion.PotionRegeneration;
import cupcake.nikky.imperium.tower.buff.exact.potion.PotionSpeed;
import cupcake.nikky.imperium.tower.buff.exact.regeneration.RegenerationInTickBuff;
import cupcake.nikky.imperium.tower.buff.exact.MonsterImmobility;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.client.protocol.image.FantasyImage;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.buff.BuffCategory;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectRegistry;
import cupcake.nikky.imperium.tower.item.exact.aspect.exact.*;
import cupcake.nikky.imperium.tower.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import me.anfanik.steda.api.utility.TextUtility;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public enum BuffType {

    NECROSIS(1, BuffCategory.NEGATIVE, FantasyImage.NECROSIS, NecrosisDebuff::new),
    BLEEDING(2, BuffCategory.NEGATIVE, FantasyImage.BLEEDING, (value, duration) -> new Bleeding(duration, value)),
    ATTACK_SPEED_DEBUFF(4, BuffCategory.NEGATIVE, FantasyImage.WEAK, AttackSpeedDebuff::new),
    HOCHMA_STABILITY_BUFF(5, BuffCategory.POSITIVE, FantasyImage.FRACTION_GENERAL, HochmaStabilityBuff::new),
    SLOWNESS(7, BuffCategory.NEGATIVE, FantasyImage.SLOWNESS, SlownessDebuff::new),
    POISON(10, BuffCategory.NEGATIVE, FantasyImage.POISON, (value, duration) -> new Poison(duration, value)),
    POTION_SPEED_4(13, BuffCategory.POSITIVE, FantasyImage.SPEED_UP, () -> new PotionSpeed(20)),
    POTION_HEAL_4(14, BuffCategory.POSITIVE, FantasyImage.HEAL, () -> new PotionHeal(2)),
    POTION_CRITICAL_CHANCE_4(16, BuffCategory.POSITIVE, FantasyImage.CRITICAL_CHANCE, () -> new IncreaseCriticalChanceBuff(15)),
    POTION_CRITICAL_DAMAGE_4(17, BuffCategory.POSITIVE, FantasyImage.CRITICAL_DAMAGE, () -> new IncreaseCriticalDamage(20)),

    SHOCK(8, BuffCategory.NEGATIVE, FantasyImage.SHOCK, Shock::new),
    IGNITE(6, BuffCategory.NEGATIVE, FantasyImage.IGNITE, Ignite::new),
    FREEZE(18, BuffCategory.NEGATIVE, FantasyImage.FREEZE, Freeze::new),

    POTION_SPEED_7(19, BuffCategory.POSITIVE, FantasyImage.SPEED_UP, () -> new PotionSpeed(27.5)),
    POTION_HEAL_7(20, BuffCategory.POSITIVE, FantasyImage.HEAL, () -> new PotionHeal(3.1)),
    POTION_CRITICAL_CHANCE_7(22, BuffCategory.POSITIVE, FantasyImage.CRITICAL_CHANCE, () -> new IncreaseCriticalChanceBuff(20)),
    POTION_CRITICAL_DAMAGE_7(23, BuffCategory.POSITIVE, FantasyImage.CRITICAL_DAMAGE, () -> new IncreaseCriticalDamage(30)),

    POTION_SPEED_10(23, BuffCategory.POSITIVE, FantasyImage.SPEED_UP, () -> new PotionSpeed(35)),
    POTION_HEAL_10(24, BuffCategory.POSITIVE, FantasyImage.HEAL, () -> new PotionHeal(4.5)),
    POTION_CRITICAL_CHANCE_10(26, BuffCategory.POSITIVE, FantasyImage.CRITICAL_CHANCE, () -> new IncreaseCriticalChanceBuff(30)),
    POTION_CRITICAL_DAMAGE_10(27, BuffCategory.POSITIVE, FantasyImage.CRITICAL_DAMAGE, () -> new IncreaseCriticalDamage(50)),

    IMMOBILITY(30, BuffCategory.NEGATIVE, FantasyImage.IMMOBILITY, MonsterImmobility::new),
    BURN(32, BuffCategory.NEGATIVE, FantasyImage.WISDOM, Ignite::new),
    SKARIGA_DEBUFF(33, BuffCategory.NEGATIVE, FantasyImage.WISDOM, () -> new FantasyBuff(7, "Скаррига: Страх")
            .registerPercentAttribute(ImperiumAttribute.ATTACK_SPEED, -40)
            .setShouldInvalidateAfterDeath(true)),

    ELEMENTAL_RESISTANCE_INVERT(34, BuffCategory.NEGATIVE, FantasyImage.WEAK, ElementalResistanceInvert::new),

    POTION_MASK_CHANCE_10(35, BuffCategory.POSITIVE, FantasyImage.CHANCE, () -> new FantasyBuff(60 * 10, "Шанс выпадения: Слепок модификатора")
            .registerPercentAttribute(ImperiumAttribute.INCREASED_MASK_DROP_CHANCE, 50)),
    POTION_REGENERATION_MANA_4(36, BuffCategory.POSITIVE, FantasyImage.EVASION_BUFF, () -> new PotionMana(4)),
    POTION_REGENERATION_MANA_7(37, BuffCategory.POSITIVE, FantasyImage.EVASION_BUFF, () -> new PotionMana(6.2)),
    POTION_REGENERATION_MANA_10(38, BuffCategory.POSITIVE, FantasyImage.EVASION_BUFF, () -> new PotionMana(9)),

    POWER_CHARGE(50, BuffCategory.POSITIVE, FantasyImage.POWER_CHARGE, PowerChargeBuff::new),
    ENDURANCE_CHARGE(51, BuffCategory.POSITIVE, FantasyImage.ENDURANCE_CHARGE, EnduranceChargeBuff::new),
    FRENZY_CHARGE(52, BuffCategory.POSITIVE, FantasyImage.FRENZY_CHARGE, FrenzyChargeBuff::new),

    FRAGMENT_DEBUFF_INFO(70, FantasyImage.FRAGMENT_INFO, () -> new FantasyBuff(Integer.MAX_VALUE, "Модификаторы Осколка") {

        private Player player;

        @Override
        public void onReceive(LivingEntity entity) {
            super.onReceive(entity);

            if (entity instanceof Player player)
                this.player = player;
        }

        @Override
        public List<String> getDescription() {
            return player == null ? Collections.emptyList() : player.<User>getBungeePlayer().findFragmentSettings().getDescriptionOfFragmentModifiers();
        }

    }),

    DEFENSE_AMPLIFIER(100, BuffCategory.POSITIVE, FantasyImage.DEFENSE_POSITIVE, DefenseAmplifier::new),
    DEFENSE_PENETRATION_AMPLIFIER(101, BuffCategory.POSITIVE, FantasyImage.DEFENSE_NEGATIVE, DefensePenetrationAmplifier::new),
    FATUM_DEFENSE_AMPLIFIER(102, BuffCategory.POSITIVE, FantasyImage.DEFENSE_POSITIVE, FatumDefense::new),
    EDGE_DEFENSE_AMPLIFIER(103, BuffCategory.POSITIVE, FantasyImage.DEFENSE_POSITIVE, EdgeDefense::new),
    EDGE_PROTECTION_AMPLIFIER(104, BuffCategory.POSITIVE, FantasyImage.HEAL, EdgeProtection::new),
    SPEED_AMPLIFIER(105, BuffCategory.POSITIVE, FantasyImage.SPEED_UP, SpeedAmplifier::new),
    BLEED_AMPLIFIER(106, BuffCategory.POSITIVE, FantasyImage.ATTACK_BUFF, () -> new FantasyBuff(Integer.MAX_VALUE, "Усиление кровотечения")
            .registerPercentAttribute(ImperiumAttribute.CHANCE_TO_CAUSE_BLEEDING, 100)),
    ATTACK_AMPLIFIER(106, BuffCategory.POSITIVE, FantasyImage.ATTACK_BUFF, () -> new FantasyBuff(Integer.MAX_VALUE, "Усиление атаки") {
                @Override
                public List<String> getDescription() {
                    return List.of("&350% &6Увеличение урона");
                }
            }.registerMultiplierAttribute(ImperiumAttribute.MIN_PHYSICAL_DAMAGE, 1.50)
            .registerMultiplierAttribute(ImperiumAttribute.MAX_PHYSICAL_DAMAGE, 1.50)
            .registerMultiplierAttribute(ImperiumAttribute.MIN_FIRE_DAMAGE, 1.50)
            .registerMultiplierAttribute(ImperiumAttribute.MAX_FIRE_DAMAGE, 1.50)
            .registerMultiplierAttribute(ImperiumAttribute.MIN_COLD_DAMAGE, 1.50)
            .registerMultiplierAttribute(ImperiumAttribute.MAX_COLD_DAMAGE, 1.50)
            .registerMultiplierAttribute(ImperiumAttribute.MIN_LIGHTNING_DAMAGE, 1.50)
            .registerMultiplierAttribute(ImperiumAttribute.MAX_LIGHTNING_DAMAGE, 1.50)
            .registerMultiplierAttribute(ImperiumAttribute.MIN_CHAOS_DAMAGE, 1.50)
            .registerMultiplierAttribute(ImperiumAttribute.MAX_CHAOS_DAMAGE, 1.50)
    ),

    EVENT_EXP_BUFF(150, BuffCategory.POSITIVE, FantasyImage.EXPERIENCE_BUFF, () -> new FantasyBuff(60 * 60, "Весеннее усиление опыта")
            .registerPercentAttribute(ImperiumAttribute.BATTLE_EXPERIENCE_MULTIPLIER, 40)
            .setDurationOnlineOnly(false)),
    EVENT_DEFENSE_BUFF(151, BuffCategory.POSITIVE, FantasyImage.DEFENSE_POSITIVE, () -> new FantasyBuff(60 * 60, "Весеннее усиление защиты")
            .registerPercentAttribute(ImperiumAttribute.DEFENSE, 15)
            .setDurationOnlineOnly(false)),
    EVENT_ELEM_RES_BUFF(152, BuffCategory.POSITIVE, FantasyImage.DEFENSE_POSITIVE, () -> new FantasyBuff(60 * 60, "Весеннее усиление стихийных сопротивлений")
            .registerPercentAttribute(ImperiumAttribute.FIRE_RESISTANCE, 25)
            .registerPercentAttribute(ImperiumAttribute.COLD_RESISTANCE, 25)
            .registerPercentAttribute(ImperiumAttribute.LIGHTNING_RESISTANCE, 25)
            .setDurationOnlineOnly(false)),
    EVENT_CHAOS_RES_BUFF(153, BuffCategory.POSITIVE, FantasyImage.DEFENSE_POSITIVE, () -> new FantasyBuff(60 * 60, "Весеннее усиление сопротивления хаосу")
            .registerPercentAttribute(ImperiumAttribute.CHAOS_RESISTANCE, 20)
            .setDurationOnlineOnly(false)),
    EVENT_ATTACK_BUFF(154, BuffCategory.POSITIVE, FantasyImage.ATTACK_BUFF, () -> new FantasyBuff(60 * 60, "Весеннее усиление атаки") {
        @Override
        public List<String> getDescription() {
            return Collections.singletonList(TextUtility.colorize("&315% &6Увеличение максимального физического, элементального и хаос урона"));
        }

        @Override
        public void onReceive(LivingEntity entity) {
            super.onReceive(entity);
            addAttributeHandler(level -> {
                getAttributes().register(ImperiumAttribute.MAX_PHYSICAL_DAMAGE, new AttributeInstance(0).applyIncrease(0.15));
                getAttributes().register(ImperiumAttribute.MAX_FIRE_DAMAGE, new AttributeInstance(0).applyIncrease(0.15));
                getAttributes().register(ImperiumAttribute.MAX_COLD_DAMAGE, new AttributeInstance(0).applyIncrease(0.15));
                getAttributes().register(ImperiumAttribute.MAX_LIGHTNING_DAMAGE, new AttributeInstance(0).applyIncrease(0.15));
                getAttributes().register(ImperiumAttribute.MAX_CHAOS_DAMAGE, new AttributeInstance(0).applyIncrease(0.15));
            });
        }
    }.setDurationOnlineOnly(false)),
    EVENT_HEALTH_BUFF(155, BuffCategory.POSITIVE, FantasyImage.HEAL, () -> new FantasyBuff(60 * 60, "Весеннее усиление здоровья") {
        @Override
        public List<String> getDescription() {
            return Collections.singletonList(TextUtility.colorize("&320% &6Увеличение здоровья"));
        }

        @Override
        public void onReceive(LivingEntity entity) {
            super.onReceive(entity);
            addAttributeHandler(level -> getAttributes().register(ImperiumAttribute.HEALTH, new AttributeInstance(0).applyIncrease(0.20)));
        }
    }.setDurationOnlineOnly(false)),

    WEAK_EXPERIENCE_BUFF(201, BuffCategory.NEGATIVE, FantasyImage.WEAK, () -> new FantasyBuff(60 * 5, "Слабость") {
                @Override
                public List<String> getDescription() {
                    return Collections.singletonList(TextUtility.colorize("&325% &6Уменьшение получаемого опыта"));
                }
    }.setDurationOnlineOnly(false)),

    EXPERIENCE_BUFF_REKT(310, BuffCategory.POSITIVE, FantasyImage.EXPERIENCE_BUFF, () -> new FantasyBuff(60 * 60 * 24 * 14, "Усиление Ректа, опыт")
            .registerPercentAttribute(ImperiumAttribute.BATTLE_EXPERIENCE_MULTIPLIER, 500)
            .registerPercentAttribute(ImperiumAttribute.INCREASE_TOOL_CURRENCY, 100)
            .registerPercentAttribute(ImperiumAttribute.INCREASE_TOOL_EXP, 100)
            .setDurationOnlineOnly(true)),
    SUPPORT_ATTACK_BUFF_REKT(311, BuffCategory.POSITIVE, FantasyImage.SUPPORT_ATTACK_BUFF, () -> new FantasyBuff(60 * 60 * 24 * 14, "Усиление Ректа, атака") {
        @Override
        public List<String> getDescription() {
            return Collections.singletonList(TextUtility.colorize("&3100% &6Увеличение урона"));
        }

        @Override
        public void onReceive(LivingEntity entity) {
            super.onReceive(entity);
            addAttributeHandler(level -> {
                getAttributes().register(ImperiumAttribute.MIN_PHYSICAL_DAMAGE, new AttributeInstance(0).applyIncrease(1));
                getAttributes().register(ImperiumAttribute.MAX_PHYSICAL_DAMAGE, new AttributeInstance(0).applyIncrease(1));

                getAttributes().register(ImperiumAttribute.MIN_FIRE_DAMAGE, new AttributeInstance(0).applyIncrease(1));
                getAttributes().register(ImperiumAttribute.MAX_FIRE_DAMAGE, new AttributeInstance(0).applyIncrease(1));

                getAttributes().register(ImperiumAttribute.MIN_COLD_DAMAGE, new AttributeInstance(0).applyIncrease(1));
                getAttributes().register(ImperiumAttribute.MAX_COLD_DAMAGE, new AttributeInstance(0).applyIncrease(1));

                getAttributes().register(ImperiumAttribute.MIN_LIGHTNING_DAMAGE, new AttributeInstance(0).applyIncrease(1));
                getAttributes().register(ImperiumAttribute.MAX_LIGHTNING_DAMAGE, new AttributeInstance(0).applyIncrease(1));

                getAttributes().register(ImperiumAttribute.MIN_CHAOS_DAMAGE, new AttributeInstance(0).applyIncrease(1));
                getAttributes().register(ImperiumAttribute.MAX_CHAOS_DAMAGE, new AttributeInstance(0).applyIncrease(1));
            });
        }
    }.setDurationOnlineOnly(false)),

    EXPERIENCE_BUFF(321, BuffCategory.POSITIVE, FantasyImage.POTION_1, () -> new FantasyBuff(60 * 60 * 2, "Усиление опыта")
            .registerPercentAttribute(ImperiumAttribute.BATTLE_EXPERIENCE_MULTIPLIER, 50)
            .registerPercentAttribute(ImperiumAttribute.INCREASE_TOOL_CURRENCY, 25)
            .registerPercentAttribute(ImperiumAttribute.INCREASE_TOOL_EXP, 50)
    ),
    SUPPORT_ATTACK_BUFF(322, BuffCategory.POSITIVE, FantasyImage.POTION_2, () -> new FantasyBuff(60 * 60 * 2, "Усиление атаки") {
        @Override
        public List<String> getDescription() {
            return Collections.singletonList(TextUtility.colorize("&315% &6Увеличение урона"));
        }

        @Override
        public void onReceive(LivingEntity entity) {
            super.onReceive(entity);
            addAttributeHandler(level -> {
                getAttributes().register(ImperiumAttribute.MIN_PHYSICAL_DAMAGE, new AttributeInstance(0).applyIncrease(.15));
                getAttributes().register(ImperiumAttribute.MAX_PHYSICAL_DAMAGE, new AttributeInstance(0).applyIncrease(.15));
                getAttributes().register(ImperiumAttribute.MIN_FIRE_DAMAGE, new AttributeInstance(0).applyIncrease(.15));
                getAttributes().register(ImperiumAttribute.MAX_FIRE_DAMAGE, new AttributeInstance(0).applyIncrease(.15));
                getAttributes().register(ImperiumAttribute.MIN_COLD_DAMAGE, new AttributeInstance(0).applyIncrease(.15));
                getAttributes().register(ImperiumAttribute.MAX_COLD_DAMAGE, new AttributeInstance(0).applyIncrease(.15));
                getAttributes().register(ImperiumAttribute.MIN_LIGHTNING_DAMAGE, new AttributeInstance(0).applyIncrease(.15));
                getAttributes().register(ImperiumAttribute.MAX_LIGHTNING_DAMAGE, new AttributeInstance(0).applyIncrease(.15));
                getAttributes().register(ImperiumAttribute.MIN_CHAOS_DAMAGE, new AttributeInstance(0).applyIncrease(.15));
                getAttributes().register(ImperiumAttribute.MAX_CHAOS_DAMAGE, new AttributeInstance(0).applyIncrease(.15));
            });
        }
    }),
    ATTACK_BUFF(323, BuffCategory.POSITIVE, FantasyImage.POTION_3, () -> new FantasyBuff(60 * 60 * 2, "Усиление точности и крит. удара")
            .registerPercentAttribute(ImperiumAttribute.ACCURACY, 15)
            .registerPercentAttribute(ImperiumAttribute.CRITICAL_RATE, 10)
    ),
    CRITICAL_DAMAGE_BUFF(324, BuffCategory.POSITIVE, FantasyImage.POTION_4, () -> new FantasyBuff(60 * 60 * 2, "Усиление защиты и крит. урона")
            .registerPercentAttribute(ImperiumAttribute.CRITICAL_DAMAGE, 35)
            .registerPercentAttribute(ImperiumAttribute.DEFENSE_PENETRATION, 10)
    ),
    DEFENSE_BUFF(325, BuffCategory.POSITIVE, FantasyImage.POTION_5, () -> new FantasyBuff(60 * 60 * 2, "Усиление защиты и здоровья"){
        @Override
        public List<String> getDescription() {
            return List.of(
                    TextUtility.colorize("&3+15% &6Защита"),
                    TextUtility.colorize("&310% &6Увеличение здоровья")
            );
        }
    }.registerPercentAttribute(ImperiumAttribute.DEFENSE, 15).registerMultiplierAttribute(ImperiumAttribute.HEALTH, 0.10)
    ),

    FRACTION_CHANGE_DEBUFF(400, BuffCategory.NEGATIVE, FantasyImage.FRACTION_COOLDOWN, () -> new FantasyBuff(60 * 60 * 24, "Слабость: Смена фракции силы") {

        @Override
        public List<String> getDescription() {
            return TextUtility.colorize(Arrays.asList("&6Слабость не позволяет Вам", "&6сменить фракцию повторно"));
        }

    }.setDurationOnlineOnly(false)),

    FRACTION_BUFF(401, BuffCategory.POSITIVE, FantasyImage.FRACTION_GENERAL, () -> new FantasyBuff(60 * 8, "Усиление фракции: Синергия") {
        @Override
        public List<String> getDescription() {
            return List.of(
                    TextUtility.colorize("&3+15% &6Скорость передвижения"),
                    TextUtility.colorize("&320% &6Увеличение получаемой добычи"),
                    TextUtility.colorize("&320% &6Увеличение выпадаемых предметов")
            );
        }
    }.registerMultiplierAttribute(ImperiumAttribute.MONSTER_QUANTITY_OF_ITEMS, .2)
     .registerPercentAttribute(ImperiumAttribute.MOVEMENT_SPEED, 15)
     .registerPercentAttribute(ImperiumAttribute.INCREASE_TOOL_CURRENCY, 20).setDurationOnlineOnly(false)),

    WIND_KILL(504, BuffCategory.POSITIVE, FantasyImage.SPEED_UP, (value, duration) -> new FantasyBuff(duration, AspectRegistry.getInstance().findAspect(WindKillAspect.class).title()) {
        @Override
        public List<String> getDescription() {
            return List.of("&3%.1f%% &6увеличение Скорости атаки",
                    "&3+%.1f%% &6к Скорости передвижения");
        }
    }.registerPercentAttribute(ImperiumAttribute.ATTACK_SPEED, value).registerPercentAttribute(ImperiumAttribute.MOVEMENT_SPEED, value)),
    REGENERATION_DISTORTION(505, BuffCategory.POSITIVE, FantasyImage.BLEEDING, RegenerationDistortionDebuff::new),
    DEFENSE_DISTORTION(506, BuffCategory.NEGATIVE, FantasyImage.DEFENSE_NEGATIVE, (value, duration) ->
            new FantasyBuff(duration, AspectRegistry.getInstance().findAspect(DefenseDistortionAspect.class).title()).registerPercentAttribute(ImperiumAttribute.DEFENSE, value)),
    TITAN_SHROUD(507, BuffCategory.POSITIVE, FantasyImage.DEFENSE_POSITIVE, (value, duration) ->
            new FantasyBuff(duration, AspectRegistry.getInstance().findAspect(TitanShroudAspect.class).title()).registerPercentAttribute(ImperiumAttribute.GLOBAL_DEFENSE, value)),

    GREEN_EDGE_CORE(1000, BuffCategory.POSITIVE, FantasyImage.WISDOM, (value, duration) -> new FantasyBuff(duration, "Ядро Грани: Жадность") {
                @Override
                public List<String> getDescription() {
                    return List.of("&3%.0f%% &6увеличение количества выпадаемых предметов".formatted(value));
                }
            }.registerMultiplierAttribute(ImperiumAttribute.MONSTER_QUANTITY_OF_ITEMS, value / 100)
            .setShouldInvalidateAfterDeath(true)),
    RED_EDGE_CORE(1001, BuffCategory.POSITIVE, FantasyImage.HEAL, (value, duration) -> new RegenerationInTickBuff(duration, value).setShouldInvalidateAfterDeath(true)),
    PURPLE_EDGE_CORE(1002, BuffCategory.POSITIVE, FantasyImage.CRITICAL_DAMAGE, (value, duration) -> new FantasyBuff(duration, "Ядро Грани: Усиление атаки") {
                @Override
                public List<String> getDescription() {
                    return Collections.singletonList(TextUtility.colorize(String.format("&3%.1f%% &6увеличение урона", value * 100)));
                }

                @Override
                public void onReceive(LivingEntity entity) {
                    super.onReceive(entity);
                    addAttributeHandler(level -> {
                        getAttributes().register(ImperiumAttribute.MIN_PHYSICAL_DAMAGE, new AttributeInstance(0).applyIncrease(value));
                        getAttributes().register(ImperiumAttribute.MAX_PHYSICAL_DAMAGE, new AttributeInstance(0).applyIncrease(value));
                        getAttributes().register(ImperiumAttribute.MIN_FIRE_DAMAGE, new AttributeInstance(0).applyIncrease(value));
                        getAttributes().register(ImperiumAttribute.MAX_FIRE_DAMAGE, new AttributeInstance(0).applyIncrease(value));
                        getAttributes().register(ImperiumAttribute.MIN_COLD_DAMAGE, new AttributeInstance(0).applyIncrease(value));
                        getAttributes().register(ImperiumAttribute.MAX_COLD_DAMAGE, new AttributeInstance(0).applyIncrease(value));
                        getAttributes().register(ImperiumAttribute.MIN_LIGHTNING_DAMAGE, new AttributeInstance(0).applyIncrease(value));
                        getAttributes().register(ImperiumAttribute.MAX_LIGHTNING_DAMAGE, new AttributeInstance(0).applyIncrease(value));
                        getAttributes().register(ImperiumAttribute.MIN_CHAOS_DAMAGE, new AttributeInstance(0).applyIncrease(value));
                        getAttributes().register(ImperiumAttribute.MAX_CHAOS_DAMAGE, new AttributeInstance(0).applyIncrease(value));
                    });
                }
            }
            .setShouldInvalidateAfterDeath(true)),
    WHITE_EDGE_CORE(1003, BuffCategory.POSITIVE, FantasyImage.DEFENSE_POSITIVE, (value, duration) -> new FantasyBuff(duration, "Ядро Грани: Усиление защиты")
            .registerPercentAttribute(ImperiumAttribute.DEFENSE, value)
            .setShouldInvalidateAfterDeath(true)),
    BLUE_EDGE_CORE(1004, BuffCategory.POSITIVE, FantasyImage.SPEED_UP, (value, duration) -> new FantasyBuff(duration, "Ядро Грани: Ускорение")
            .registerPercentAttribute(ImperiumAttribute.MOVEMENT_SPEED, value)
            .registerPercentAttribute(ImperiumAttribute.ATTACK_SPEED, value)
            .setShouldInvalidateAfterDeath(true)),
    YELLOW_EDGE_CORE(1005, BuffCategory.POSITIVE, FantasyImage.TORNADO_POSITIVE, (value, duration) -> new FantasyBuff(duration, "Ядро Грани: Усиление сопротивлений")
            .registerPercentAttribute(ImperiumAttribute.CHAOS_RESISTANCE_PENETRATION, value)
            .registerPercentAttribute(ImperiumAttribute.COLD_RESISTANCE, value)
            .registerPercentAttribute(ImperiumAttribute.LIGHTNING_RESISTANCE, value)
            .registerPercentAttribute(ImperiumAttribute.FIRE_RESISTANCE, value)
            .setShouldInvalidateAfterDeath(true));

    private final int id;
    private BuffCategory buffCategory = BuffCategory.NEUTRAL;
    private final FantasyImage image;
    private final BiFunction<Double, Integer, Buff> buff;

    BuffType(final int id, @NotNull final BuffCategory buffCategory, @NotNull final FantasyImage image, @NotNull final Supplier<Buff> buffSupplier) {
        this.id = id;
        this.buffCategory = buffCategory;
        this.image = image;
        this.buff = (value, duration) -> buffSupplier.get();
    }

    BuffType(final int id, @NotNull final FantasyImage image, @NotNull final Supplier<Buff> buffSupplier) {
        this.id = id;
        this.image = image;
        this.buff = (value, duration) -> buffSupplier.get();
    }

    public Buff createNew() {
        return this.createNew(1);
    }

    public Buff createNew(final double value, final int duration) {
        val instance = buff.apply(value, duration);
        instance.setMultiplier(1);
        instance.setType(this);
        return instance;
    }

    public Buff createNew(final int multiplier, final double value, final int duration) {
        val instance = buff.apply(value, duration);
        instance.setMultiplier(multiplier);
        instance.setType(this);
        return instance;
    }

    public Buff createNew(final int multiplier) {
        val instance = buff.apply(1D, 0);
        instance.setMultiplier(multiplier);
        instance.setType(this);
        return instance;
    }

}
