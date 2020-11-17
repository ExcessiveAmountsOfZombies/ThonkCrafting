package com.epherical.crafting.commands;

import com.epherical.crafting.ThonkCrafting;
import com.epherical.crafting.ui.Menu;
import com.epherical.crafting.ui.MenuDefaults;
import com.epherical.crafting.ui.click.MenuButton;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RecipeCommand implements CommandExecutor, TabCompleter {

    private ThonkCrafting thonkCrafting;
    private List<String> recipeKeys;

    public RecipeCommand(ThonkCrafting thonkCrafting) {
        this.thonkCrafting = thonkCrafting;
        recipeKeys = thonkCrafting.getNmsInterface().getRecipeKeys().stream().map(NamespacedKey::toString).collect(Collectors.toList());
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            NamespacedKey key = ThonkCrafting.createKey(args[0]);

            Recipe recipe = Bukkit.getRecipe(key);
            if (recipe == null) {
                return false;
            }

            if (recipe instanceof ShapedRecipe) {
                createCraftingMenu(shapedRecipeMenu((ShapedRecipe) recipe), commandSender, recipe.getResult());
            } else if (recipe instanceof ShapelessRecipe) {
                createCraftingMenu(shapelessRecipeMenu((ShapelessRecipe) recipe), commandSender, recipe.getResult());
            } else if (recipe instanceof CookingRecipe) {
                CookingRecipe<?> cookingRecipe = (CookingRecipe<?>) recipe;
                createCookingMenu(cookingRecipe.getInput(), cookingRecipe.getResult(), commandSender);
            }

        }

        return true;
    }

    private Map<Integer, MenuButton> shapelessRecipeMenu(ShapelessRecipe recipe) {
        int firstSlot = 11;
        int slotRow = 0;
        int i = 0;
        Map<Integer, MenuButton> displayedIngredients = new HashMap<>();
        for (ItemStack itemStack : recipe.getIngredientList()) {
            int slot = firstSlot+(slotRow*9)+i;
            displayedIngredients.put(slot, new MenuButton(event -> event.setCancelled(true), slot, itemStack));
            if (i % 2 == 0) {
                slotRow++;
                i = 0;
            }
            i++;
        }
        return displayedIngredients;
    }


    private Map<Integer, MenuButton> shapedRecipeMenu(ShapedRecipe recipe) {
        Map<Character, ItemStack> ingredients = recipe.getIngredientMap();
        Map<Integer, MenuButton> displayedIngredients = new HashMap<>();
        String[] shape = recipe.getShape();
        int firstSlot = 11;
        int slotRow = 0;
        for (String row : shape) {
            for (int i = 0; i < row.toCharArray().length; i++) {
                ItemStack item = ingredients.get(row.toCharArray()[i]);
                int slot = firstSlot+(slotRow*9)+i;
                displayedIngredients.put(slot, new MenuButton(event -> event.setCancelled(true), slot, item));
            }
            slotRow++;
        }
        return displayedIngredients;
    }



    private void createCraftingMenu(Map<Integer, MenuButton> displayedIngredients, CommandSender sender, ItemStack result) {
        Menu.Builder builder = new Menu.Builder(6, "Crafting Menu", true, Material.BLACK_STAINED_GLASS_PANE)
                .addMenuButton(24, new MenuButton(event -> event.setCancelled(true), 24, result))
                .addMenuButton(37, MenuDefaults.defaultCloseButton(37))
                .addMenuButton(38, new MenuButton(event -> event.setCancelled(true), 38, Material.PAPER))
                .addMenuButton(39, new MenuButton(event -> event.setCancelled(true), 39, Material.COMMAND_BLOCK))
                .addMenuButton(42, new MenuButton(event -> event.setCancelled(true), 42, Material.PRISMARINE_SHARD))
                .addMenuButton(43, new MenuButton(event -> event.setCancelled(true), 43, Material.FEATHER))
                .addMenuButtons(new MenuButton(event -> event.setCancelled(true), 0, Material.AIR), 11, 12, 13, 20, 21, 22, 29, 30, 31)
                .addMenuButtons(displayedIngredients);
        Menu menu = builder.build();
        menu.openInventory((HumanEntity) sender);
    }

    private void createCookingMenu(ItemStack input, ItemStack result, CommandSender sender) {
        Menu.Builder builder = new Menu.Builder(6, "Cooking Menu", true, Material.BLACK_STAINED_GLASS_PANE)
                .addMenuButton(24, new MenuButton(event -> event.setCancelled(true), 24, result))
                .addMenuButton(37, MenuDefaults.defaultCloseButton(37))
                .addMenuButton(38, new MenuButton(event -> event.setCancelled(true), 38, Material.PAPER))
                .addMenuButton(39, new MenuButton(event -> event.setCancelled(true), 39, Material.COMMAND_BLOCK))
                .addMenuButton(42, new MenuButton(event -> event.setCancelled(true), 42, Material.PRISMARINE_SHARD))
                .addMenuButton(43, new MenuButton(event -> event.setCancelled(true), 43, Material.FEATHER))
                .addMenuButton(12, new MenuButton(event -> event.setCancelled(true), 12, input))
                .addMenuButton(30, new MenuButton(event -> event.setCancelled(true), 30, Material.LAVA_BUCKET));
        Menu menu = builder.build();
        menu.openInventory((HumanEntity) sender);
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        List<String> completedList = new ArrayList<>();
        if (args.length == 1) {
            String commandPiece = args[0];

            StringUtil.copyPartialMatches(commandPiece, recipeKeys, completedList);
        }

        return completedList;
    }
}
