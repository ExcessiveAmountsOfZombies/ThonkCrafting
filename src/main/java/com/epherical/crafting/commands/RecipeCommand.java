package com.epherical.crafting.commands;

import com.epherical.crafting.ThonkCrafting;
import com.epherical.crafting.item.RecipeBook;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RecipeCommand implements CommandExecutor {

    private ThonkCrafting thonkCrafting;

    public RecipeCommand(ThonkCrafting thonkCrafting) {
        this.thonkCrafting = thonkCrafting;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            Book book = Book.builder().pages(new RecipeBook(thonkCrafting).getBook()).build();
            BukkitAudiences.create(thonkCrafting).player(player).openBook(book);
        }


        return true;
    }
}
