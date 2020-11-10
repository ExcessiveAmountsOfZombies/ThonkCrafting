package com.epherical.crafting.test.api;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import java.util.Map;

public class UpdatedBukkitRecipe implements Recipe, Keyed {
    private String perm;
    private ShapedRecipe recipe;

    public UpdatedBukkitRecipe(NamespacedKey key, ItemStack result, String permission) {
        recipe = new ShapedRecipe(key, result);
        this.perm = permission;
    }

    public UpdatedBukkitRecipe shape( String... shape) {
        recipe.shape(shape);
        return this;
    }
    
    public UpdatedBukkitRecipe setIngredient(char key, RecipeChoice ingredient) {
        recipe.setIngredient(key, ingredient);
        return this;
    }

    public UpdatedBukkitRecipe setIngredient(char key, ItemStack item) {
        return this.setIngredient(key, (new RecipeChoice.ExactChoice(item)));
    }

    public Map<Character, ItemStack> getIngredientMap() {
        return recipe.getIngredientMap();
    }

    public Map<Character, RecipeChoice> getChoiceMap() {
       return recipe.getChoiceMap();
    }

    public String[] getShape() {
        return recipe.getShape();
    }

    public ItemStack getResult() {
        return recipe.getResult();
    }

    public NamespacedKey getKey() {
        return recipe.getKey();
    }

    public String getGroup() {
        return recipe.getGroup();
    }

    public void setGroup(String group) {
        recipe.setGroup(group);
    }
}
