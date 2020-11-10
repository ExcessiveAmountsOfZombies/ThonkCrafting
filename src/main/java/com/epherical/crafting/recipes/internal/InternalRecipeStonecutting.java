package com.epherical.crafting.recipes.internal;

import com.epherical.crafting.CraftingRegistry;
import net.minecraft.server.v1_16_R2.*;
import org.bukkit.Material;

public class InternalRecipeStonecutting extends InternalRecipeSingleItem {

    public InternalRecipeStonecutting(MinecraftKey key, String group, RecipeItemStack input, ItemStack result) {
        super(Recipes.STONECUTTING, CraftingRegistry.STONECUTTING_SERIALIZER, key, group, input, result);
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
