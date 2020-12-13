package com.epherical.crafting.util;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class ItemUtil {

    public static ItemStack createDisplayItemString(ItemStack item, List<String> lore) {
        ItemMeta meta = item.getItemMeta();
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item.clone();
    }

    /*public static ItemStack createDisplayItemComponent(ItemStack item, List<BaseComponent[]> lore) {
        ItemMeta meta = item.getItemMeta();
        meta.setLoreComponents(lore);
        item.setItemMeta(meta);
        return item.clone();
    }*/

    public static ItemStack createContainerItem(Material material, String name, String... lore) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setLore(Arrays.asList(lore));
        meta.setDisplayName(name);
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
