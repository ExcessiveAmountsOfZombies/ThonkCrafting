package com.epherical.crafting.commands;

import com.epherical.crafting.ThonkCrafting;
import com.epherical.crafting.gui.RecipeMenus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

import java.util.*;
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
                    } else {
                        player.sendMessage(ChatColor.RED + "You have not discovered that recipe yet, so you can't view it.");
                        return true;
                    }
                }


                if (recipe == null) {
                    return true;
                }

                boolean editRecipe = false;
                boolean convertToThonkCraftingRecipe = false;

                if (player.hasPermission("thonkcrafting.edit.recipe")) {
                    if (args.length > 1) {
                        if (args[1].equalsIgnoreCase("edit")) {
                            editRecipe = true;
                        } else {
                            editRecipe = Boolean.parseBoolean(args[1]);
                        }
                    }

                    if (args.length > 2) {
                        if (args[2].equalsIgnoreCase("convert")) {
                            convertToThonkCraftingRecipe = true;
                        } else {
                            convertToThonkCraftingRecipe = Boolean.parseBoolean(args[2]);
                        }
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "You do not have permission to edit the recipe!");
                    return true;
                }

                new RecipeMenus(recipe, commandSender, editRecipe, convertToThonkCraftingRecipe);
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

        if (args.length == 2) {
            String commandPiece = args[1];
            copyPartialMatches(commandPiece, Arrays.asList("edit", "true", "false"), completedList);
        }

        if (args.length == 3) {
            String commandPiece = args[2];
            copyPartialMatches(commandPiece, Arrays.asList("convert", "true", "false"), completedList);
        }

        return completedList;
    }

    private static <T extends Collection<? super String>> T copyPartialMatches(String token, Iterable<String> originals, T collection) throws UnsupportedOperationException, IllegalArgumentException {
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
