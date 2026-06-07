package cupcake.nikky.imperium.tower.item.exact.aspect;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.item.*;
import cupcake.nikky.imperium.tower.item.settings.exact.cursor.CursorContainer;
import cupcake.nikky.imperium.tower.item.settings.exact.cursor.CursorResultType;
import cupcake.nikky.imperium.tower.item.watcher.ItemWatcher;
import lombok.Getter;
import lombok.val;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

@Getter
public class AspectItem extends DefaultImperiumItem {

    private final AspectBase aspect;
    private final BiConsumer<CursorContainer, ImperiumItem> clickHandler;

    public AspectItem(@NotNull final AspectBase aspect) {
        super(ItemRare.NONE, ItemType.ASPECT, Material.STICK, aspect.data(), aspect.title(), Collections.emptyList());
        this.aspect = aspect;

        this.getGeneralSettings().npcTradePrice(100 * (getGearSettings().corrupted() + 1));

        this.getLootSettings().lootGeneratorSettings().potential(true);
        getDescriptionSettings().showItemLevel(true).showItemType(true).itemLevelPattern("&fУровень аспекта: %s%d");

        this.getItemWatcherRegistry().registerWatcher(ItemWatcher.LEVEL);
        this.getItemWatcherRegistry().registerWatcher(ItemWatcher.CORRUPTED);
        this.getItemWatcherRegistry().registerWatcher(ItemWatcher.COPY_STATEMENT);
        this.getItemWatcherRegistry().registerWatcher(ItemWatcher.DROPPABLE);
        this.getItemWatcherRegistry().registerWatcher(ItemWatcher.GEAR_POTENTIAL);
        this.getItemWatcherRegistry().registerWatcher(ItemWatcher.ASPECT_EXALTED_VALUE);

        this.clickHandler = (container, item) -> {
            if (!item.getRare().highestThanOrEqual(ItemRare.EPIC)) {
                Fantasy.clientProvider().notification(container.player(), SystemNotificationType.ERROR, "Необходимая редкость предмета: " + ItemRare.EPIC.getColoredName());
                container.cursorResultType(CursorResultType.CANCEL);
                return;
            }

            if (!aspect.type().getItemTypes().contains(item.getImperiumType())) {
                Fantasy.clientProvider().notification(container.player(), SystemNotificationType.ERROR, "Данный аспект нельзя применить", "на данном предмете");
                container.cursorResultType(CursorResultType.CANCEL);
                return;
            }

            if (container.cursorItemStack().getAmount() > 1) {
                Fantasy.clientProvider().notification(container.player(), SystemNotificationType.ERROR, "Применять можно только", "один аспект за раз");
                container.cursorResultType(CursorResultType.CANCEL);
                return;
            }

            item.getAspectSettings().aspect(aspect);
            item.getAspectSettings().aspectCorrupt(getGearSettings().corrupted());
            item.getAspectSettings().aspectCopy(getGearSettings().copy());
            item.getAspectSettings().exaltedValue(getAspectSettings().exaltedValue());
            container.cursorResultType(CursorResultType.REMOVE_ONE_ITEM);
        };
    }

    @Override
    public String getName() {
        return getAspectSettings().exaltedValue() == 0 ? aspect.title() : "Возвышенный " + aspect.title();
    }

    @Override
    public List<String> getAdditionalDescription() {
        final List<String> result = new ArrayList<>();
        val level = this.getGearSettings().corrupted() + this.getAspectSettings().exaltedValue();

        result.add("&fКатегория: &6" + aspect.category().getCategoryTitle());
        result.add("&fЦель применения: &6" + aspect.type().getTypeTitle());
        result.add("&fСтоимость: " + aspect.costData().getCostProcessType().getDescriptionProcess().apply(aspect.costData().getCostValue().apply(level)));
        result.add("  ");
        result.add("&7Добавляет аспект на снаряжение,");
        result.add("&7редкость которого %sэпическое &7или выше".formatted(ItemRare.EPIC.getColor()));
        result.add(" ");
        result.addAll(aspect.description().apply(level).stream().map(s -> "&f" + s).toList());

        return result;
    }

    @Override
    public ItemObject getObject() {
        return ItemObject.valueOf(this.convertName());
    }

    private String convertName() {
        val builder = new StringBuilder();

        for (char c : aspect.getClass().getSimpleName().toCharArray()) {
            if (!builder.isEmpty() && Character.isUpperCase(c)) {
                builder.append("_");
            }
            builder.append(Character.toUpperCase(c));
        }

        return builder.toString();
    }

    @Override
    public ImperiumItem create() {
        return new AspectItem(aspect);
    }

}
