package com.epherical.crafting.listener;

import com.epherical.crafting.ThonkCrafting;
import com.epherical.crafting.options.OptionContext;
import com.epherical.crafting.options.SuccessOptions;
import com.epherical.crafting.options.TestOptions;
import com.epherical.crafting.recipes.CustomRecipe;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.block.TileState;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockCookEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class RecipeListener implements Listener {

    private ThonkCrafting plugin;

    public RecipeListener(ThonkCrafting plugin) {
        this.plugin = plugin;
    }


    @EventHandler // Shapeless, Shaped
    public void onPreCraftItem(PrepareItemCraftEvent event) {

    }

    @EventHandler // Shapeless, Shaped
    public void onCraftItem(CraftItemEvent event) {
        if (event.getRecipe() instanceof CustomRecipe) {
            Player player = (Player) event.getWhoClicked();
            processRecipe(player, (CustomRecipe) event.getRecipe(), event, player.getLocation());
        }
    }

    @EventHandler
    public void onBurn(FurnaceBurnEvent event) {
        Recipe recipe = plugin.getNmsInterface().getCookingRecipeFromIngredient((TileState) event.getBlock().getState(), event.getBlock().getWorld());
        if (recipe instanceof CustomRecipe) {
            CustomRecipe customRecipe = (CustomRecipe) recipe;
            UUID playerUUID = UUID.fromString(event.getBlock().getMetadata(ThonkCrafting.USED_BY).get(0).asString());
            Player player = Bukkit.getPlayer(playerUUID);
            if (player == null) {
                event.setCancelled(true);
                return;
            }

            if (!processRecipe(player, customRecipe, event, event.getBlock().getLocation())) {
                World world = event.getBlock().getWorld();
                Location blockLocation = event.getBlock().getLocation();
                Furnace furnaceType = (Furnace) event.getBlock().getState();
                Directional direction = (Directional) event.getBlock().getBlockData();
                ItemStack fuel = furnaceType.getInventory().getFuel();
                world.dropItem(blockLocation.add(direction.getFacing().getDirection()), fuel);
                furnaceType.getInventory().setFuel(new ItemStack(Material.AIR, 1));
            }
        }
    }

    @EventHandler // Campfire, Smelting, Smoking, Blasting
    public void onCook(BlockCookEvent event) {
        List<CustomRecipe> customRecipes = Bukkit.getRecipesFor(event.getResult()).stream()
                .filter(recipe -> recipe instanceof CustomRecipe)
                .map(recipe -> (CustomRecipe) recipe).collect(Collectors.toList());

        if (customRecipes.size() == 0) {
            return;
        }

        UUID playerUUID = UUID.fromString(event.getBlock().getMetadata(ThonkCrafting.USED_BY).get(0).asString());
        Material blockMaterial = event.getBlock().getType();
        for (CustomRecipe customRecipe : customRecipes) {
            if (customRecipe.getRelevantMaterial() == blockMaterial) {
                Player player = Bukkit.getPlayer(playerUUID);
                // We already know it's a custom recipe, it wont pass any checks so just cancel early.
                if (player == null) {
                    event.setCancelled(true);
                    return;
                }
                processRecipe(player, customRecipe, event, event.getBlock().getLocation());
            }
        }
    }

    @EventHandler // Used for Stonecutting recipes
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() instanceof StonecutterInventory) {
            StonecutterInventory inventory = (StonecutterInventory) event.getClickedInventory();
            ItemStack result = inventory.getItem(2);
            ItemStack inventoryItem = inventory.getItem(event.getSlot());
            if (result != null) {
                if (result.equals(inventoryItem)) {
                    List<Recipe> recipes = Bukkit.getRecipesFor(result);
                    for (Recipe recipe : recipes) {
                        if (recipe instanceof CustomRecipe) {
                            CustomRecipe customRecipe = (CustomRecipe) recipe;
                            if (customRecipe.getRelevantMaterial() == Material.STONECUTTER) {
                                OptionContext context = new OptionContext((Player) event.getWhoClicked());
                                boolean passedTest = passedTest(context, customRecipe.getTestOptions(), event.getWhoClicked().getLocation().getBlock());
                                if (!passedTest) {
                                    event.setCancelled(true);
                                    return;
                                }
                                applySuccess(context, customRecipe.getSuccessOptions());
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean processRecipe(Player player, CustomRecipe recipe, Cancellable cancellable, Location location) {
        OptionContext context = new OptionContext(player);
        boolean passedTest = passedTest(context, recipe.getTestOptions(), location.getBlock());
        if (!passedTest) {
            cancellable.setCancelled(true);
            return false;
        }
        applySuccess(context, recipe.getSuccessOptions());
        return true;
    }

    private boolean passedTest(OptionContext context, List<TestOptions> options, Block block) {
        for (TestOptions testOption : options) {
            if (!testOption.test(context)) {
                String loc = " @ " + block.getX() + ", " + block.getY() + ", " + block.getZ();
                context.getPlayer().sendMessage(testOption.getFailMessage() + loc);
                return false;
            }
        }
        return true;
    }

    private void applySuccess(OptionContext context, List<SuccessOptions> options) {
        for (SuccessOptions option : options) {
            option.playOption(context);
        }
    }
}
