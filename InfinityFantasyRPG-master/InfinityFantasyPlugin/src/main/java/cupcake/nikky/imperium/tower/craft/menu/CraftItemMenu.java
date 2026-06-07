package cupcake.nikky.imperium.tower.craft.menu;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.chat.ChatType;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.craft.CraftResult;
import cupcake.nikky.imperium.tower.craft.Recipe;
import cupcake.nikky.imperium.tower.item.ItemObject;
import cupcake.nikky.imperium.tower.sound.AdvancedSounds;
import lombok.Getter;
import lombok.val;
import me.anfanik.steda.api.menu.Menu;
import me.anfanik.steda.api.menu.MenuSession;
import me.anfanik.steda.api.menu.content.exact.StaticContentProvider;
import me.anfanik.steda.api.menu.item.MenuItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CraftItemMenu {

    @Getter private static final CraftItemMenu instance = new CraftItemMenu();

    private final Map<Recipe, Menu<MenuSession>> menus = new HashMap<>();
    private final int[] requirementSlots = {0, 1, 2, 3, 4, 5, 9, 10, 11, 12, 13, 14};

    public void open(@NotNull final Player player, @NotNull final String category, final int index) {
        Fantasy.recipeProvider().findRecipe(category, index).ifPresent(recipe -> {
            if (menus.get(recipe) == null)
                this.createRecipeMenu(recipe);

            this.menus.get(recipe).open(player);
        });
    }

    private void createRecipeMenu(@NotNull final Recipe recipe) {
        if (recipe.size() > requirementSlots.length) return;

        val extended = recipe.size() > 6;

        val resultItem = recipe.getResult().getItem().create();
        if (resultItem == null) return;

        val menuBuilder = Menu.builder()
                .lockUserInventory()
                .size(extended ? 18 : 9)
                .title("Преобразование предметов в " + resultItem.getName());
        val provider = new StaticContentProvider();

        val requirements = recipe.getAllRequirements();

        for (int i = 0; i < recipe.getAllRequirements().size(); i++) {
            val item = requirements.get(i).getItem().create();
            if (item == null) return;

            provider.setItem(requirementSlots[i], MenuItem.createStatic(item.createNew(recipe.getAmount(item.getObject()))));
        }

        provider.setItem(extended ? 8 : 7, MenuItem.createDynamic(session -> {
            val lore = new ArrayList<String>();
            lore.add(" ");

            val materials = recipe.findPlayerMaterials(session.getPlayer());

            if (materials.size() > 0) {
                lore.add("&eНедостаточно материалов: ");

                for (ItemObject id : materials) {
                    val item = id.getItem().create();
                    if (item == null) continue;

                    lore.add("&c&r" + item.getRare().getColor() + item.getName());
                }
            } else {
                lore.add("&7Нажмите, чтобы создать предмет");
            }

            return Fantasy.newItemBuilder(resultItem.createNew(recipe.getAmountResult()))
                    .setLore(lore)
                    .build();
        }, (session, menuClick) -> {
            val player = session.getPlayer();
            val result = recipe.canCraft(player);

            if (result == CraftResult.OK && recipe.removeRequirementsFrom(player)) {
                recipe.giveResult(player);
                Fantasy.bukkitProvider().sound(player, AdvancedSounds.TAP);
            } else if (result == CraftResult.FULL_INVENTORY) {
                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&cУ Вас полный инвентарь. Освободите место, чтобы создать предмет.");
            } else if (result == CraftResult.INSUFFICIENT_LEVEL) {
                Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "У вас слишком маленький уровень", "для данного преобразования");
            }

            if (result == CraftResult.FULL_INVENTORY) {
                player.closeInventory();
            }

            if (result != CraftResult.OK) {
                Fantasy.bukkitProvider().sound(player, AdvancedSounds.CONFUSION);
            }
        }));

        provider.setItem(extended ? 17 : 8, MenuItem.createStatic(
                Fantasy.newItemBuilder(Material.STAINED_GLASS_PANE)
                        .setDurability(14)
                        .setName("&cВернуться")
                        .build(),
                (session, menuClick) -> CraftCategoriesMenu.getInstance().open(session.getPlayer())));

        this.menus.put(recipe, menuBuilder.contentProvider(provider).build());
    }

}
