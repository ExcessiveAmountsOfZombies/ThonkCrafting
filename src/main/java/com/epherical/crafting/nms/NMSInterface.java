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

    void registerRecipes(Map<NamespacedKey, JsonObject> map);

    Recipe getCookingRecipeFromIngredient(TileState furnace, World world);

    ArrayList<NamespacedKey> getRecipeKeys();

    Object createRecipeChoice(JsonElement element);

    Object createRecipeProvider(JsonObject object);

    Object createNMSItemStack(JsonObject object);

    Object constructRecipeItemStack(Stream<?> object);
}
