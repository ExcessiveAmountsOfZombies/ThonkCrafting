package com.epherical.crafting;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.epherical.crafting.api.CustomRecipe;
import com.epherical.crafting.config.ConfigManager;
import com.epherical.crafting.listener.RecipeListener;
import com.epherical.crafting.recipes.impl.RecipeCampfire;
import com.epherical.crafting.recipes.impl.RecipeShaped;
import com.epherical.crafting.recipes.impl.RecipeStonecutting;
import com.epherical.crafting.test.api.PermissionRecipe;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockCookEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Iterator;


public class ThonkCrafting extends JavaPlugin implements Listener {

    private ProtocolManager protocolManager;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void onLoad() {
        CraftingRegistry.init();
        protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        PacketListener.addPacketRecipeListener(protocolManager, this);
        DataPackAdder.addDataPackLocation(this);
        OptionRegister.init();
        ConfigManager manager = new ConfigManager(this);
        //getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new RecipeListener(), this);

        RecipeStonecutting stonecutting = new RecipeStonecutting(new NamespacedKey(this, "baddy"),
                new ItemStack(Material.IRON_BLOCK), Material.IRON_INGOT, new ArrayList<>());
        Bukkit.getServer().addRecipe(stonecutting);


        RecipeShaped shaped = new RecipeShaped(new NamespacedKey(this, "hello"), new ItemStack(Material.DIAMOND, 2), new ArrayList<>());
        shaped.shape("5 5", " 5 ");
        shaped.setIngredient('5', Material.POTATO);
        Bukkit.getServer().addRecipe(shaped);

        NamespacedKey key = new NamespacedKey(NamespacedKey.MINECRAFT, "cooked_beef_from_campfire_cooking");
        RecipeCampfire campfire = new RecipeCampfire(key, new ItemStack(Material.IRON_INGOT), Material.BEEF, 0.5f, 100, new ArrayList<>());
        Bukkit.getServer().removeRecipe(key);
        Bukkit.getServer().addRecipe(campfire);

    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        System.out.println(event.getRecipe().getClass());
        if (event.getRecipe() instanceof CustomRecipe) {
            RecipeShaped shaped = (RecipeShaped) event.getRecipe();
            if (shaped.getOptions().size() > 0) {
                event.setCancelled(true);
            }
            System.out.println(shaped.getOptions());
        }

        if (event.getRecipe() instanceof PermissionRecipe) {
            System.out.println("This is a permission recipe");
            System.out.println("Here is the permission: " + ((PermissionRecipe) event.getRecipe()).getPermission());
            System.out.println("Here is the fail message: " + ((PermissionRecipe) event.getRecipe()).getFailMessage());
            System.out.println(((PermissionRecipe) event.getRecipe()).getCounter());
        }

        System.out.println("CraftItemEvent: --> Class Name --> " +  event.getRecipe().getClass());
    }

    @EventHandler
    public void onCook(FurnaceSmeltEvent smeltEvent) {
        Iterator<Recipe> recipeIterator = Bukkit.getServer().recipeIterator();
        recipeIterator.forEachRemaining(recipe -> {
            if (recipe.getResult().isSimilar(smeltEvent.getResult())) {
                System.out.println(recipe.getClass());
                System.out.println("Hey now");
            }
        });

    }

    @EventHandler
    public void cook(BlockCookEvent event) {
        Iterator<Recipe> recipeIterator = Bukkit.getServer().recipeIterator();
        recipeIterator.forEachRemaining(recipe -> {
            if (recipe.getResult().isSimilar(event.getResult())) {
                if (recipe instanceof CustomRecipe) {
                    System.out.println(event.getBlock().getType() == ((CustomRecipe) recipe).getRelevantMaterial());
                }
                System.out.println(recipe.getClass());
                System.out.println("Hey now");
            }
        });
    }

    @Override
    public void onDisable() {
        protocolManager.removePacketListeners(this);
    }

    public Gson getGson() {
        return gson;
    }
}
