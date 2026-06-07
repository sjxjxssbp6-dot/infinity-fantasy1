package cupcake.nikky.imperium.tower.item;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

@Getter
@RequiredArgsConstructor
public enum ItemRare {

    NONE(0, ChatColor.WHITE, "", 0),
    COMMON(1, ChatColor.GREEN, "Обычное", 0),
    RARE(2, ChatColor.BLUE, "Редкое", 1),
    EPIC(3, ChatColor.DARK_PURPLE, "Эпическое", 3),
    EXALTED(4, ChatColor.AQUA, "Возвышенное", 3),
    UNIQUE(5, ChatColor.GOLD, "Уникальное", 3);

    private final int position;
    private final ChatColor color;
    private final String name;
    private final int modifierSize;

    public String getColoredName() {
        return this.color + this.name;
    }

    public boolean highestThanOrEqual(@NotNull final ItemRare itemRare) {
        return getPosition() >= itemRare.getPosition();
    }

    public boolean highestThan(@NotNull final ItemRare itemRare) {
        return getPosition() > itemRare.getPosition();
    }

    public boolean lowestThan(@NotNull final ItemRare itemRare) {
        return getPosition() < itemRare.getPosition();
    }

    public boolean lowestThanOrEqual(@NotNull final ItemRare itemRare) {
        return getPosition() <= itemRare.getPosition();
    }

    public static ItemRare findByPosition(final int position) {
        for (ItemRare value : values()) {
            if (value.getPosition() == position) return value;
        }

        return null;
    }

}
