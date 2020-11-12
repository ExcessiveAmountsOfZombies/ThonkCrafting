package com.epherical.crafting.options.result.test;

import com.epherical.crafting.options.OptionContext;
import com.epherical.crafting.options.TestOptions;
import com.epherical.crafting.util.JsonUtil;
import com.google.gson.JsonObject;
import org.bukkit.NamespacedKey;

public class WorldCheck extends TestOptions {

    private final String world;

    public WorldCheck(String failMessage, String world) {
        super(failMessage);
        this.world = world;
    }

    public WorldCheck(NamespacedKey key, JsonObject object) {
        super(JsonUtil.getValue(object, "fail-message").getAsString());
        this.world = JsonUtil.getValue(object, "world-to-check").getAsString();
    }

    @Override
    public boolean test(OptionContext context) {
        return context.getPlayer().getWorld().getName().equalsIgnoreCase(world);
    }
}
