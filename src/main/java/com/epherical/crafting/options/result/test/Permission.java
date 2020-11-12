package com.epherical.crafting.options.result.test;

import com.epherical.crafting.options.OptionContext;
import com.epherical.crafting.options.Options;
import com.epherical.crafting.options.TestOptions;
import com.google.gson.JsonObject;
import org.bukkit.NamespacedKey;


public class Permission extends TestOptions {
    private final String permissionNode;

    public Permission(String failMessage, String permissionNode) {
        super(failMessage);
        this.permissionNode = permissionNode;
    }

    public Permission(NamespacedKey key, JsonObject object) {
        super( object.getAsJsonPrimitive("fail-message").getAsString());
        this.permissionNode = object.getAsJsonPrimitive("permission").getAsString();
    }

    @Override
    public boolean test(OptionContext context) {
        return context.getPlayer().hasPermission(permissionNode);
    }
}
