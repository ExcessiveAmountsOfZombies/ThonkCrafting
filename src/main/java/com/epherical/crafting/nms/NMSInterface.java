package com.epherical.crafting.nms;

import com.google.gson.JsonObject;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Furnace;
import org.bukkit.block.TileState;
import org.bukkit.inventory.Recipe;

import java.util.Map;

public interface NMSInterface {

    Object getMinecraftKey(NamespacedKey key);

    void registerRecipes(Map<NamespacedKey, JsonObject> map);

    Recipe getCookingRecipeFromIngredient(TileState furnace, World world);
}
