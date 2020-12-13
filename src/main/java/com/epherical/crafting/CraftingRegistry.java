package com.epherical.crafting;

import com.epherical.crafting.gui.RecipeMenus;
import com.epherical.crafting.options.Options;
import com.epherical.crafting.recipes.impl.*;
import com.epherical.crafting.recipes.impl.RecipeBlasting;
import com.epherical.crafting.recipes.impl.RecipeCampfire;
import com.epherical.crafting.recipes.impl.RecipeSmoking;
import com.epherical.crafting.recipes.impl.RecipeStonecutting;
import com.epherical.crafting.recipes.internal.*;
import com.epherical.crafting.ui.Menu;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_16_R3.inventory.*;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftNamespacedKey;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CraftingRegistry {

    public static InternalRecipeShaped.ShapedSerializer SHAPED_SERIALIZER;
    public static InternalRecipeShapeless.ShapelessSerializer SHAPELESS_SERIALIZER;
    public static InternalRecipeCooking.CookingSerializer<InternalRecipeBlasting> BLASTING_SERIALIZER;
    public static InternalRecipeCooking.CookingSerializer<InternalRecipeCampfire> CAMPFIRE_SERIALIZER;
    public static InternalRecipeCooking.CookingSerializer<InternalRecipeSmelting> SMELTING_SERIALIZER;
    public static InternalRecipeCooking.CookingSerializer<InternalRecipeSmoking>  SMOKING_SERIALIZER;
    public static InternalRecipeSingleItem.SingleItemSerializer<InternalRecipeStonecutting> STONECUTTING_SERIALIZER;

    public static Map<RecipeSerializer<?>, RecipeCreator<Menu>> serializingMenuMaps = new HashMap<>();
    public static Map<RecipeSerializer<?>, RecipeType> recipeTypeMap = new HashMap<>();

    public static Map<String, NamespacedKey> recipeClassSerializerMap = new HashMap<>();
    public static Map<String, Class<?>> baseToCustomClass = new HashMap<>();


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


        serializingMenuMaps.put(RecipeSerializer.a, RecipeMenus::recipeCreatorMenu);
        serializingMenuMaps.put(SHAPED_SERIALIZER, RecipeMenus::recipeCreatorMenu);

        serializingMenuMaps.put(RecipeSerializer.b, RecipeMenus::recipeCreatorMenu);
        serializingMenuMaps.put(SHAPELESS_SERIALIZER, RecipeMenus::recipeCreatorMenu);

        serializingMenuMaps.put(RecipeSerializer.p, RecipeMenus::recipeCreatorMenu);
        serializingMenuMaps.put(SMELTING_SERIALIZER, RecipeMenus::recipeCreatorMenu);

        serializingMenuMaps.put(RecipeSerializer.q, RecipeMenus::recipeCreatorMenu);
        serializingMenuMaps.put(BLASTING_SERIALIZER, RecipeMenus::recipeCreatorMenu);

        serializingMenuMaps.put(RecipeSerializer.r, RecipeMenus::recipeCreatorMenu);
        serializingMenuMaps.put(SMOKING_SERIALIZER, RecipeMenus::recipeCreatorMenu);

        serializingMenuMaps.put(RecipeSerializer.s, RecipeMenus::recipeCreatorMenu);
        serializingMenuMaps.put(CAMPFIRE_SERIALIZER, RecipeMenus::recipeCreatorMenu);

        serializingMenuMaps.put(RecipeSerializer.t, RecipeMenus::recipeCreatorMenu);
        serializingMenuMaps.put(STONECUTTING_SERIALIZER, RecipeMenus::recipeCreatorMenu);

        recipeTypeMap.put(RecipeSerializer.a, RecipeType.SHAPED);
        recipeTypeMap.put(SHAPED_SERIALIZER, RecipeType.SHAPED);

        recipeTypeMap.put(RecipeSerializer.b, RecipeType.SHAPELESS);
        recipeTypeMap.put(SHAPELESS_SERIALIZER, RecipeType.SHAPELESS);

        recipeTypeMap.put(RecipeSerializer.p, RecipeType.COOKING);
        recipeTypeMap.put(SMELTING_SERIALIZER, RecipeType.COOKING);

        recipeTypeMap.put(RecipeSerializer.q, RecipeType.COOKING);
        recipeTypeMap.put(BLASTING_SERIALIZER, RecipeType.COOKING);

        recipeTypeMap.put(RecipeSerializer.r, RecipeType.COOKING);
        recipeTypeMap.put(SMOKING_SERIALIZER, RecipeType.COOKING);

        recipeTypeMap.put(RecipeSerializer.s, RecipeType.COOKING);
        recipeTypeMap.put(CAMPFIRE_SERIALIZER, RecipeType.COOKING);

        recipeTypeMap.put(RecipeSerializer.t, RecipeType.CUTTING);
        recipeTypeMap.put(STONECUTTING_SERIALIZER, RecipeType.CUTTING);

        recipeClassSerializerMap.put(RecipeShaped.class.getName(), CraftNamespacedKey.fromMinecraft(IRegistry.RECIPE_SERIALIZER.getKey(SHAPED_SERIALIZER)));
        recipeClassSerializerMap.put(RecipeShapeless.class.getName(), CraftNamespacedKey.fromMinecraft(IRegistry.RECIPE_SERIALIZER.getKey(SHAPELESS_SERIALIZER)));

        recipeClassSerializerMap.put(RecipeStonecutting.class.getName(), CraftNamespacedKey.fromMinecraft(IRegistry.RECIPE_SERIALIZER.getKey(STONECUTTING_SERIALIZER)));

        recipeClassSerializerMap.put(RecipeBlasting.class.getName(), CraftNamespacedKey.fromMinecraft(IRegistry.RECIPE_SERIALIZER.getKey(BLASTING_SERIALIZER)));
        recipeClassSerializerMap.put(RecipeCampfire.class.getName(), CraftNamespacedKey.fromMinecraft(IRegistry.RECIPE_SERIALIZER.getKey(CAMPFIRE_SERIALIZER)));
        recipeClassSerializerMap.put(RecipeSmoking.class.getName(), CraftNamespacedKey.fromMinecraft(IRegistry.RECIPE_SERIALIZER.getKey(SMOKING_SERIALIZER)));
        recipeClassSerializerMap.put(RecipeSmelting.class.getName(), CraftNamespacedKey.fromMinecraft(IRegistry.RECIPE_SERIALIZER.getKey(SMELTING_SERIALIZER)));

        recipeClassSerializerMap.put(CraftShapedRecipe.class.getName(), CraftNamespacedKey.fromMinecraft(IRegistry.RECIPE_SERIALIZER.getKey(RecipeSerializer.a)));
        recipeClassSerializerMap.put(CraftShapelessRecipe.class.getName(), CraftNamespacedKey.fromMinecraft(IRegistry.RECIPE_SERIALIZER.getKey(RecipeSerializer.b)));

        recipeClassSerializerMap.put(CraftStonecuttingRecipe.class.getName(), CraftNamespacedKey.fromMinecraft(IRegistry.RECIPE_SERIALIZER.getKey(RecipeSerializer.t)));

        recipeClassSerializerMap.put(CraftBlastingRecipe.class.getName(), CraftNamespacedKey.fromMinecraft(IRegistry.RECIPE_SERIALIZER.getKey(RecipeSerializer.q)));
        recipeClassSerializerMap.put(CraftCampfireRecipe.class.getName(), CraftNamespacedKey.fromMinecraft(IRegistry.RECIPE_SERIALIZER.getKey(RecipeSerializer.s)));
        recipeClassSerializerMap.put(CraftSmokingRecipe.class.getName(), CraftNamespacedKey.fromMinecraft(IRegistry.RECIPE_SERIALIZER.getKey(RecipeSerializer.r)));
        recipeClassSerializerMap.put(CraftFurnaceRecipe.class.getName(), CraftNamespacedKey.fromMinecraft(IRegistry.RECIPE_SERIALIZER.getKey(RecipeSerializer.p)));


        baseToCustomClass.put(CraftBlastingRecipe.class.getName(), RecipeBlasting.class);
        baseToCustomClass.put(CraftCampfireRecipe.class.getName(), RecipeCampfire.class);
        baseToCustomClass.put(CraftSmokingRecipe.class.getName(),  RecipeSmoking.class);
        baseToCustomClass.put(CraftFurnaceRecipe.class.getName(),  RecipeSmelting.class);

        /*serializingMenuMaps.put(RecipeSerializer.u);
        serializingMenuMaps.put()*/
    }

    private static <S extends RecipeSerializer<T>, T extends IRecipe<?>> S initSerializer(MinecraftKey key, S object) {
        S instance = (S) RegistryMaterials.RECIPE_SERIALIZER.get(key);
        if (instance == null) {
            instance = RegistryMaterials.a(RegistryMaterials.RECIPE_SERIALIZER, key, object);
        }
        return instance;
    }

    public interface RecipeCreator<T extends Menu> {
        T create(String menuName, NamespacedKey key, String group, CraftingRegistry.RecipeType type, boolean vanillaRecipe, NamespacedKey recipeSerializerKey, Player player, List<Options> optionsList);
    }

    public enum RecipeType {
        SHAPED,
        SHAPELESS,
        COOKING,
        CUTTING,
        SMITHING
    }
}
