package com.epherical.crafting.ui;

import com.epherical.crafting.ui.click.MenuButton;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;


public class Menu implements InventoryHolder {

    private Inventory inventory;
    private String title;
    private Map<Integer, MenuButton> consumableSlots;
    private Consumer<InventoryCloseEvent> closeEvent;


    private Menu(String title, int rows, Map<Integer, MenuButton> menuButtons) {
        this.title = title;
        this.inventory = Bukkit.createInventory(this, rows * 9, title);
        consumableSlots = menuButtons;
    }

    public void openInventory(HumanEntity human) {
        inventory.clear();

        consumableSlots.forEach((key, value) -> inventory.setItem(key, value.getDisplayItem()));

        human.openInventory(inventory);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void setCloseEvent(Consumer<InventoryCloseEvent> closeEvent) {
        this.closeEvent = closeEvent;
    }

    public Consumer<InventoryCloseEvent> getCloseEvent() {
        return closeEvent;
    }

    public Map<Integer, MenuButton> getConsumableSlots() {
        return consumableSlots;
    }

    public String getTitle() {
        return title;
    }

    public static class Builder {

        private int rows;
        private String title;
        private Map<Integer, MenuButton> buttons;

        public Builder(int rows, String title) {
            this(rows, title, false, null);

        }

        public Builder(int rows, String title, boolean fillEmptySlots, Material fillMaterial) {
            this.rows = rows;
            this.title = title;
            this.buttons = new HashMap<>();
            if (fillEmptySlots) {
                for (int i = 0; i < rows * 9; i++) {
                    buttons.put(i, new MenuButton(event -> event.setCancelled(true), i, fillMaterial));
                }
            }
        }

        public Builder rows(int rows) {
            this.rows = rows;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder addMenuButtons(Map<Integer, MenuButton> buttons) {
            this.buttons.putAll(buttons);
            return this;
        }

        public Builder addMenuButton(int slot, MenuButton button) {
            this.buttons.put(slot, button);
            return this;
        }

        public Builder addMenuButtons(MenuButton button, int... slots) {
            for (int slot : slots) {
                button.setSlot(slot);
                this.buttons.put(slot, button);
            }
            return this;
        }

        public Menu build() {
            return new Menu(title, rows, buttons);
        }
    }
}
