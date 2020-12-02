package com.epherical.crafting.nms;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.TileState;
import org.bukkit.inventory.Recipe;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Stream;

public interface NMSInterface {

    Object getMinecraftKey(NamespacedKey key);

    void overrideRecipe(Object minecraftKey, JsonObject object);

    void registerRecipes(Map<NamespacedKey, JsonObject> map);

    Recipe getCookingRecipeFromIngredient(TileState furnace, World world);

    ArrayList<NamespacedKey> getRecipeKeys();

    Object createRecipeItemStack(JsonElement element);

    Object createRecipeItemStackProvider(JsonObject object);

    Object createNMSItemStack(JsonObject object);

    Object constructRecipeItemStack(Stream<?> object, boolean hasData);
}
