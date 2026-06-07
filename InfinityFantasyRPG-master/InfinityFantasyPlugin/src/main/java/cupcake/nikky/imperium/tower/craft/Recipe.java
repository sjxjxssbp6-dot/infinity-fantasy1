package cupcake.nikky.imperium.tower.craft;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.item.ItemObject;
import cupcake.nikky.imperium.tower.utility.NbtUtility;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import me.anfanik.steda.api.utility.InventoryUtility;
import me.anfanik.steda.api.utility.Pair;
import me.anfanik.steda.api.utility.TextUtility;
import me.func.mod.Anime;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class Recipe {

    /*
        key - imperium item id
        value - amount to craft
     */
    private final Map<ItemObject, Integer> requirement;
    @Getter private final ItemObject result;
    @Getter private final int amountResult;
    @Getter private final int levelRequire;

    @SafeVarargs
    public final Recipe appendRequirement(@NotNull final Pair<ItemObject, Integer>... args) {
        for (Pair<ItemObject, Integer> arg : args) {
            this.requirement.put(arg.getFirst(), arg.getSecond());
        }

        return this;
    }

    public int getAmount(@NotNull final ItemObject id) {
        return this.requirement.get(id);
    }

    public List<ItemObject> getAllRequirements() {
        return new ArrayList<>(this.requirement.keySet());
    }

    public List<ItemObject> findPlayerMaterials(@NotNull final Player player) {
        final Map<ItemObject, Integer> result = new HashMap<>();
        this.requirement.keySet().forEach(id -> result.put(id, requirement.get(id)));

        for (ItemStack storageContent : player.getInventory().getStorageContents()) {
            val imperiumItem = Fantasy.itemProvider().find(storageContent);
            if (imperiumItem == null) continue;

            val id = imperiumItem.getObject();

            if (this.requirement.containsKey(id)) {
                result.put(id, result.get(id) - storageContent.getAmount());
            }
        }

        return result.keySet().stream()
                .filter(id -> result.get(id) > 0)
                .collect(Collectors.toList());
    }

    public boolean removeRequirementsFrom(@NotNull final Player player) {
        for (ItemObject requirement : getAllRequirements()) {
            val amount = getAmount(requirement);
            if (!InventoryUtility.removeItem(player.getInventory(), NbtUtility.clearSessionTags(requirement.getItem().createNew()), amount)) {
                return false; // todo in future improve
            }
        }

        return true;
    }

    public CraftResult canCraft(@NotNull final Player player) {
        val user = Fantasy.userService().findById(player.getUniqueId()).orElse(null);
        if (user == null || user.getLevel() < getLevelRequire()) return CraftResult.INSUFFICIENT_LEVEL;

        if (findPlayerMaterials(player).size() > 0) return CraftResult.NOT_ENOUGH_MATERIALS;

        return InventoryUtility.canMoveToOtherInventory(player.getInventory()) ? CraftResult.OK : CraftResult.FULL_INVENTORY;
    }

    public void giveResult(@NotNull final Player player) {
        val imperiumItem = result.getItem().create();
        if (imperiumItem == null) return;

        val item = imperiumItem.createNew(amountResult);
        player.getInventory().addItem(item);

        Fantasy.userService().findById(player.getUniqueId()).ifPresent(user ->
                Fantasy.clientProvider().cursorNotify(user, TextUtility.colorize(imperiumItem.getName() + " &fX " + amountResult), ""));
    }

    public int size() {
        return this.requirement.size();
    }

}
