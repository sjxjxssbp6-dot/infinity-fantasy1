package cupcake.nikky.imperium.tower.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;

@AllArgsConstructor
@RequiredArgsConstructor
public enum ItemType {

    FOOD("Еда"),
    POTION("Зелье"),
    POTION_EVENT("Зелье"),
    TOOL("Инструмент"),
    ASPECT("Аспект", ChatColor.DARK_GREEN.toString()),
    WEAPON("Оружие"),
    ARMOR("Экипировка"),
    QUESTION("Для задания"),
    MATERIAL("Материал"),
    RUNE("Руна"),
    XENO_MATERIAL("Ксеноматериал"),
    GENE_MODIFICATION("Генетическая модификация"),
    XENO_GENE_MODIFICATION("Ксено-генетическая модификация"),
    SHARD("Предмет грани"),
    SHARD_BREAKTHROUGH("Осколок прорыва"),
    INCUBATOR("Инкубатор"),
    CATALYST("Катализатор"),
    EDGE_FRAGMENT("Осколок Грани"),
    DIVINATION_CARD("Гадальная карта"),
    CURRENCY("Валюта"),
    EMPTY("Неизвестно");

    @Getter
    private final String title;
    @Getter
    private String typeColor = "";

}
