package com.epherical.crafting.ui;

import com.epherical.crafting.ui.click.MenuButton;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class MenuDefaults {

   /* public static MenuButton[] createBorder(Material borderMaterial, int rows) {
        int size = rows * 9;
        ArrayList<MenuButton> buttons = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if ((((i / 9 == 0) || i / 9 == rows - 1)) || ((i % 9) == 8 || i % 9 == 0)) {
                buttons.add(new MenuButton(event -> event.setCancelled(true), i, new ItemStack(borderMaterial)));
            }
        }
        return buttons.toArray(new MenuButton[]{});
    }*/

    public static MenuButton closeButton(Material material, int slot) {
        return closeButton(new ItemStack(material), slot);
    }

    public static MenuButton closeButton(ItemStack itemStack, int slot) {
        return new MenuButton(event -> {
            event.setCancelled(true);
            event.getWhoClicked().closeInventory(InventoryCloseEvent.Reason.PLUGIN);
        }, itemStack);
    }

    public static MenuButton defaultCloseButton(int slot) {
        ItemStack itemStack = new ItemStack(Material.BARRIER, 1);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName("Close Inventory");
        itemStack.setItemMeta(meta);
        return new MenuButton(event -> {
            event.setCancelled(true);
            event.getWhoClicked().closeInventory(InventoryCloseEvent.Reason.PLUGIN);
        }, itemStack);
    }
}
