package com.epherical.crafting.recipes.internal;

import com.epherical.crafting.CraftingRegistry;
import com.epherical.crafting.api.CustomRecipe;
import com.epherical.crafting.recipes.impl.RecipeShapeless;
import com.google.gson.JsonObject;
import net.minecraft.server.v1_16_R2.*;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftItemStack;
import org.bukkit.inventory.Recipe;

import java.util.HashMap;
import java.util.Map;

public class InternalRecipeShapeless implements CustomRecipe, RecipeCrafting {
    private ShapelessRecipes recipes;
    private String group;
    private Map<String, Object> options;

    public InternalRecipeShapeless(MinecraftKey key, String group, ItemStack itemStack, NonNullList<RecipeItemStack> items,
                                   Map<String, Object> options) {
        this.recipes = new ShapelessRecipes(key, group, itemStack, items);
        this.options = options;
        this.group = group;
    }

    public InternalRecipeShapeless(ShapelessRecipes recipes, String group, Map<String, Object> options) {
        this(recipes.getKey(), group, recipes.getResult(), recipes.a(), options);
    }

    public Map<String, Object> getOptions() {
        return options;
    }

    @Override
    public Material getRelevantMaterial() {
        return null;
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
        return CraftingRegistry.SHAPELESS_SERIALIZER;
    }

    @Override
    public IRecipe<?> getVanillaRecipe() {
        return recipes;
    }

    @Override
    public Recipe toBukkitRecipe() {
        CraftItemStack result = CraftItemStack.asCraftMirror(recipes.getResult());
        return new RecipeShapeless(result, this);
    }


    public static class ShapelessSerializer implements RecipeSerializer<InternalRecipeShapeless> {

        @Override
        public InternalRecipeShapeless a(MinecraftKey minecraftKey, JsonObject jsonObject) {
            Map<String, Object> map = new HashMap<>();
            String s = ChatDeserializer.a(jsonObject, "group", "");
            ShapelessRecipes recipe = RecipeSerializer.b.a(minecraftKey, jsonObject);
            JsonObject object = jsonObject.getAsJsonObject("options");
            map.put("permission", object.getAsJsonPrimitive("permission").getAsString());
            return new InternalRecipeShapeless(recipe, s, map);
        }

        @Override // client method?
        public InternalRecipeShapeless a(MinecraftKey minecraftKey, PacketDataSerializer packetDataSerializer) {
            ShapelessRecipes recipe = RecipeSerializer.b.a(minecraftKey, packetDataSerializer);
            return new InternalRecipeShapeless(recipe, "", new HashMap<>());
        }

        @Override
        public void a(PacketDataSerializer packetDataSerializer, InternalRecipeShapeless internalRecipeShapeless) {
            packetDataSerializer.a(internalRecipeShapeless.group);
            packetDataSerializer.d(internalRecipeShapeless.recipes.a().size());
            for (RecipeItemStack itemStack : internalRecipeShapeless.a()) {
                itemStack.a(packetDataSerializer);
            }

            packetDataSerializer.a(internalRecipeShapeless.recipes.getResult());
        }
    }
}
