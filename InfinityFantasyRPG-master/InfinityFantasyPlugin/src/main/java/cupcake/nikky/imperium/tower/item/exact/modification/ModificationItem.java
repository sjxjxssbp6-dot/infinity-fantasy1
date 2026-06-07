package cupcake.nikky.imperium.tower.item.exact.modification;

import cupcake.nikky.imperium.tower.item.*;
import cupcake.nikky.imperium.tower.item.watcher.ItemWatcher;
import lombok.Getter;
import lombok.val;
import org.jetbrains.annotations.NotNull;

@Getter
public class ModificationItem extends DefaultImperiumItem {

    private final Modification modification;
    private final String alternativeName;

    public ModificationItem(@NotNull final Modification modification) {
        this(modification, "");
    }

    public ModificationItem(@NotNull final Modification modification, @NotNull final String alternativeName) {
        super(ItemRare.NONE, ItemType.MATERIAL, modification.material(), (short) modification.data(), modification.title(), modification.description());
        this.modification = modification;
        this.alternativeName = alternativeName;

        getGeneralSettings().color(modification.color());
        modification.modificationDrop().getInitHandler().accept(this);
        getGeneralSettings().npcTradePrice(getLootSettings().dropEntityLevel() * 10);

        getItemGenerator().setUnique(modification.unique());

        getItemWatcherRegistry().registerWatcher(ItemWatcher.DROPPABLE);
    }

    @Override
    public ItemObject getObject() {
        return alternativeName.isBlank() ? ItemObject.valueOf(this.convertName() + "_MOD_ITEM") : ItemObject.valueOf(alternativeName + "_MOD_ITEM");
    }

    private String convertName() {
        val builder = new StringBuilder();

        for (char c : modification.getClass().getSimpleName().toCharArray()) {
            if (!builder.isEmpty() && Character.isUpperCase(c)) {
                builder.append("_");
            }
            builder.append(Character.toUpperCase(c));
        }

        return builder.toString();
    }

    @Override
    public ImperiumItem create() {
        return new ModificationItem(modification, alternativeName);
    }

}
