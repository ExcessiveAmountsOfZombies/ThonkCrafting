package com.epherical.crafting.recipes.impl;

import com.epherical.crafting.recipes.CustomRecipe;
import com.epherical.crafting.options.Options;
import com.epherical.crafting.recipes.internal.InternalRecipeShaped;
import net.minecraft.server.v1_16_R3.MinecraftServer;
import net.minecraft.server.v1_16_R3.NonNullList;
import net.minecraft.server.v1_16_R3.RecipeItemStack;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftRecipe;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftNamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.Map;

public class RecipeShaped implements CraftRecipe, CustomRecipe {
    private InternalRecipeShaped recipe;
    private ShapedRecipe bukkitRecipe;
    private ArrayList<Options> options;

    public RecipeShaped(NamespacedKey key, ItemStack result, ArrayList<Options> options) {
        this.bukkitRecipe = new ShapedRecipe(key, result);
        this.options = options;
    }

    public RecipeShaped(ItemStack result, InternalRecipeShaped recipe) {
        this(CraftNamespacedKey.fromMinecraft(recipe.getKey()), result, recipe.getOptions());
        this.recipe = recipe;
    }

    public RecipeShaped shape(String... shape) {
        bukkitRecipe.shape(shape);
        return this;
    }

    public RecipeShaped setIngredient(char key, MaterialData ingredient) {
        return this.setIngredient(key, ingredient.getItemType(), ingredient.getData());
    }

    public RecipeShaped setIngredient(char key, Material ingredient) {
        return this.setIngredient(key, ingredient, 0);
    }

    public RecipeShaped setIngredient(char key, Material ingredient, int raw) {
        bukkitRecipe.setIngredient(key, ingredient, raw);
        return this;
    }

    public RecipeShaped setIngredient(char key, RecipeChoice ingredient) {
        bukkitRecipe.setIngredient(key, ingredient);
        return this;
    }

    public RecipeShaped setIngredient(char key, ItemStack item) {
        return this.setIngredient(key, (new RecipeChoice.ExactChoice(item)));
    }

    public Map<Character, ItemStack> getIngredientMap() {
        return bukkitRecipe.getIngredientMap();
    }

    public Map<Character, RecipeChoice> getChoiceMap() {
        return bukkitRecipe.getChoiceMap();
    }

    public String[] getShape() {
        return bukkitRecipe.getShape();
    }

    public NamespacedKey getKey() {
        return bukkitRecipe.getKey();
    }

    public String getGroup() {
        return bukkitRecipe.getGroup();
    }

    public void setGroup(String group) {
        bukkitRecipe.setGroup(group);
    }

    public ArrayList<Options> getOptions() {
        return options;
    }

    @Override
    public void addToCraftingManager() {
        String[] shape = this.getShape();
        Map<Character, RecipeChoice> ingred = this.getChoiceMap();
        int width = shape[0].length();
        NonNullList<RecipeItemStack> data = NonNullList.a(shape.length * width, RecipeItemStack.a);

        for(int i = 0; i < shape.length; ++i) {
            String row = shape[i];

            for(int j = 0; j < row.length(); ++j) {
                data.set(i * width + j, this.toNMS(ingred.get(row.charAt(j)), false));
            }
        }

        MinecraftServer.getServer().getCraftingManager().addRecipe(
                new InternalRecipeShaped(CraftNamespacedKey.toMinecraft(this.getKey()), getGroup(),
                        width, shape.length, data,
                        CraftItemStack.asNMSCopy(this.getResult()), options));
    }

    @Override
    public ItemStack getResult() {
        return bukkitRecipe.getResult();
    }

    @Override
    public Material getRelevantMaterial() {
        return null;
    }
}
