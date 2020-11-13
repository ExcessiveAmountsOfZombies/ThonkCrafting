package com.epherical.crafting.listener;

import com.epherical.crafting.options.OptionContext;
import com.epherical.crafting.options.SuccessOptions;
import com.epherical.crafting.options.TestOptions;
import com.epherical.crafting.recipes.CustomRecipe;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.craftbukkit.v1_16_R2.inventory.util.CraftTileInventoryConverter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockCookEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class RecipeListener implements Listener {



    @EventHandler // Shapeless, Shaped
    public void onPreCraftItem(PrepareItemCraftEvent event) {

    }


    @EventHandler // Shapeless, Shaped
    public void onCraftItem(CraftItemEvent event) {
        Player player = (Player) event.getWhoClicked();
        OptionContext context = new OptionContext(player);
        boolean passedTest;
        if (event.getRecipe() instanceof CustomRecipe) {
            CustomRecipe recipe = (CustomRecipe) event.getRecipe();

            passedTest = passedTest(context, recipe.getTestOptions());

            if (!passedTest) {
                event.setCancelled(true);
                return;
            }
            applySuccess(context, recipe.getSuccessOptions());
        }
    }

    // TODO: FURNACE BURN START EVENT DENIAL

    @EventHandler // Campfire, Smelting, Smoking, Blasting
    public void onCook(BlockCookEvent event) {
        List<CustomRecipe> customRecipes = Bukkit.getRecipesFor(event.getResult()).stream()
                .filter(recipe -> recipe instanceof CustomRecipe)
                .map(recipe -> (CustomRecipe) recipe).collect(Collectors.toList());

        if (customRecipes.size() == 0) {
            return;
        }
        // TODO: use global variable for metadata tag
        UUID playerUUID = UUID.fromString(event.getBlock().getMetadata("used-by").get(0).asString());
        Material blockMaterial = event.getBlock().getType();
        for (CustomRecipe customRecipe : customRecipes) {
            if (customRecipe.getRelevantMaterial() == blockMaterial) {
                Player player = Bukkit.getPlayer(playerUUID);
                // We already know it's a custom recipe, it wont pass any checks so just cancel early.
                if (player == null) {
                    event.setCancelled(true);
                    return;
                }
                OptionContext context = new OptionContext(player);

                boolean passedTest = passedTest(context, customRecipe.getTestOptions());
                if (!passedTest) {
                    event.setCancelled(true);
                    return;
                }

                applySuccess(context, customRecipe.getSuccessOptions());
            }
        }
    }

    @EventHandler // Used for Stonecutting recipes
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() instanceof StonecutterInventory) {
            StonecutterInventory inventory = (StonecutterInventory) event.getClickedInventory();
            ItemStack result = inventory.getResult();
            ItemStack inventoryItem = inventory.getItem(event.getSlot());
            if (result != null) {
                if (result.equals(inventoryItem)) {
                    List<Recipe> recipes = Bukkit.getRecipesFor(result);
                    for (Recipe recipe : recipes) {
                        if (recipe instanceof CustomRecipe) {
                            CustomRecipe customRecipe = (CustomRecipe) recipe;
                            if (customRecipe.getRelevantMaterial() == Material.STONECUTTER) {
                                OptionContext context = new OptionContext((Player) event.getWhoClicked());
                                boolean passedTest = passedTest(context, customRecipe.getTestOptions());
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

    private boolean passedTest(OptionContext context, List<TestOptions> options) {
        for (TestOptions testOption : options) {
            if (!testOption.test(context)) {
                context.getPlayer().sendMessage(testOption.getFailMessage());
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
