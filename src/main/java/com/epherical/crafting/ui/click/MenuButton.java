package com.epherical.crafting.ui.click;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class MenuButton {
    private int slot;
    private final Consumer<InventoryClickEvent> clickEvent;
    private ItemStack displayItem;

    public MenuButton(Consumer<InventoryClickEvent> clickEvent, int slot, ItemStack displayItem) {
        this.clickEvent = clickEvent;
        this.slot = slot;
        this.displayItem = displayItem;
    }

    public MenuButton(Consumer<InventoryClickEvent> clickEvent, int slot, Material displayItem) {
        this(clickEvent, slot, new ItemStack(displayItem));
    }

    public Consumer<InventoryClickEvent> getClickEvent() {
        return clickEvent;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public int getSlot() {
        return slot;
    }

    public void setDisplayItem(ItemStack displayItem) {
        this.displayItem = displayItem;
    }

    public ItemStack getDisplayItem() {
        return displayItem;
    }
}
