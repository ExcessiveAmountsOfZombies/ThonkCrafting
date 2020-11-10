package com.epherical.crafting;

import com.epherical.crafting.recipes.internal.*;
import net.minecraft.server.v1_16_R2.*;

public class CraftingRegistry {

    public static InternalRecipeShaped.ShapedSerializer SHAPED_SERIALIZER;
    public static InternalRecipeShapeless.ShapelessSerializer SHAPELESS_SERIALIZER;
    public static InternalRecipeCooking.CookingSerializer<InternalRecipeBlasting> BLASTING_SERIALIZER;
    public static InternalRecipeCooking.CookingSerializer<InternalRecipeCampfire> CAMPFIRE_SERIALIZER;
    public static InternalRecipeCooking.CookingSerializer<InternalRecipeSmelting> SMELTING_SERIALIZER;
    public static InternalRecipeCooking.CookingSerializer<InternalRecipeSmoking>  SMOKING_SERIALIZER;
    public static InternalRecipeSingleItem.SingleItemSerializer<InternalRecipeStonecutting> STONECUTTING_SERIALIZER;


    public static void init() {
        // We only need to register this once, if it gets registered multiple times players won't be able to connect to the server
        SHAPED_SERIALIZER = initSerializer(new MinecraftKey("thonkcrafting", "shaped_recipe"),
                new InternalRecipeShaped.ShapedSerializer());

        SHAPELESS_SERIALIZER = initSerializer(new MinecraftKey("thonkcrafting", "shapeless_recipe"),
                new InternalRecipeShapeless.ShapelessSerializer());

        BLASTING_SERIALIZER = initSerializer(new MinecraftKey("thonkcrafting", "blasting_recipe"),
                new InternalRecipeCooking.CookingSerializer<>(InternalRecipeBlasting::new));

        CAMPFIRE_SERIALIZER = initSerializer(new MinecraftKey("thonkcrafting", "campfire_recipe"),
                new InternalRecipeCooking.CookingSerializer<>(InternalRecipeCampfire::new));

        SMELTING_SERIALIZER = initSerializer(new MinecraftKey("thonkcrafting", "smelting_recipe"),
                new InternalRecipeCooking.CookingSerializer<>(InternalRecipeSmelting::new));

        SMOKING_SERIALIZER = initSerializer(new MinecraftKey("thonkcrafting", "smoking_recipe"),
                new InternalRecipeCooking.CookingSerializer<>(InternalRecipeSmoking::new));

        STONECUTTING_SERIALIZER = initSerializer(new MinecraftKey("thonkcrafting", "stonecutting_recipe"),
                new InternalRecipeSingleItem.SingleItemSerializer<>(InternalRecipeStonecutting::new));


    }

    private static <S extends RecipeSerializer<T>, T extends IRecipe<?>> S initSerializer(MinecraftKey key, S object) {
        S instance = (S) RegistryMaterials.RECIPE_SERIALIZER.get(key);
        if (instance == null) {
            instance = RegistryMaterials.a(RegistryMaterials.RECIPE_SERIALIZER, key, object);
        }
        return instance;
    }
}
