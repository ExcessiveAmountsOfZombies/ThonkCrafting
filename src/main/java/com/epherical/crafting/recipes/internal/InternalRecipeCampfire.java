package com.epherical.crafting.recipes.internal;

import com.epherical.crafting.CraftingRegistry;
import com.epherical.crafting.recipes.impl.RecipeCampfire;
import net.minecraft.server.v1_16_R2.*;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftRecipe;
import org.bukkit.craftbukkit.v1_16_R2.util.CraftNamespacedKey;
import org.bukkit.inventory.Recipe;

import java.util.Map;

public class InternalRecipeCampfire extends InternalRecipeCooking {

    public InternalRecipeCampfire(MinecraftKey key, String group, RecipeItemStack input, ItemStack result, float exp, int cookTime, Map<String, Object> options) {
        super(Recipes.CAMPFIRE_COOKING, key, group, input, result, exp, cookTime, options);
    }

    @Override
    public RecipeSerializer<?> getRecipeSerializer() {
        return CraftingRegistry.CAMPFIRE_SERIALIZER;
    }

    @Override
    public Recipe toBukkitRecipe() {
        CraftItemStack result = CraftItemStack.asCraftMirror(output);
        return new RecipeCampfire(CraftNamespacedKey.fromMinecraft(this.key), result, CraftRecipe.toBukkit(this.input), this.experience, this.cookingTime, this.options);
    }

    @Override
    public IRecipe<?> getVanillaRecipe() {
        return new net.minecraft.server.v1_16_R2.RecipeCampfire(key, group, input, result, experience, cookingTime);
    }

    @Override
    public Material getRelevantMaterial() {
        return Material.CAMPFIRE;
    }
}
