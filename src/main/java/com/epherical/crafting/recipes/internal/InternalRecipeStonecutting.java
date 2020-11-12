package com.epherical.crafting.recipes.internal;

import com.epherical.crafting.CraftingRegistry;
import com.epherical.crafting.options.Options;
import net.minecraft.server.v1_16_R2.*;
import org.bukkit.Material;

import java.util.ArrayList;

public class InternalRecipeStonecutting extends InternalRecipeSingleItem {

    public InternalRecipeStonecutting(MinecraftKey key, String group, RecipeItemStack input, ItemStack result, ArrayList<Options> options) {
        super(Recipes.STONECUTTING, CraftingRegistry.STONECUTTING_SERIALIZER, key, group, input, result, options);
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
