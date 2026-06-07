package cupcake.nikky.imperium.tower.tree;

import cupcake.nikky.imperium.tower.client.protocol.image.FantasyImage;
import cupcake.nikky.imperium.tower.item.Icons;
import cupcake.nikky.imperium.tower.item.ItemObject;
import cupcake.nikky.imperium.tower.utility.NbtUtility;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@Getter
public enum TreeIcon {

    CRITICAL(FantasyImage.CRITICAL_ATTACK.toTexture()),
    HEALTH(FantasyImage.HEALTH.toTexture()),
    MANA(FantasyImage.MANA.toTexture()),
    ARMOR(new ItemStack(Material.IRON_CHESTPLATE)),
    POISON(Icons.POISON),
    BLEEDING(Icons.BASE_BLOOD),
    FIRE(Icons.FIRE),
    COLD(Icons.COLD),
    LIGHTNING(Icons.LIGHTNING),
    CHAOS(Icons.CHAOS),
    SHOCK(FantasyImage.SHOCK.toTexture()),
    IGNITE(FantasyImage.IGNITE.toTexture()),
    FREEZE(FantasyImage.FREEZE.toTexture()),
    DAMAGE(Icons.AWAKENED_SWORD),
    DAMAGE_OVER_TIME(FantasyImage.DAMAGE_OVER_TIME.toTexture()),
    MISC(FantasyImage.CONVERT.toTexture()),
    SPELL(FantasyImage.SPELL.toTexture()),
    SWORD(Icons.IRON_SWORD),
    SPEAR(Icons.IRON_SPEAR),
    DAGGER(Icons.IRON_DAGGER),
    BATTLE_AXE(Icons.IRON_AXE),
    PANDORA(FantasyImage.CHEST.toTexture()),
    SPEED(FantasyImage.SPEED.toTexture()),
    MOVEMENT_SPEED(FantasyImage.MOVEMENT_SPEED.toTexture()),
    ATTACK_SPEED(FantasyImage.ATTACK_SPEED.toTexture()),
    MASK(NbtUtility.applyTagCustomize(new ItemStack(Material.STICK), nbtTagCompound -> nbtTagCompound.setString("rid", "2311"))),
    WAY_FRAGMENT(ItemObject.WAY_FRAGMENT_8.getItem().createNew()),
    INCUBATOR(ItemObject.INCUBATOR_RANDOM_STANDARD_MATERIAL_1_ITEM.getItem().createNew()),
    DURATION(FantasyImage.DURATION.toTexture()),
    EDGE_CORE(ItemObject.ORB_OF_DISTORTION_MOD_ITEM.getItem().createNew()),
    EXPERIENCE(FantasyImage.EXPERIENCE.toTexture());

    private final ItemStack icon;
    private final String image;

    TreeIcon(@NotNull final ItemStack itemStack) {
        this.icon = itemStack;
        this.image = null;
    }

    TreeIcon(@NotNull final String image) {
        this.icon = null;
        this.image = image;
    }

}
