package com.epherical.crafting.commands;

import com.epherical.crafting.ThonkCrafting;
import com.epherical.crafting.gui.RecipeCreatorMenu;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
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
            if (args.length >= 1) {
                NamespacedKey key = ThonkCrafting.createKey(args[0]);

                Recipe recipe = null;
                if (player.hasPermission("thonkcrafting.view.allrecipes")) {
                    recipe = Bukkit.getRecipe(key);
                } else {
                    Set<NamespacedKey> keySet = player.getDiscoveredRecipes();
                    if (keySet.contains(key)) {
                        recipe = Bukkit.getRecipe(key);
                    }
                }


                if (recipe == null) {
                    return false;
                }

                boolean editRecipe = false;
                boolean convertToThonkCraftingRecipe = false;
                if (args.length > 1 && player.hasPermission("thonkcrafting.edit.recipe")) {
                    editRecipe = Boolean.parseBoolean(args[1]);
                }

                if (args.length > 2 && player.hasPermission("thonkcrafting.edit.recipe")) {
                    convertToThonkCraftingRecipe = Boolean.parseBoolean(args[2]);
                }

                new RecipeCreatorMenu(recipe, commandSender, editRecipe, convertToThonkCraftingRecipe);
            }

        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        List<String> completedList = new ArrayList<>();
        if (args.length == 1) {
            String commandPiece = args[0];

            copyPartialMatches(commandPiece, recipeKeys, completedList);
        }

        return completedList;
    }

    public static <T extends Collection<? super String>> T copyPartialMatches(String token, Iterable<String> originals, T collection) throws UnsupportedOperationException, IllegalArgumentException {
        for (String string : originals) {
            if (containsIgnoreCase(string, token)) {
                collection.add(string);
            }
        }

        return collection;
    }

    public static boolean containsIgnoreCase(String string, String prefix) throws IllegalArgumentException, NullPointerException {
        return string.toLowerCase().contains(prefix.toLowerCase());
    }
}
