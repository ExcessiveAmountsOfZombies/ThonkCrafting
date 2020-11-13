package com.epherical.crafting.recipes.impl;

import com.epherical.crafting.options.Options;
import com.epherical.crafting.recipes.CustomRecipe;
import com.epherical.crafting.recipes.internal.InternalRecipeSmithing;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.SmithingRecipe;

import java.util.ArrayList;

public class RecipeSmithing implements CraftRecipe, CustomRecipe {
    private InternalRecipeSmithing recipe;
    private SmithingRecipe bukkitRecipe;


    @Override
    public void addToCraftingManager() {

    }

    @Override
    public ItemStack getResult() {
        return null;
    }

    @Override
    public Material getRelevantMaterial() {
        return Material.SMITHING_TABLE;
    }


    @Override
    public ArrayList<Options> getOptions() {
        return null;
    }
}
