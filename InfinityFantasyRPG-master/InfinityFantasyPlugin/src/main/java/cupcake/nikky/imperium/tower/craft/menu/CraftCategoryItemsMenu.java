package cupcake.nikky.imperium.tower.craft.menu;

import cupcake.nikky.imperium.tower.Fantasy;
import lombok.Getter;
import lombok.val;
import me.anfanik.steda.api.menu.Menu;
import me.anfanik.steda.api.menu.MenuSession;
import me.anfanik.steda.api.menu.content.exact.StaticContentProvider;
import me.anfanik.steda.api.menu.item.MenuItem;
import me.anfanik.steda.api.utility.InventoryUtility;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CraftCategoryItemsMenu {

    @Getter
    private static final CraftCategoryItemsMenu instance = new CraftCategoryItemsMenu();

    private final Map<String, Menu<MenuSession>> menus = new HashMap<>();

    public void open(@NotNull final Player player, @NotNull final String category) {
        if (this.menus.get(category) == null)
            this.createCategoryItems(category);

        this.menus.get(category).open(player);
    }

    private void createCategoryItems(@NotNull final String category) {
        val recipes = Fantasy.recipeProvider().findRecipes(category);
        recipes.ifPresent(data -> {
            val menuBuilder = Menu.builder()
                    .lockUserInventory()
                    .chestRows(InventoryUtility.getNearInventorySize(recipes.get().size()) / 9)
                    .title("Список преобразования предметов");
            val provider = new StaticContentProvider();

            for (int i = 0; i < data.size(); i++) {
                val index = i;
                val recipe = data.get(i);

                val item = recipe.getResult().getItem().create();
                if (item == null) continue;

                val itemStack = item.createNew(1);

                ItemStack result = Fantasy.newItemBuilder(itemStack)
                        .appendLore("", "&7Нажмите, чтобы перейти в меню преобразования")
                        .build();

                if (recipe.getLevelRequire() > 0) {
                    result = Fantasy.newItemBuilder(itemStack)
                            .appendLore("", "Необходимый уровень для преобразования: " + recipe.getLevelRequire(), "&7Нажмите, чтобы перейти в меню преобразования")
                            .build();
                }

                provider.setItem(i, MenuItem.createStatic(result, (session, menuClick) -> CraftItemMenu.getInstance().open(session.getPlayer(), category, index)));
            }

            menus.put(category, menuBuilder.contentProvider(provider).build());
        });
    }

}
