package com.epherical.crafting.listener;

import com.epherical.crafting.ui.Menu;
import com.epherical.crafting.ui.click.MenuButton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;


public class InventoryListener implements Listener {


    @EventHandler(priority = EventPriority.NORMAL)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof Menu)) {
            return;
        }

        Menu menu = (Menu) event.getInventory().getHolder();

        MenuButton button = menu.getConsumableSlots().get(event.getRawSlot());
        if (button != null) {
            button.getClickEvent().accept(event);
        }
    }
}
