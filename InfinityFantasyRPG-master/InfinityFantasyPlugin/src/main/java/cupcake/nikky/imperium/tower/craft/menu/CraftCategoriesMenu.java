package cupcake.nikky.imperium.tower.craft.menu;

import cupcake.nikky.imperium.tower.Fantasy;
import lombok.Getter;
import lombok.val;
import me.anfanik.steda.api.menu.Menu;
import me.anfanik.steda.api.menu.MenuSession;
import me.anfanik.steda.api.menu.content.exact.StaticContentProvider;
import me.anfanik.steda.api.menu.item.MenuItem;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CraftCategoriesMenu {

    @Getter
    private static final CraftCategoriesMenu instance = new CraftCategoriesMenu();
    private final Menu<MenuSession> menu;

    public CraftCategoriesMenu() {
        val builder = Menu.builder()
                .chestRows(1)
                .title(session -> "Категории предметов");
        val provider = new StaticContentProvider();

        for (int i = 0; i < Fantasy.recipeProvider().getCategories().size(); i++) {
            val category = Fantasy.recipeProvider().getCategories().get(i);
            provider.setItem(i, MenuItem.createStatic(
                    Fantasy.newItemBuilder(Fantasy.recipeProvider().findIcon(category))
                            .setName("&a" + category)
                            .appendLore("", "&7Нажмите, чтобы перейти в данную категорию преобразования предметов")
                            .build(),
                    (session, menuClick) -> CraftCategoryItemsMenu.getInstance().open(session.getPlayer(), category)));
        }

        this.menu = builder.contentProvider(provider).build();
    }

    public void open(@NotNull final Player player) {
        this.menu.open(player);
    }

}
