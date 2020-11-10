package com.epherical.crafting.recipes.impl;

import com.epherical.crafting.api.CustomRecipe;
import com.epherical.crafting.recipes.internal.InternalRecipeStonecutting;
import net.minecraft.server.v1_16_R2.MinecraftServer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftRecipe;
import org.bukkit.craftbukkit.v1_16_R2.util.CraftNamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.StonecuttingRecipe;

import java.util.Map;

public class RecipeStonecutting implements CraftRecipe, CustomRecipe {
    private InternalRecipeStonecutting recipe;
    private StonecuttingRecipe bukkitRecipe;
    private Map<String, Object> options;
    
    
    public RecipeStonecutting(NamespacedKey key, ItemStack result, Material input, Map<String, Object> options) {
        this.bukkitRecipe = new StonecuttingRecipe(key, result, input);
        this.options = options;
    }
    
    public RecipeStonecutting(NamespacedKey key, ItemStack result, RecipeChoice input, Map<String, Object> options) {
        this.bukkitRecipe = new StonecuttingRecipe(key, result, input);
        this.options = options;
    }

    public Map<String, Object> getOptions() {
        return options;
    }

    public RecipeStonecutting setInput(Material input) {
        this.bukkitRecipe.setInput(input);
        return this;
    }
    
    public ItemStack getInput() {
        return this.bukkitRecipe.getInput();
    }

    public RecipeStonecutting setInputChoice(RecipeChoice input) {
        this.bukkitRecipe.setInputChoice(input);
        return this;
    }

    public RecipeChoice getInputChoice() {
        return this.bukkitRecipe.getInputChoice();
    }

    public ItemStack getResult() {
        return this.bukkitRecipe.getResult();
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
        ItemStack result = this.getResult();
        MinecraftServer.getServer()
                .getCraftingManager()
                .addRecipe(new InternalRecipeStonecutting(CraftNamespacedKey.toMinecraft(this.getKey()),
                        this.getGroup(), this.toNMS(this.getInputChoice(), true), CraftItemStack.asNMSCopy(result)));
    }

    @Override
    public Material getRelevantMaterial() {
        return Material.STONECUTTER;
    }
}