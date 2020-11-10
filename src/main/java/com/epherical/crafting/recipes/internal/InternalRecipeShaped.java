package com.epherical.crafting.recipes.internal;

import com.epherical.crafting.CraftingRegistry;
import com.epherical.crafting.api.CustomRecipe;
import com.epherical.crafting.options.Options;
import com.epherical.crafting.options.TestOptions;
import com.epherical.crafting.recipes.impl.RecipeShaped;
import com.google.gson.JsonObject;
import net.minecraft.server.v1_16_R2.*;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class InternalRecipeShaped implements CustomRecipe, RecipeCrafting {
    private ShapedRecipes recipes;
    private String group;
    private Map<String, Object> options;


    public InternalRecipeShaped(MinecraftKey key, String group, int width, int height, NonNullList<RecipeItemStack> ingredients,
                                ItemStack result, Map<String, Object> options) {
        this.recipes = new ShapedRecipes(key, group, width, height, ingredients, result);
        this.options = options;
        this.group = group;
    }

    public InternalRecipeShaped(ShapedRecipes recipes, String group, Map<String, Object> options) {
        this(recipes.getKey(), group, recipes.i(), recipes.j(), recipes.a(), recipes.getResult(), options);
    }

    public Map<String, Object> getOptions() {
        return options;
    }

    public String getGroup() {
        return group;
    }

    public ShapedRecipes getRecipes() {
        return recipes;
    }

    @Override
    public boolean a(InventoryCrafting inventoryCrafting, World world) {
        return recipes.a(inventoryCrafting, world);
    }

    @Override
    public ItemStack a(InventoryCrafting inventoryCrafting) {
        return recipes.a(inventoryCrafting);
    }

    @Override
    public ItemStack getResult() {
        return recipes.getResult();
    }

    @Override
    public MinecraftKey getKey() {
        return recipes.getKey();
    }

    @Override
    public RecipeSerializer<?> getRecipeSerializer() {
        return CraftingRegistry.SHAPED_SERIALIZER;
    }

    @Override
    public RecipeShaped toBukkitRecipe() {
        CraftItemStack result = CraftItemStack.asCraftMirror(recipes.getResult());
        return new RecipeShaped(result, this);
    }

    public static class ShapedSerializer implements RecipeSerializer<InternalRecipeShaped> {

        @Override
        public InternalRecipeShaped a(MinecraftKey minecraftKey, JsonObject jsonObject) {
            Map<String, Object> map = new HashMap<>();
            String s = ChatDeserializer.a(jsonObject, "group", "");
            ShapedRecipes recipe = RecipeSerializer.a.a(minecraftKey, jsonObject);
            JsonObject object = jsonObject.getAsJsonObject("options");
            map.put("permission", object.getAsJsonPrimitive("permission").getAsString());
            return new InternalRecipeShaped(recipe, s, map);
        }

        @Override
        public InternalRecipeShaped a(MinecraftKey minecraftKey, PacketDataSerializer packetDataSerializer) {
            ShapedRecipes recipe = RecipeSerializer.a.a(minecraftKey, packetDataSerializer);
            // no group for a packet because the packet has already been parsed out. this method is probably never used to create
            // a recipe anyways
            return new InternalRecipeShaped(recipe, "", new HashMap<>());
        }

        @Override
        public void a(PacketDataSerializer packetDataSerializer, InternalRecipeShaped internalRecipeShaped) {
            // matching the vanilla shaped recipe serialization.
            // width
            packetDataSerializer.d(internalRecipeShaped.recipes.i());
            // height
            packetDataSerializer.d(internalRecipeShaped.recipes.j());
            packetDataSerializer.a(internalRecipeShaped.group);

            for (RecipeItemStack recipeitemstack : internalRecipeShaped.recipes.a()) {
                recipeitemstack.a(packetDataSerializer);
            }

            packetDataSerializer.a(internalRecipeShaped.recipes.getResult());

        }
    }

    @Override
    public IRecipe<?> getVanillaRecipe() {
        return recipes;
    }

    @Override
    public org.bukkit.Material getRelevantMaterial() {
        return null;
    }
}