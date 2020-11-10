package com.epherical.crafting.options.result.test;

import com.epherical.crafting.options.OptionContext;
import com.epherical.crafting.options.TestOptions;

public class WorldCheck extends TestOptions {

    private final String world;

    public WorldCheck(String failMessage, String world) {
        super(failMessage);
        this.world = world;
    }

    @Override
    public boolean test(OptionContext context) {
        return context.getPlayer().getWorld().getName().equalsIgnoreCase(world);
    }
}
