package com.epherical.crafting.recipes.impl;

import com.epherical.crafting.recipes.CustomRecipe;
import com.epherical.crafting.options.Options;
import com.epherical.crafting.recipes.internal.InternalRecipeBlasting;
import net.minecraft.server.v1_16_R3.MinecraftServer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftRecipe;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftNamespacedKey;
import org.bukkit.inventory.BlastingRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;

import java.util.ArrayList;

public class RecipeBlasting extends AbstractCooking implements CraftRecipe, CustomRecipe {
    private InternalRecipeBlasting recipe;

    public RecipeBlasting(NamespacedKey key, ItemStack result, Material input, float exp, int cookingTime, ArrayList<Options> options) {
        super(options);
        this.bukkitRecipe = new BlastingRecipe(key, result, input, exp, cookingTime);
    }

    public RecipeBlasting(NamespacedKey key, ItemStack result, RecipeChoice input, float exp, int cookingTime, ArrayList<Options> options) {
        super(options);
        this.bukkitRecipe = new BlastingRecipe(key, result, input, exp, cookingTime);
    }


    @Override
    public void addToCraftingManager() {
        ItemStack result = this.getResult();
        MinecraftServer.getServer().getCraftingManager().addRecipe(
                new InternalRecipeBlasting(
                        CraftNamespacedKey.toMinecraft(this.getKey()), this.getGroup(),
                        this.toNMS(this.getInputChoice(), true), CraftItemStack.asNMSCopy(result),
                        this.getExperience(), this.getCookingTime(), this.getOptions()));
    }

    @Override
    public Material getRelevantMaterial() {
        return Material.BLAST_FURNACE;
    }
}
