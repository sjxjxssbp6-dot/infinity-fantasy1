package cupcake.nikky.imperium.tower.utility;

import dev.xdark.clientapi.event.render.ItemTooltipRender;
import dev.xdark.clientapi.item.ItemStack;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;
import ru.cristalix.enginex.Enginex;
import ru.cristalix.enginex.util.Color;
import ru.cristalix.enginex.util.Palette;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemUtility {

    public static Color findColorByItemRarity(@NotNull final String name) {
        if (name.length() < 4) return Palette.WHITE.getColor();
        switch (name.substring(2, 4).replace("&", "§")) {
            case "§a": return Palette.GREEN.getColor();
            case "§9": return Palette.BLUE.getColor();
            case "§5": return Palette.PURPLE.getColor();
            case "§6": return Palette.YELLOW.getColor();
            case "§b": return Palette.CYAN.getColor();
            default: return Palette.WHITE.getColor();
        }
    }

    public static Color findColorByItemRarity(@NotNull final ItemStack itemStack) {
        return findColorByItemRarity(itemStack.getDisplayName());
    }

    public static List<String> findAdvancedLore(@NotNull final ItemStack itemStack) {
        val tagCompound = itemStack.getTagCompound();
        if (tagCompound == null || tagCompound.isEmpty()) return Collections.emptyList();

        val advancedLore = tagCompound.getString("advancedLore");
        if (advancedLore == null || advancedLore.isEmpty()) return Collections.emptyList();

        return Stream.of(advancedLore.split("\n")).collect(Collectors.toList());
    }

    public static List<String> transformComparedLore(@NotNull final List<String> content, @NotNull final ItemStack itemStack) {
        val isAlt = Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU);
        val advancedContent = ItemUtility.findAdvancedLore(itemStack);
        val isEmpty = advancedContent.isEmpty();

        if (isAlt && !isEmpty) {
            return advancedContent;
        }
        return content;
    }

    public static int handleItemTooltipEvent(@NotNull final ItemTooltipRender itemTooltipRender) {
        val content = itemTooltipRender.getContent();
        val isAlt = Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU);
        val advancedContent = ItemUtility.findAdvancedLore(itemTooltipRender.getStack());
        val isEmpty = advancedContent.isEmpty();

        int additionalContent = 0;

        if (isAlt && !isEmpty) {
            advancedContent.add(0, itemTooltipRender.getStack().getDisplayName());
            val isCompared = content.get(content.size() - 1).startsWith("§8");
            if (isCompared) {
                advancedContent.add("§8CTRL сравнить снаряжение");
                additionalContent += 1;
            }

            itemTooltipRender.getContent().clear();
            itemTooltipRender.getContent().addAll(advancedContent);
        } else {
            if (!isEmpty) {
                content.add("§8ALT расширенное описание");
                additionalContent += 1;
            }
        }

        return additionalContent;
    }

}
