package com.epherical.crafting.nms;

import com.epherical.crafting.logging.Log;
import com.google.gson.JsonObject;

import net.minecraft.server.v1_16_R3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.*;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.block.CraftBlockEntityState;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftNamespacedKey;
import org.bukkit.inventory.Recipe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

public class NMS1_16V3 implements NMSInterface {

    private static final Logger LOG_MANAGER = LogManager.getLogger();

    private Class<?> craftNamespacedKey;
    private Method toMinecraftKey;



    public NMS1_16V3() throws ClassNotFoundException, NoSuchMethodException {
        this.craftNamespacedKey = Class.forName("org.bukkit.craftbukkit.v1_16_R3.util.CraftNamespacedKey");
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

    @Override
    public void registerRecipes(Map<NamespacedKey, JsonObject> recipes) {
        try {
            Class<?> craftingClass = Class.forName("net.minecraft.server.v1_16_R3.CraftingManager");
            Class<?> minecraftKey = Class.forName("net.minecraft.server.v1_16_R3.MinecraftKey");
            Class<?> serverClass = Class.forName("net.minecraft.server.v1_16_R3.MinecraftServer");
            Class<?> iRecipe = Class.forName("net.minecraft.server.v1_16_R3.IRecipe");
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


    public Recipe getCookingRecipeFromIngredient(TileState block, World world) {
        // TODO: reflection or somehow get API this is bad

        CraftingManager manager = MinecraftServer.getServer().getCraftingManager();
        IRecipe<?> recipes = null;
        /*if (block instanceof Campfire) {
            CraftBlockEntityState<TileEntityCampfire> tile = (CraftBlockEntityState<TileEntityCampfire>) block;
            recipes = manager.craft(Recipes.CAMPFIRE_COOKING, tile.getTileEntity()., ((CraftWorld) world).getHandle()).orElse(null);
        }*/ if (block instanceof BlastFurnace) {
            CraftBlockEntityState<TileEntityBlastFurnace> tile = (CraftBlockEntityState<TileEntityBlastFurnace>) block;
            recipes = manager.craft(Recipes.BLASTING, tile.getTileEntity(), ((CraftWorld) world).getHandle()).orElse(null);
        } else if (block instanceof Smoker) {
            CraftBlockEntityState<TileEntitySmoker> tile = (CraftBlockEntityState<TileEntitySmoker>) block;
            recipes = manager.craft(Recipes.SMOKING, tile.getTileEntity(), ((CraftWorld) world).getHandle()).orElse(null);
        } else if (block instanceof Furnace) {
            CraftBlockEntityState<TileEntityFurnaceFurnace> tile = (CraftBlockEntityState<TileEntityFurnaceFurnace>) block;
            recipes = manager.craft(Recipes.SMELTING, tile.getTileEntity(), ((CraftWorld) world).getHandle()).orElse(null);
        }

        return recipes != null ? recipes.toBukkitRecipe() : null;
    }

    public ArrayList<NamespacedKey> getRecipeKeys() {
        // TODO: reflection i guess lazy
        ArrayList<NamespacedKey> keys = new ArrayList<>();
        MinecraftServer.getServer().getCraftingManager().b().forEach(iRecipe -> {
            keys.add(CraftNamespacedKey.fromMinecraft(iRecipe.getKey()));
        });
        return keys;
    }
}
