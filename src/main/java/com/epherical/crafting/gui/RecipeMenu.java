package com.epherical.crafting.gui;

import com.epherical.crafting.recipes.CustomRecipe;
import com.epherical.crafting.recipes.impl.AbstractCooking;
import com.epherical.crafting.recipes.impl.RecipeShaped;
import com.epherical.crafting.recipes.impl.RecipeShapeless;
import com.epherical.crafting.recipes.impl.RecipeStonecutting;
import com.epherical.crafting.ui.Menu;
import com.epherical.crafting.ui.MenuDefaults;
import com.epherical.crafting.ui.click.MenuButton;
import com.epherical.crafting.util.ItemUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.*;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeMenu {

    public RecipeMenu(Recipe recipe, CommandSender commandSender) {
        List<BaseComponent[]> information = new ArrayList<>();


        if (recipe instanceof CustomRecipe) {
            information.addAll(((CustomRecipe) recipe).getOptionComponent());
        }

        MenuButton paperButton = new MenuButton(event -> event.setCancelled(true), ItemUtil.createDisplayItemComponent(new ItemStack(Material.PAPER, 1), information));

        // TODO this is terrible to do could be improved
        if (recipe instanceof ShapedRecipe) {
            ShapedRecipe shapedRecipe = (ShapedRecipe) recipe;
            createCraftingMenu(shapedRecipeMenu(shapedRecipe.getIngredientMap(), shapedRecipe.getShape()), paperButton, commandSender, recipe.getResult());
        } else if (recipe instanceof RecipeShaped) {
            RecipeShaped shaped = (RecipeShaped) recipe;
            createCraftingMenu(shapedRecipeMenu(shaped.getIngredientMap(), shaped.getShape()), paperButton, commandSender, recipe.getResult());
        } else if (recipe instanceof ShapelessRecipe) {
            ShapelessRecipe shapelessRecipe = (ShapelessRecipe) recipe;
            createCraftingMenu(shapelessRecipeMenu(shapelessRecipe.getIngredientList()), paperButton, commandSender, recipe.getResult());
        } else if (recipe instanceof RecipeShapeless) {
            RecipeShapeless recipeShapeless = (RecipeShapeless) recipe;
            createCraftingMenu(shapelessRecipeMenu(recipeShapeless.getIngredientList()), paperButton, commandSender, recipe.getResult());
        } else if (recipe instanceof CookingRecipe) {
            CookingRecipe<?> cookingRecipe = (CookingRecipe<?>) recipe;
            createCookingMenu(cookingRecipe.getInput(), cookingRecipe.getResult(), paperButton, commandSender);
        } else if (recipe instanceof AbstractCooking) {
            AbstractCooking cooking = (AbstractCooking) recipe;
            createCookingMenu(cooking.getInput(), cooking.getResult(), paperButton, commandSender);
        } else if (recipe instanceof StonecuttingRecipe) {
            StonecuttingRecipe stonecuttingRecipe = (StonecuttingRecipe) recipe;
            createStonecuttingMenu(stonecuttingRecipe.getInput(), stonecuttingRecipe.getResult(), paperButton, commandSender);
        } else if (recipe instanceof RecipeStonecutting) {
            RecipeStonecutting cutting = (RecipeStonecutting) recipe;
            createStonecuttingMenu(cutting.getInput(), cutting.getResult(), paperButton, commandSender);
        } else if (recipe instanceof SmithingRecipe) {
            SmithingRecipe smith = (SmithingRecipe) recipe;
            createSmithingMenu(smith.getBase().getItemStack(), smith.getAddition().getItemStack(), smith.getResult(), paperButton, commandSender);
        } /*else if (recipe instanceof RecipeSmithing) {
                RecipeSmithing smith = (RecipeSmithing) recipe;
                createSmithingMenu(smith.getBase().getItemStack(), smithing.getAddition().getItemStack(), smithing.getResult(), paperButton, commandSender);
            }*/
    }

    private Map<Integer, MenuButton> shapelessRecipeMenu(List<ItemStack> ingredientList) {
        int firstSlot = 11;
        int slotRow = 0;
        int i = 0;
        Map<Integer, MenuButton> displayedIngredients = new HashMap<>();
        for (ItemStack itemStack : ingredientList) {
            int slot = firstSlot+(slotRow*9)+i;
            displayedIngredients.put(slot, new MenuButton(event -> event.setCancelled(true), itemStack));
            if (i % 2 == 0) {
                slotRow++;
                i = 0;
            }
            i++;
        }
        return displayedIngredients;
    }


    private Map<Integer, MenuButton> shapedRecipeMenu(Map<Character, ItemStack> ingredients, String[] shape) {
        Map<Integer, MenuButton> displayedIngredients = new HashMap<>();
        int firstSlot = 11;
        int slotRow = 0;
        for (String row : shape) {
            for (int i = 0; i < row.toCharArray().length; i++) {
                ItemStack item = ingredients.get(row.toCharArray()[i]);
                int slot = firstSlot+(slotRow*9)+i;
                displayedIngredients.put(slot, new MenuButton(event -> event.setCancelled(true), item));
            }
            slotRow++;
        }
        return displayedIngredients;
    }



    private void createCraftingMenu(Map<Integer, MenuButton> displayedIngredients, MenuButton informationButton, CommandSender sender, ItemStack result) {
        Menu.Builder builder = new Menu.Builder(6, "Crafting Menu", true, Material.BLACK_STAINED_GLASS_PANE)
                .addMenuButton(24, new MenuButton(event -> event.setCancelled(true), result))
                .addMenuButton(37, MenuDefaults.defaultCloseButton(37))
                .addMenuButton(38, informationButton)
                .addMenuButton(39, new MenuButton(event -> event.setCancelled(true), Material.COMMAND_BLOCK))
                .addMenuButton(42, new MenuButton(event -> event.setCancelled(true), Material.PRISMARINE_SHARD))
                .addMenuButton(43, new MenuButton(event -> event.setCancelled(true), Material.FEATHER))
                .addMenuButtons(new MenuButton(event -> event.setCancelled(true), Material.AIR), 11, 12, 13, 20, 21, 22, 29, 30, 31)
                .addMenuButtons(displayedIngredients);
        Menu menu = builder.build();
        menu.openInventory((HumanEntity) sender);
    }

    private void createStonecuttingMenu(ItemStack input, ItemStack result, MenuButton informationButton, CommandSender sender) {
        Menu.Builder builder = new Menu.Builder(6, "Stonecutting Menu", true, Material.BLACK_STAINED_GLASS_PANE)
                .addMenuButton(24, new MenuButton(event -> event.setCancelled(true), result))
                .addMenuButton(37, MenuDefaults.defaultCloseButton(37))
                .addMenuButton(38, informationButton)
                .addMenuButton(39, new MenuButton(event -> event.setCancelled(true), Material.COMMAND_BLOCK))
                .addMenuButton(42, new MenuButton(event -> event.setCancelled(true), Material.PRISMARINE_SHARD))
                .addMenuButton(43, new MenuButton(event -> event.setCancelled(true), Material.FEATHER))
                .addMenuButton(22, new MenuButton(event -> event.setCancelled(true), Material.YELLOW_STAINED_GLASS_PANE))
                .addMenuButton(23, new MenuButton(event -> event.setCancelled(true), Material.LIME_STAINED_GLASS_PANE))
                .addMenuButton(21, new MenuButton(event -> event.setCancelled(true), input));
        Menu menu = builder.build();
        menu.openInventory((HumanEntity) sender);
    }

    private void createSmithingMenu(ItemStack input, ItemStack secondaryInput, ItemStack result, MenuButton informationButton, CommandSender sender) {
        Menu.Builder builder = new Menu.Builder(6, "Smithing Recipe", true, Material.BLACK_STAINED_GLASS_PANE)
                .addMenuButton(24, new MenuButton(event -> event.setCancelled(true), result))
                .addMenuButton(37, MenuDefaults.defaultCloseButton(37))
                .addMenuButton(38, informationButton)
                .addMenuButton(39, new MenuButton(event -> event.setCancelled(true), Material.COMMAND_BLOCK))
                .addMenuButton(42, new MenuButton(event -> event.setCancelled(true), Material.PRISMARINE_SHARD))
                .addMenuButton(43, new MenuButton(event -> event.setCancelled(true), Material.FEATHER))
                .addMenuButton(21, new MenuButton(event -> event.setCancelled(true), input))
                .addMenuButton(22, new MenuButton(event -> event.setCancelled(true), secondaryInput));
        Menu menu = builder.build();
        menu.openInventory((HumanEntity) sender);
    }

    private void createCookingMenu(ItemStack input, ItemStack result, MenuButton informationButton, CommandSender sender) {
        Menu.Builder builder = new Menu.Builder(6, "Cooking Menu", true, Material.BLACK_STAINED_GLASS_PANE)
                .addMenuButton(24, new MenuButton(event -> event.setCancelled(true), result))
                .addMenuButton(37, MenuDefaults.defaultCloseButton(37))
                .addMenuButton(38, informationButton)
                .addMenuButton(39, new MenuButton(event -> event.setCancelled(true), Material.COMMAND_BLOCK))
                .addMenuButton(42, new MenuButton(event -> event.setCancelled(true), Material.PRISMARINE_SHARD))
                .addMenuButton(43, new MenuButton(event -> event.setCancelled(true), Material.FEATHER))
                .addMenuButton(12, new MenuButton(event -> event.setCancelled(true), input))
                .addMenuButton(30, new MenuButton(event -> event.setCancelled(true), Material.LAVA_BUCKET));
        Menu menu = builder.build();
        menu.openInventory((HumanEntity) sender);
    }
}
