package com.epherical.crafting.ui;

import com.epherical.crafting.ui.click.MenuButton;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;


public class MenuDefaults<C> {

    public static ArrayList<Integer> acceptableSlots(int rows) {
        int size = rows * 9;
        ArrayList<Integer> slots = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (!((((i / 9 == 0) || i / 9 == rows - 1)) || ((i % 9) == 8 || i % 9 == 0))) {
               slots.add(i);
            }
        }
        return slots;
    }

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


    /*public static Menu createPaginatedMenu(int rows, String menuName, int page, ArrayList<ContainerButton<?>> buttons) {
        ArrayList<Integer> slots = acceptableSlots(rows);
        Menu.Builder builder = new Menu.Builder(rows, menuName, true, Material.BLACK_STAINED_GLASS_PANE);
        if (buttons.size() > slots.size()) {
            // recurse?
        }

    }*/
}
