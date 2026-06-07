package cupcake.nikky.imperium.tower.client.protocol.attribute;

import lombok.Getter;

@Getter
public enum ImperiumAttribute {

    HEALTH(0, "Здоровье", "&fЗдоровье: %s%.0f%s"),
    MOVEMENT_SPEED(10, "Скорость передвижения", "&fСкорость передвижения: %s%.0f%s", "%"),

    MIN_PHYSICAL_DAMAGE(-1),
    MAX_PHYSICAL_DAMAGE(-1),

    MIN_FIRE_DAMAGE(-1),
    MAX_FIRE_DAMAGE(-1),

    MIN_COLD_DAMAGE(-1),
    MAX_COLD_DAMAGE(-1),

    MIN_LIGHTNING_DAMAGE(-1),
    MAX_LIGHTNING_DAMAGE(-1),

    MIN_CHAOS_DAMAGE(-1),
    MAX_CHAOS_DAMAGE(-1),

    CRITICAL_RATE(5, "Шанс крит. удара", "&fШанс крит. удара: %s%.1f%s",  "%"),
    CRITICAL_DAMAGE(6, "Крит. урон", "&fКрит. урон: %s%.0f%s",  "%"),

    DEFENSE(2, "Защита", "&fЗащита: %s%.1f%s", "%"),
    DEFENSE_PENETRATION(7, "Пробивание", "&fПробитие защиты: %s%.1f%s", "%"),

    EVASION(3, "Уклонение", "&fУклонение: %s%.0f%s", "%"),
    ACCURACY(8, "Точность", "&fТочность: %s%.0f%s", "%"),

    REGENERATION_HEALTH(4, "Восстановление здоровья", "&fВосст. здоровья: %s%.0f%s"),
    REGENERATION_HEALTH_IN_FIGHT(5,"Восстановление здоровья в бою", "&fВосст. здоровья в бою: %s%.0f%s"),

    FIRE_RESISTANCE(6, "Сопротивление огню", "&fСопротивление &cогню&7: %s%.0f%s", "%"),
    COLD_RESISTANCE(7, "Сопротивление холоду", "&fСопротивление &bхолоду&7: %s%.0f%s", "%"),
    LIGHTNING_RESISTANCE(8, "Сопротивление молнии", "&fСопротивление &fмолнии&7: %s%.0f%s", "%"),
    CHAOS_RESISTANCE(9, "Сопротивление хаосу", "&fСопротивление &5хаосу&7: %s%.0f%s", "%"),

    FIRE_RESISTANCE_MAXIMUM(-1, "Максимум сопротивления огню", "&fМаксимум сопротивления огню: %s%.0f%s", "%"),
    COLD_RESISTANCE_MAXIMUM(-1, "Максимум сопротивления холоду", "&fМаксимум сопротивления холоду: %s%.0f%s", "%"),
    LIGHTNING_RESISTANCE_MAXIMUM(-1, "Максимум сопротивления молнии", "&fМаксимум сопротивления молнии: %s%.0f%s", "%"),
    CHAOS_RESISTANCE_MAXIMUM(-1, "Максимум сопротивления хаосу", "&fМаксимум сопротивления хаосу: %s%.0f%s", "%"),

    FIRE_RESISTANCE_PENETRATION(6, "Игнорирование сопротивления огню", "&fИгнорирование сопротивления огню: %s%.0f%s", "%"),
    COLD_RESISTANCE_PENETRATION(7, "Игнорирование сопротивления холоду", "&fИгнорирование сопротивления холоду: %s%.0f%s", "%"),
    LIGHTNING_RESISTANCE_PENETRATION(8, "Игнорирование сопротивления молнии", "&fИгнорирование сопротивления молнии: %s%.0f%s", "%"),
    CHAOS_RESISTANCE_PENETRATION(9, "Игнорирование сопротивления хаосу", "&fИгнорирование сопротивления хаосу: %s%.0f%s", "%"),

    BATTLE_EXPERIENCE_MULTIPLIER(9, "Боевой опыт", "&fБоевой опыт: %s%.0f%s", "%"),
    ATTACK_SPEED(10, "Скорость атаки", "&fСкорость атаки: %s%.0f%s", "%"),

    GLOBAL_DEFENSE(-1, "Уменьшение урона", "&fУменьшение урона: %s%.0f%s", "%"),
    MORE_DAMAGE(-1, "Увеличение урона", "&fУвеличение урона: %s%.0f%s", "%"),

    MANA(3, "Максимум маны", "&fМаксимум маны: %s%.0f%s"),
    MANA_REGENERATION(4, "Восстановление маны", "&fВосстановление маны: %s%.0f%s"),
    MANA_RESERVED(-1, "Зарезервированная мана", "&fЗарезервированная мана: %s%.0f%s", "%"),
    MANA_RESERVED_VALUE(-1, "Зарезервированная мана", "&fЗарезервированная мана: %s%.0f%s"),
    MANA_RESERVATION_EFFICIENCY(-1, "Эффективность резервации маны", "&fЭфф. резервации маны: %s%.0f%s", "%"),

    CHANCE_TO_CAUSE_BLEEDING(11, "Шанс вызвать кровотичение", "&fШанс вызвать кровотичение: %s%.1f%s", "%"),
    CHANCE_TO_POISON(12, "Шанс отравить", "&fШанс отравить: %s%.1f%s", "%"),

    AILMENT_THRESHOLD(-7),
    AILMENT_DURATION(-7),

    CHANCE_TO_FREEZE_ON_HIT(-7),
    FREEZE_DURATION(-7),

    CHANCE_TO_IGNITE_ON_HIT(-7),
    IGNITE_DAMAGE(-7),

    SHOCK_EFFECT(-7),
    CHANCE_TO_SHOCK_ON_HIT(-7),

    CHANCE_TO_GAIN_POWER_CHARGE_ON_HIT(),
    CHANCE_TO_GAIN_ENDURANCE_CHARGE_ON_HIT(),
    CHANCE_TO_GAIN_FRENZY_CHARGE_ON_HIT(),

    CAN_RECEIVE_ONLY_PHYSICAL_AND_CHAOS_DAMAGE_BY_PLAYER(-1),
    CONVERT_ELEMENT_DAMAGE_INTO_PHYS_DAMAGE(-1),
    INCREASE_CRITICAL_RATE_ON_PLAYER(-1),

    POWER_CHARGE(-1, "Заряд энергии", ""),
    ENDURANCE_CHARGE(-1, "Заряд выносливости", ""),
    FRENZY_CHARGE(-1, "Заряд безумия", ""),

    FRAGMENT_CANNOT_DEAL_CRITICAL_STRIKE(-1),
    FRAGMENT_CANNOT_REGENERATE_HEALTH_AND_MANA(-1),
    FRAGMENT_INCREASE_INPUT_FIRE_DAMAGE(-1),
    FRAGMENT_INCREASE_INPUT_COLD_DAMAGE(-1),
    FRAGMENT_INCREASE_INPUT_LIGHTNING_DAMAGE(-1),
    FRAGMENT_INCREASE_INPUT_CHAOS_DAMAGE(-1),
    FRAGMENT_CHANCE_GET_DOUBLE_DAMAGE(-1),
    FRAGMENT_REFLECT_PHYSICAL_DAMAGE(-1),
    FRAGMENT_REFLECT_ELEMENTAL_DAMAGE(-1),

    FRAGMENT_EVENT_MAXIMUM_AMOUNT(-1),

    INCREASED_MASK_DROP_CHANCE(-3, "Увеличение шанса выпадения слепка модификатора", "%s%.0f%s &fувеличение шанса выпадения слепка модификатора", "%"),
    LEECH_PERCENT(-5, "Похищение здоровья", "%s%.1f%s &fурона от атак похищается в виде здоровья", "%"),
    SWORD_INCREASE_DAMAGE(-7),
    DAGGER_INCREASE_DAMAGE(-7),
    SPEAR_INCREASE_DAMAGE(-7),
    BATTLE_AXE_INCREASE_DAMAGE(-7),
    DAMAGE_OVER_TIME(-7, "Постепенный урон", ""),
    SPELL_DAMAGE(-7),

    MASK_ENTITY_SPAWN_CHANCE(-7),
    ADDITIONAL_MASK_DROP(-7),
    INCREASE_MASK_DROP_AMOUNT(-7),

    INCREASE_EDGE_CORE_CHANCE(-7),
    INCREASE_EDGE_CORE_DURATION(-7),
    INCREASE_EDGE_CORE_AMPLIFIER(-7),

    PANDORA_CHANCE(-7),
    PANDORA_BASE_ATTEMPTS(-7),
    PANDORA_BASE_PRICE(-7),
    PANDORA_HAS_EQUIP(-7),

    INCREASE_DROP_CHANCE_WAY_FRAGMENT(-7),
    INCREASE_DROP_CHANCE_INCUBATOR(-7),
    INCREASE_FRAGMENT_DURATION(-7),

    MONSTER_INCREASE_EXP_DROP(-7),
    MONSTER_QUANTITY_OF_ITEMS(-7, "Увеличение выпадаемых предметов", "%s%.0f%s &fувеличение выпадаемых предметов", "%"),
    MONSTER_CHANCE_TO_DROP_RELICT_ITEMS(-7),

    INCREASE_TOOL_EXP(-8, "Увеличение получаемого опыта инструментом", "%s%.0f%s &fувеличение получаемого опыта инструментом", "%"),
    INCREASE_TOOL_CURRENCY(-9, "Увеличение получаемой добычи", "%s%.0f%s &fувеличение получаемой добычи", "%"),
    INCREASE_TOOL_POWER(-10),
    INCREASE_TOOL_CHANCE_TO_DOUBLE_EXP(-11),
    INCREASE_TOOL_CHANCE_TO_DOUBLE_CURRENCY(-12);

    private final int position;
    private final String title;
    private final String descriptionText;
    private final String specialSym;
    private final String negativeColor;
    private final String positiveColor;
    private final String neutralColor;

    ImperiumAttribute(int position, String title, String descriptionText) {
        this(position, title, descriptionText, "", "&3", "&c", "&3");
    }

    ImperiumAttribute(int position, String title, String descriptionText, String specialSym) {
        this(position, title, descriptionText, specialSym, "&3", "&c", "&3");
    }

    ImperiumAttribute(int position, String title, String descriptionText, String specialSym, String positiveColor, String negativeColor, String neutralColor) {
        this.position = position;
        this.title = title;
        this.descriptionText = descriptionText;
        this.specialSym = specialSym;
        this.negativeColor = negativeColor;
        this.positiveColor = positiveColor;
        this.neutralColor = neutralColor;
    }

    ImperiumAttribute(int position) {
        this(position, "unknown", "unknown", "", "&3", "&c", "&3");
    }

    ImperiumAttribute() {
        this(-1);
    }

    public static ImperiumAttribute[] findAttackAttributes() {
        return new ImperiumAttribute[] {
                MANA, MANA_REGENERATION, CRITICAL_RATE, CRITICAL_DAMAGE, DEFENSE_PENETRATION, ACCURACY, ATTACK_SPEED, BATTLE_EXPERIENCE_MULTIPLIER
        };
    }

    public static ImperiumAttribute[] findDefenseAttributes() {
        return new ImperiumAttribute[] {
                HEALTH, DEFENSE, /*EVASION,*/ REGENERATION_HEALTH, REGENERATION_HEALTH_IN_FIGHT, FIRE_RESISTANCE, COLD_RESISTANCE, LIGHTNING_RESISTANCE, CHAOS_RESISTANCE, MOVEMENT_SPEED
        };
    }

    public static ImperiumAttribute[] findAttackAndDefenseAttributes() {
        return new ImperiumAttribute[] {
                MANA, MANA_REGENERATION, CRITICAL_RATE, CRITICAL_DAMAGE, DEFENSE_PENETRATION, ACCURACY, ATTACK_SPEED, BATTLE_EXPERIENCE_MULTIPLIER,
                HEALTH, DEFENSE, /*EVASION,*/ REGENERATION_HEALTH, REGENERATION_HEALTH_IN_FIGHT, FIRE_RESISTANCE, COLD_RESISTANCE, LIGHTNING_RESISTANCE, CHAOS_RESISTANCE, MOVEMENT_SPEED
        };
    }

    public String generateWithValue(final double value) {
        return this.generateWithValue(value, true);
    }

    public String generateWithValue(final double value, boolean specialSymbol) {
        return String.format(getDescriptionText(), value > 0 ? getPositiveColor() : value < 0 ? getNegativeColor() : getNeutralColor(), value, specialSymbol ? getSpecialSym() : "");
    }

}
