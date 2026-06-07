package cupcake.nikky.imperium.tower.harvest.mining;

import cupcake.nikky.imperium.tower.item.ItemObject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum MiningPatterns {

    COAL_ORE(MiningPattern.of(3, 1, ItemObject.COAL_HARVEST, Material.COAL_ORE, (byte) 0, 20 * 60)),
    IRON_ORE(MiningPattern.of(5, 2, ItemObject.IRON_HARVEST, Material.IRON_ORE, (byte) 0, 20 * 60)),
    GOLD_ORE(MiningPattern.of(13, 3, ItemObject.GOLD_HARVEST, Material.GOLD_ORE, (byte) 0, 20 * 60)),
    DIAMOND_ORE(MiningPattern.of(30, 4, ItemObject.DIAMOND_HARVEST, Material.DIAMOND_ORE, (byte) 0, 20 * 60 * 2)),
    EMERALD_ORE(MiningPattern.of(61, 5, ItemObject.EMERALD_HARVEST, Material.EMERALD_ORE, (byte) 0, 20 * 60 * 5)),
    REDSTONE_ORE(MiningPattern.of(61, 6, ItemObject.RED_STONE_HARVEST, Material.REDSTONE_ORE, (byte) 0, 20 * 60 * 7)),
    GLOWING_REDSTONE_ORE(MiningPattern.of(61, 6, ItemObject.RED_STONE_HARVEST, Material.GLOWING_REDSTONE_ORE, (byte) 0, 20 * 60 * 7));

    private final MiningPattern pattern;

    public static MiningPatterns findPatternEnum(@NotNull final Block block) {
        for (MiningPatterns value : values()) {
            if (value.getPattern().getBlockMaterial() == block.getType() && value.getPattern().getData() == block.getData()) {
                return value;
            }
        }

        return null;
    }

    public static MiningPattern findPattern(@NotNull final Block block) {
        return Optional.ofNullable(findPatternEnum(block)).map(MiningPatterns::getPattern).orElse(null);
    }

}
