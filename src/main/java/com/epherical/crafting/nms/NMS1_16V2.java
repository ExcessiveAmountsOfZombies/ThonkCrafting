package com.epherical.crafting.nms;

import com.epherical.crafting.logging.Log;
import com.google.gson.JsonObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class NMS1_16V2 implements NMSInterface {

    private static final Logger LOG_MANAGER = LogManager.getLogger();

    private Class<?> craftNamespacedKey;
    private Method toMinecraftKey;



    public NMS1_16V2() throws ClassNotFoundException, NoSuchMethodException {
        this.craftNamespacedKey = Class.forName("org.bukkit.craftbukkit.v1_16_R2.util.CraftNamespacedKey");
        this.toMinecraftKey = craftNamespacedKey.getMethod("toMinecraft", NamespacedKey.class);
    }

    @Override
    public Object getMinecraftKey(NamespacedKey key) {
        try {
            return toMinecraftKey.invoke(craftNamespacedKey, key);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void registerRecipes(Map<NamespacedKey, JsonObject> recipes) {
        try {
            Class<?> craftingClass = Class.forName("net.minecraft.server.v1_16_R2.CraftingManager");
            Class<?> minecraftKey = Class.forName("net.minecraft.server.v1_16_R2.MinecraftKey");
            Class<?> serverClass = Class.forName("net.minecraft.server.v1_16_R2.MinecraftServer");
            Class<?> iRecipe = Class.forName("net.minecraft.server.v1_16_R2.IRecipe");
            // MinecraftServer object
            Object minecraftServer = serverClass.getMethod("getServer").invoke(serverClass);
            Object craftManagerClass = null;
            Method addRecipe = null;
            if (minecraftServer != null) {
                Method craftingMethod = minecraftServer.getClass().getMethod("getCraftingManager");
                craftManagerClass = craftingMethod.invoke(minecraftServer);
                if (craftManagerClass != null) {
                    addRecipe = craftManagerClass.getClass().getMethod("addRecipe", iRecipe);
                }
            }

            if (addRecipe == null) {
                LOG_MANAGER.error("Could not find NMS method to add recipe, aborting recipe initialization.");
                return;
            }

            Method deserializeRecipe = craftingClass.getDeclaredMethod("a", minecraftKey, JsonObject.class);
            for (Map.Entry<NamespacedKey, JsonObject> entry : recipes.entrySet()) {
                // IRecipe<?>
                try {
                    Object recipe = deserializeRecipe.invoke(craftingClass, getMinecraftKey(entry.getKey()), entry.getValue());
                    if (recipe != null) {
                        Bukkit.getServer().removeRecipe(entry.getKey());
                        addRecipe.invoke(craftManagerClass, recipe);
                    }
                } catch (InvocationTargetException ex) {
                    Log.error("Could not parse the recipe {} {}", ex, entry.getKey(), ex.getCause());
                }
            }
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            LOG_MANAGER.error("Could not reflect into NMS", e);
        }
    }
}
