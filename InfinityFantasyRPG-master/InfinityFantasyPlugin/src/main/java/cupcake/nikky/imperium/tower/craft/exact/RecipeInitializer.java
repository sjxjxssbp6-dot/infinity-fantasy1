package cupcake.nikky.imperium.tower.craft.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import org.bukkit.Material;

public class RecipeInitializer {

    public RecipeInitializer() {
//        RecipeBuilder.newBuilder()
//                .setCategory("Материалы")
//                .appendRequirement(
//                        new Pair<>(ItemObject.RED_CRYSTAL, 64),
//                        new Pair<>(ItemObject.ORANGE_CRYSTAL, 64),
//                        new Pair<>(ItemObject.GREEN_CRYSTAL, 64))
//                .setResult(ItemObject.HARMONY_CRYSTAL, 1)
//                .setLevelRequire(5)
//                .build();

        Fantasy.recipeProvider().registerIcon("Материалы", Material.DIAMOND);
    }

}
