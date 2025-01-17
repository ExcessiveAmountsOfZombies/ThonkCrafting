package com.epherical.crafting.recipes.impl;

import com.epherical.crafting.recipes.CustomRecipe;
import com.epherical.crafting.options.Options;
import com.epherical.crafting.recipes.internal.InternalRecipeSmoking;
import net.minecraft.server.v1_16_R3.MinecraftServer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftRecipe;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftNamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.SmokingRecipe;

import java.util.ArrayList;

public class RecipeSmoking extends AbstractCooking implements CraftRecipe, CustomRecipe {
    private InternalRecipeSmoking recipe;


    public RecipeSmoking(NamespacedKey key, ItemStack result, Material input, float exp, int cookingTime, ArrayList<Options> options) {
        super(options);
        this.bukkitRecipe = new SmokingRecipe(key, result, input, exp, cookingTime);
    }

    public RecipeSmoking(NamespacedKey key, ItemStack result, RecipeChoice input, float exp, int cookingTime, ArrayList<Options> options) {
        super(options);
        this.bukkitRecipe = new SmokingRecipe(key, result, input, exp, cookingTime);
    }

    @Override
    public void addToCraftingManager() {
        ItemStack result = this.getResult();
        MinecraftServer.getServer().getCraftingManager().addRecipe(
                new InternalRecipeSmoking(CraftNamespacedKey.toMinecraft(this.getKey()),
                        this.getGroup(), this.toNMS(this.getInputChoice(), true),
                        CraftItemStack.asNMSCopy(result), this.getExperience(), this.getCookingTime(), this.getOptions()));
    }

    @Override
    public Material getRelevantMaterial() {
        return Material.SMOKER;
    }
}
