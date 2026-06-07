package cupcake.nikky.imperium.tower.craft;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.item.ItemObject;
import lombok.val;
import me.anfanik.steda.api.utility.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class RecipeBuilder {

    private String category;
    private ItemObject result;
    private int amount, levelRequire;
    private final Map<ItemObject, Integer> requirement = new HashMap<>();

    public static RecipeBuilder newBuilder() {
        return new RecipeBuilder();
    }

    @SafeVarargs
    public final RecipeBuilder appendRequirement(@NotNull final Pair<ItemObject, Integer>... args) {
        for (Pair<ItemObject, Integer> arg : args) {
            this.requirement.put(arg.getFirst(), arg.getSecond());
        }
        return this;
    }

    public RecipeBuilder setCategory(@NotNull final String category) {
        this.category = category;
        return this;
    }

    public RecipeBuilder setLevelRequire(final int levelRequire) {
        this.levelRequire = levelRequire;
        return this;
    }

    public RecipeBuilder setResult(@NotNull final ItemObject item, final int amount) {
        this.result = item;
        this.amount = amount;
        return this;
    }

    public Recipe build() {
        val recipe = new Recipe(requirement, result, amount, levelRequire);
        return Fantasy.recipeProvider().registerRecipe(category, recipe);
    }

}
