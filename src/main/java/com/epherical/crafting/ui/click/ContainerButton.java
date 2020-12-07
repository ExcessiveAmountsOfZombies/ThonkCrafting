package com.epherical.crafting.ui.click;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class ContainerButton<T> extends MenuButton {

    private T container;

    public ContainerButton(Consumer<InventoryClickEvent> clickEvent, ItemStack displayItem, T value) {
        super(clickEvent, displayItem);
        this.container = value;
    }

    public ContainerButton(Consumer<InventoryClickEvent> clickEvent, Material displayItem, T value) {
        this(clickEvent, new ItemStack(displayItem), value);
    }

    public T getContainer() {
        return container;
    }

    public void setContainer(T value) {
        this.container = value;
    }
}
