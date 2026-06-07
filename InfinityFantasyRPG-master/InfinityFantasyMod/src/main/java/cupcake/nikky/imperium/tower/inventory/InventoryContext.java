package cupcake.nikky.imperium.tower.inventory;

import cupcake.nikky.imperium.tower.button.FantasyButton;
import cupcake.nikky.imperium.tower.cooldown.CooldownService;
import cupcake.nikky.imperium.tower.item.FantasyRenderItem;
import dev.xdark.clientapi.item.Item;
import dev.xdark.clientapi.item.ItemStack;
import dev.xdark.clientapi.item.Items;
import lombok.Getter;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.cristalix.enginex.Enginex;
import ru.cristalix.enginex.element.CarvedRectangle;
import ru.cristalix.enginex.element.GridLayout;
import ru.cristalix.enginex.util.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class InventoryContext extends CarvedRectangle {

    private final GridLayout generalLayout = new GridLayout(LayoutPriority.HORIZONTAL, 0, 9, 7.5);
    private final Map<Integer, FantasyButton> items = new HashMap<>();

    @Getter private final Map<Integer, Integer> cachedFlags = new HashMap<>();

    private final BiConsumer<Integer, ItemStack> onItemClick;

    public InventoryContext(@Nullable final BiConsumer<Integer, ItemStack> onItemClick) {
        super(6D);
        this.onItemClick = onItemClick;
        this.initialize();
    }

    public InventoryContext() {
        this(null);
    }

    public void initialize() {
        this.setSize(new V3(492D + 32D, 214.5D + 32D)).setColor(Palette.GREY_DARK.alpha(.62)).setOrigin(Relative.CENTER).setAlign(Relative.CENTER);
        generalLayout.setOrigin(Relative.CENTER).setAlign(Relative.CENTER);
        this.addChild(generalLayout);
    }

    public void resetColors() {
        items.keySet().forEach(index -> this.updateItemColor(index, 0));
    }

    public void clearInventory() {
        generalLayout.clearChilds();
    }

    public void clearItems() {
        for (int i = 0; i < 36; i++) {
            this.setItem(i, ItemStack.of(Items.AIR, 1, 0));
        }
    }

    public void setItem(final int slot, @NotNull final ItemStack itemStack) {
        val component = this.createItemComponent(slot, itemStack);
        val oldItem = items.get(slot);
        if (oldItem != null) this.generalLayout.removeChild(oldItem);
        this.generalLayout.addChild(slot, oldItem);
        items.put(slot, component);
    }

    public void fillItems() {
        this.fillItems(false);
    }

    public void fillItems(final boolean empty) {
        generalLayout.clearChilds();

        val inventory = Enginex.getPlayer().getInventory();
        val air = Item.of(Items.AIR.getId()).newStack(1, 0);

        for (int i = 9; i < 36; i++) {
            val component = this.createItemComponent(i, empty ? air : inventory.getStackInSlot(i));
            items.put(i, component);
            generalLayout.addChild(component);
        }

        for (int i = 0; i < 9; i++) {
            val component = this.createItemComponent(i, empty ? air : inventory.getStackInSlot(i));
            items.put(i, component);
            generalLayout.addChild(component);
        }
    }

    public void fillItems(@NotNull final List<ItemStack> itemStacks) {
        generalLayout.clearChilds();

        val air = ItemStack.of(Items.AIR, 1, 0);
        val size = itemStacks.size();

        for (int i = 0; i < 36; i++) {
            val itemStack = i > size - 1 ? air : itemStacks.get(i);
            val component = this.createItemComponent(i, itemStack);
            items.put(i, component);
            generalLayout.addChild(component);
        }
    }

    public void updateItemColor(final int index, final int flag) {
        if (flag == 1) {
            this.items.get(index).setButtonColor(ButtonColor.GREEN);
            cachedFlags.put(index, flag);
        } else {
            this.items.get(index).setButtonColor(ButtonColor.GREY);
            cachedFlags.remove(index);
        }
    }

    private FantasyButton createItemComponent(final int index, @NotNull final ItemStack itemStack) {
        val button = new FantasyButton(itemStack);
        val item = itemStack.getItem();
        val itemComponent = new FantasyRenderItem(itemStack, true, false);

        if (onItemClick != null && item != Items.AIR && item != null) {
            button.onLeftClick(element -> {
                if (!CooldownService.getInstance().getAndExtendCooldown("inventory-context-click", 0.1)) return;
                onItemClick.accept(index, itemStack);
            });
        } else if (item != Items.AIR && item != null) {
            button.setInteractive(true);
        } else {
            button.setInteractive(false);
        }

        button.setButtonColor(cachedFlags.getOrDefault(index, 0) == 1 ? ButtonColor.GREEN : ButtonColor.GREY);

        return (FantasyButton) button.setCarveSize(4D).setSize(new V3(48, 48))
                .addChild(itemComponent.setSize(new V3(32, 32)).setOrigin(Relative.CENTER).setAlign(Relative.CENTER));
    }

}
