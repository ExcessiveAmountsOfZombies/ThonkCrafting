package com.epherical.crafting.recipes.internal;

import com.epherical.crafting.CraftingRegistry;
import com.epherical.crafting.options.Options;
import net.minecraft.server.v1_16_R2.*;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftRecipe;
import org.bukkit.craftbukkit.v1_16_R2.util.CraftNamespacedKey;
import org.bukkit.inventory.Recipe;

import java.util.ArrayList;

public class InternalRecipeStonecutting extends InternalRecipeSingleItem {

    public InternalRecipeStonecutting(MinecraftKey key, String group, RecipeItemStack input, ItemStack result, ArrayList<Options> options) {
        super(Recipes.STONECUTTING, CraftingRegistry.STONECUTTING_SERIALIZER, key, group, input, result, options);
    }

    @Override
    public Recipe toBukkitRecipe() {
        CraftItemStack result = CraftItemStack.asCraftMirror(this.result);
        com.epherical.crafting.recipes.impl.RecipeStonecutting recipe = new com.epherical.crafting.recipes.impl.RecipeStonecutting(CraftNamespacedKey.fromMinecraft(this.key), result, CraftRecipe.toBukkit(this.ingredient), options);
        recipe.setGroup(this.group);
        return recipe;
    }

    @Override
    public IRecipe<?> getVanillaRecipe() {
        return new RecipeStonecutting(key, group, ingredient, result);
    }

    @Override
    public Material getRelevantMaterial() {
        return Material.STONECUTTER;
    }
}
