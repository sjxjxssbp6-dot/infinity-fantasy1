package cupcake.nikky.imperium.tower.protocol.fraction;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.anfanik.steda.api.utility.TextUtility;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.Nullable;

@Getter
@RequiredArgsConstructor
public enum FractionType {

    ORDER("Порядок", "\uE19D", ChatColor.RESET),
    CHAOS("Хаос", "\uE19F",ChatColor.RESET),
    NONE("Нейтральный", "", ChatColor.RESET);

    private final String title;
    private final String nameChar;
    private final ChatColor nameColor;

    public String getColoredName() {
        return TextUtility.colorize("&7[" + nameColor + nameChar + "&7]");
    }

    public static FractionType findByName(@Nullable final String name) {
        if (name == null || name.isEmpty()) return NONE;

        for (FractionType value : values()) {
            if (value.name().equalsIgnoreCase(name)) return value;
        }

        return NONE;
    }

}
