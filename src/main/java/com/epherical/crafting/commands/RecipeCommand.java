package com.epherical.crafting.commands;

import com.epherical.crafting.ThonkCrafting;
import com.epherical.crafting.recipes.CustomRecipe;
import com.epherical.crafting.recipes.impl.*;
import com.epherical.crafting.ui.Menu;
import com.epherical.crafting.ui.MenuDefaults;
import com.epherical.crafting.ui.click.MenuButton;
import com.epherical.crafting.util.ItemUtil;
import net.md_5.bungee.api.chat.BaseComponent;
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
            NamespacedKey key = ThonkCrafting.createKey(args[0]);

            Recipe recipe = Bukkit.getRecipe(key);
            if (recipe == null) {
                return false;
            }


        }

        return true;
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
