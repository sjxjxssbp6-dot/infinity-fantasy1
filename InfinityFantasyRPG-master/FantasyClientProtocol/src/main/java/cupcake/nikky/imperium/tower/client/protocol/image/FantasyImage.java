package cupcake.nikky.imperium.tower.client.protocol.image;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@AllArgsConstructor
public enum FantasyImage {

    SLOWNESS("buff/slowness.png"),
    NECROSIS("buff/necrosis.png"),
    BLEEDING("buff/bleeding.png"),

    IGNITE("buff/fire_2.png"),
    SHOCK("buff/lightning.png"),
    FREEZE("buff/snowflake.png"),

    DAMAGE_OVER_TIME("dot.png"),
    CRITICAL_ATTACK("critical_attack.png"),
    ATTACK_SPEED("speed_attack.png"),
    MOVEMENT_SPEED("movement_speed.png"),
    SPEED("speed.png"),
    SPELL("spell.png"),
    CONVERT("convert.png"),
    UPGRADE("upgrade.png"),
    EXPERIENCE("experience.png"),

    BESTIARY("bestiary.png"),
    ACHIEVEMENTS("achievements.png"),
    QUESTS("quests.png"),
    WARDROBE("wardrobe.png"),
    PAPER("paper.png"),
    TREE_ICON("tree.png"),
    CHEST("chest.png"),

    FRACTION_ORDER("order.png"),
    FRACTION_CHAOS("chaos.png"),
    FRACTION_COOLDOWN("fraction_cd.png"),
    FRACTION_GENERAL("fraction_general.png"),
    FRACTION_TELEPORT("fraction_teleport.png"),

    FRAGMENT_INFO("fragment_info.png"),

    BASE_BLOOD("base_blood.png"),
    HEART_BLOOD("heart_blood.png"),
    WHITE_BLOOD("white_blood.png"),
    MULTICOLORED_BLOOD("multicolored_blood.png"),

    DEATH_DARK_EXPLOSION("death/black_explosion.png"),
    DEATH_PURPLE_EXPLOSION("death/purple_explosion.png"),
    DEATH_RED_FADE("death/red_fade.png"),

    POTION_1("buff/potion2/dbuff_1.png"),
    POTION_2("buff/potion2/dbuff_2.png"),
    POTION_3("buff/potion2/dbuff_3.png"),
    POTION_4("buff/potion2/dbuff_4.png"),
    POTION_5("buff/potion2/dbuff_5.png"),
    POTION_6("buff/potion2/dbuff_6.png"),

    WEAK("buff/weak.png"),
    SPEED_UP("buff/speedup.png"),
    HEAL("buff/heal.png"),
    CRITICAL_CHANCE("buff/crit_chance.png"),
    CRITICAL_DAMAGE("buff/crit_dmg.png"),
    WISDOM("buff/wisdom.png"),
    IMMOBILITY("buff/immobility.png"),
    DEFENSE_POSITIVE("buff/def_pos.png"),
    DEFENSE_NEGATIVE("buff/def_neg.png"),
    TORNADO_POSITIVE("buff/tornado_pos.png"),
    TORNADO_NEGATIVE("buff/tornado_neg.png"),
    POWER_CHARGE("buff/power_charge.png"),
    ENDURANCE_CHARGE("buff/endurance_charge.png"),
    FRENZY_CHARGE("buff/frenzy_charge.png"),
    EXPERIENCE_BUFF("buff/experience.png"),
    CRITICAL_BUFF("buff/crit.png"),
    DEFENSE_BUFF("buff/defense.png"),
    EVASION_BUFF("buff/evasion.png"),
    ATTACK_BUFF("buff/attack.png"),
    SUPPORT_ATTACK_BUFF("buff/sup_attack.png"),
    CHANCE("buff/chance.png"),
    POISON("buff/poison.png"),

    CURRENCY("https://storage.c7x.dev/one1side/", "emoji/203.png"),
    MINE("https://storage.c7x.dev/one1side/", "emoji/205.png"),
    GRAN("https://storage.c7x.dev/one1side/", "emoji/197.png"),
    HARVEST_EXP("https://storage.c7x.dev/one1side/", "emoji/366.png"),
    DURATION("https://storage.c7x.dev/one1side/", "emoji/11.png"),
    FLOWER_1("flower_1.png"),

    HEART("https://storage.c7x.dev/one1side/", "emoji/353.png"),
    MANA("mana.png"),
    MANA8("mana8.png"),
    HEALTH("health.png"),

    LOOT_FILTER("hopper_up.png"),
    BUFFS("buffs.png"),
    COINS_1("coins_1.png"),
    COINS_2("coins_2.png"),
    COINS_3("coins_3.png"),
    PREMIUM("golden_crown.png"),

    GUIDE_PLAYER_TRADE_1("guide/player_trade_1.png"),
    GUIDE_MOUNT_1("guide/mount_1.png"),
    GUIDE_LEVEL_1("guide/level_1.png"),
    GUIDE_MANA_1("guide/mana_1.png"),
    GUIDE_TREE_1("guide/tree_1.png"),
    GUIDE_TREE_2("guide/tree_2.png"),
    GUIDE_ITEMS_1("guide/items_1.png"),
    GUIDE_ITEMS_3("guide/items_3.png"),
    GUIDE_TELEPORTS_1("guide/teleports_1.png"),
    GUIDE_DONATE_1("guide/donate_1.png"),
    GUIDE_ASPECT_1("guide/aspect_1.png"),
    GUIDE_FIRMOLOG_1("guide/firmolog_1.png"),
    GUIDE_FIRMOLOG_3("guide/firmolog_3.png"),
    GUIDE_FILTER_1("guide/filter_1.png"),
    GUIDE_STAR_1("guide/star_1.png"),
    GUIDE_STAR_2("guide/star_2.png"),
    GUIDE_AURA_1("guide/aura_1.png"),

    TELEPORT_1("teleport/tp_1.png"),
    TELEPORT_2("teleport/tp_2.png"),
    TELEPORT_3("teleport/tp_3.png"),
    TELEPORT_4("teleport/tp_4.png"),
    TELEPORT_5("teleport/tp_5.png"),
    TELEPORT_6("teleport/tp_6.png"),
    TELEPORT_7("teleport/tp_7.png"),
    TELEPORT_8("teleport/tp_8.png"),
    TELEPORT_9("teleport/tp_9.png"),
    TELEPORT_10("teleport/tp_10.png"),
    TELEPORT_11("teleport/tp_11.png"),
    TELEPORT_12("teleport/tp_12.png"),
    TELEPORT_13("teleport/tp_13.png"),
    TELEPORT_14("teleport/tp_14.png"),
    TELEPORT_15("teleport/tp_15.png"),
    TELEPORT_16("teleport/tp_16.png"),
    TELEPORT_17("teleport/tp_17.png"),
    TELEPORT_18("teleport/tp_18.png"),
    TELEPORT_19("teleport/tp_19.png"),
    TELEPORT_20("teleport/tp_20.png"),
    TELEPORT_21("teleport/tp_21.png"),
    TELEPORT_22("teleport/tp_22.png"),
    TELEPORT_23("teleport/tp_23.png"),
    TELEPORT_24("teleport/tp_24.png"),
    TELEPORT_25("teleport/tp_25.png"),
    TELEPORT_26("teleport/tp_26.png"),
    TELEPORT_27("teleport/tp_27.png"),
    TELEPORT_28("teleport/tp_28.png"),
    TELEPORT_29("teleport/tp_29.png"),
    TELEPORT_30("teleport/tp_30.png"),
    TELEPORT_31("teleport/tp_31.png"),
    TELEPORT_32("teleport/tp_32.png"),
    TELEPORT_33("teleport/tp_33.png");

    private String url = "https://storage.c7x.dev/neitexx/";
    @Getter private final String name;

    public String getUrl() {
        return url + name;
    }

    public String toTexture() {
        return "neitexx:" + getName();
    }

    public static FantasyImage findByName(@NotNull final String name) {
        for (FantasyImage value : values()) {
            if (value.name().equalsIgnoreCase(name)) return value;
        }

        return null;
    }

}
