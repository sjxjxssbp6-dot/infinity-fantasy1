package cupcake.nikky.imperium.tower.item.generator.name;

import cupcake.nikky.imperium.tower.entity.monster.MonsterType;
import lombok.*;
import me.anfanik.steda.api.utility.MathUtility;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EquipDisplayNameGenerator {

    @Getter
    private static final EquipDisplayNameGenerator instance = new EquipDisplayNameGenerator().initialize();

    private final Map<MonsterType, String[][]> data = new EnumMap<>(MonsterType.class);

    public EquipDisplayNameGenerator initialize() {
        this.data.put(MonsterType.GHOUL, new String[][]{{"Мертвый"}, {"Мертвое"}, {"Мертвые"}});
        this.data.put(MonsterType.INFECTED_WOLF, new String[][]{{"Зараженный"}, {"Зараженное"}, {"Зараженные"}});
        this.data.put(MonsterType.STILLBORN_SPIDER, new String[][]{{"Мертворожденный"}, {"Мертворожденное"}, {"Мертворожденные"}});
        this.data.put(MonsterType.CANRIAN_SPIDER, new String[][]{{"Канрийский"}, {"Канрийское"}, {"Канрийские"}});
        this.data.put(MonsterType.WOLF_LEADER, new String[][]{{"Волчий"}, {"Волчее"}, {"Волчие"}});
        this.data.put(MonsterType.FIRE_ELEMENTAL, new String[][]{{"Элементальный"}, {"Элементальное"}, {"Элементальные"}});
        this.data.put(MonsterType.CANRIAN_ENDERMITE, new String[][]{{"Канрийский"}, {"Канрийское"}, {"Канрийские"}});
        this.data.put(MonsterType.ANDOR, new String[][]{{"Крепкий"}, {"Крепкое"}, {"Крепкие"}});
        this.data.put(MonsterType.MOON_SPIDER, new String[][]{{"Лунный"}, {"Лунное"}, {"Лунные"}});
        this.data.put(MonsterType.SHADOW_OCELOT, new String[][]{{"Пятнистый"}, {"Пятнистое"}, {"Пятнистые"}});
        this.data.put(MonsterType.DREAM_WALKER_SPIDER, new String[][]{{"Звездный"}, {"Звездное"}, {"Звездные"}});
        this.data.put(MonsterType.ALP, new String[][]{{"Кровавый"}, {"Кровавое"}, {"Кровавые"}});
        this.data.put(MonsterType.SKARIGA, new String[][]{{"Устрашающий"}, {"Устрашающее"}, {"Устрашающие"}});
        this.data.put(MonsterType.IRON_SKELETON, new String[][]{{"Усиленный"}, {"Усиленное"}, {"Усиленные"}});
        this.data.put(MonsterType.INFECTED_MLENIA_RESIDENT, new String[][]{{"Искусственный"}, {"Искусственное"}, {"Искусственные"}});
        this.data.put(MonsterType.DISCIPLE_OF_LICH, new String[][]{{"Огненный"}, {"Огненное"}, {"Огненные"}});
        this.data.put(MonsterType.CHAOS_SKELETON, new String[][]{{"Системный"}, {"Системное"}, {"Системные"}});
        this.data.put(MonsterType.ELEMENTAL_SKELETON, new String[][]{{"Стихийный"}, {"Стихийное"}, {"Стихийные"}});
        this.data.put(MonsterType.LICH, new String[][]{{"Прогнивший"}, {"Прогнившее"}, {"Прогнившие"}});
        this.data.put(MonsterType.ABYSS_SKELETON, new String[][]{{"Покинутый"}, {"Покинутое"}, {"Покинутые"}});
        this.data.put(MonsterType.FOLLOWER_OF_BINA, new String[][]{{"Далекий"}, {"Далекое"}, {"Далекие"}});
        this.data.put(MonsterType.CHARMED_GOLEM, new String[][]{{"Заколдованный"}, {"Заколдованное"}, {"Заколдованные"}});
        this.data.put(MonsterType.ELEMENTAL_GOLEM, new String[][]{{"Стойкий"}, {"Стойкое"}, {"Стойкие"}});
        this.data.put(MonsterType.CORRUPTED_PIG, new String[][]{{"Оскверненный"}, {"Оскверненное"}, {"Оскверненные"}});
        this.data.put(MonsterType.DARK_ENDERMIT, new String[][]{{"Темный"}, {"Темное"}, {"Темные"}});
        this.data.put(MonsterType.DISTORTED_VEX, new String[][]{{"Искаженный"}, {"Искаженное"}, {"Искаженные"}});
        this.data.put(MonsterType.DECAYED_SHELL, new String[][]{{"Сгнивший"}, {"Сгнившее"}, {"Сгнившие"}});
        this.data.put(MonsterType.DISTORT_OF_MORTA, new String[][]{{"Старинный"}, {"Старинное"}, {"Старинные"}});
        this.data.put(MonsterType.DISTORTED_ILLAGER, new String[][]{{"Потерянный"}, {"Потерянное"}, {"Потерянные"}});
        this.data.put(MonsterType.DISTORTED_SPIDER, new String[][]{{"Истлевший"}, {"Истлевшое"}, {"Истлевшие"}});
        this.data.put(MonsterType.ETERNAL_GUARDIAN, new String[][]{{"Вечный"}, {"Вечное"}, {"Вечные"}});
        this.data.put(MonsterType.ETERNAL_BABY, new String[][]{{"Вечный"}, {"Вечное"}, {"Вечные"}});
        this.data.put(MonsterType.ASHEN_WOLF, new String[][]{{"Пепельный"}, {"Пепельное"}, {"Пепельные"}});
        this.data.put(MonsterType.MORK, new String[][]{{"Пугающий"}, {"Пугающее"}, {"Пугающие"}});
        this.data.put(MonsterType.FREE_SOUL, new String[][]{{"Вольный"}, {"Вольное"}, {"Вольные"}});
        this.data.put(MonsterType.FOLLOWER_OF_FATUM, new String[][]{{"Неотвратимый"}, {"Неотвратимое"}, {"Неотвратимые"}});
        this.data.put(MonsterType.SOUL_OF_BANDIT_WARRIOR, new String[][]{{"Духовный"}, {"Духовное"}, {"Духовные"}});
        this.data.put(MonsterType.SOUL_OF_BANDIT_ARCHER, new String[][]{{"Духовный"}, {"Духовное"}, {"Духовные"}});
        this.data.put(MonsterType.EDGE_WATCHER, new String[][]{{"Созерцаемый"}, {"Созерцаемое"}, {"Созерцаемые"}});
        this.data.put(MonsterType.ABSORBED_BY_EDGE, new String[][]{{"Поглощенный"}, {"Поглощенное"}, {"Поглощенные"}});
        this.data.put(MonsterType.FOLLOWER_OF_KETHER, new String[][]{{"Коронный"}, {"Коронное"}, {"Коронные"}});
        this.data.put(MonsterType.PRIEST_OF_CHESED, new String[][]{{"Милостливый"}, {"Милостливое"}, {"Милостливые"}});
        this.data.put(MonsterType.EDGE_OCELOT, new String[][]{{"Полярный"}, {"Полярное"}, {"Полярные"}});
        this.data.put(MonsterType.ETERNAL_CENTURION, new String[][]{{"Господский"}, {"Господское"}, {"Господские"}});
        this.data.put(MonsterType.FOREST_GUARDIAN, new String[][]{{"Лесной"}, {"Лесное"}, {"Лесные"}});
        this.data.put(MonsterType.FOREST_VILLAGER, new String[][]{{"Лесной"}, {"Лесное"}, {"Лесные"}});
        this.data.put(MonsterType.FOREST_GOLEM, new String[][]{{"Лесной"}, {"Лесное"}, {"Лесные"}});
        this.data.put(MonsterType.EDGE_WARRIOR, new String[][]{{"Заколенный"}, {"Заколенное"}, {"Заколенные"}});
        this.data.put(MonsterType.WANDERER_EDGE, new String[][]{{"Непостоянный"}, {"Непостоянное"}, {"Непостоянные"}});
        this.data.put(MonsterType.MERNIGA, new String[][]{{"Проклятый"}, {"Проклятое"}, {"Проклятые"}});
        this.data.put(MonsterType.SERVANT_OF_MERNIGA, new String[][]{{"Проклятый"}, {"Проклятое"}, {"Проклятые"}});
        this.data.put(MonsterType.DISTORTED_COW, new String[][]{{"Искаженный"}, {"Искаженное"}, {"Искаженные"}});
        this.data.put(MonsterType.SHADOW_OF_CHESED, new String[][]{{"Теневой"}, {"Теневое"}, {"Теневые"}});
        this.data.put(MonsterType.WARRIOR_OF_CHESED, new String[][]{{"Хаосидский"}, {"Хаосидское"}, {"Хаосидские"}});
        this.data.put(MonsterType.CHAOS_ENTITY, new String[][]{{"Хаоситский"}, {"Хаоситское"}, {"Хаоситские"}});
        this.data.put(MonsterType.LEADER_OF_CHAOS, new String[][]{{"Избранный"}, {"Избранное"}, {"Избранные"}});
        this.data.put(MonsterType.DISTORTED_BY_CHAOS, new String[][]{{"Искаженный"}, {"Искаженное"}, {"Искаженные"}});
        this.data.put(MonsterType.EDGE_BEAR, new String[][]{{"Медвежий"}, {"Медвежое"}, {"Медвежие"}});
        this.data.put(MonsterType.MINE_EXILE, new String[][]{{"Изгнанный"}, {"Изгнанное"}, {"Изгнанные"}});
        this.data.put(MonsterType.GAZE_OF_CHESED, new String[][]{{"Хеседский"}, {"Хеседское"}, {"Хеседские"}});
        this.data.put(MonsterType.HAND_OF_CHESED, new String[][]{{"Великодушный"}, {"Великодушное"}, {"Великодушные"}});
        this.data.put(MonsterType.MINER, new String[][]{{"Позолоченный"}, {"Позолоченное"}, {"Позолоченные"}});
        this.data.put(MonsterType.ALIEN_MINER, new String[][]{{"Чуждый"}, {"Чуждое"}, {"Чуждые"}});

        this.data.put(MonsterType.FUGITIVE_SOUL, new String[][]{{"Убегающий"}, {"Убегающее"}, {"Убегающие"}});
        this.data.put(MonsterType.FUGITIVE_SHELL, new String[][]{{"Убегающий"}, {"Убегающее"}, {"Убегающие"}});
        this.data.put(MonsterType.JUNIOR_VIRAN, new String[][]{{"Вираний"}, {"Виранее"}, {"Вирание"}});
        this.data.put(MonsterType.SENIOR_VIRAN, new String[][]{{"Вираний"}, {"Виранее"}, {"Вирание"}});
        this.data.put(MonsterType.STILLBIRTH_SPIDER, new String[][]{{"Паучий"}, {"Паучее"}, {"Паучие"}});
        this.data.put(MonsterType.CANRIAN_FUGITIVE, new String[][]{{"Убегающий"}, {"Убегающее"}, {"Убегающие"}});
        this.data.put(MonsterType.EDGE_WOLF, new String[][]{{"Закаленный"}, {"Закаленное"}, {"Закаленные"}});
        this.data.put(MonsterType.EDGE_ELEMENTALIST, new String[][]{{"Закаленный"}, {"Закаленное"}, {"Закаленные"}});
        this.data.put(MonsterType.CHAOS_INFUSED_ENEDERMITE, new String[][]{{"Насыщенный"}, {"Насыщенное"}, {"Насыщенные"}});
        this.data.put(MonsterType.NEWBORN_BY_CHAOS, new String[][]{{"Насыщенный"}, {"Насыщенное"}, {"Насыщенные"}});
        this.data.put(MonsterType.CHAOS_BLOODHOUND, new String[][]{{"Стремительный"}, {"Стремительное"}, {"Стремительные"}});
        this.data.put(MonsterType.EDGE_PILLAR, new String[][]{{"Небесный"}, {"Небесное"}, {"Небесные"}});
        this.data.put(MonsterType.AWAKENED_BEAR, new String[][]{{"Пробужденный"}, {"Пробужденное"}, {"Пробужденные"}});
        this.data.put(MonsterType.AWAKENED_SKELET, new String[][]{{"Пробужденный"}, {"Пробужденное"}, {"Пробужденные"}});
        this.data.put(MonsterType.CHAOS_MLENIA, new String[][]{{"Контрольный"}, {"Контрольное"}, {"Контрольные"}});
        this.data.put(MonsterType.PRIEST_OF_CHAOS, new String[][]{{"Контрольный"}, {"Контрольное"}, {"Контрольные"}});

        this.data.put(MonsterType.SHELL_SYNERGY_MELLE, new String[][]{{"Сбалансированный"}, {"Сбалансированное"}, {"Сбалансированные"}});
        this.data.put(MonsterType.SHELL_SYNERGY_BOW, new String[][]{{"Сбалансированный"}, {"Сбалансированное"}, {"Сбалансированные"}});
        this.data.put(MonsterType.WARRIOR_SYNERGY, new String[][]{{"Сбалансированный"}, {"Сбалансированное"}, {"Сбалансированные"}});

        this.data.put(MonsterType.BATTLE_GROUND_WITCH, new String[][]{{"Изношенный"}, {"Изношенное"}, {"Изношенные"}});
        this.data.put(MonsterType.BATTLE_GROUND_HUSK, new String[][]{{"Изношенный"}, {"Изношенное"}, {"Изношенные"}});
        this.data.put(MonsterType.BATTLE_GROUND_ENDERMAN, new String[][]{{"Изношенный"}, {"Изношенное"}, {"Изношенные"}});
        this.data.put(MonsterType.BATTLE_GROUND_IRON_GOLEM, new String[][]{{"Изношенный"}, {"Изношенное"}, {"Изношенные"}});
        this.data.put(MonsterType.BATTLE_GROUND_MAGMA_CUBE, new String[][]{{"Расплавленный"}, {"Расплавленное"}, {"Расплавленные"}});

        this.data.put(MonsterType.PROJECTION_OF_THE_YOUNG_INDIVIDUAL_BINA, new String[][]{{"Разумный"}, {"Разумное"}, {"Разумные"}});
        this.data.put(MonsterType.PROJECTION_OF_THE_YOUNG_INDIVIDUAL_HOCHMA, new String[][]{{"Мудрый"}, {"Мудрое"}, {"Мудрые"}});

        this.data.put(MonsterType.MASK_ENTITY, new String[][]{{"Путеводный"}, {"Путеводное"}, {"Путеводные"}});

        this.data.put(MonsterType.DISTORTED_MINER, new String[][]{{"Шахтерский"}, {"Шахтерское"}, {"Шахтерские"}});
        this.data.put(MonsterType.MISGUIDED_MINER, new String[][]{{"Заблудший"}, {"Заблудшее"}, {"Заблудшие"}});
        return this;
    }

    public String generate(@NotNull final MonsterType monsterType, final DisplayNameType type) {
        val allNames = data.get(monsterType);
        if (allNames == null) return "Unknown";

        val names = allNames[type.getType()];

        return names[MathUtility.randomInRange(0, names.length - 1)];
    }

    public String generate(final DisplayNameType type) {
        val allNames = new ArrayList<>(data.values());
        if (allNames.isEmpty()) return "Unknown";

        val names = allNames.get(MathUtility.randomInRange(0, allNames.size() - 1))[type.getType()];

        return names[MathUtility.randomInRange(0, names.length - 1)];
    }

    @Getter
    @RequiredArgsConstructor
    public enum DisplayNameType {

        ONE(1), NEUTRAL(2), MANY(3);

        private final int type;

        public int getType() {
            return type - 1;
        }
    }

}
