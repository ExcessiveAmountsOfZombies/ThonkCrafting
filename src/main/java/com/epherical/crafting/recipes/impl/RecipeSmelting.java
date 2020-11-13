package com.epherical.crafting.recipes.impl;

import com.epherical.crafting.recipes.CustomRecipe;
import com.epherical.crafting.options.Options;
import com.epherical.crafting.recipes.internal.InternalRecipeSmelting;
import net.minecraft.server.v1_16_R2.MinecraftServer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftRecipe;
import org.bukkit.craftbukkit.v1_16_R2.util.CraftNamespacedKey;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;

import java.util.ArrayList;

public class RecipeSmelting extends AbstractCooking implements CraftRecipe, CustomRecipe {

    public RecipeSmelting(NamespacedKey key, ItemStack result, Material input, float exp, int cookingTime, ArrayList<Options> options) {
        super(options);
        this.bukkitRecipe = new FurnaceRecipe(key, result, input, exp, cookingTime);
    }

    public RecipeSmelting(NamespacedKey key, ItemStack result, RecipeChoice input, float exp, int cookingTime, ArrayList<Options> options) {
        super(options);
        this.bukkitRecipe = new FurnaceRecipe(key, result, input, exp, cookingTime);
    }

    @Override
    public void addToCraftingManager() {
        ItemStack result = this.getResult();
        MinecraftServer.getServer().getCraftingManager().addRecipe(
                new InternalRecipeSmelting(CraftNamespacedKey.toMinecraft(this.getKey()),
                        this.getGroup(), this.toNMS(this.getInputChoice(), true),
                        CraftItemStack.asNMSCopy(result), this.getExperience(), this.getCookingTime(), this.getOptions()));
    }

    @Override
    public Material getRelevantMaterial() {
        return Material.FURNACE;
    }
}
