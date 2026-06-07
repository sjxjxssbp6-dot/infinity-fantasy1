package cupcake.nikky.imperium.tower.item.generator;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.ItemRare;
import cupcake.nikky.imperium.tower.item.ItemType;
import cupcake.nikky.imperium.tower.item.exact.gear.tool.FishingRodItem;
import cupcake.nikky.imperium.tower.item.exact.gear.tool.PickaxeItem;
import cupcake.nikky.imperium.tower.item.exact.gear.tool.ToolItem;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.utility.NbtUtility;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.val;
import me.anfanik.steda.api.utility.TextUtility;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@RequiredArgsConstructor
public class ItemGenerator {

    @Getter
    protected final ImperiumItem item;

    @Setter @Getter
    private boolean isUnique = false;

    public ItemStack createItemStack(final int amount, Player player) {
        getItem().updateAttributes();

        final User user = player == null ? null : Fantasy.userService().findById(player.getUniqueId()).orElse(null);
        val session = user == null ? null : user.getSession();

        String title = item.getRare().getColor() + item.getImperiumType().getTypeColor() + item.getName();
        if (item.getGearSettings().corrupted() > 0)
            title += " &c+" + item.getGearSettings().corrupted();

        val itemBuilder = Fantasy.newItemBuilder(item.getMaterial())
                .setName(title)
                .setAmount(amount)
                .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .addItemFlag(ItemFlag.HIDE_ENCHANTS)
                .addItemFlag(ItemFlag.HIDE_DESTROYS)
                .addItemFlag(ItemFlag.HIDE_UNBREAKABLE);

        if (getItem().getMaterial() != Material.WRITTEN_BOOK)
            itemBuilder.setUnbreakable(true);

        if (item.getRare() == ItemRare.UNIQUE && (item.getImperiumType() == ItemType.WEAPON || item.getImperiumType() == ItemType.ARMOR)) {
            itemBuilder.addEnchantment(Enchantment.DURABILITY, 1);
        }

        if (item.checkInstance(ToolItem.class)) {
            val toolPower = item.getAttributes().find(ImperiumAttribute.INCREASE_TOOL_POWER).getValue();
            if (toolPower >= 1) {
                if (item.checkInstance(PickaxeItem.class)) {
                    itemBuilder.addEnchantment(Enchantment.DIG_SPEED, (int) toolPower);
                } else if (item.checkInstance(FishingRodItem.class)) {
                    itemBuilder.addEnchantment(Enchantment.LUCK, (int) toolPower);
                }
            }
        }

        val descriptionContainer = item.processDescriptionContainer();

        if (isUnique) {
            var nbtBuilder = itemBuilder.nbtBuilder();
            nbtBuilder.setTag("uid", item.getUniqueId())
                    .setTag("uuid", UUID.randomUUID().toString())
                    .setTag("uniquePerSession", session == null ? "" : session.getSessionUuid().toString())
                    .setTag("rid", String.valueOf(item.getDurability()))
                    .setTag("advancedLore", descriptionContainer.isWithAdvanced() ? String.join("\n", TextUtility.colorize(descriptionContainer.getAdvancedLore())) : "");

            item.getItemWatcherRegistry().unwatchAll(nbtBuilder);

            nbtBuilder.apply()
                    .setLore(descriptionContainer.getLore());
        } else {
            var nbtBuilder = itemBuilder.nbtBuilder();
            nbtBuilder
                    .setTag("uid", item.getUniqueId())
                    .setTag("uniquePerSession", session == null ? "" : session.getSessionUuid().toString())
                    .setTag("rid", String.valueOf(item.getDurability()))
                    .setTag("advancedLore", descriptionContainer.isWithAdvanced() ? String.join("\n", TextUtility.colorize(descriptionContainer.getAdvancedLore())) : "");

            item.getItemWatcherRegistry().unwatchAll(nbtBuilder);

            nbtBuilder.apply().setLore(descriptionContainer.getLore());
        }

        return NbtUtility.applyTagCustomize(itemBuilder.build(), nbtTagCompound -> {
            nbtTagCompound.setBoolean("Unbreakable", true);
            nbtTagCompound.setInt("price", item.getGeneralSettings().calculatePrice());

            val customKey = item.getGeneralSettings().customNBTKey();
            val customValue = item.getGeneralSettings().customNBTValue();
            if (customKey != null && customValue != null) {
                nbtTagCompound.setString(customKey, customValue);
                nbtTagCompound.remove("rid");
            }
            if (item.getGeneralSettings().color() != -1) nbtTagCompound.setInt("color", item.getGeneralSettings().color());
        });
    }

}
