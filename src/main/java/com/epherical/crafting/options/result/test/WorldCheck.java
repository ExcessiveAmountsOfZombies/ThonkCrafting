package com.epherical.crafting.options.result.test;

import com.epherical.crafting.options.OptionContext;
import com.epherical.crafting.options.TestOptions;
import com.google.gson.JsonObject;
import org.bukkit.NamespacedKey;

public class WorldCheck extends TestOptions {

    private final String world;

    public WorldCheck(String failMessage, String world) {
        super(failMessage);
        this.world = world;
    }

    public WorldCheck(NamespacedKey key, JsonObject object) {
        super(object.getAsJsonPrimitive("fail-message").getAsString());
        this.world = object.getAsJsonPrimitive("world-to-check").getAsString();
    }

    @Override
    public boolean test(OptionContext context) {
        return context.getPlayer().getWorld().getName().equalsIgnoreCase(world);
    }
}
