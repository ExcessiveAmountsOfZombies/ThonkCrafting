package com.epherical.crafting.recipes.internal;

import com.epherical.crafting.CraftingRegistry;
import com.epherical.crafting.options.Options;
import com.epherical.crafting.recipes.impl.RecipeSmoking;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftRecipe;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftNamespacedKey;
import org.bukkit.inventory.Recipe;

import java.util.ArrayList;

public class InternalRecipeSmoking extends InternalRecipeCooking {


    public InternalRecipeSmoking(MinecraftKey key, String group, RecipeItemStack input,
                                 ItemStack result, float exp, int cookTime, ArrayList<Options> options) {
        super(Recipes.SMOKING, key, group, input, result, exp, cookTime, options);
    }

    @Override
    public RecipeSerializer<?> getRecipeSerializer() {
        return CraftingRegistry.SMOKING_SERIALIZER;
    }

    @Override
    public Recipe toBukkitRecipe() {
        CraftItemStack result = CraftItemStack.asCraftMirror(this.output);
        RecipeSmoking recipe = new RecipeSmoking(CraftNamespacedKey.fromMinecraft(this.key), result, CraftRecipe.toBukkit(this.input), this.experience, this.cookingTime, this.options);
        recipe.setGroup(this.group);
        return recipe;
    }

    @Override
    public IRecipe<?> getVanillaRecipe() {
        return new net.minecraft.server.v1_16_R3.RecipeSmoking(key, group, input, result, experience, cookingTime);
    }

    @Override
    public Material getRelevantMaterial() {
        return Material.SMOKER;
    }
}
