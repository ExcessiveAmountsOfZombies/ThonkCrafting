package com.epherical.crafting.ui.click;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class MenuButton {
    private final Consumer<InventoryClickEvent> clickEvent;
    private ItemStack displayItem;

    public MenuButton(Consumer<InventoryClickEvent> clickEvent, ItemStack displayItem) {
        this.clickEvent = clickEvent;
        this.displayItem = displayItem;
    }

    public MenuButton(Consumer<InventoryClickEvent> clickEvent, Material displayItem) {
        this(clickEvent, new ItemStack(displayItem));
    }

    public Consumer<InventoryClickEvent> getClickEvent() {
        return clickEvent;
    }

    public void setDisplayItem(ItemStack displayItem) {
        this.displayItem = displayItem;
    }

    public ItemStack getDisplayItem() {
        return displayItem;
    }
}
