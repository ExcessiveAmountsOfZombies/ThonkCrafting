package com.epherical.crafting.test.api;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public class PermissionRecipe extends DamageDurabilityRecipe {
    private String permission;
    private String failMessage;
    private int counter;

    public PermissionRecipe(NamespacedKey key, ItemStack result, String permission, String failMessage, int counter) {
        super(key, result);
        this.permission = permission;
        this.failMessage = failMessage;
        this.counter = counter;
    }

    public String getFailMessage() {
        return failMessage;
    }

    public String getPermission() {
        return permission;
    }

    public int getCounter() {
        return counter;
    }
}
