package cupcake.nikky.imperium.tower.client.protocol;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GuidePage {

    LEVEL_AND_CORPSE("Уровень и пасхалки", "Уровень и\nпасхалки"),
    MOUNT("Спутники", "Спутники"),
    ATTRIBUTE("Атрибуты", "Атрибуты"),
    TREE("Древо искажений", "Древо искажений"),
    TELEPORTS("Телепорты", "Телепорты"),
    ITEMS_AND_GEAR("Предметы и снаряжение", "Предметы\nи снаряжение"),
    CORRUPT("Осквернение", "Осквернение"),
    CRAFT("Крафт", "Базовый крафт"),
    MONSTERS("Монстры и бестиарий", "Монстры\nи бестиарий"),
    AILMENTS("Состояния", "Состояния"),
    MINING_AND_TRADERS("Добыча и торговцы", "Добыча \nи торговцы"),
    ITEM_FILTER("Фильтр предметов", "Фильтр предметов"),
    SPECIAL_SHOP("Специальный магазин и ауры", "Специальный\nмагазин"),
    EDGE_CORES("Ядра Грани", "Ядра Грани"),
    MASK_EDGE("Слепки Грани", "Слепки Грани"),
    PANDORA_CHEST("Ящик Пандоры", "Ящик Пандоры"),
    TRADE("Обмен с игроками", "Обмен\nс игроками"),
    ASPECTS_AND_MANA("Аспекты и мана", "Аспекты\nи мана"),
    FIRMOLOG("Фирмолог", "Фирмолог"),
    BOSSES("Монстры из осколков прорыва", "Монстры из\nосколков прорыва");

    private final String title;
    private final String miniTitle;

}
