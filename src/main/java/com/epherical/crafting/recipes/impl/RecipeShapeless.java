package com.epherical.crafting.recipes.impl;

import com.epherical.crafting.recipes.CustomRecipe;
import com.epherical.crafting.options.Options;
import com.epherical.crafting.recipes.internal.InternalRecipeShapeless;
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
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.material.MaterialData;

import java.util.*;

public class RecipeShapeless implements CraftRecipe, CustomRecipe {
    private InternalRecipeShapeless recipe;
    private ShapelessRecipe bukkitRecipe;
    private ArrayList<Options> options;

    public RecipeShapeless(NamespacedKey key, ItemStack result, ArrayList<Options> options) {
        this.bukkitRecipe = new ShapelessRecipe(key, result);
        this.options = options;
    }

    public RecipeShapeless(ItemStack result, InternalRecipeShapeless recipe) {
        this(CraftNamespacedKey.fromMinecraft(recipe.getKey()), result, recipe.getOptions());

        bukkitRecipe.setGroup(recipe.getGroup());
        for (RecipeItemStack itemStack : recipe.a()) {
            bukkitRecipe.addIngredient(CraftRecipe.toBukkit(itemStack));
        }

        this.recipe = recipe;
    }
    
    public RecipeShapeless addIngredient(MaterialData ingredient) {
        return this.addIngredient(1, ingredient);
    }
    
    public RecipeShapeless addIngredient(Material ingredient) {
        return this.addIngredient(1, ingredient, 0);
    }
    
    public RecipeShapeless addIngredient(Material ingredient, int rawdata) {
        return this.addIngredient(1, ingredient, rawdata);
    }
    
    public RecipeShapeless addIngredient(int count, MaterialData ingredient) {
        return this.addIngredient(count, ingredient.getItemType(), ingredient.getData());
    }
    
    public RecipeShapeless addIngredient(int count, Material ingredient) {
        return this.addIngredient(count, ingredient, 0);
    }

    public RecipeShapeless addIngredient(int count, Material ingredient, int rawdata) {
        this.bukkitRecipe.addIngredient(count, ingredient, rawdata);
        return this;
    }

    public RecipeShapeless addIngredient(RecipeChoice ingredient) {
        this.bukkitRecipe.addIngredient(ingredient);
        return this;
    }

    /*public RecipeShapeless addIngredient(ItemStack item) {
        this.bukkitRecipe.addIngredient(item);
        return this;
    }

    public RecipeShapeless addIngredient(int count, ItemStack item) {
        this.bukkitRecipe.addIngredient(count, item);
        return this;
    }

    public RecipeShapeless removeIngredient(ItemStack item) {
        this.bukkitRecipe.removeIngredient(item);
        return this;
    }

    public RecipeShapeless removeIngredient(int count, ItemStack item) {
        this.bukkitRecipe.removeIngredient(count, item);
        return this;
    }*/

    public RecipeShapeless removeIngredient(RecipeChoice ingredient) {
        this.bukkitRecipe.removeIngredient(ingredient);
        return this;
    }

    public RecipeShapeless removeIngredient(Material ingredient) {
        this.bukkitRecipe.removeIngredient(ingredient);
        return this;
    }

    public RecipeShapeless removeIngredient(MaterialData ingredient) {
        this.bukkitRecipe.removeIngredient(ingredient);
        return this;
    }

    public RecipeShapeless removeIngredient(int count, Material ingredient) {
        return this.removeIngredient(count, ingredient, 0);
    }

    public RecipeShapeless removeIngredient(int count, MaterialData ingredient) {
        return this.removeIngredient(count, ingredient.getItemType(), ingredient.getData());
    }

    public RecipeShapeless removeIngredient(Material ingredient, int rawdata) {
        return this.removeIngredient(1, ingredient, rawdata);
    }

    public RecipeShapeless removeIngredient(int count, Material ingredient, int rawdata) {
        this.bukkitRecipe.removeIngredient(count, ingredient, rawdata);
        return this;
    }

    public ItemStack getResult() {
        return this.bukkitRecipe.getResult();
    }

    public List<ItemStack> getIngredientList() {
        return this.bukkitRecipe.getIngredientList();
    }
    
    public List<RecipeChoice> getChoiceList() {
        return this.bukkitRecipe.getChoiceList();
    }
    
    public NamespacedKey getKey() {
        return this.bukkitRecipe.getKey();
    }
    
    public String getGroup() {
        return this.bukkitRecipe.getGroup();
    }

    public void setGroup(String group) {
        this.bukkitRecipe.setGroup(group);
    }

    @Override
    public void addToCraftingManager() {
        List<RecipeChoice> ingred = this.getChoiceList();
        NonNullList<RecipeItemStack> data = NonNullList.a(ingred.size(), RecipeItemStack.a);

        for(int i = 0; i < ingred.size(); ++i) {
            data.set(i, this.toNMS(ingred.get(i), true));
        }

        MinecraftServer.getServer()
                .getCraftingManager().addRecipe(
                        new InternalRecipeShapeless(CraftNamespacedKey.toMinecraft(this.getKey()),
                                this.getGroup(), CraftItemStack.asNMSCopy(this.getResult()), data, this.options));
    }

    @Override
    public Material getRelevantMaterial() {
        return null;
    }

    @Override
    public ArrayList<Options> getOptions() {
        return options;
    }
}
