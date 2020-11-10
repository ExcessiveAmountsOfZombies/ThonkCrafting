package com.epherical.crafting.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockCookEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;

public class RecipeListener implements Listener {



    @EventHandler // Shapeless, Shaped
    public void onPreCraftItem(PrepareItemCraftEvent event) {

    }


    @EventHandler // Shapeless, Shaped
    public void onCraftItem(CraftItemEvent event) {

    }


    @EventHandler // Campfire, Smelting, Smoking, Blasting
    public void onCook(BlockCookEvent event) {

    }

    @EventHandler // Used for Stonecutting recipes
    public void onInventoryClick(InventoryClickEvent event) {

    }


}
