package cupcake.nikky.imperium.tower.item;

import cupcake.nikky.imperium.tower.utility.NbtUtility;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class Icons {

    public static ItemStack WOOD_DAGGER = NbtUtility.applyTagCustomize(new ItemStack(Material.WOOD_SWORD), nbtTagCompound -> nbtTagCompound.setString("rid", "1001"));
    public static ItemStack STONE_DAGGER = NbtUtility.applyTagCustomize(new ItemStack(Material.WOOD_SWORD), nbtTagCompound -> nbtTagCompound.setString("rid", "1002"));
    public static ItemStack IRON_DAGGER = NbtUtility.applyTagCustomize(new ItemStack(Material.WOOD_SWORD), nbtTagCompound -> nbtTagCompound.setString("rid", "1003"));
    public static ItemStack DIAMOND_DAGGER = NbtUtility.applyTagCustomize(new ItemStack(Material.WOOD_SWORD), nbtTagCompound -> nbtTagCompound.setString("rid", "1004"));
    public static ItemStack AWAKENED_DAGGER = NbtUtility.applyTagCustomize(new ItemStack(Material.WOOD_SWORD), nbtTagCompound -> nbtTagCompound.setString("rid", "1005"));

    public static ItemStack WOOD_SWORD = new ItemStack(Material.WOOD_SWORD);
    public static ItemStack STONE_SWORD = new ItemStack(Material.STONE_SWORD);
    public static ItemStack IRON_SWORD = new ItemStack(Material.IRON_SWORD);
    public static ItemStack DIAMOND_SWORD = new ItemStack(Material.DIAMOND_SWORD);
    public static ItemStack AWAKENED_SWORD = NbtUtility.applyTagCustomize(new ItemStack(Material.DIAMOND_SWORD), nbtTagCompound -> nbtTagCompound.setString("rid", "1020"));

    public static ItemStack WOOD_SPEAR = NbtUtility.applyTagCustomize(new ItemStack(Material.WOOD_SWORD), nbtTagCompound -> nbtTagCompound.setString("rid", "1011"));
    public static ItemStack STONE_SPEAR = NbtUtility.applyTagCustomize(new ItemStack(Material.WOOD_SWORD), nbtTagCompound -> nbtTagCompound.setString("rid", "1012"));
    public static ItemStack IRON_SPEAR = NbtUtility.applyTagCustomize(new ItemStack(Material.WOOD_SWORD), nbtTagCompound -> nbtTagCompound.setString("rid", "1013"));
    public static ItemStack DIAMOND_SPEAR = NbtUtility.applyTagCustomize(new ItemStack(Material.WOOD_SWORD), nbtTagCompound -> nbtTagCompound.setString("rid", "1014"));
    public static ItemStack AWAKENED_SPEAR = NbtUtility.applyTagCustomize(new ItemStack(Material.WOOD_SWORD), nbtTagCompound -> nbtTagCompound.setString("rid", "1015"));

    public static ItemStack WOOD_AXE = NbtUtility.applyTagCustomize(new ItemStack(Material.WOOD_SWORD), nbtTagCompound -> nbtTagCompound.setString("rid", "1016"));
    public static ItemStack STONE_AXE = NbtUtility.applyTagCustomize(new ItemStack(Material.WOOD_SWORD), nbtTagCompound -> nbtTagCompound.setString("rid", "1017"));
    public static ItemStack IRON_AXE = NbtUtility.applyTagCustomize(new ItemStack(Material.WOOD_SWORD), nbtTagCompound -> nbtTagCompound.setString("rid", "1018"));
    public static ItemStack DIAMOND_AXE = NbtUtility.applyTagCustomize(new ItemStack(Material.WOOD_SWORD), nbtTagCompound -> nbtTagCompound.setString("rid", "1019"));
    public static ItemStack AWAKENED_AXE = NbtUtility.applyTagCustomize(new ItemStack(Material.WOOD_SWORD), nbtTagCompound -> nbtTagCompound.setString("rid", "1020"));

    public static ItemStack FIRE = NbtUtility.applyTagCustomize(new ItemStack(Material.STICK), nbtTagCompound -> nbtTagCompound.setString("rid", "1045"));
    public static ItemStack COLD = NbtUtility.applyTagCustomize(new ItemStack(Material.STICK), nbtTagCompound -> nbtTagCompound.setString("rid", "1046"));
    public static ItemStack LIGHTNING = NbtUtility.applyTagCustomize(new ItemStack(Material.STICK), nbtTagCompound -> nbtTagCompound.setString("rid", "1047"));
    public static ItemStack CHAOS = NbtUtility.applyTagCustomize(new ItemStack(Material.STICK), nbtTagCompound -> nbtTagCompound.setString("rid", "1048"));

    public static ItemStack POISON = NbtUtility.applyTagCustomize(new ItemStack(Material.MAGMA_CREAM), nbtTagCompound -> nbtTagCompound.setString("rid", "0"));
    public static ItemStack BASE_BLOOD = NbtUtility.applyTagCustomize(new ItemStack(Material.MAGMA_CREAM), nbtTagCompound -> nbtTagCompound.setString("rid", "1"));
    public static ItemStack MULTI_COLORED_BLOOD = NbtUtility.applyTagCustomize(new ItemStack(Material.MAGMA_CREAM), nbtTagCompound -> nbtTagCompound.setString("rid", "2"));
    public static ItemStack WHITE_BLOOD = NbtUtility.applyTagCustomize(new ItemStack(Material.MAGMA_CREAM), nbtTagCompound -> nbtTagCompound.setString("rid", "3"));

}
