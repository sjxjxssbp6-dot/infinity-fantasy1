package cupcake.nikky.imperium.tower.craft;

import lombok.val;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class RecipeProvider {

    private final Map<String, List<Recipe>> data = new HashMap<>();
    private final Map<String, Material> categoryIcons = new HashMap<>();

    public Recipe registerRecipe(@NotNull final String category, @NotNull final Recipe recipe) {
        if (!this.data.containsKey(category)) this.data.put(category, new ArrayList<>());

        this.data.get(category).add(recipe);
        return recipe;
    }

    public RecipeProvider registerIcon(@NotNull final String category, @NotNull final Material material) {
        this.categoryIcons.put(category, material);
        return this;
    }

    public List<String> getCategories() {
        return new ArrayList<>(this.data.keySet());
    }

    public Material findIcon(@NotNull final String category) {
        val icon = this.categoryIcons.get(category);
        return icon == null ? Material.BARRIER : icon;
    }

    public Optional<List<Recipe>> findRecipes(@NotNull final String category) {
        return Optional.ofNullable(this.data.get(category));
    }

    public Optional<Recipe> findRecipe(@NotNull final String category, final int index) {
        return Optional.ofNullable(this.data.get(category).get(index));
    }

}
