package com.epherical.crafting.recipes.impl;

import com.epherical.crafting.api.CustomRecipe;
import com.epherical.crafting.recipes.internal.InternalRecipeCooking;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftRecipe;
import org.bukkit.inventory.CookingRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;

import java.util.Map;

public abstract class AbstractCooking implements CraftRecipe, CustomRecipe {
    private InternalRecipeCooking recipe;
    protected CookingRecipe<?> bukkitRecipe;
    private Map<String, Object> options;

    public AbstractCooking(Map<String, Object> options) {
        this.options = options;
    }

    public AbstractCooking(ItemStack result, InternalRecipeCooking recipe) {
        this(recipe.getOptions());
        this.recipe = recipe;
    }

    public void setInput(Material input) {
        bukkitRecipe.setInput(input);
    }

    public ItemStack getInput() {
        return bukkitRecipe.getInput();
    }

    public void setInputChoice(RecipeChoice input) {
        bukkitRecipe.setInputChoice(input);
    }

    public RecipeChoice getInputChoice() {
        return bukkitRecipe.getInputChoice();
    }

    public ItemStack getResult() {
        return bukkitRecipe.getResult();
    }

    public void setExperience(float experience) {
        bukkitRecipe.setExperience(experience);
    }

    public float getExperience() {
        return bukkitRecipe.getExperience();
    }

    public void setCookingTime(int cookingTime) {
        bukkitRecipe.setCookingTime(cookingTime);
    }

    public int getCookingTime() {
        return bukkitRecipe.getCookingTime();
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

    public Map<String, Object> getOptions() {
        return options;
    }
}
