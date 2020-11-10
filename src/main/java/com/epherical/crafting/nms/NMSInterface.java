package com.epherical.crafting.nms;

import com.google.gson.JsonObject;
import org.bukkit.NamespacedKey;

import java.util.Map;

public interface NMSInterface {

    Object getMinecraftKey(NamespacedKey key);

    void registerRecipes(Map<NamespacedKey, JsonObject> map);
}
