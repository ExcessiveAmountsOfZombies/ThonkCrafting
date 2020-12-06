package com.epherical.crafting.commands;

import com.epherical.crafting.CraftingRegistry;
import com.epherical.crafting.ThonkCrafting;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class RecipeCreator implements CommandExecutor, TabCompleter {

    private List<String> recipeSerializers = new ArrayList<>();

    public RecipeCreator() {
        recipeSerializers.addAll(ThonkCrafting.getNmsInterface().getRecipeSerializers().stream()
                .filter(key -> !key.toString().contains("special"))
                .map(NamespacedKey::toString).collect(Collectors.toList()));
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            NamespacedKey recipeSerializerKey = ThonkCrafting.createKey(args[0]);
            NamespacedKey recipeKey = ThonkCrafting.createKey(args[1]);
            if (recipeKey != null && recipeSerializerKey != null) {
                boolean vanillaRecipe = false;
                Object recipeSerializer = ThonkCrafting.getNmsInterface().getRecipeSerializer(recipeSerializerKey);
                if (recipeSerializerKey.getNamespace().startsWith("minecraft")) {
                    vanillaRecipe = true;
                }
                CraftingRegistry.RecipeType type = CraftingRegistry.recipeTypeMap.get(recipeSerializer);
                CraftingRegistry.serializingMenuMaps.get(recipeSerializer).create("Recipe Creator", recipeKey, "custom", type, vanillaRecipe, recipeSerializerKey).openInventory(player);

            } else {
                player.sendMessage("Could not create the recipe as the key used was invalid.");
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        List<String> completedList = new ArrayList<>();
        if (args.length == 1) {
            String commandPiece = args[0];
            copyPartialMatches(commandPiece, recipeSerializers, completedList);
        }

        if (args.length == 2) {
            String commandPiece = args[1];
            copyPartialMatches(commandPiece, Collections.singletonList("namespacekey:example"), completedList);
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
