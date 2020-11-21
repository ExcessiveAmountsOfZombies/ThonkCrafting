package com.epherical.crafting.options.result.test;

import com.epherical.crafting.options.OptionContext;
import com.epherical.crafting.options.TestOptions;
import com.epherical.crafting.util.JsonUtil;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.NamespacedKey;


public class Permission extends TestOptions {
    private final String permissionNode;

    public Permission(String failMessage, String permissionNode) {
        super(failMessage);
        this.permissionNode = permissionNode;
    }

    public Permission(NamespacedKey key, JsonObject object) {
        super(JsonUtil.getValue(object, "fail-message").getAsString());
        this.permissionNode = JsonUtil.getValue(object, "permission").getAsString();
    }

    @Override
    public boolean test(OptionContext context) {
        return context.getPlayer().hasPermission(permissionNode);
    }

    @Override
    public String toString() {
        return "Permission required to craft this item: " + permissionNode;
    }

    @Override
    public BaseComponent[] textDisplay() {
        ComponentBuilder builder = new ComponentBuilder("Permission required to craft this item: ").color(ChatColor.DARK_GRAY).italic(false);
        builder.append(permissionNode).color(ChatColor.GRAY).italic(false);
        return builder.create();
    }
}
