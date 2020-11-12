package com.epherical.crafting.options.result.test;

import com.epherical.crafting.options.OptionContext;
import com.epherical.crafting.options.TestOptions;
import com.epherical.crafting.util.JsonUtil;
import com.google.gson.JsonObject;
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
}
