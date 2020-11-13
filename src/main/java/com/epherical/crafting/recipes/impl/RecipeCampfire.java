package com.epherical.crafting.recipes.impl;

import com.epherical.crafting.recipes.CustomRecipe;
import com.epherical.crafting.options.Options;
import com.epherical.crafting.recipes.internal.InternalRecipeCampfire;
import net.minecraft.server.v1_16_R2.MinecraftServer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftRecipe;
import org.bukkit.craftbukkit.v1_16_R2.util.CraftNamespacedKey;
import org.bukkit.inventory.CampfireRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;

import java.util.ArrayList;

public class RecipeCampfire extends AbstractCooking implements CraftRecipe, CustomRecipe {
    private InternalRecipeCampfire recipe;

    public RecipeCampfire(NamespacedKey key, ItemStack result, Material input, float exp, int cookingTime, ArrayList<Options> options) {
        super(options);
        this.bukkitRecipe = new CampfireRecipe(key, result, input, exp, cookingTime);
    }

    public RecipeCampfire(NamespacedKey key, ItemStack result, RecipeChoice input, float exp, int cookingTime, ArrayList<Options> options) {
        super(options);
        this.bukkitRecipe = new CampfireRecipe(key, result, input, exp, cookingTime);
    }


    @Override
    public void addToCraftingManager() {
        ItemStack result = this.getResult();
        MinecraftServer.getServer().getCraftingManager().addRecipe(
                new InternalRecipeCampfire(CraftNamespacedKey.toMinecraft(this.getKey()),
                        this.getGroup(), this.toNMS(this.getInputChoice(), true),
                        CraftItemStack.asNMSCopy(result), this.getExperience(), this.getCookingTime(), this.getOptions()));
    }

    @Override
    public Material getRelevantMaterial() {
        return Material.CAMPFIRE;
    }
}
