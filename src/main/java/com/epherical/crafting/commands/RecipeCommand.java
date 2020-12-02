package com.epherical.crafting.commands;

import com.epherical.crafting.ThonkCrafting;
import com.epherical.crafting.gui.RecipeCreatorMenu;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static net.minecraft.server.v1_16_R3.Items.jf;

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

            boolean trufalse = Boolean.parseBoolean(args[1]);

            /*Consumer<ji.a> consumer = jf1 -> {
            };
            consumer.accept(new ji.a());*/

            /*net.minecraft.server.v1_16_R3.jf clasz;
            ji.a jia = new ji.a();
            jia.a();*/

            new RecipeCreatorMenu(recipe, commandSender, trufalse);
            //new RecipeMenu(recipe, commandSender);
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
